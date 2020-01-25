package com.system.processing;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.system.database.MongoConfig;
import com.system.pojo.PredictionData;
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

    public static final class SaveCollectedData {
        public final String jobUUID;
        public final PredictionData predictionData;

        public SaveCollectedData(String jobUUID, PredictionData predictionData) {
            this.jobUUID = jobUUID;
            this.predictionData = predictionData;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SaveCollectedData.class, msg -> {
                    System.out.println("PersistenceActor received collected data with job ID: " + msg.jobUUID);
                    System.out.println(msg.predictionData);
                })
                .build();
    }
}
