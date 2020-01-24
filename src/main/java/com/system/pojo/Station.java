package com.system.pojo;

import com.opencsv.bean.CsvBindByName;
import jdk.vm.ci.meta.Local;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

public final class Station {

    @CsvBindByName(column = "station id")
    private int id;

    @CsvBindByName(column = "time")
    private String actionTime;

    @CsvBindByName(column = "type")
    private int type;

    @CsvBindByName(column = "station name")
    private String stationName;

    @CsvBindByName(column = "station latitude")
    private String stationLatitude;

    @CsvBindByName(column = "station longitude")
    private String stationLongitude;

    @CsvBindByName(column = "bike_id")
    private int bikeId;

    @CsvBindByName(column = "current bike count")
    private int currentBikeCount;

    @CsvBindByName(column = "initial bike count")
    private int initialBikeCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActionTime() {
        return actionTime;
    }

    public void setActionTime(String actionTime) {
        this.actionTime = actionTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getBikeId() {
        return bikeId;
    }

    public void setBikeId(int bikeId) {
        this.bikeId = bikeId;
    }

    public int getCurrentBikeCount() {
        return currentBikeCount;
    }

    public void setCurrentBikeCount(int currentBikeCount) {
        this.currentBikeCount = currentBikeCount;
    }

    public int getInitialBikeCount() {
        return initialBikeCount;
    }

    public void setInitialBikeCount(int initialBikeCount) {
        this.initialBikeCount = initialBikeCount;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", actionTime=" + actionTime +
                ", type=" + type +
                ", stationName='" + stationName + '\'' +
                ", stationLatitude='" + stationLatitude + '\'' +
                ", stationLongitude='" + stationLongitude + '\'' +
                ", bikeId=" + bikeId +
                ", currentBikeCount=" + currentBikeCount +
                ", initialBikeCount=" + initialBikeCount +
                '}';
    }
}
