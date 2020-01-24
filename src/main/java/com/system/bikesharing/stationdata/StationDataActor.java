package com.system.bikesharing.stationdata;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.system.bikesharing.services.FileService;
import com.system.pojo.Station;
import com.system.pojo.UserRequest;

import java.io.IOException;
import java.util.List;

public class StationDataActor extends AbstractLoggingActor {
    private final String stationDataActorId;
    private final FileService fileService;

    private StationDataActor(String stationDataActorId) {
        this.stationDataActorId = stationDataActorId;
        this.fileService = new FileService();
        log().info("StationDataActor {} created", stationDataActorId);
    }

    static Props props(String stationDataActorId) {
        return Props.create(StationDataActor.class, () -> new StationDataActor(stationDataActorId));
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    public static final class DownloadStationsData {
        public final UserRequest userRequest;

        public DownloadStationsData(UserRequest userRequest) {
            this.userRequest = userRequest;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(DownloadStationsData.class, msg -> {
                    try {
                        List<Station> stations = fileService.readStationsData(msg.userRequest);
                        getSender().tell(new StationDataActorRouter.DownloadedStationsData(stations), self());
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }

                })
                .build();
    }
}
