package com.system.prediction;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class PredictionModelActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final String predictionModelActorId;

    public PredictionModelActor(String predictionModelActorId) {
        log.info("PredictionModelActor {} created", predictionModelActorId);
        this.predictionModelActorId = predictionModelActorId;
    }

    static Props props(String predictionModelActorId) {
        return Props.create(PredictionModelActor.class, () -> new PredictionModelActor(predictionModelActorId));
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
