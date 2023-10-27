package org.service.topsquad.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskAssignModel {
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("ticketNumber")
    private String ticketNumber;
    @JsonProperty("taskUrl")
    private String taskUrl;

    public String getUserName() {
        return userName;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public String getTaskUrl() {
        return taskUrl;
    }
}
