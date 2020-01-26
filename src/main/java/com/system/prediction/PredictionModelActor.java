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

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PredictDemand.class, msg -> {
                    System.out.println("PredictionModelActor received data with job ID: " + msg.jobUUID);
                    List<ProcessedRecord> trainData = predictionModelService.splitData(msg.processedRecords).get(false);
                    List<ProcessedRecord> testData = predictionModelService.splitData(msg.processedRecords).get(true);
                    System.out.println("Train data");
                    trainData.forEach(System.out::println);
                    System.out.println("Test data");
                    testData.forEach(System.out::println);


                    predictionModelService.wekaWeka(trainData, testData);
                })
                .build();
    }
}
