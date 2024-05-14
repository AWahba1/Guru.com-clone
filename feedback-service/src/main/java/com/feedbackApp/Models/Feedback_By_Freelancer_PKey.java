package com.feedbackApp.Models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@EqualsAndHashCode
public class Feedback_By_Freelancer_PKey implements Serializable {
    UUID freelancer_id;
    Timestamp created_at ;
    UUID feedback_id;

    public Feedback_By_Freelancer_PKey(UUID freelancer_id, Timestamp created_at, UUID feedback_id) {
        this.freelancer_id = freelancer_id;
        this.created_at = created_at;
        this.feedback_id = feedback_id;
    }

    public UUID getFreelancer_id() {
        return freelancer_id;
    }

    public void setFreelancer_id(UUID freelancer_id) {
        this.freelancer_id = freelancer_id;
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
}
