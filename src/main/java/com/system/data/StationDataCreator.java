package com.system.data;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

import static java.util.stream.Collectors.toMap;

public final class StationDataCreator {

    // /home/michal/Downloads/tripdata.csv
    // D:\Coding\Java\demand-prediction-in-bike-sharing-system
    private static final String CSV_FILE = "D:\\Coding\\Java\\demand-prediction-in-bike-sharing-system\\JC-201909-citibike-tripdata.csv";
//    private static final String STATIONS_DATA_CSV = "stations_data.csv";
    private static final String STATIONS_DATA_CSV = "D:\\Coding\\Java\\stations_data.csv";

    public static void run() {
        try {
            Map<String, Map<Date, Integer>> stationTimeTypeMap = getDataForEachStationWithTimeAndTypeOfAction();
            Map<String, Map<Date, Integer>> sortedTimeTypeMapMap = sortDataForEachStationByTime(stationTimeTypeMap);
            Map<String, Map<Date, Integer>> bikeCountMap = getCountOfBikesForEachStation(sortedTimeTypeMapMap);
            saveResult(bikeCountMap);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Map<Date, Integer>> getDataForEachStationWithTimeAndTypeOfAction() throws IOException, ParseException {
        Map<String, Map<Date, Integer>> stationMap = new HashMap<>();

        CSVReader reader;
        reader = new CSVReader(new FileReader(CSV_FILE));
        reader.readNext();
        String[] line;
        while ((line = reader.readNext()) != null) {
            System.out.println();
            readDeparture(stationMap, line);
            readArrival(stationMap, line);
        }

        return stationMap;
    }

    private static Map<String, Map<Date, Integer>> sortDataForEachStationByTime(Map<String, Map<Date, Integer>> stationTimeTypeMap) {
        Map<String, Map<Date, Integer>> sortedTimeTypeMapMap = new LinkedHashMap<>();

        for (Map.Entry<String, Map<Date, Integer>> m : stationTimeTypeMap.entrySet()) {
            Map<Date, Integer> sorted = m.getValue().entrySet().stream()
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
            sortedTimeTypeMapMap.put(m.getKey(), sorted);
        }
        return sortedTimeTypeMapMap;
    }

    private static Map<Date, String[]> createdWriteMap(Map<String, Map<Date, Integer>> bikeCountMap) throws IOException, ParseException {
        String[] line;
        Map<Date, String[]> writeMap = new LinkedHashMap<>();
        CSVReader reader = new CSVReader(new FileReader(CSV_FILE));
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            writeMap.put(parseDate(1, line),
                    writeDeparture(bikeCountMap, line));
            writeMap.put(parseDate(2, line),
                    writeArrival(bikeCountMap, line));
        }
        return writeMap;
    }

    private static Map<String, Map<Date, Integer>> getCountOfBikesForEachStation(Map<String, Map<Date, Integer>> sortedTimeMap) {
        return sortedTimeMap.entrySet().stream().
                collect(toMap(Map.Entry::getKey, StationDataCreator::getAllCountsOfBikesForStation, (a, b) -> b, LinkedHashMap::new));
    }

    private static Map<Date, Integer> getAllCountsOfBikesForStation(Map.Entry<String, Map<Date, Integer>> stationEntry) {
        int day = 1;
        int prevCount = getBikeCountPerStation(stationEntry.getKey());
        Map<Date, Integer> bikeCountMap = new LinkedHashMap<>();

        for (Map.Entry<Date, Integer> entry : stationEntry.getValue().entrySet()) {
            if (entry.getKey().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth() > day) {
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTime(entry.getKey());
                if (calendar.get(Calendar.HOUR) > 5) {
                    day = entry.getKey().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth();
                    prevCount = getBikeCountPerStation(stationEntry.getKey());
                }
            }
            if (entry.getValue() == 0) {
                prevCount--;
            } else {
                prevCount++;
            }
            bikeCountMap.put(entry.getKey(), prevCount);

            if (prevCount < 0) {
                System.out.println(entry.getKey() + " " + stationEntry.getKey() + " " + prevCount);
            }
        }

        return bikeCountMap;
    }

    private static void saveResult(Map<String, Map<Date, Integer>> bikeCountMap) throws IOException, ParseException {
        CSVWriter writer;
        writer = new CSVWriter(new FileWriter(STATIONS_DATA_CSV));

        Map<Date, String[]> sortedWriteMap = createdWriteMap(bikeCountMap).entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        writer.writeNext(getLabel());
        writer.writeAll(sortedWriteMap.values());
        writer.close();
    }

    private static String[] writeDeparture(Map<String, Map<Date, Integer>> bikeCountMap, String[] line) throws ParseException {
        final String[] writeLine = createWriteLine(
                new String[]{line[3], line[1], "0", line[4], line[5], line[6], line[11],
                        getCurrecntBikeCount(bikeCountMap, line, 3, 1), getMaxBikeCount(line[3])});
        return writeLine;
    }

    private static String[] writeArrival(Map<String, Map<Date, Integer>> bikeCountMap, String[] line) throws ParseException {
        final String[] writeLine = createWriteLine(
                new String[]{line[7], line[2], "1", line[8], line[9], line[10], line[11],
                        getCurrecntBikeCount(bikeCountMap, line, 7, 2), getMaxBikeCount(line[7])});
        return writeLine;
    }

    private static String[] getLabel() {
        return createWriteLine(
                new String[]{"station id", "time", "type", "station name", "station latitude",
                        "station longitude", "bike_id", "current bike count", "initial bike count"});
    }

    private static String[] createWriteLine(String[] text) {
        String[] line = new String[9];
        System.arraycopy(text, 0, line, 0, 9);
        return line;
    }

    private static String getMaxBikeCount(String key) {
        return String.valueOf(getCountMap().get(key));
    }

    private static String getCurrecntBikeCount(Map<String, Map<Date, Integer>> bikeCountMap, String[] line, int i, int i2) throws ParseException {
        return String.valueOf(bikeCountMap.get(line[i]).get(parseDate(i2, line)));
    }

    private static int getBikeCountPerStation(String id) {
        return getCountMap().get(id);
    }

    private static void readDeparture(Map<String, Map<Date, Integer>> timeMap, String[] line) throws ParseException {
        Map<Date, Integer> dateMap = !timeMap.containsKey(line[3]) ? new LinkedHashMap<>()
                : timeMap.get(line[3]);
        Date parse = parseDate(1, line);

        dateMap.put(parse, 0);
        timeMap.put(line[3], dateMap);
    }

    private static void readArrival(Map<String, Map<Date, Integer>> timeMap, String[] line) throws ParseException {
        Map<Date, Integer> dateMap = !timeMap.containsKey(line[7]) ? new LinkedHashMap<>()
                : timeMap.get(line[7]);
        Date parse = parseDate(2, line);

        dateMap.put(parse, 1);
        timeMap.put(line[7], dateMap);
    }

    private static Date parseDate(int i, String[] line) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(removeLastZero(line[i]));
    }

