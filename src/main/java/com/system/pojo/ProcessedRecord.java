package com.system.pojo;

public class ProcessedRecord {

    private int demand;

    private int stationId;

    private Double temperature;

    private Double windspeed;

    private Double pressure;

    public ProcessedRecord(int demand, int stationId, String stationName, String stationLatitude, String stationLongitude,
                           Double temperature, Double windspeed, Double pressure) {
        this.demand = demand;
        this.stationId = stationId;
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
                ", temperature=" + temperature +
                ", windspeed=" + windspeed +
                ", pressure=" + pressure +
                '}';
    }
}
