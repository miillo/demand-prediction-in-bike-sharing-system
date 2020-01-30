package com.system.pojo;

public class ProcessedRecord {

    private int demand;

    private int stationId;

    private String stationName;

    private String stationLatitude;

    private String stationLongitude;

    private Double temperature;

    private Double windspeed;

    private Double pressure;

    public ProcessedRecord(int demand, int stationId, String stationName, String stationLatitude, String stationLongitude,
                           Double temperature, Double windspeed, Double pressure) {
        this.demand = demand;
        this.stationId = stationId;
        this.stationName = stationName;
        this.stationLatitude = stationLatitude;
        this.stationLongitude = stationLongitude;
        this.temperature = temperature;
        this.windspeed = windspeed;
        this.pressure = pressure;
    }

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationLatitude() {
        return stationLatitude;
    }

    public void setStationLatitude(String stationLatitude) {
        this.stationLatitude = stationLatitude;
    }

    public String getStationLongitude() {
        return stationLongitude;
    }

    public void setStationLongitude(String stationLongitude) {
        this.stationLongitude = stationLongitude;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(Double windspeed) {
        this.windspeed = windspeed;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    @Override
    public String toString() {
        return "ProcessedRecord{" +
                "demand=" + demand +
                ", stationId=" + stationId +
                ", stationName='" + stationName + '\'' +
                ", stationLatitude='" + stationLatitude + '\'' +
                ", stationLongitude='" + stationLongitude + '\'' +
                ", temperature=" + temperature +
                ", windspeed=" + windspeed +
                ", pressure=" + pressure +
                '}';
    }
}
