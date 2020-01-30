package com.system.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PredRequest {
    final String stationId;
    final String predDate;

    @JsonCreator
    public PredRequest(@JsonProperty("station-id") String stationId, @JsonProperty("pred-date") String predDate) {
        this.stationId = stationId;
        this.predDate = predDate;
    }

    public String getPredDate() {
        return predDate;
    }

    public String getStationId() {
        return stationId;
    }

    @Override
    public String toString() {
        return "PredRequest{" +
                "stationId='" + stationId + '\'' +
                ", predDate='" + predDate + '\'' +
                '}';
    }
}
