package com.system.pojo.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherAPI {
    @SerializedName("meta")
    @Expose
    private Meta meta;

    @SerializedName("data")
    @Expose
    private List<Weather> weathersByDay;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Weather> getWeathersByDay() {
        return weathersByDay;
    }

    public void setWeathersByDay(List<Weather> weathersByDay) {
        this.weathersByDay = weathersByDay;
    }

    @Override
    public String toString() {
        return "WeatherAPI{" +
                "meta=" + meta +
                ", weathersByDay=" + weathersByDay +
                '}';
    }
}
