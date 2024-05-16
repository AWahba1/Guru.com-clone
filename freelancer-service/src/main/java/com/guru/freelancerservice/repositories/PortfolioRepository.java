package com.guru.freelancerservice.repositories;

import com.guru.freelancerservice.dtos.portfolios.PortfolioDetailsDto;
import com.guru.freelancerservice.dtos.portfolios.PortfolioListViewDto;
import com.guru.freelancerservice.models.Portfolio;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;
import java.util.UUID;

public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {

    @Query(value = "select portfolio_id from portfolios where freelancer_id=(?1)", nativeQuery = true)
    List<UUID> getPortfolioIds(UUID freelancer_id);

    @Procedure(name="add_portfolio")
    void add_portfolio(UUID freelancer_id, String title, String cover_image_url,UUID[] portfolio_skills ,String[] attachments);

    @Procedure(name="unpublish_portfolio")
    void unpublish_portfolio(UUID portfolio_id);

    @Procedure(name="publish_portfolio")
    void publish_portfolio(UUID portfolio_id);

    @Procedure(name="delete_portfolio")
    void delete_portfolio(UUID portfolio_id);

    @Procedure(name="update_portfolio")
    void update_portfolio(UUID portfolio_id, String title, String cover_image_url, UUID[] portfolio_skills, String[] attachments);

    @Query(value = "select * from get_portfolio(?1)", nativeQuery = true)
    List<PortfolioDetailsDto> get_portfolio_by_id(UUID portfolio_id);

    @Query(value = "select * from get_all_freelancer_portfolios(?1)", nativeQuery = true)
    List<PortfolioListViewDto> get_all_freelancer_portfolios(UUID freelancer_id);

    @Query(value = "SELECT p.portfolio_id,p.title,p.cover_image_url FROM portfolios p WHERE p.portfolio_id=(?1)", nativeQuery = true)
    List<PortfolioListViewDto> get_portfolio_list_view_by_id(UUID portfolio_id);
}






