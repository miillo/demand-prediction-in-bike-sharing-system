package com.system.processing;

import akka.actor.AbstractLoggingActor;
import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.system.bikesharing.stationdata.StationDataActorRouter;
import com.system.bikesharing.tripdata.TripDataActorRouter;
import com.system.pojo.*;
import com.system.pojo.weather.WeatherAPI;
import com.system.prediction.PredictionModelActor;
import com.system.processing.services.ProcessingDataService;
import com.system.settings.AppSettings;
import com.system.weatherdata.WeatherDataActorRouter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import weka.core.Instances;

import java.io.File;
import java.util.*;

public class ProcessingDataActor extends AbstractLoggingActor {
    private final String processingDataActorId;
    private final Map<String, ActorRef> actorRefMap;
    private final Map<String, PredictionData> predictionDataMap;
    private final ProcessingDataService processingDataService;
    private int stationId;

    private Map<String, UserRequest> jobUuidUserRequest;
    private final int PREDICTION_KIDS = 20; //todo do application.conf
    private int predCreationCounter = 0;
    private Map<String, ActorRef> datePredActorMap; // data - aktor ktory ja trzyma

    public ProcessingDataActor(String processingDataActorId) {
        log().info("ProcessingDataActor {} created", processingDataActorId);
        this.processingDataActorId = processingDataActorId;
        this.jobUuidUserRequest = new HashMap<>();
        this.predictionDataMap = new HashMap<>();
        this.processingDataService = new ProcessingDataService();
        this.actorRefMap = createChildActors();
        this.datePredActorMap = new HashMap<>();
    }

    static Props props(String processingDataActorId) {
        return Props.create(ProcessingDataActor.class, () ->
                new ProcessingDataActor(processingDataActorId));
    }

    public static final class HandleUserRequest {
        final UserRequest userRequest;

        public HandleUserRequest(UserRequest userRequest) {
            this.userRequest = userRequest;
        }
    }

    public static final class HandlePredictionRequest {
        final PredRequest predRequest;

        public HandlePredictionRequest(PredRequest predRequest) {
            this.predRequest = predRequest;
        }
    }

    public static final class WeatherApiData {
        public final WeatherAPI weatherAPI;
        public final String jobUUID;

        public WeatherApiData(WeatherAPI weatherAPI, String jobUUID) {
            this.weatherAPI = weatherAPI;
            this.jobUUID = jobUUID;
        }
    }

    public static final class StationsData {
        public final List<Station> stations;
        public final String jobUUID;

        public StationsData(List<Station> stations, String jobUUID) {
            this.stations = stations;
            this.jobUUID = jobUUID;
        }
    }

    public static final class TripsData {
        public final List<Trip> trips;
        public final String jobUUID;

        public TripsData(List<Trip> trips, String jobUUID) {
            this.trips = trips;
            this.jobUUID = jobUUID;
        }
    }

    public static final class InstancesData {
        public final Instances instances;
        public final DateTime date;

