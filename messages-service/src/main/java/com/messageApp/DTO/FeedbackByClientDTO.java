package com.messageApp.DTO;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class FeedbackByClientDTO {
    @NotNull
    private UUID feedback_id;

    @NotNull
    private UUID freelancer_id;

    @NotNull
    private UUID client_id;

    @NotNull
    private UUID job_id;

    @NotNull
    private String client_name;

    @NotNull
    private String job_title;

    @NotNull
    private Boolean satisfied;

    public FeedbackByClientDTO(UUID feedback_id, UUID freelancer_id, UUID client_id, UUID job_id, String client_name, String job_title, Boolean satisfied) {
        this.feedback_id = feedback_id;
        this.freelancer_id = freelancer_id;
        this.client_id = client_id;
        this.job_id = job_id;
        this.client_name = client_name;
        this.job_title = job_title;
        this.satisfied = satisfied;
    }

    public UUID getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(UUID feedback_id) {
        this.feedback_id = feedback_id;
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

    public UUID getJob_id() {
        return job_id;
    }

    public void setJob_id(UUID job_id) {
        this.job_id = job_id;
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

    public Boolean getSatisfied() {
        return satisfied;
    }

    public void setSatisfied(Boolean satisfied) {
        this.satisfied = satisfied;
    }
}
