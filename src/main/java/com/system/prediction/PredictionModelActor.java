package com.system.prediction;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.system.pojo.ProcessedRecord;
import com.system.prediction.services.PredictionModelService;

import java.util.List;

public class PredictionModelActor extends AbstractLoggingActor {
    private final String predictionModelActorId;
    private final PredictionModelService predictionModelService;

    public PredictionModelActor(String predictionModelActorId) {
        log().info("PredictionModelActor {} created", predictionModelActorId);
        this.predictionModelActorId = predictionModelActorId;
        this.predictionModelService = new PredictionModelService();
    }

    static Props props(String predictionModelActorId) {
        return Props.create(PredictionModelActor.class, () -> new PredictionModelActor(predictionModelActorId));
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    public static final class PredictDemand {
        public final String jobUUID;
        public final List<ProcessedRecord> processedRecords;

        public PredictDemand(String jobUUID, List<ProcessedRecord> processedRecords) {
            this.jobUUID = jobUUID;
            this.processedRecords = processedRecords;
        }
    }

    public static final class PredictDemandWithModel {
        public final String jobUUID;
        public final List<ProcessedRecord> processedRecords;

        public PredictDemandWithModel(String jobUUID, List<ProcessedRecord> processedRecords) {
            this.jobUUID = jobUUID;
            this.processedRecords = processedRecords;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PredictDemand.class, msg -> {
                    System.out.println("PredictionModelActor received data with job ID: " + msg.jobUUID);
                    predictionModelService.createTrainTestData(msg.processedRecords);
                    predictionModelService.train();
                    predictionModelService.saveClassifier();
                })
                .match(PredictDemandWithModel.class, msg -> {
                    System.out.println("PredictionModelActor received data with job ID: " + msg.jobUUID);
                    predictionModelService.predict(msg.processedRecords);
                })
                .build();
    }

}
