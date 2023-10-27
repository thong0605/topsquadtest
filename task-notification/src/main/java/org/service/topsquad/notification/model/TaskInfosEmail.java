package org.service.topsquad.notification.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskInfosEmail {
    @JsonProperty(value = "taskName")
    private String taskName;
    @JsonProperty(value = "taskUrl")
    private String taskUrl;
    @JsonProperty(value = "assigneeEmailAddress")
    private String assigneeEmailAddress;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskUrl() {
        return taskUrl;
    }

    public void setTaskUrl(String taskUrl) {
        this.taskUrl = taskUrl;
    }

    public String getAssigneeEmailAddress() {
        return assigneeEmailAddress;
    }

    public void setAssigneeEmailAddress(String assigneeEmailAddress) {
        this.assigneeEmailAddress = assigneeEmailAddress;
    }
}
