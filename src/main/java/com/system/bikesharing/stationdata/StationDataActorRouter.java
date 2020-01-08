package com.system.bikesharing.stationdata;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.system.settings.AppSettings;

import java.util.ArrayList;
import java.util.List;

public class StationDataActorRouter extends AbstractLoggingActor {
    private final String stationDataActorRouterId;
    private final Router router;


    public static final class DownloadStationData {
        final long requestId;

        public DownloadStationData(long requestId) {
            this.requestId = requestId;
        }
    }

    public static final class DownloadedStationData {
        final long requestId;
        final String stationData; //probably parsed JSON

        public DownloadedStationData(long requestId, String stationData) {
            this.requestId = requestId;
            this.stationData = stationData;
        }
    }


    public StationDataActorRouter(String stationDataActorRouterId) {
        log().info("StationDataActorRouter {} created", stationDataActorRouterId);
        this.stationDataActorRouterId = stationDataActorRouterId;
        this.router = createRouter();
    }

    static Props props(String stationDataActorRouterId) {
        return Props.create(StationDataActorRouter.class, () -> new StationDataActorRouter(stationDataActorRouterId));
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .build();
    }

    /**
     * Creates router
     *
     * @return router instance
     */
    private Router createRouter() {
        List<Routee> routees = new ArrayList<>();
        for (int i = 0; i < AppSettings.noOfStationDataActors; i++) {
            ActorRef r = getContext().actorOf(Props.create(StationDataActor.class, String.valueOf(i)));
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        return new Router(new RoundRobinRoutingLogic(), routees);
    }
}
