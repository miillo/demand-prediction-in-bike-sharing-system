package com.system.processing;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.system.bikesharing.stationdata.StationDataActorRouter;
import com.system.bikesharing.tripdata.TripDataActorRouter;
import com.system.pojo.PredictionData;
import com.system.pojo.Station;
import com.system.pojo.Trip;
import com.system.pojo.UserRequest;
import com.system.pojo.weather.Weather;
import com.system.pojo.weather.WeatherAPI;
import com.system.prediction.PredictionModelActor;
import com.system.weatherdata.WeatherDataActorRouter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProcessingDataActor extends AbstractLoggingActor {
    private final String processingDataActorId;
    private final Map<String, ActorRef> actorRefMap;
    private final Map<String, PredictionData> predictionDataMap;

    public ProcessingDataActor(String processingDataActorId) {
        log().info("ProcessingDataActor {} created", processingDataActorId);
        this.processingDataActorId = processingDataActorId;
        this.predictionDataMap = new HashMap<>();
        this.actorRefMap = createChildActors();
    }

    static Props props(String processingDataActorId) {
        return Props.create(ProcessingDataActor.class, () ->
                new ProcessingDataActor(processingDataActorId));
    }

    public static final class HandleUserRequest {
        final UserRequest userRequest;

        public HandleUserRequest(UserRequest userRequest) {
            this.userRequest = userRequest;
        }
    }

    public static final class WeatherApiData {
        public final WeatherAPI weatherAPI;
        public final String jobUUID;

        public WeatherApiData(WeatherAPI weatherAPI, String jobUUID) {
            this.weatherAPI = weatherAPI;
            this.jobUUID = jobUUID;
        }
    }

    public static final class StationsData {
        public final List<Station> stations;
        public final String jobUUID;

        public StationsData(List<Station> stations, String jobUUID) {
            this.stations = stations;
            this.jobUUID = jobUUID;
        }
    }

    public static final class TripsData {
        public final List<Trip> trips;
        public final String jobUUID;

        public TripsData(List<Trip> trips, String jobUUID) {
            this.trips = trips;
            this.jobUUID = jobUUID;
        }
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(HandleUserRequest.class, msg -> {
                    String jobUUID = UUID.randomUUID().toString();
                    predictionDataMap.put(jobUUID, new PredictionData());
                    actorRefMap.get("weatherGeoActorRouter").tell(new WeatherDataActorRouter.DownloadWeatherData(msg.userRequest, jobUUID), getSelf());
                    actorRefMap.get("stationDataActorRouter").tell(new StationDataActorRouter.DownloadStationsData(msg.userRequest, jobUUID), getSelf());
                    actorRefMap.get("tripDataActorRouter").tell(new TripDataActorRouter.DownloadTripsData(msg.userRequest, jobUUID), getSelf());
                })
                .match(WeatherApiData.class, msg -> {
                    System.out.println("ProcessingDataActor received Weather API data for job: " + msg.jobUUID);
                    PredictionData predictionData = predictionDataMap.get(msg.jobUUID);
                    predictionData.setWeatherAPI(msg.weatherAPI);
                    predictionData.incrementDataCompleteness();
                    if (predictionData.getDataCompleteness() == 3) {
                        actorRefMap.get("predictionModelActor").tell(new PredictionModelActor.PredictDemand(msg.jobUUID, predictionData), self());
                        actorRefMap.get("persistenceActor").tell(new PersistenceActor.SaveCollectedData(msg.jobUUID, predictionData), self());
                        predictionDataMap.remove(msg.jobUUID); //test
                        System.out.println(predictionDataMap);
                    }
                })
                .match(StationsData.class, msg -> {
                    System.out.println("ProcessingDataActor received stations data for job: " + msg.jobUUID);
                    PredictionData predictionData = predictionDataMap.get(msg.jobUUID);
                    predictionData.setStations(msg.stations);
                    predictionData.incrementDataCompleteness();
                    if (predictionData.getDataCompleteness() == 3) {
                        actorRefMap.get("predictionModelActor").tell(new PredictionModelActor.PredictDemand(msg.jobUUID, predictionData), self());
                        actorRefMap.get("persistenceActor").tell(new PersistenceActor.SaveCollectedData(msg.jobUUID, predictionData), self());
                        predictionDataMap.remove(msg.jobUUID);
                        System.out.println(predictionDataMap);
                    }
                })
                .match(TripsData.class, msg -> {
                    System.out.println("ProcessingDataActor received trips data for job: " + msg.jobUUID);
                    PredictionData predictionData = predictionDataMap.get(msg.jobUUID);
                    predictionData.setTrips(msg.trips);
                    predictionData.incrementDataCompleteness();
                    if (predictionData.getDataCompleteness() == 3) {
                        actorRefMap.get("predictionModelActor").tell(new PredictionModelActor.PredictDemand(msg.jobUUID, predictionData), self());
                        actorRefMap.get("persistenceActor").tell(new PersistenceActor.SaveCollectedData(msg.jobUUID, predictionData), self());
                        predictionDataMap.remove(msg.jobUUID);
                        System.out.println(predictionDataMap);
                    }
                })
                .build();
    }

    /**
     * Creates child actors: PersistenceActor | PredictionModelActor | TripDataActorRouter | StationDataActorRouter
     * WeatherDataActorRouter
     *
     * @return updated map with actor references
     */
    private Map<String, ActorRef> createChildActors() {
        ActorRef tripDataActorRouter = getContext()
                .actorOf(Props.create(TripDataActorRouter.class, "0"), "tripDataActorRouter");
        ActorRef stationDataActorRouter = getContext()
                .actorOf(Props.create(StationDataActorRouter.class, "0"), "stationDataActorRouter");
        ActorRef weatherGeoActorRouter = getContext()
                .actorOf(Props.create(WeatherDataActorRouter.class, "0"), "weatherGeoActorRouter");

        ActorRef persistenceActor = getContext().actorOf(Props.create(PersistenceActor.class, "0"), "PersistenceActor");
        ActorRef predictionModelActor = getContext().actorOf(Props.create(PredictionModelActor.class, "0"), "PredictionModelActor");

        return new HashMap<String, ActorRef>() {{
            put("tripDataActorRouter", tripDataActorRouter);
            put("stationDataActorRouter", stationDataActorRouter);
            put("weatherGeoActorRouter", weatherGeoActorRouter);
            put("persistenceActor", persistenceActor);
            put("predictionModelActor", predictionModelActor);
        }};
    }
}