    private static String removeLastZero(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == '0') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private static Map<String, Integer> getCountMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("3280", 15);
        map.put("3681", 15);
        map.put("3640", 10);
        map.put("3483", 11);
        map.put("3481", 12);
        map.put("3281", 11);
        map.put("3638", 13);
        map.put("3639", 14);
        map.put("3279", 25);
        map.put("3278", 39);

        map.put("3677", 10);
        map.put("3678", 17);
        map.put("3679", 11);
        map.put("3694", 10);
        map.put("3211", 20);
        map.put("3210", 15);
        map.put("3207", 24);
        map.put("3206", 18);
        map.put("3205", 15);
        map.put("3203", 81);

        map.put("3202", 26);
        map.put("3201", 10);
        map.put("3209", 26);
        map.put("3187", 15);
        map.put("3220", 15);
        map.put("3186", 10);
        map.put("3185", 11);
        map.put("3184", 10);
        map.put("3214", 16);
        map.put("3213", 23);

        map.put("3212", 12);
        map.put("3192", 24);
        map.put("3191", 10);
        map.put("3791", 13);
        map.put("3792", 10);
        map.put("3277", 12);
        map.put("3199", 13);
        map.put("3276", 33);
        map.put("3275", 10);
        map.put("3198", 10);

        map.put("3196", 10);
        map.put("3273", 14);
        map.put("3195", 38);
        map.put("3272", 19);
        map.put("3194", 28);
        map.put("3270", 36);
        map.put("3193", 11);
        map.put("3426", 10);
        map.put("3225", 11);
        map.put("3269", 39);

        map.put("3268", 17);
        map.put("3267", 34);

        return map;
    }
}
