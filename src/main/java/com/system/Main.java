package com.system;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.system.bikesharing.stationdata.StationDataActorRouter;
import com.system.bikesharing.tripdata.TripDataActorRouter;
import com.system.http.ApplicationServer;
import com.system.processing.ProcessingDataActor;
import com.system.settings.AppSettings;
import com.system.weatherdata.WeatherGeoActorRouter;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        AppSettings.readConfig();

        ActorSystem system = ActorSystem.create("BikesharingSystem");
        Map<String, ActorRef> actorRefMap = createHighLevelActors(system);
        ActorRef processingDataActor = system
                .actorOf(Props.create(ProcessingDataActor.class, "0", actorRefMap), "processingDataActor");
        new ApplicationServer(system, processingDataActor);
    }

    /**
     * Creates high level actors
     *
     * @param system actor system instance
     * @return map with high level actors
     */
    private static Map<String, ActorRef> createHighLevelActors(ActorSystem system) {
        ActorRef tripDataActorRouter = system
                .actorOf(Props.create(TripDataActorRouter.class, "0"), "tripDataActorRouter");
        ActorRef stationDataActorRouter = system
                .actorOf(Props.create(StationDataActorRouter.class, "0"), "stationDataActorRouter");
        ActorRef weatherGeoActorRouter = system
                .actorOf(Props.create(WeatherGeoActorRouter.class, "0"), "weatherGeoActorRouter");

        return new HashMap<String, ActorRef>() {{
            put("tripDataActorRouter", tripDataActorRouter);
            put("stationDataActorRouter", stationDataActorRouter);
            put("weatherGeoActorRouter", weatherGeoActorRouter);
        }};
    }
}
