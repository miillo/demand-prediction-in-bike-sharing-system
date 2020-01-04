package com.system.processing;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.Map;

public class PersistenceActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final String persistenceActorId;

    public PersistenceActor(String persistenceActorId) {
        log.info("PersistenceActor {} created", persistenceActorId);
        this.persistenceActorId = persistenceActorId;
    }

    static Props props(String persistenceActorId) {
        return Props.create(PersistenceActor.class, () -> new PersistenceActor(persistenceActorId));
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
