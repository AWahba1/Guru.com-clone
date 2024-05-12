package com.guru.jobservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "job_invitations")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobInvitations {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private UUID invitation_id;
    private UUID freelancer_id;
    private UUID client_id;
    private UUID job_id;
    private Date invitation_date;
}
