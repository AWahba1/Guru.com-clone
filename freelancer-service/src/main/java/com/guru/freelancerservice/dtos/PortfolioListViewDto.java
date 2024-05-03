package com.guru.freelancerservice.dtos;

import java.util.UUID;

public interface PortfolioListViewDto {
    UUID getPortfolio_id();
    String getTitle();
    String getCover_image_url();
}
