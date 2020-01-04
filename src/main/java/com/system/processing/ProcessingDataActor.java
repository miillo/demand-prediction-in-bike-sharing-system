package com.system.processing;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.system.bikesharing.tripdata.TripDataActor;
import com.system.prediction.PredictionModelActor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProcessingDataActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final String processingDataActorId;
    private final Map<String, ActorRef> actorRefMap;

    public ProcessingDataActor(String processingDataActorId, Map<String, ActorRef> actorRefMap) {
        log.info("ProcessingDataActor {} created", processingDataActorId);
        this.processingDataActorId = processingDataActorId;
        this.actorRefMap = createActorRefMap(actorRefMap);
    }

    static Props props(String processingDataActorId, Map<String, ActorRef> actorRefMap) {
        return Props.create(ProcessingDataActor.class, () ->
                new ProcessingDataActor(processingDataActorId, actorRefMap));
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

    private Map<String, ActorRef> createActorRefMap(Map<String, ActorRef> actorRefMap) {
        ActorRef persistenceActor = getContext().actorOf(Props.create(PersistenceActor.class, "0"), "PersistenceActor");
        ActorRef predictionModelActor = getContext().actorOf(Props.create(PredictionModelActor.class, "0"), "PredictionModelActor");
        actorRefMap.put("persistenceActor", persistenceActor);
        actorRefMap.put("predictionModelActor", predictionModelActor);
        return actorRefMap;
    }
}
