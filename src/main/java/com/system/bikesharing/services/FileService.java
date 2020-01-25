package com.system.bikesharing.services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.system.pojo.Station;
import com.system.pojo.Trip;
import com.system.pojo.UserRequest;
import com.system.settings.AppSettings;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FileService {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String REQUEST_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * Reads stations data from CSV file
     *
     * @param userRequest user request data
     * @return list of filtered station data
     * @throws IOException whether file exception occurred
     */
    public List<Station> readStationsData(UserRequest userRequest) throws IOException {
        String stationsPath = AppSettings.stationsPath;
        Reader reader = Files.newBufferedReader(Paths.get(stationsPath));

        List<Station> stations = new CsvToBeanBuilder<Station>(reader)
                .withType(Station.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build()
                .parse();

        return stations.stream()
                .filter(station -> parseDate(station.getActionTime()).after(parseRequestStartDate(userRequest.getStartDate())))
                .filter(station -> parseDate(station.getActionTime()).before(parseRequestEndDate(userRequest.getEndDate())))
                .collect(Collectors.toList());
    }

    /**
     * Reads trips data from CSV file
     *
     * @param userRequest user request data
     * @return list of filtered trips data
     * @throws IOException whether file exception occurred
     */
    public List<Trip> readTripsData(UserRequest userRequest) throws IOException {
        String tripsPath = AppSettings.tripsPath;
        Reader reader = Files.newBufferedReader(Paths.get(tripsPath));

        CsvToBean<Trip> csvToBean = new CsvToBeanBuilder<Trip>(reader)
                .withType(Trip.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        return csvToBean.parse().stream()
                .filter(trip -> parseDate(trip.getStartTime()).after(parseRequestStartDate(userRequest.getStartDate())))
                .filter(trip -> parseDate(trip.getEndTime()).before(parseRequestEndDate(userRequest.getEndDate())))
                .collect(Collectors.toList());
    }

    private Date parseRequestStartDate(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(REQUEST_DATE_PATTERN);
        return dateTimeFormatter.parseDateTime(date).toDate();
    }

    private Date parseRequestEndDate(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(REQUEST_DATE_PATTERN);
        return dateTimeFormatter.parseDateTime(date).plusDays(1).minusMillis(1).toDate();
    }

    private Date parseDate(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_PATTERN);
        return dateTimeFormatter.parseDateTime(removeLastZero(date)).toDate();
    }

    private static String removeLastZero(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == '0') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
