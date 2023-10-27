package org.service.topsquad.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;

public class UserModel {
    @JsonProperty(value = "userName")
    private String userName;
    @JsonProperty(value = "password")
    @JsonIgnore
    private String password;
    @JsonProperty(value = "mailAddress")
    private String mailAddress;
    @JsonProperty(value = "role")
    private String role;
    @JsonProperty(value = "status")
    private String status;
    @JsonProperty(value = "lastModified")
    private Date lastModified;
    @JsonProperty(value = "createdDate")
    private Date createdDate;
    @JsonProperty(value = "tasks")
    private List<TaskEntity> tasks;
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }
}
