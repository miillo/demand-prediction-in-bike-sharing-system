package com.system.bikesharing.tripdata;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.system.bikesharing.services.FileService;
import com.system.pojo.Trip;
import com.system.pojo.UserRequest;

import java.io.IOException;
import java.util.List;

public class TripDataActor extends AbstractLoggingActor {
    private final String tripDataActorId;
    private final FileService fileService;

    private TripDataActor(String tripDataActorId) {
        this.tripDataActorId = tripDataActorId;
        this.fileService = new FileService();
        log().info("TripDataActor {} created", tripDataActorId);
    }

    static Props props(String tripDataActorId) {
        return Props.create(TripDataActor.class, () -> new TripDataActor(tripDataActorId));
    }

    public static final class DownloadTripsData {
        public final UserRequest userRequest;
        public final String jobUUID;

        public DownloadTripsData(UserRequest userRequest, String jobUUID) {
            this.userRequest = userRequest;
            this.jobUUID = jobUUID;
        }
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(DownloadTripsData.class, msg -> {
                    try {
                        List<Trip> trips = fileService.readTripsData(msg.userRequest);
                        getSender().tell(new TripDataActorRouter.DownloadedTripsData(trips, msg.jobUUID), self());
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                })
                .build();
    }
}
