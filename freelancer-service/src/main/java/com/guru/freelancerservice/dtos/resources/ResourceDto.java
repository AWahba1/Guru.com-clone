package com.guru.freelancerservice.dtos.resources;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDto {
    private UUID freelancer_id;
    private UUID resource_id;
    private String resource_name;
    private String resource_title;
    private String resource_summary;
    private UUID[] resource_skills;
    private BigDecimal resource_rate;
    private String minimum_duration;
    private String resource_image;
    private UUID[] portfolio_ids;

}
