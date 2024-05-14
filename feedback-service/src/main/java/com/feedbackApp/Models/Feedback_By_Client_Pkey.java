package com.feedbackApp.Models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@EqualsAndHashCode
public class Feedback_By_Client_Pkey implements Serializable {
    UUID client_id;
    Timestamp created_at ;
    UUID feedback_id;

    public Feedback_By_Client_Pkey(UUID client_id, Timestamp created_at, UUID feedback_id) {
        this.client_id = client_id;
        this.created_at = created_at;
        this.feedback_id = feedback_id;
    }


}
