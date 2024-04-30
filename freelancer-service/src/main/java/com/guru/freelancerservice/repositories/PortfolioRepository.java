package com.guru.freelancerservice.repositories;

import com.guru.freelancerservice.dtos.FreelancerProfileDto;
import com.guru.freelancerservice.models.Freelancer;
import com.guru.freelancerservice.models.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {


    @Procedure(name="add_portfolio")
    void add_portfolio(UUID freelancer_id, String title, String cover_image_url, String[] attachments);

    @Procedure(name="unpublish_portfolio")
    void unpublish_portfolio(UUID portfolio_id);

    @Procedure(name="delete_portfolio")
    void delete_portfolio(UUID portfolio_id);


}






