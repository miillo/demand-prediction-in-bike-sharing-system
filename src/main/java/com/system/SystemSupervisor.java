package com.system;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.system.bikesharing.tripdata.TripDataActor;
import com.system.bikesharing.tripdata.TripDataActorRouter;

import java.util.List;

public class SystemSupervisor extends AbstractBehavior<Void> {

    private SystemSupervisor(ActorContext<Void> context) {
        super(context);
        context.getLog().info("Application started");
    }

    public static Behavior<Void> create() {
        return Behaviors.setup(SystemSupervisor::initializeSystem);
    }

    @Override
    public Receive<Void> createReceive() {
        return newReceiveBuilder()
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }

    private static SystemSupervisor initializeSystem(ActorContext<Void> context) {
        ActorRef<TripDataActorRouter.Command> tripDataActorRouter = context
                .spawn(TripDataActorRouter.create("001"), "tripDataActorRouter");

        return new SystemSupervisor(context);
    }

    private SystemSupervisor onPostStop() {
        getContext().getLog().info("Application stopped");
        return this;
    }
}
