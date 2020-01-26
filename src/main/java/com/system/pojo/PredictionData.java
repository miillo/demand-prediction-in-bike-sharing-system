package com.system.pojo;

import com.system.pojo.weather.Weather;
import com.system.pojo.weather.WeatherAPI;

import java.util.ArrayList;
import java.util.List;

public class PredictionData {
    private int dataCompleteness = 0;
    private List<Trip> trips;
    private List<Station> stations;
    private WeatherAPI weatherAPI;
    private List<Weather> weathers;

    public PredictionData() {
        this.trips = new ArrayList<>();
        this.stations = new ArrayList<>();
    }

    public void incrementDataCompleteness() {
        dataCompleteness++;
    }

    public int getDataCompleteness() {
        return dataCompleteness;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public WeatherAPI getWeatherAPI() {
        return weatherAPI;
    }

    public void setWeatherAPI(WeatherAPI weatherAPI) {
        this.weatherAPI = weatherAPI;
    }

    public List<Weather> getWeathers() {
        return weathers;
    }

    public void setWeathers(List<Weather> weathers) {
        this.weathers = weathers;
    }

    @Override
    public String toString() {
        return "PredictionData{" +
                ", trips=" + trips +
                ", stations=" + stations +
                ", weatherAPI=" + weatherAPI +
                '}';
    }
}
