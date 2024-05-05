package com.guru.freelancerservice.dtos.services;

import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioServiceDetailsDto {
   private UUID portfolio_id;
   private String title;
   private String cover_image_url;
}
