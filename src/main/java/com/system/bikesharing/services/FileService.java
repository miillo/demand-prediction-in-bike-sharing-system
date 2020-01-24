package com.system.bikesharing.services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.system.pojo.Station;
import com.system.pojo.UserRequest;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileService {


    /**
     * Reads stations data from CSV file
     *
     * @param userRequest user request data
     * @return list of filtered station data
     * @throws IOException whether file exception occurred
     */
    public List<Station> readStationsData(UserRequest userRequest) throws IOException {
        List<Station> stations = new ArrayList<>();
        //todo sciezka do configa???
        try (Reader reader = Files.newBufferedReader(Paths.get("src\\main\\resources\\stations_data_small.csv"))) {
            CsvToBean<Station> csvToBean = new CsvToBeanBuilder<Station>(reader)
                    .withType(Station.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<Station> csvStationIterator = csvToBean.iterator();

            while (csvStationIterator.hasNext()) {
                Station station = csvStationIterator.next();
                //todo filtr po dacie na podstawie userRequest
                stations.add(station);
            }
        }
        return stations;
    }
}
