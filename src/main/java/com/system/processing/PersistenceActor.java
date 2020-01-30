package com.system.processing;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.system.database.Collections;
import com.system.database.MongoConfig;
import com.system.pojo.PredictionData;
import com.system.pojo.Station;
import com.system.pojo.Trip;
import com.system.pojo.weather.Weather;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.UUID;

public class PersistenceActor extends AbstractLoggingActor {
    private final String persistenceActorId;

    private final MongoOperations mongoOperations;

    public PersistenceActor(String persistenceActorId) {
        log().info("PersistenceActor {} created", persistenceActorId);
        this.persistenceActorId = persistenceActorId;
        MongoConfig config = new MongoConfig();
        this.mongoOperations = config.mongoTemplate();
    }

    static Props props(String persistenceActorId) {
        return Props.create(PersistenceActor.class, () -> new PersistenceActor(persistenceActorId));
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    public static final class SaveCollectedData {
        public final String jobUUID;
        public final PredictionData predictionData;

        public SaveCollectedData(String jobUUID, PredictionData predictionData) {
            this.jobUUID = jobUUID;
            this.predictionData = predictionData;
        }
    }

    public static final class GetCollectedData {
        public final String jobUUID;
        public final PredictionData predictionData;

        public GetCollectedData(String jobUUID, PredictionData predictionData) {
            this.jobUUID = jobUUID;
            this.predictionData = predictionData;
        }

    }

    public static final class GetCollectedDataforStation {
        public final String jobUUID;
        public final int stationId;

        public GetCollectedDataforStation(String jobUUID, int stationId) {
            this.jobUUID = jobUUID;
            this.stationId = stationId;
        }

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SaveCollectedData.class, msg -> {
                    System.out.println("PersistenceActor received collected data with job ID: " + msg.jobUUID);

                    dropCollections();
                    msg.predictionData.getStations()
                            .forEach(station -> mongoOperations.save(station, Collections.STATIONS.name()));
                    msg.predictionData.getTrips()
                            .forEach(trip -> mongoOperations.save(trip, Collections.TRIPS.name()));
                    msg.predictionData.getWeatherAPI().getWeathersByDay()
                            .forEach(weather -> mongoOperations.save(weather, Collections.WEATHERS.name()));
                    msg.predictionData.setWeathers(msg.predictionData.getWeatherAPI().getWeathersByDay());

//                    String jobUUID = UUID.randomUUID().toString();
                    getContext().getParent().tell(new GetCollectedData(msg.jobUUID, msg.predictionData), self());
                })
                .match(GetCollectedDataforStation.class, msg -> {
                    PredictionData data = new PredictionData();

                    data.setStations(mongoOperations.find(new Query().addCriteria(Criteria.where("stationId").is(msg.stationId)),
                            Station.class, Collections.STATIONS.name()));
                    data.setTrips(mongoOperations.find(new Query().addCriteria(Criteria.where("startStationId").is(msg.stationId)
                            .orOperator(Criteria.where("endStationId").is(msg.stationId))), Trip.class, Collections.TRIPS.name()));
                    data.setWeathers(mongoOperations.findAll(Weather.class, Collections.WEATHERS.name()));

                    String jobUUID = UUID.randomUUID().toString();
                    getContext().getParent().tell(new GetCollectedData(msg.jobUUID, data), self());
                })
                .build();
    }

    private void dropCollections() {
        mongoOperations.getCollection(Collections.STATIONS.name()).drop();
        mongoOperations.getCollection(Collections.TRIPS.name()).drop();
        mongoOperations.getCollection(Collections.WEATHERS.name()).drop();
        mongoOperations.getCollection(Collections.STATIONS.name()).dropIndexes();

    }
}
