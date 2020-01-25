package com.system.weatherdata;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.system.pojo.UserRequest;
import com.system.pojo.weather.WeatherAPI;
import com.system.weatherdata.services.WeatherService;

public class WeatherDataActor extends AbstractLoggingActor {
    private final String weatherDataActorId;
    private final WeatherService weatherService;

    private WeatherDataActor(String weatherDataActorId) {
        this.weatherDataActorId = weatherDataActorId;
        this.weatherService = new WeatherService();
        log().info("WeatherDataActor {} created", weatherDataActorId);
    }

    static Props props(String weatherDataActorId) {
        return Props.create(WeatherDataActor.class, () -> new WeatherDataActor(weatherDataActorId));
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    public static final class DownloadWeatherAPIData {
        public final UserRequest userRequest;
        public final String jobUUID;

        public DownloadWeatherAPIData(UserRequest userRequest, String jobUUID) {
            this.userRequest = userRequest;
            this.jobUUID = jobUUID;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(DownloadWeatherAPIData.class, msg -> {
                    try {
                        WeatherAPI weatherData = weatherService.getWeatherData(msg.userRequest);
                        getSender().tell(new WeatherDataActorRouter.DownloadedWeatherData(weatherData, msg.jobUUID), getSelf());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                })
                .build();
    }
}
