package com.system.processing;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.system.database.Collections;
import com.system.database.MongoConfig;
import com.system.pojo.PredictionData;
import com.system.pojo.Station;
import com.system.pojo.Trip;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.UUID;

public class PersistenceActor extends AbstractLoggingActor {
    private final String persistenceActorId;

    private final MongoOperations mongoOperations;

    public PersistenceActor(String persistenceActorId) {
        log().info("PersistenceActor {} created", persistenceActorId);
        this.persistenceActorId = persistenceActorId;
        MongoConfig config = new MongoConfig();
        this.mongoOperations = config.mongoTemplate();
        dropCollections();
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

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SaveCollectedData.class, msg -> {
                    System.out.println("PersistenceActor received collected data with job ID: " + msg.jobUUID);
                    System.out.println(msg.predictionData);

                    msg.predictionData.getStations()
                            .forEach(station -> mongoOperations.save(station, Collections.STATIONS.name()));

                    msg.predictionData.getTrips()
                            .forEach(trip -> mongoOperations.save(trip, Collections.TRIPS.name()));

                    msg.predictionData.getWeatherAPI().getWeathersByDay()
                            .forEach(weather -> mongoOperations.save(weather, Collections.WEATHERS.name()));

                    System.out.println("Stations collection size: " + mongoOperations.findAll(Station.class, Collections.STATIONS.name()).size());
                    System.out.println("Trips collection size: " + mongoOperations.findAll(Trip.class, Collections.TRIPS.name()).size());
                    System.out.println("Weathers collection size: " + mongoOperations.findAll(Trip.class, Collections.WEATHERS.name()).size());

                    String jobUUID = UUID.randomUUID().toString();
                    getContext().getParent().tell(new GetCollectedData(jobUUID, msg.predictionData), self());
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
