package com.system;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.system.bikesharing.stationdata.StationDataActorRouter;
import com.system.bikesharing.tripdata.TripDataActorRouter;
import com.system.http.ApplicationServer;
import com.system.processing.ProcessingDataActor;
import com.system.settings.AppSettings;
import com.system.weatherdata.WeatherDataActorRouter;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        AppSettings.readConfig();

        ActorSystem system = ActorSystem.create("BikesharingSystem");
        ActorRef processingDataActor = system
                .actorOf(Props.create(ProcessingDataActor.class, "0"), "processingDataActor");
        new ApplicationServer(system, processingDataActor);
    }
}
