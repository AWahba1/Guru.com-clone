package com.guru.jobservice.dtos;

import java.sql.Timestamp;
import java.util.UUID;


public interface LastBidsUntilThreshold {

    public UUID getId();

    public UUID getFreelancer_id();

    public UUID getJob_id();

    public String getProposal();

    public String getQuote_status();

    public int getBids_used();

    public Timestamp getBid_date();

    public int getCumulative_sum();
}