        public InstancesData(Instances instances, DateTime date) {
            this.instances = instances;
            this.date = date;
        }
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(HandleUserRequest.class, msg -> {
                    String jobUUID = UUID.randomUUID().toString();
                    this.jobUuidUserRequest.put(jobUUID, msg.userRequest);
                    this.stationId = Integer.parseInt(msg.userRequest.getStationId());

                    if (AppSettings.generateFlag) {
                        predictionDataMap.put(jobUUID, new PredictionData());
                        actorRefMap.get("weatherGeoActorRouter").tell(new WeatherDataActorRouter.DownloadWeatherData(msg.userRequest, jobUUID), getSelf());
                        actorRefMap.get("stationDataActorRouter").tell(new StationDataActorRouter.DownloadStationsData(msg.userRequest, jobUUID), getSelf());
                        actorRefMap.get("tripDataActorRouter").tell(new TripDataActorRouter.DownloadTripsData(msg.userRequest, jobUUID), getSelf());
                    } else {
                        actorRefMap.get("persistenceActor").tell(new PersistenceActor.GetCollectedDataforStation(jobUUID, stationId), self());
                    }
                })
                .match(WeatherApiData.class, msg -> {
                    System.out.println("ProcessingDataActor received Weather API data for job: " + msg.jobUUID);
                    PredictionData predictionData = predictionDataMap.get(msg.jobUUID);
                    predictionData.setWeatherAPI(msg.weatherAPI);
                    predictionData.incrementDataCompleteness();
                    if (predictionData.getDataCompleteness() == 3) {
                        actorRefMap.get("persistenceActor").tell(new PersistenceActor.SaveCollectedData(msg.jobUUID, predictionData), self());
                        predictionDataMap.remove(msg.jobUUID);
                    }
                })
                .match(StationsData.class, msg -> {
                    System.out.println("ProcessingDataActor received stations data for job: " + msg.jobUUID);
                    PredictionData predictionData = predictionDataMap.get(msg.jobUUID);
                    predictionData.setStations(msg.stations);
                    predictionData.incrementDataCompleteness();
                    if (predictionData.getDataCompleteness() == 3) {
                        actorRefMap.get("persistenceActor").tell(new PersistenceActor.SaveCollectedData(msg.jobUUID, predictionData), self());
                        predictionDataMap.remove(msg.jobUUID);
                    }
                })
                .match(TripsData.class, msg -> {
                    System.out.println("ProcessingDataActor received trips data for job: " + msg.jobUUID);
                    PredictionData predictionData = predictionDataMap.get(msg.jobUUID);
                    predictionData.setTrips(msg.trips);
                    predictionData.incrementDataCompleteness();
                    if (predictionData.getDataCompleteness() == 3) {
                        actorRefMap.get("persistenceActor").tell(new PersistenceActor.SaveCollectedData(msg.jobUUID, predictionData), self());
                        predictionDataMap.remove(msg.jobUUID);
                    }
                })
                .match(PersistenceActor.GetCollectedData.class, msg -> {
                    //todo to wykonywane tylko raz na endpoint request-pred

                    List<ProcessedRecord> processedRecords = processingDataService.prepareDataForStation(msg.predictionData, stationId);

                    if (predCreationCounter != 0) {
                        System.out.println("@@@@@@@@@@@@@@@@@@@@@ PREDCREATIONCOUNTER != 0 @@@@@@@@@@@@@@@@@@@@@@");
                        System.exit(1);
                    }

                    UserRequest userRequest = jobUuidUserRequest.get(msg.jobUUID);

                    ActorRef predictionModelActor = getContext().actorOf(Props.create(PredictionModelActor.class, "0"), "PredictionModelActor" + predCreationCounter);

                    System.out.println(userRequest);
                    DateTime dateTime = parseRequestStartDate(userRequest.getStartDate());

                    predictionModelActor.tell(new PredictionModelActor.InitPrediction(msg.jobUUID, processedRecords, dateTime), self());
                    datePredActorMap.put(dateTime.toString(), predictionModelActor);
                    predCreationCounter++;

//                    actorRefMap.get("predictionModelActor").tell(new PredictionModelActor.InitPrediction(msg.jobUUID, processedRecords, userRequest.getStartDate()), self());
                })
                .match(InstancesData.class, msg -> {
                    if (predCreationCounter == PREDICTION_KIDS) {
                        System.out.println("@@@@@@@@@@ PREDICTION KIDS CREATED @@@@@@@@@@");
                        return;
                    }

                    ActorRef predictionModelActor = getContext().actorOf(Props.create(PredictionModelActor.class, "0"), "PredictionModelActor" + predCreationCounter);
                    predictionModelActor.tell(new PredictionModelActor.NextPrediction(msg.instances, msg.date), self());
                    datePredActorMap.put(msg.date.toString(), predictionModelActor);
                    predCreationCounter++;
                })
                .match(HandlePredictionRequest.class, msg -> {
                    DateTime dateTime = parseRequestStartDate(msg.predRequest.getPredDate());
                    ActorRef predActor = datePredActorMap.get(dateTime.toString());
                    if (predActor == null) {
                        System.out.println("!!!!!!!!!!!!!!!!!! PRED DATE NOE AVAILABLE !!!!!!!!!!!!!");
                        return;
                    }

                    predActor.tell(new PredictionModelActor.ReturnPrediction(), self());

                })
                .build();
    }

    /**
     * Creates child actors: PersistenceActor | PredictionModelActor | TripDataActorRouter | StationDataActorRouter
     * WeatherDataActorRouter
     *
     * @return updated map with actor references
     */
    private Map<String, ActorRef> createChildActors() {
        ActorRef tripDataActorRouter = getContext()
                .actorOf(Props.create(TripDataActorRouter.class, "0"), "tripDataActorRouter");
        ActorRef stationDataActorRouter = getContext()
                .actorOf(Props.create(StationDataActorRouter.class, "0"), "stationDataActorRouter");
        ActorRef weatherGeoActorRouter = getContext()
                .actorOf(Props.create(WeatherDataActorRouter.class, "0"), "weatherGeoActorRouter");

        ActorRef persistenceActor = getContext().actorOf(Props.create(PersistenceActor.class, "0"), "PersistenceActor");
        ActorRef predictionModelActor = getContext().actorOf(Props.create(PredictionModelActor.class, "0"), "PredictionModelActor");    //todo do usuniecia

        return new HashMap<String, ActorRef>() {{
            put("tripDataActorRouter", tripDataActorRouter);
            put("stationDataActorRouter", stationDataActorRouter);
            put("weatherGeoActorRouter", weatherGeoActorRouter);
            put("persistenceActor", persistenceActor);
            put("predictionModelActor", predictionModelActor);
        }};
    }


    private DateTime parseRequestStartDate(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        Date date1 = dateTimeFormatter.parseDateTime(date).toDate();
        return new DateTime(date1);
    }
}
