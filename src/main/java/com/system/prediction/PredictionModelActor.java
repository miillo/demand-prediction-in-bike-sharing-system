package com.system.prediction;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.system.pojo.ProcessedRecord;
import com.system.prediction.services.PredictionModelService;
import com.system.processing.ProcessingDataActor;
import org.joda.time.DateTime;
import weka.core.Instances;

import java.util.List;

public class PredictionModelActor extends AbstractLoggingActor {
    private final String predictionModelActorId;
    private final PredictionModelService predictionModelService;

    private DateTime actorDate;

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

    public static final class InitPrediction {
        public final String jobUUID;
        public final List<ProcessedRecord> processedRecords;
        public final DateTime date;

        public InitPrediction(String jobUUID, List<ProcessedRecord> processedRecords, DateTime date) {
            this.jobUUID = jobUUID;
            this.processedRecords = processedRecords;
            this.date = date;
        }
    }

    public static final class NextPrediction {
        public final Instances instances;
        public final DateTime dateTime;

        public NextPrediction(Instances instances, DateTime dateTime) {
            this.instances = instances;
            this.dateTime = dateTime;
        }
    }

    public static final class ReturnPrediction {
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(InitPrediction.class, msg -> {
                    this.actorDate = msg.date;
                    System.out.println("PredictionModelActor received INIT MESSAGE with job ID: " + msg.jobUUID);
                    predictionModelService.createTrainTestData(msg.processedRecords);
                    Instances computedInstances = predictionModelService.initialComputation();

                    getContext().getParent().tell(new ProcessingDataActor.InstancesData(computedInstances, actorDate.plusDays(1)), getSelf());
                })
                .match(NextPrediction.class, msg -> {
                    this.actorDate = msg.dateTime;
                    Instances compute = predictionModelService.compute(msg.instances);
                    getContext().getParent().tell(new ProcessingDataActor.InstancesData(compute, actorDate.plusDays(1)), getSelf());
                })
                .match(ReturnPrediction.class, msg -> {
                    predictionModelService.predict(this.actorDate);
                })
                .build();
    }

}
