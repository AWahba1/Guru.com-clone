package com.guru.freelancerservice.dtos.services;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceWithEmbeddedPortfoliosDto {
  private UUID service_id;
  private UUID freelancer_id;
  private String service_title;
  private String service_description;
  private BigDecimal service_rate;
  private BigDecimal minimum_budget;
  private String service_thumbnail;
  private int service_views;
  private String[] service_skills;
  private List<PortfolioServiceDetailsDto> portfolios;

}

