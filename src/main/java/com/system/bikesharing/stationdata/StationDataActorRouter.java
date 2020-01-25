package com.system.bikesharing.stationdata;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.system.pojo.Station;
import com.system.pojo.UserRequest;
import com.system.pojo.weather.WeatherAPI;
import com.system.processing.ProcessingDataActor;
import com.system.settings.AppSettings;

import java.util.ArrayList;
import java.util.List;

public class StationDataActorRouter extends AbstractLoggingActor {
    private final String stationDataActorRouterId;
    private final Router router;

    public static final class DownloadStationsData {
        public final UserRequest userRequest;
        public final String jobUUID;

        public DownloadStationsData(UserRequest userRequest, String jobUUID) {
            this.userRequest = userRequest;
            this.jobUUID = jobUUID;
        }
    }

    public static final class DownloadedStationsData {
        public final List<Station> stations;
        public final String jobUUID;

        public DownloadedStationsData(List<Station> stations, String jobUUID) {
            this.stations = stations;
            this.jobUUID = jobUUID;
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
                .match(DownloadStationsData.class, msg -> {
                    router.route(new StationDataActor.DownloadStationsData(msg.userRequest, msg.jobUUID), self());
                })
                .match(DownloadedStationsData.class, msg -> {
                    if (msg.stations != null) {
                        getContext().getParent().tell(new ProcessingDataActor.StationsData(msg.stations, msg.jobUUID),self());
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
        for (int i = 0; i < AppSettings.noOfStationDataActors; i++) {
            ActorRef r = getContext().actorOf(Props.create(StationDataActor.class, String.valueOf(i)));
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        return new Router(new RoundRobinRoutingLogic(), routees);
    }
}
