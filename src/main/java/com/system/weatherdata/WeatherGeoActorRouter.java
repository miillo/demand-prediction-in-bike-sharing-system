package com.system.weatherdata;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;

import java.util.ArrayList;
import java.util.List;

public class WeatherGeoActorRouter extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final String weatherGeoActorRouterId;
    private final Router router;


    public static final class DownloadWeatherData {
        final long requestId;

        public DownloadWeatherData(long requestId) {
            this.requestId = requestId;
        }
    }

    public static final class DownloadedWeatherData {
        final long requestId;
        final String weatherData; //probably parsed JSON

        public DownloadedWeatherData(long requestId, String weatherData) {
            this.requestId = requestId;
            this.weatherData = weatherData;
        }
    }


    public WeatherGeoActorRouter(String weatherGeoActorRouterId) {
        log.info("WeatherGeoDataActorRouter {} created", weatherGeoActorRouterId);
        this.weatherGeoActorRouterId = weatherGeoActorRouterId;
        this.router = createRouter();
    }

    static Props props(String weatherGeoActorRouterId) {
        return Props.create(WeatherGeoActorRouter.class, () -> new WeatherGeoActorRouter(weatherGeoActorRouterId));
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

    //todo number of routees should be read from config file
    private Router createRouter() {
        List<Routee> routees = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ActorRef r = getContext().actorOf(Props.create(WeatherDataActor.class, String.valueOf(i)));
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        return new Router(new RoundRobinRoutingLogic(), routees);
    }
}
