package com.system.pojo;

import com.opencsv.bean.CsvBindByName;

public final class Trip {
    @CsvBindByName(column = "tripduration")
    private long duration;

    @CsvBindByName(column = "starttime")
    private String startTime;

    @CsvBindByName(column = "stoptime")
    private String endTime;

    @CsvBindByName(column = "start station id")
    private int startStationId;

    @CsvBindByName(column = "end station id")
    private int endStationId;

    @CsvBindByName(column = "bikeid")
    private int bikeId;

    @CsvBindByName(column = "usertype")
    private String userType;

    @CsvBindByName(column = "birth year")
    private int birthYear;

    @CsvBindByName(column = "gender")
    private int gender;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getStartStationId() {
        return startStationId;
    }

    public void setStartStationId(int startStationId) {
        this.startStationId = startStationId;
    }

    public int getEndStationId() {
        return endStationId;
    }

    public void setEndStationId(int endStationId) {
        this.endStationId = endStationId;
    }

    public int getBikeId() {
        return bikeId;
    }

    public void setBikeId(int bikeId) {
        this.bikeId = bikeId;
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

    @Override
    public String toString() {
        return "Trip{" +
                "duration=" + duration +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", startStationId=" + startStationId +
                ", endStationId=" + endStationId +
                ", bikeId=" + bikeId +
                ", userType='" + userType + '\'' +
                ", birthYear=" + birthYear +
                ", gender=" + gender +
                '}';
    }
}
