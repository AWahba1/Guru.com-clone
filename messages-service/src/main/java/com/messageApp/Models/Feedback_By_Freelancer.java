package com.messageApp.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;


@Entity
@Table(name = "Feedback_By_Freelancer")
@IdClass(Feedback_By_Freelancer_PKey.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback_By_Freelancer {
    @Id
    UUID freelancer_id;
    @Id
    Timestamp created_at ;
    @Id
    UUID feedback_id;
    private UUID client_id;
    private UUID job_id;
    private String client_name;
    private String job_title;
    private boolean satisfied;
}
