package com.system.weatherdata;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;

public class WeatherDataActor extends AbstractLoggingActor {
    private final String weatherDataActorId;

    private WeatherDataActor(String weatherDataActorId) {
        this.weatherDataActorId = weatherDataActorId;
        log().info("WeatherDataActor {} created", weatherDataActorId);
    }

    static Props props(String weatherDataActorId) {
        return Props.create(WeatherDataActor.class, () -> new WeatherDataActor(weatherDataActorId));
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
}
