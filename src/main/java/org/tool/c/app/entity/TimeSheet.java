package org.tool.c.app.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.tool.c.utils.constants.Constants;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeSheet {

    private int id;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("work_day")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate workDay; // "2020-03-30"

    @JsonProperty("work_hours")
    private double workHours; // "2.74861111111111"

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = Constants.TIMEZONE)
    private LocalDateTime createdAt; // "2020-03-30T01:14:26.822+00:00"

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = Constants.TIMEZONE)
    private LocalDateTime updatedAt; // "2020-03-30T03:59:21.097+00:00"

    @JsonProperty("check_in_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = Constants.TIMEZONE)
    private LocalDateTime checkInTime; // "2020-03-30T01:14:26.814+00:00"

    @JsonProperty("check_out_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = Constants.TIMEZONE)
    private LocalDateTime checkOutTime; // "2020-03-30T03:59:21.087+00:00"

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getWorkDay() {
        return workDay;
    }

    public void setWorkDay(LocalDate workDay) {
        this.workDay = workDay;
    }

    public double getWorkHours() {
        return workHours;
    }

    public void setWorkHours(double workHours) {
        this.workHours = workHours;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }
}
