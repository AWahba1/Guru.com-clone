package com.feedbackApp.Models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;


@Entity
@Table(name = "Feedback_By_Client")
@IdClass(Feedback_By_Client_Pkey.class)
@Data
public class Feedback_By_Client implements Serializable {
    @Id
    UUID client_id;

    @Id
    Timestamp created_at ;
    @Id
    UUID feedback_id;
    private UUID job_id;
    private UUID freelancer_id;
    private String client_name;
    private String job_title;
    private boolean satisfied;

    public Feedback_By_Client(UUID client_id, Timestamp created_at, UUID feedback_id, UUID job_id, UUID freelancer_id, String client_name, String job_title, boolean satisfied) {
        this.client_id = client_id;
        this.created_at = created_at;
        this.feedback_id = feedback_id;
        this.job_id = job_id;
        this.freelancer_id = freelancer_id;
        this.client_name = client_name;
        this.job_title = job_title;
        this.satisfied = satisfied;
    }

    public UUID getClient_id() {
        return client_id;
    }

    public void setClient_id(UUID client_id) {
        this.client_id = client_id;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public UUID getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(UUID feedback_id) {
        this.feedback_id = feedback_id;
    }

    public UUID getJob_id() {
        return job_id;
    }

    public void setJob_id(UUID job_id) {
        this.job_id = job_id;
    }

    public UUID getFreelancer_id() {
        return freelancer_id;
    }

    public void setFreelancer_id(UUID freelancer_id) {
        this.freelancer_id = freelancer_id;
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

    public boolean isSatisfied() {
        return satisfied;
    }

    public void setSatisfied(boolean satisfied) {
        this.satisfied = satisfied;
    }
}
