package com.guru.freelancerservice.dtos.portfolios;

import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioRequestDto {
    private UUID freelancer_id;
    private UUID portfolio_id;
    private String title;
    private String cover_image_url;
    private UUID[] portfolio_skills;
    private String[] attachments;
}
