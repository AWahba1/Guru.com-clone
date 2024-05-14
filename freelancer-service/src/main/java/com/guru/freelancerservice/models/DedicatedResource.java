package com.guru.freelancerservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "dedicated_resource")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DedicatedResource {
    @Id
    @GeneratedValue(
            strategy = GenerationType.UUID
    )
    private UUID resource_id;
    private UUID freelancer_id;
    private String resource_name;
    private String resource_title;
    private String resource_summary;
    private BigDecimal resource_rate;
    private String minimum_duration;
    private String resource_image;
    private int resource_views;
    private boolean is_draft;

}
