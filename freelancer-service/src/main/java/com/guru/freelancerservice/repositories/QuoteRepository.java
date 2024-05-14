package com.guru.freelancerservice.repositories;

import com.guru.freelancerservice.models.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.sql.Timestamp;
import java.util.UUID;

public interface QuoteRepository extends JpaRepository<Quote, UUID> {
//
//    IN _freelancer_id uuid,
//    IN job_id uuid,
//    IN proposal varchar(3000),
//    IN bids_used decimal,
//    IN bid_date timestamp
    @Procedure(name = "add_quote")
    void add_quote(UUID _freelancer_id, UUID job_id, String proposal, int bids_used, Timestamp bid_date);
}
