package com.messageApp.Models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Feedback_By_Freelancer_PKey implements Serializable {
    UUID freelancer_id;
    Timestamp created_at ;
    UUID feedback_id;
}
