package com.guru.jobservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuoteRequest {

    private UUID id;
    private UUID freelancer_id;
    private UUID job_id;
    private String proposal;
    private String quote_status;
    private int bids_used;
    private Timestamp bid_date;
}
