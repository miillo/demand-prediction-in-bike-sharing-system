package com.system.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.internal.$Gson$Preconditions;

public class UserRequest {
    final String stationId;
    final String startDate;
    final String endDate;

    @JsonCreator
    public UserRequest(@JsonProperty("station-id") String stationId, @JsonProperty("start-date") String startDate,
                       @JsonProperty("end-date") String endDate) {
        this.stationId = stationId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getStationId() {
        return stationId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "stationId='" + stationId + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
