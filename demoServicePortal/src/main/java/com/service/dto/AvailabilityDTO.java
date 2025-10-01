package com.service.dto;

import com.service.enums.AvailabilityStatus;

public class AvailabilityDTO {

    private Long professionalId;
    private String day;
    private String startTime;
    private String endTime;
    private AvailabilityStatus status;

    public AvailabilityDTO() {
    }

    public AvailabilityDTO(Long professionalId, String day, String startTime, String endTime, AvailabilityStatus status) {
        this.professionalId = professionalId;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Long getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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

    public AvailabilityStatus getStatus() {
        return status;
    }

    public void setStatus(AvailabilityStatus status) {
        this.status = status;
    }
}
