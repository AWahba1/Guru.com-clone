package com.guru.freelancerservice.dtos.services;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDto {
    private UUID freelancer_id;
    private UUID service_id;
    private String service_title;
    private String service_description;
    private UUID[] service_skills;
    private BigDecimal service_rate;
    private BigDecimal minimum_budget;
    private String service_thumbnail;
    private UUID[] portfolio_id;
}
