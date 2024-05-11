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
@NoArgsConstructor
@EqualsAndHashCode
public class Feedback_By_Client_Pkey implements Serializable {
    UUID client_id;
    Timestamp created_at ;
    UUID feedback_id;
}
