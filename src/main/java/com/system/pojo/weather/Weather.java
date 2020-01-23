package com.system.pojo.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class Weather {
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("temperature")
    @Expose
    private Double temperature;
    @SerializedName("temperature_min")
    @Expose
    private Double temperatureMin;
    @SerializedName("temperature_max")
    @Expose
    private Double temperatureMax;
    @SerializedName("precipitation")
    @Expose
    private Object precipitation;
    @SerializedName("snowfall")
    @Expose
    private Object snowfall;
    @SerializedName("snowdepth")
    @Expose
    private Object snowdepth;
    @SerializedName("winddirection")
    @Expose
    private Object winddirection;
    @SerializedName("windspeed")
    @Expose
    private Double windspeed;
    @SerializedName("peakgust")
    @Expose
    private Object peakgust;
    @SerializedName("sunshine")
    @Expose
    private Object sunshine;
    @SerializedName("pressure")
    @Expose
    private Double pressure;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(Double temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public Double getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(Double temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public Object getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Object precipitation) {
        this.precipitation = precipitation;
    }

    public Object getSnowfall() {
        return snowfall;
    }

    public void setSnowfall(Object snowfall) {
        this.snowfall = snowfall;
    }

    public Object getSnowdepth() {
        return snowdepth;
    }

    public void setSnowdepth(Object snowdepth) {
        this.snowdepth = snowdepth;
    }

    public Object getWinddirection() {
        return winddirection;
    }

    public void setWinddirection(Object winddirection) {
        this.winddirection = winddirection;
    }

    public Double getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(Double windspeed) {
        this.windspeed = windspeed;
    }

    public Object getPeakgust() {
        return peakgust;
    }

    public void setPeakgust(Object peakgust) {
        this.peakgust = peakgust;
    }

    public Object getSunshine() {
        return sunshine;
    }

    public void setSunshine(Object sunshine) {
        this.sunshine = sunshine;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    @Override
    public String toString() {
        return "Weather{" +
                ", date='" + date + '\'' +
                ", temperature=" + temperature +
                ", temperatureMin=" + temperatureMin +
                ", temperatureMax=" + temperatureMax +
                ", precipitation=" + precipitation +
                ", snowfall=" + snowfall +
                ", snowdepth=" + snowdepth +
                ", winddirection=" + winddirection +
                ", windspeed=" + windspeed +
                ", peakgust=" + peakgust +
                ", sunshine=" + sunshine +
                ", pressure=" + pressure +
                '}';
    }
}
