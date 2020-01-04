package com.system.settings;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class AppSettings {

    public static int noOfWeatherActors;
    public static int noOfTripDataActors;
    public static int noOfStationDataActors;

    public static String httpDomain;
    public static int httpPort;

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
    }
}
