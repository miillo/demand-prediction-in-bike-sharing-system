package com.system.processing;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.system.database.MongoConfig;
import org.springframework.data.mongodb.core.MongoOperations;

public class PersistenceActor extends AbstractLoggingActor {
    private final String persistenceActorId;

    private final MongoOperations mongoOperations;

    public PersistenceActor(String persistenceActorId) {
        log().info("PersistenceActor {} created", persistenceActorId);
        this.persistenceActorId = persistenceActorId;
        MongoConfig config = new MongoConfig();
        this.mongoOperations = config.mongoTemplate();
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
