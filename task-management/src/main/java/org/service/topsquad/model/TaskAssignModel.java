package org.service.topsquad.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskAssignModel {
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("ticketNumber")
    private String ticketNumber;

    public TaskAssignModel() {
    }

    public TaskAssignModel(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }
}
