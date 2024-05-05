package com.guru.freelancerservice.models;
import lombok.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

//quote_id UUID PRIMARY KEY,
//freelancer_id UUID,
//job_id UUID,
//proposal VARCHAR(3000),
//quote_status VARCHAR(255) CHECK (quote_status IN ('AWAITING_ACCEPTANCE', 'PRIORITY', 'ACCEPTED', 'ARCHIVED')),
//bids_used DECIMAL,
//bid_date TIMESTAMP,
@Entity
@Table(name = "quotes")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Quote {
    @Id
    @GeneratedValue(
            strategy = GenerationType.UUID
    )
    private UUID quote_id;
    private UUID freelancer_id;
    private UUID job_id;
    private String proposal;
    private String quote_status;
    private int bids_used;
    private Timestamp bid_date;
}
