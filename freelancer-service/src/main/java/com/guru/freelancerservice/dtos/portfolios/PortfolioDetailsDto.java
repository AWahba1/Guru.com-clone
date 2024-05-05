package com.guru.freelancerservice.dtos.portfolios;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.UUID;

@JsonPropertyOrder({
        "portfolio_id", "freelancer_id", "title", "cover_image_url", "portfolio_skills", "attachments", "is_draft"
})
public interface PortfolioDetailsDto {
    UUID getPortfolio_id();
    UUID getFreelancer_id();
    String getTitle();
    String getCover_image_url();
    String[] getPortfolio_skills();
    String[] getAttachments();
    boolean getIs_draft();
}
