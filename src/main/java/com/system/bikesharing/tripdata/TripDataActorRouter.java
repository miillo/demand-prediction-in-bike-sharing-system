package com.system.bikesharing.tripdata;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.system.settings.AppSettings;

import java.util.ArrayList;
import java.util.List;

public class TripDataActorRouter extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final String tripDataActorRouterId;
    private final Router router;


    public static final class DownloadTripData {
        final long requestId;

        public DownloadTripData(long requestId) {
            this.requestId = requestId;
        }
    }

    public static final class DownloadedTripData {
        final long requestId;
        final String tripData; //probably parsed JSON

        public DownloadedTripData(long requestId, String tripData) {
            this.requestId = requestId;
            this.tripData = tripData;
        }
    }


    public TripDataActorRouter(String tripDataActorRouterId) {
        log.info("TripDataActorRouter {} created", tripDataActorRouterId);
        this.tripDataActorRouterId = tripDataActorRouterId;
        this.router = createRouter();
    }

    static Props props(String tripDataActorRouterId) {
        return Props.create(TripDataActorRouter.class, () -> new TripDataActorRouter(tripDataActorRouterId));
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
        for (int i = 0; i < AppSettings.noOfTripDataActors; i++) {
            ActorRef r = getContext().actorOf(Props.create(TripDataActor.class, String.valueOf(i)));
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        return new Router(new RoundRobinRoutingLogic(), routees);
    }
}
