package com.system.processing.services;

import com.system.pojo.PredictionData;
import com.system.pojo.ProcessedRecord;
import com.system.pojo.Station;
import com.system.pojo.Trip;
import com.system.pojo.weather.Weather;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProcessingDataService {

    public List<ProcessedRecord> prepareData(PredictionData predictionData) {
        List<Station> stations = predictionData.getStations();
        List<ProcessedRecord> processedData = new ArrayList<>();

        int index = 1;
        for (Station s : stations) {
            Optional<Weather> w = getWeatherByDate(predictionData, s);
            Optional<Trip> t = getTripForStation(s, predictionData.getTrips());

            if (t.isPresent() && w.isPresent()) {
                processedData.add(
                        new ProcessedRecord(index++, s.getType(), s.getActionTime(), s.getId(), s.getStationName(),
                                s.getStationLatitude(), s.getStationLongitude(), s.getBikeId(), s.getCurrentBikeCount(),
                                s.getInitialBikeCount(), t.get().getUserType(), t.get().getBirthYear(), t.get().getGender(),
                                w.get().getTemperature(), w.get().getWindspeed(), w.get().getPressure()));
            }
        }


        return processedData;
    }

    private Optional<Weather> getWeatherByDate(PredictionData predictionData, Station s) {
        return predictionData.getWeatherAPI().getWeathersByDay().stream()
                .filter(w -> w.getDate().equals(s.getActionTime().substring(0, 10)))
                .findFirst();
    }

    private Optional<Trip> getTripForStation(Station station, List<Trip> trips) {
        return station.getType() == 0 ? getDepartureTrip(station, trips)
                : getArrivalTrip(station, trips);
    }

    private Optional<Trip> getArrivalTrip(Station station, List<Trip> trips) {
        return trips.stream()
                .filter(trip -> trip.getEndTime().equals(station.getActionTime()))
                .filter(trip -> trip.getEndStationId() == station.getId())
                .findFirst();
    }

    private Optional<Trip> getDepartureTrip(Station station, List<Trip> trips) {
        return trips.stream()
                .filter(trip -> trip.getStartTime().equals(station.getActionTime()))
                .filter(trip -> trip.getStartStationId() == station.getId())
                .findFirst();
    }
}
