package com.system.bikesharing.tripdata;

import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class TripDataActor extends AbstractBehavior<TripDataActorRouter.Command> {
    private final String tripDataActorId;

    public interface Command {
    }

    private TripDataActor(ActorContext<TripDataActorRouter.Command> context, String tripDataActorId) {
        super(context);
        this.tripDataActorId = tripDataActorId;
        context.getLog().info("TripDataActor {} started", tripDataActorId);
    }

    public static Behavior<TripDataActorRouter.Command> create(String tripDataActorId) {
        return Behaviors.setup(context -> new TripDataActor(context, tripDataActorId));
    }

    @Override
    public Receive<TripDataActorRouter.Command> createReceive() {
        return newReceiveBuilder()
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }

    private Behavior<TripDataActorRouter.Command> onPostStop() {
        getContext().getLog().info("TripDataActor {} stopped", tripDataActorId);
        return Behaviors.stopped();
    }
}
