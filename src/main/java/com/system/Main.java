package com.system;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.system.data.StationDataCreator;
import com.system.http.ApplicationServer;
import com.system.processing.ProcessingDataActor;
import com.system.settings.AppSettings;

public class Main {
    public static void main(String[] args) {
        AppSettings.readConfig();

        ActorSystem system = ActorSystem.create("BikesharingSystem");
        ActorRef processingDataActor = system
                .actorOf(Props.create(ProcessingDataActor.class, "0"), "processingDataActor");
        new ApplicationServer(system, processingDataActor);
    }
}
