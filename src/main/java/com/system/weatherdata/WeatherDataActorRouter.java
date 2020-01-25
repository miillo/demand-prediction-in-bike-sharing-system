package com.system.weatherdata;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.system.pojo.UserRequest;
import com.system.pojo.weather.WeatherAPI;
import com.system.processing.ProcessingDataActor;
import com.system.settings.AppSettings;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataActorRouter extends AbstractLoggingActor {
    private final String weatherDataActorRouterId;
    private final Router router;


    public static final class DownloadWeatherData {
        public final UserRequest userRequest;
        public final String jobUUID;

        public DownloadWeatherData(UserRequest userRequest, String jobUUID) {
            this.userRequest = userRequest;
            this.jobUUID = jobUUID;
        }
    }

    public static final class DownloadedWeatherData {
        public final WeatherAPI weatherData;
        public final String jobUUID;

        public DownloadedWeatherData(WeatherAPI weatherData, String jobUUID) {
            this.weatherData = weatherData;
            this.jobUUID = jobUUID;
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
                .match(DownloadWeatherData.class, msg -> {
                    router.route(new WeatherDataActor.DownloadWeatherAPIData(msg.userRequest, msg.jobUUID), self());
                })
                .match(DownloadedWeatherData.class, msg -> {
                    if (msg.weatherData != null) {
                        getContext().getParent().tell(new ProcessingDataActor.WeatherApiData(msg.weatherData, msg.jobUUID), self());
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
        for (int i = 0; i < AppSettings.noOfWeatherActors; i++) {
            ActorRef r = getContext().actorOf(Props.create(WeatherDataActor.class, String.valueOf(i)));
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        return new Router(new RoundRobinRoutingLogic(), routees);
    }
}
