package com.guru.freelancerservice.repositories;

import com.guru.freelancerservice.dtos.FreelancerProfileDto;
import com.guru.freelancerservice.dtos.PortfolioListViewDto;
import com.guru.freelancerservice.models.Freelancer;
import com.guru.freelancerservice.models.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {

    @Query(value = "select portfolio_id from portfolios where freelancer_id=(?1)", nativeQuery = true)
    List<UUID> getPortfolioIds(UUID freelancer_id);

    @Procedure(name="add_portfolio")
    void add_portfolio(UUID freelancer_id, String title, String cover_image_url, String[] attachments);

    @Procedure(name="unpublish_portfolio")
    void unpublish_portfolio(UUID portfolio_id);

    @Procedure(name="delete_portfolio")
    void delete_portfolio(UUID portfolio_id);

    @Procedure(name="update_portfolio")
    void update_portfolio(UUID portfolio_id, String title, String cover_image_url, String[] attachments);

//    @Query(value = "select * from get_portfolio(?1)", nativeQuery = true)
//    List<Portfolio> getPortfolioByFreelancerId(UUID portfolio_id);

    @Query(value = "select * from get_all_freelancer_portfolios(?1)", nativeQuery = true)
    List<PortfolioListViewDto> getAllFreelancerPortfolios(UUID freelancer_id);
}






