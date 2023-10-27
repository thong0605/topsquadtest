package org.service.topsquad.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class TaskModel {
    @JsonProperty(value = "title")
    private String title;
    @JsonProperty(value = "ticketNumber")
    private String ticketNumber;
    @JsonProperty(value = "status")
    private String status;
    @JsonProperty(value = "description")
    private String description;
    @JsonProperty(value = "lastModified")
    private Date lastModified;
    @JsonProperty(value = "createdDate")
    private Date createdDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
