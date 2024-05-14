package com.messageApp.Models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "Reports_By_Job")
@Data

public class Reports_By_Job {

    @Id
    @NotNull
    private UUID job_id;
    private UUID report_id;
    @NotNull
    private UUID freelancer_id;
    @NotNull
    private UUID client_id;
    @NotNull
    private String client_name;
    @NotNull
    private String job_title;
    @NotNull
    @NotBlank(message = "jobStatus should not be empty")
    private String job_status;
    @NotNull
    @NotBlank(message = "report should not be empty")
    private String report;

    private Timestamp created_at;

    public Reports_By_Job(UUID job_id, UUID report_id, UUID freelancer_id, UUID client_id, String client_name, String job_title, String job_status, String report, Timestamp created_at) {
        this.job_id = job_id;
        this.report_id = report_id;
        this.freelancer_id = freelancer_id;
        this.client_id = client_id;
        this.client_name = client_name;
        this.job_title = job_title;
        this.job_status = job_status;
        this.report = report;
        this.created_at = created_at;
    }

    public UUID getJob_id() {
        return job_id;
    }

    public void setJob_id(UUID job_id) {
        this.job_id = job_id;
    }

    public UUID getReport_id() {
        return report_id;
    }

    public void setReport_id(UUID report_id) {
        this.report_id = report_id;
    }

    public UUID getFreelancer_id() {
        return freelancer_id;
    }

    public void setFreelancer_id(UUID freelancer_id) {
        this.freelancer_id = freelancer_id;
    }

    public UUID getClient_id() {
        return client_id;
    }

    public void setClient_id(UUID client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }


    public String getJob_status() {
        return job_status;
    }

    public void setJob_status(String job_status) {
        this.job_status = job_status;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}

