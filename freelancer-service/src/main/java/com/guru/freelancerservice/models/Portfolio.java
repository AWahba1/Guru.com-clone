package com.guru.freelancerservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "portfolios")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Portfolio {
    @Id
    @GeneratedValue(
            strategy = GenerationType.UUID
    )
    private UUID portfolio_id;
    private UUID freelancer_id;
    private String title;
    private String cover_image_url;
    private String[] attachments;
    private boolean is_draft;
    private int portfolio_views;
}
