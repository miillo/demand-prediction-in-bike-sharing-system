package com.system.processing;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.system.pojo.UserRequest;
import com.system.prediction.PredictionModelActor;

import java.util.Map;

public class ProcessingDataActor extends AbstractLoggingActor {
    private final String processingDataActorId;
    private final Map<String, ActorRef> actorRefMap;

    public ProcessingDataActor(String processingDataActorId, Map<String, ActorRef> actorRefMap) {
        log().info("ProcessingDataActor {} created", processingDataActorId);
        this.processingDataActorId = processingDataActorId;
        this.actorRefMap = createChildActors(actorRefMap);
    }

    static Props props(String processingDataActorId, Map<String, ActorRef> actorRefMap) {
        return Props.create(ProcessingDataActor.class, () ->
                new ProcessingDataActor(processingDataActorId, actorRefMap));
    }

    public static final class HandleUserRequest {
        final UserRequest userRequest;

        public HandleUserRequest(UserRequest userRequest) {
            this.userRequest = userRequest;
        }
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(HandleUserRequest.class, userRequest -> {
                    //todo wysylanie requestow o dane
                })
                .build();
    }

    /**
     * Creates child actors: PersistenceActor and PredictionModelActor
     *
     * @param actorRefMap map with actor references
     * @return updated map with actor references
     */
    private Map<String, ActorRef> createChildActors(Map<String, ActorRef> actorRefMap) {
        ActorRef persistenceActor = getContext().actorOf(Props.create(PersistenceActor.class, "0"), "PersistenceActor");
        ActorRef predictionModelActor = getContext().actorOf(Props.create(PredictionModelActor.class, "0"), "PredictionModelActor");
        actorRefMap.put("persistenceActor", persistenceActor);
        actorRefMap.put("predictionModelActor", predictionModelActor);
        return actorRefMap;
    }
}
