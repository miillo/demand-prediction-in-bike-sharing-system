package com.system.bikesharing.stationdata;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class StationDataActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final String stationDataActorId;

    private StationDataActor(String stationDataActorId) {
        this.stationDataActorId = stationDataActorId;
        log.info("StationDataActor {} created", stationDataActorId);
    }

    static Props props(String stationDataActorId) {
        return Props.create(StationDataActor.class, () -> new StationDataActor(stationDataActorId));
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
