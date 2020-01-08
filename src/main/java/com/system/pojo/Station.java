package com.system.pojo;

import org.joda.time.DateTime;

public final class Station {

    private int id;

    private DateTime actionTime;

    private int type;

    private String stationName;

    private String stationLatitude;

    private String stationLongitude;

    private int bikeId;

    private int currentBikeCount;

    private int initialBikeCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DateTime getActionTime() {
        return actionTime;
    }

    public void setActionTime(DateTime actionTime) {
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
}
