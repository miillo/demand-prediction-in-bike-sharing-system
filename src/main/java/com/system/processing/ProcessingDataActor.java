package com.system.processing;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.system.bikesharing.stationdata.StationDataActorRouter;
import com.system.bikesharing.tripdata.TripDataActorRouter;
import com.system.pojo.Station;
import com.system.pojo.UserRequest;
import com.system.pojo.weather.Weather;
import com.system.pojo.weather.WeatherAPI;
import com.system.prediction.PredictionModelActor;
import com.system.weatherdata.WeatherDataActorRouter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessingDataActor extends AbstractLoggingActor {
    private final String processingDataActorId;
    private final Map<String, ActorRef> actorRefMap;

    public ProcessingDataActor(String processingDataActorId) {
        log().info("ProcessingDataActor {} created", processingDataActorId);
        this.processingDataActorId = processingDataActorId;
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

        public WeatherApiData(WeatherAPI weatherAPI) {
            this.weatherAPI = weatherAPI;
        }
    }

    public static final class StationsData {
        public final List<Station> stations;

        public StationsData(List<Station> stations) {
            this.stations = stations;
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
                    //todo wysylanie requestow o dane stacji / tras / geo
//                    actorRefMap.get("weatherGeoActorRouter").tell(new WeatherDataActorRouter.DownloadWeatherData(msg.userRequest), getSelf());
                    actorRefMap.get("stationDataActorRouter").tell(new StationDataActorRouter.DownloadStationsData(msg.userRequest), getSelf());
                })
                .match(WeatherApiData.class, msg -> {
                    //todo save and check if rest was computed
                    System.out.println("ProcessingDataActor received Weather API data");
                    System.out.println(msg.weatherAPI);
                })
                .match(StationsData.class, msg -> {
                    //todo save and check if rest was computed
                    System.out.println("ProcessingDataActor received Stations data");
                    System.out.println(msg.stations);
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
