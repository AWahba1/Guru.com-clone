package com.messageApp.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@EqualsAndHashCode
public class Feedback_By_Project_Pkey implements Serializable {
    UUID job_id;
    Timestamp created_at ;
    UUID feedback_id;

    public Feedback_By_Project_Pkey(UUID job_id, Timestamp created_at, UUID feedback_id) {
        this.job_id = job_id;
        this.created_at = created_at;
        this.feedback_id = feedback_id;
    }

    public UUID getJob_id() {
        return job_id;
    }

    public void setJob_id(UUID job_id) {
        this.job_id = job_id;
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
