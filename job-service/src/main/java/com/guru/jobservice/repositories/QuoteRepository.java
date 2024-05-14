package com.guru.jobservice.repositories;

import com.guru.jobservice.dtos.BidsUsageHistory;
import com.guru.jobservice.dtos.BidsUsageSummary;
import com.guru.jobservice.dtos.LastBidsUntilThreshold;
import com.guru.jobservice.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface QuoteRepository extends JpaRepository<Quote, UUID> {

    @Procedure("add_quote")
    void addQuote(UUID freelancer_id, UUID job_id, String proposal, int bids_used, Timestamp bid_date);

    @Procedure("update_quote")
    void updateQuote(UUID _quote_id, String proposal, int bids_used, String quote_status);

    @Query(value = "select * from get_freelancer_quotes(?1, ?2)", nativeQuery = true)
    List<Quote> getFreelancerQuotes(UUID _freelancer_id, String quote_status);

    @Query(value = "select * from get_freelancer_quote_details(?1)", nativeQuery = true)
    List<Quote> getFreelancerQuoteDetails(UUID _quote_id);

    @Query(value = "select * from bids_usage_summary(?1)", nativeQuery = true)
    List<BidsUsageSummary> bidsUsageSummary(UUID freelancer_id);

    @Query(value = "select * from get_bids_usage_history(?1)", nativeQuery = true)
    List<BidsUsageHistory> bidsUsageHistory(UUID freelancer_id);

    @Query(value = "select * from view_last_bids_until_threshold(?1, ?2)", nativeQuery = true)
    List<LastBidsUntilThreshold> viewLastBidsUntilThreshold(UUID _freelancer_id, int threshold);
}
