package com.system.processing.services;

import com.system.pojo.PredictionData;
import com.system.pojo.ProcessedRecord;
import com.system.pojo.Station;
import com.system.pojo.weather.Weather;
import com.system.settings.AppSettings;
import org.joda.time.DateTime;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class ProcessingDataService {

    public List<ProcessedRecord> prepareDataForStation(PredictionData predictionData, int stationId) {
        List<Station> stations = predictionData.getStations().stream()
                .filter(station -> station.getId() == stationId)
                .collect(Collectors.toList());
        return prepareProcessedRecords(predictionData, stations);
    }

    private List<ProcessedRecord> prepareProcessedRecords(PredictionData predictionData, List<Station> stations) {
        List<ProcessedRecord> processedData = new ArrayList<>();

        Date startDate = new DateTime(2019, 9, AppSettings.startDay, 0, 0).toDate();
        Date endDate = new DateTime(2019, 9, AppSettings.endDay, 23, 59).toDate();

        stations.forEach(s -> s.setActionTime(trimDate(s.getActionTime())));

        Map<String, Map<Integer, List<Station>>> dateMapTypeMap = stations.stream()
                .filter(station -> {

                   return parseDate(station.getActionTime()).equals(startDate) ||
                            (parseDate(station.getActionTime()).after(startDate) && parseDate(station.getActionTime()).before(endDate));

//                    return !(parseDate(station.getActionTime()).after(startDate) && parseDate(station.getActionTime()).before(endDate));
                })
                .collect(groupingBy(Station::getActionTime, groupingBy(Station::getType)));

        for (String d : dateMapTypeMap.keySet()) {
            Map<Integer, List<Station>> m = dateMapTypeMap.get(d);
            int demand = m.get(0).size() - m.get(1).size();
            Station s = m.get(0).get(0);
            Optional<Weather> w = getWeatherByDate(predictionData, s);
            if (w.isPresent()) {
                processedData.add(
                        new ProcessedRecord(demand, s.getId(), s.getStationName(), s.getStationLatitude(), s.getStationLongitude(),
                                w.get().getTemperature(), w.get().getWindspeed(), w.get().getPressure()));
            } else {
                processedData.add(
                        new ProcessedRecord(demand, s.getId(), s.getStationName(), s.getStationLatitude(), s.getStationLongitude(),
                                0.0, 0.0, 0.0));
            }
        }

        return processedData;
    }

    private String trimDate(String date) {
        return date.substring(0, 10);
    }

    private Date parseDate(String date) {
        return new DateTime(date).toDate();
    }

    private Optional<Weather> getWeatherByDate(PredictionData predictionData, Station s) {
        return predictionData.getWeathers().stream()
                .filter(w -> w.getDate().equals(s.getActionTime().substring(0, 10)))
                .findFirst();
    }
}
