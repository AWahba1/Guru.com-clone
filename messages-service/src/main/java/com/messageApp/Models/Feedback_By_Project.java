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
@Table(name = "Feedback_By_Project")
@IdClass(Feedback_By_Project_Pkey.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback_By_Project {
    @Id
    UUID job_id;
    @Id
    Timestamp created_at ;
    @Id
    UUID feedback_id;
    private UUID client_id;
    private UUID freelancer_id;
    private String client_name;
    private String job_title;
    private boolean satisfied;
}
