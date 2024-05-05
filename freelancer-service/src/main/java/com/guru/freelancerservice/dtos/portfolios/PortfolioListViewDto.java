package com.guru.freelancerservice.dtos.portfolios;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.UUID;

@JsonPropertyOrder({
        "portfolio_id", "title", "cover_image_url"
})
public interface PortfolioListViewDto {
    UUID getPortfolio_id();
    String getTitle();
    String getCover_image_url();
}
