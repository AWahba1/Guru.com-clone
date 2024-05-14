package com.messageApp.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Feedback_By_Client_Pkey implements Serializable {
    UUID client_id;
    Timestamp created_at ;
    UUID feedback_id;

    public Feedback_By_Client_Pkey(UUID client_id, Timestamp created_at, UUID feedback_id) {
        this.client_id = client_id;
        this.created_at = created_at;
        this.feedback_id = feedback_id;
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
}
