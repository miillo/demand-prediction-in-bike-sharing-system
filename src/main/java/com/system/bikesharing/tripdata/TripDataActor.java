package com.system.bikesharing.tripdata;

import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class TripDataActor extends AbstractBehavior<TripDataActor.Command> {
    private final String tripDataActorId;

    public interface Command {
    }

    public static final class DownloadTripData implements Command {
        final long requestId;
        final ActorRef<DownloadedTripData> replyTo;

        public DownloadTripData(long requestId, ActorRef<DownloadedTripData> replyTo) {
            this.requestId = requestId;
            this.replyTo = replyTo;
        }
    }

    public static final class DownloadedTripData implements Command {
        final long requestId;
        final String tripData; //probably parsed JSON

        public DownloadedTripData(long requestId, String tripData) {
            this.requestId = requestId;
            this.tripData = tripData;
        }
    }

    private TripDataActor(ActorContext<Command> context, String tripDataActorId) {
        super(context);
        this.tripDataActorId = tripDataActorId;
        context.getLog().info("TripDataActor {} started", tripDataActorId);
    }

    public static Behavior<Command> create(String tripDataActorId) {
        return Behaviors.setup(context -> new TripDataActor(context, tripDataActorId));
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }

    private Behavior<Command> onPostStop() {
        getContext().getLog().info("TripDataActor {} stopped", tripDataActorId);
        return Behaviors.stopped();
    }
}
