package com.messageApp.DTO;

import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.util.UUID;

public class FeedbackEditDTO {
    @NotNull
    private UUID freelancer_id;


    @NotNull
    private UUID client_id;

    @NotNull
    private UUID job_id;
    @NotNull
    private UUID feedback_id;
    @NotNull
    private Timestamp created_at;

    private Boolean satisfied;

    public FeedbackEditDTO(UUID freelancer_id, UUID client_id, UUID job_id, UUID feedback_id, Timestamp created_at, Boolean satisfied) {
        this.freelancer_id = freelancer_id;
        this.client_id = client_id;
        this.job_id = job_id;
        this.feedback_id = feedback_id;
        this.created_at = created_at;
        this.satisfied = satisfied;
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

    public UUID getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(UUID feedback_id) {
        this.feedback_id = feedback_id;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Boolean getSatisfied() {
        return satisfied;
    }

    public void setSatisfied(Boolean satisfied) {
        this.satisfied = satisfied;
    }
}
