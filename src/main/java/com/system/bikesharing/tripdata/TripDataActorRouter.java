package com.system.bikesharing.tripdata;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.system.pojo.Station;
import com.system.pojo.Trip;
import com.system.pojo.UserRequest;
import com.system.processing.ProcessingDataActor;
import com.system.settings.AppSettings;

import java.util.ArrayList;
import java.util.List;

public class TripDataActorRouter extends AbstractLoggingActor {
    private final String tripDataActorRouterId;
    private final Router router;


    public static final class DownloadTripsData {
        public final UserRequest userRequest;
        public final String jobUUID;

        public DownloadTripsData(UserRequest userRequest, String jobUUID) {
            this.userRequest = userRequest;
            this.jobUUID = jobUUID;
        }
    }

    public static final class DownloadedTripsData {
        public final List<Trip> trips;
        public final String jobUUID;

        public DownloadedTripsData(List<Trip> trips, String jobUUID) {
            this.trips = trips;
            this.jobUUID = jobUUID;
        }
    }


    public TripDataActorRouter(String tripDataActorRouterId) {
        log().info("TripDataActorRouter {} created", tripDataActorRouterId);
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
                .match(DownloadTripsData.class, msg -> {
                    router.route(new TripDataActor.DownloadTripsData(msg.userRequest, msg.jobUUID), self());
                })
                .match(DownloadedTripsData.class, msg -> {
                    if (msg.trips != null) {
                        getContext().getParent().tell(new ProcessingDataActor.TripsData(msg.trips, msg.jobUUID), self());
                    }
                })
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
