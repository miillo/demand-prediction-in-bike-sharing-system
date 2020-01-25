package com.system.settings;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;

public class AppSettings {

    public static int noOfWeatherActors;
    public static int noOfTripDataActors;
    public static int noOfStationDataActors;

    public static String httpDomain;
    public static int httpPort;

    public static String databaseName;
    public static String databaseDomain;
    public static int databasePort;

    public static String stationsPath;
    public static String tripsPath;

    public static double trainDataRatio;
    public static double testDataRatio;

    /**
     * Reads configuration file
     */
    public static void readConfig() {
        Config conf = ConfigFactory.load();

        noOfWeatherActors = conf.getInt("weather-data.no-of-actors");
        noOfTripDataActors = conf.getInt("trip-data.no-of-actors");
        noOfStationDataActors = conf.getInt("station-data.no-of-actors");
        httpDomain = conf.getString("http.domain");
        httpPort = conf.getInt("http.port");
        databaseName = conf.getString("database.name");
        databaseDomain = conf.getString("database.domain");
        databasePort = conf.getInt("database.port");
        stationsPath = replaceFileSeparator(conf.getString("file-path.stations"));
        tripsPath = replaceFileSeparator(conf.getString("file-path.trips"));
        trainDataRatio = conf.getDouble("data-ratio.train");
        testDataRatio = conf.getDouble("data-ratio.test");
    }

    private static String replaceFileSeparator(String path) {
        return path.replace('\\', File.separatorChar);
    }
}
