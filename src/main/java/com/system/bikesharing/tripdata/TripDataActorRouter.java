package com.system.bikesharing.tripdata;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.*;

public class TripDataActorRouter extends AbstractBehavior<TripDataActorRouter.Command> {
    private final String tripDataActorRouterId;
    private final ActorRef<Command> router;

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

    private TripDataActorRouter(ActorContext<Command> context, String tripDataActorRouterId) {
        super(context);
        this.tripDataActorRouterId = tripDataActorRouterId;
        context.getLog().info("TripDataActorRouter {} started", tripDataActorRouterId);
        this.router = createTripDataRouter();
    }

    public static Behavior<Command> create(String tripDataActorRouterId) {
        return Behaviors.setup(context -> new TripDataActorRouter(context, tripDataActorRouterId));
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }


    private ActorRef<Command> createTripDataRouter() {
        int poolSize = 4; //todo poolsize from config file
        PoolRouter<Command> pool = Routers
                .pool(
                        poolSize,
                        Behaviors.supervise(TripDataActor.create()).onFailure(SupervisorStrategy.restart()))
                .withRoundRobinRouting();

        return getContext().spawn(pool, "tripDataWorkerPool");
    }

    private Behavior<Command> onPostStop() {
        getContext().getLog().info("TripDataActorRouter {} stopped", tripDataActorRouterId);
        return Behaviors.stopped();
    }
}
