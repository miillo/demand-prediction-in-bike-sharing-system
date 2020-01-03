package com.system.bikesharing.tripdata;

import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class TripDataActor extends AbstractBehavior<TripDataActorRouter.Command> {

    private TripDataActor(ActorContext<TripDataActorRouter.Command> context) {
        super(context);
        context.getLog().info("TripDataActor routee started");
    }

    public static Behavior<TripDataActorRouter.Command> create() {
        return Behaviors.setup(TripDataActor::new);
    }

    @Override
    public Receive<TripDataActorRouter.Command> createReceive() {
        return newReceiveBuilder()
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }

    private Behavior<TripDataActorRouter.Command> onPostStop() {
        getContext().getLog().info("TripDataActor routee stopped");
        return Behaviors.stopped();
    }
}
