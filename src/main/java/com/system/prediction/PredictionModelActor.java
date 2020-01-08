package com.system.prediction;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.system.database.MongoConfig;
import org.springframework.data.mongodb.core.MongoOperations;

public class PredictionModelActor extends AbstractLoggingActor {
    private final String predictionModelActorId;

    private final MongoOperations mongoOperations;

    public PredictionModelActor(String predictionModelActorId) {
        log().info("PredictionModelActor {} created", predictionModelActorId);
        this.predictionModelActorId = predictionModelActorId;
        MongoConfig config = new MongoConfig();
        this.mongoOperations = config.mongoTemplate();
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
