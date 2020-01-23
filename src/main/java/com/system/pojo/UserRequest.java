package com.system.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRequest {
    final String startDate;
    final String endDate;

    @JsonCreator
    public UserRequest(@JsonProperty("start-date") String startDate, @JsonProperty("end-date") String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
