package com.system.bikesharing.stationdata;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;

public class StationDataActor extends AbstractLoggingActor {
    private final String stationDataActorId;

    private StationDataActor(String stationDataActorId) {
        this.stationDataActorId = stationDataActorId;
        log().info("StationDataActor {} created", stationDataActorId);
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
