package com.system.bikesharing.tripdata;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class TripDataActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final String tripDataActorId;

    private TripDataActor(String tripDataActorId) {
        this.tripDataActorId = tripDataActorId;
        log.info("TripDataActor {} created", tripDataActorId);
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
