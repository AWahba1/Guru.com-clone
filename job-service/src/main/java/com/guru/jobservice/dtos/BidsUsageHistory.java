package com.guru.jobservice.dtos;

import java.sql.Timestamp;

public interface BidsUsageHistory {
    public int getBids_used();
    public Timestamp getBid_date();
}
