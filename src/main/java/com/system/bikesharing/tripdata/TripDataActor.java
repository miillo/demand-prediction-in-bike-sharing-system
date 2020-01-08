package com.system.bikesharing.tripdata;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;

public class TripDataActor extends AbstractLoggingActor {
    private final String tripDataActorId;

    private TripDataActor(String tripDataActorId) {
        this.tripDataActorId = tripDataActorId;
        log().info("TripDataActor {} created", tripDataActorId);
    }

    static Props props(String tripDataActorId) {
        return Props.create(TripDataActor.class, () -> new TripDataActor(tripDataActorId));
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
