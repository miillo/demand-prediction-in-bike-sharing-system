package com.system.weatherdata;

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

public class WeatherDataActorRouter extends AbstractLoggingActor {
    private final String weatherDataActorRouterId;
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


    public WeatherDataActorRouter(String weatherActorRouterId) {
        log().info("WeatherDataActorRouter {} created", weatherActorRouterId);
        this.weatherDataActorRouterId = weatherActorRouterId;
        this.router = createRouter();
    }

    static Props props(String weatherActorRouterId) {
        return Props.create(WeatherDataActorRouter.class, () -> new WeatherDataActorRouter(weatherActorRouterId));
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
        for (int i = 0; i < AppSettings.noOfWeatherActors; i++) {
            ActorRef r = getContext().actorOf(Props.create(WeatherDataActor.class, String.valueOf(i)));
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        return new Router(new RoundRobinRoutingLogic(), routees);
    }
}
