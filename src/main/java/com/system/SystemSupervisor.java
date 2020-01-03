package com.system;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.system.bikesharing.tripdata.TripDataActorRouter;

public class SystemSupervisor extends AbstractBehavior<Void> {
    private SystemSupervisor(ActorContext<Void> context) {
        super(context);
        context.getLog().info("Application started");
        initializeSystem(context);
    }

    public static Behavior<Void> create() {
        return Behaviors.setup(SystemSupervisor::new);
    }

    @Override
    public Receive<Void> createReceive() {
        return newReceiveBuilder()
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }

    //todo return value probably useless
    private static Behavior<Void> initializeSystem(ActorContext<Void> context) {
        ActorRef<TripDataActorRouter.Command> tripDataActorRouter = context
                .spawn(TripDataActorRouter.create("001"), "tripDataActorRouter");

        return Behaviors.same();
    }

    private SystemSupervisor onPostStop() {
        getContext().getLog().info("Application stopped");
        return this;
    }
}