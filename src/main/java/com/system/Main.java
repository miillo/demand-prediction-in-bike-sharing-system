package com.system;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.system.bikesharing.stationdata.StationDataActorRouter;
import com.system.bikesharing.tripdata.TripDataActorRouter;

public class Main {
    public static void main(String[] args) {
        //todo add reading application.info + global settings class

        ActorSystem system = ActorSystem.create("BikesharingSystem");
        ActorRef tripDataActorRouter = system
                .actorOf(Props.create(TripDataActorRouter.class, "0"), "tripDataActorRouter");
        ActorRef stationDataActorRouter = system
                .actorOf(Props.create(StationDataActorRouter.class, "0"), "stationDataActorRouter");

    }
}
