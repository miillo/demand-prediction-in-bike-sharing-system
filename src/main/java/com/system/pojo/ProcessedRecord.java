package com.system.pojo;

public class ProcessedRecord {

    private int id;

    private int actionType;

    private String actionTime;

    private int stationId;

    private String stationName;

    private String stationLatitude;

    private String stationLongitude;

    private int bikeId;

    private int currentBikeCount;

    private int initialBikeCount;

    private String userType;

    private int birthYear;

    private int gender;

    private Double temperature;

    private Double windspeed;

    private Double pressure;

    public ProcessedRecord(int id, int actionType, String actionTime, int stationId, String stationName, String stationLatitude,
                           String stationLongitude, int bikeId, int currentBikeCount, int initialBikeCount, String userType,
                           int birthYear, int gender, Double temperature, Double windspeed, Double pressure) {
        this.id = id;
        this.actionType = actionType;
        this.actionTime = actionTime;
        this.stationId = stationId;
        this.stationName = stationName;
        this.stationLatitude = stationLatitude;
        this.stationLongitude = stationLongitude;
        this.bikeId = bikeId;
        this.currentBikeCount = currentBikeCount;
        this.initialBikeCount = initialBikeCount;
        this.userType = userType;
        this.birthYear = birthYear;
        this.gender = gender;
        this.temperature = temperature;
        this.windspeed = windspeed;
        this.pressure = pressure;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getActionTime() {
        return actionTime;
    }

    public void setActionTime(String actionTime) {
        this.actionTime = actionTime;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
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
                "id=" + id +
                ", actionType=" + actionType +
                ", actionTime='" + actionTime + '\'' +
                ", stationId=" + stationId +
                ", stationName='" + stationName + '\'' +
                ", stationLatitude='" + stationLatitude + '\'' +
                ", stationLongitude='" + stationLongitude + '\'' +
                ", bikeId=" + bikeId +
                ", currentBikeCount=" + currentBikeCount +
                ", initialBikeCount=" + initialBikeCount +
                ", userType='" + userType + '\'' +
                ", birthYear=" + birthYear +
                ", gender=" + gender +
                ", temperature=" + temperature +
                ", windspeed=" + windspeed +
                ", pressure=" + pressure +
                '}';
    }
}
