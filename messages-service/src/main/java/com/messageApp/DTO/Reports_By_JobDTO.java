package com.messageApp.DTO;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public class Reports_By_JobDTO {
    @NotNull
    private UUID job_id;
    private String report;


    private String job_status;

    public Reports_By_JobDTO(UUID job_id, String report, String job_status) {
        this.job_id = job_id;
        this.report = report;
        this.job_status = job_status;
    }

    public String getJobStatus() {
        return job_status;
    }

    public void setJobStatus(String job_status) {
        this.job_status = job_status;
    }

    public UUID getJob_id() {
        return job_id;
    }

    public void setJob_id(UUID job_id) {
        this.job_id = job_id;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
