package com.system.weatherdata;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class WeatherDataActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final String weatherDataActorId;

    private WeatherDataActor(String weatherDataActorId) {
        this.weatherDataActorId = weatherDataActorId;
        log.info("WeatherDataActor {} created", weatherDataActorId);
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
