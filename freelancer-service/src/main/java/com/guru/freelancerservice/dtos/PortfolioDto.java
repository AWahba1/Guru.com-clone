package com.guru.freelancerservice.dtos;

import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioDto {
    private UUID freelancer_id;
    private UUID portfolio_id;
    private String title;
    private String cover_image_url;
    private String[] attachments;
}
