package com.system;

import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class SystemSupervisor extends AbstractBehavior<Void> {

    private SystemSupervisor(ActorContext<Void> context) {
        super(context);
        context.getLog().info("Application started");
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

    private SystemSupervisor onPostStop() {
        getContext().getLog().info("Application stopped");
        return this;
    }
}
