package com.guru.freelancerservice.dtos.resources;

import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioResourceDetailsDto {
   private UUID portfolio_id;
   private String title;
   private String cover_image_url;
}
