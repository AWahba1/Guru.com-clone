package com.guru.freelancerservice.services.implementation;

import com.guru.freelancerservice.dtos.FreelancerAboutSectionDto;
import com.guru.freelancerservice.dtos.FreelancerProfileDto;
import com.guru.freelancerservice.dtos.PortfolioDto;
import com.guru.freelancerservice.models.Freelancer;
import com.guru.freelancerservice.models.Portfolio;
import com.guru.freelancerservice.models.user_type_enum;
import com.guru.freelancerservice.repositories.FreelancerRepository;
import com.guru.freelancerservice.repositories.PortfolioRepository;
import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FreelancerImplementation implements FreelancerService {
    private final FreelancerRepository freelancerRepository;
    private final PortfolioRepository portfolioRepository;

    @Autowired
    public FreelancerImplementation(FreelancerRepository freelancerRepository, PortfolioRepository portfolioRepository) {
        this.freelancerRepository = freelancerRepository;
        this.portfolioRepository = portfolioRepository;
    }

    @Override
    public FreelancerProfileDto getFreelancerProfile(UUID freelancer_id) {
        Freelancer freelancer = freelancerRepository.findById(freelancer_id).orElse(null);
        if (freelancer == null) {
            return null;
        }
//        List<Object[]> profile = freelancerRepository.getFreelancerProfile(freelancer_id);
//        FreelancerProfileDto freelancerProfileDto = new FreelancerProfileDto();
//        for (Object[] object : profile) {
//            freelancerProfileDto.setFreelancer(getFreelancerFromObject(object));
//            freelancerProfileDto.setResourceSkills(populateSkills((Object[]) object[22]));
//            freelancerProfileDto.setServiceSkills(populateSkills((Object[]) object[23]));
//        }

        return freelancerRepository.getFreelancerProfile(freelancer_id).getFirst();
    }

    @Override
    public List<Freelancer> getAllFreelancers() {
        return freelancerRepository.findAll();
    }

    @Override
    public Freelancer getFreelancer(UUID freeLancerId) {
        return freelancerRepository.findById(freeLancerId).orElse(null);
    }


    @Override
    public boolean updateFreelancerAbout(FreelancerAboutSectionDto freelancerAboutSectionDto) {
        Freelancer freelancer = freelancerRepository.findById(freelancerAboutSectionDto.getFreelancer_id()).orElse(null);
        if (freelancer == null) {
            return false;
        }

        freelancerRepository.update_freelancer_profile_about_section(
                freelancerAboutSectionDto.getFreelancer_id(),
                freelancerAboutSectionDto.getFreelancer_name(),
                freelancerAboutSectionDto.getImage_url(),
                freelancerAboutSectionDto.getTagline(),
                freelancerAboutSectionDto.getBio(),
                freelancerAboutSectionDto.getWork_terms(),
                freelancerAboutSectionDto.getAttachments(),
                freelancerAboutSectionDto.getUser_type().toString(),
                freelancerAboutSectionDto.getWebsite_link(),
                freelancerAboutSectionDto.getFacebook_link(),
                freelancerAboutSectionDto.getLinkedin_link(),
                freelancerAboutSectionDto.getProfessional_video_link(),
                freelancerAboutSectionDto.getCompany_history(),
                freelancerAboutSectionDto.getOperating_since()
        );
        return true;

    }

    @Override
    public boolean updateFreelancerAboutVisibility(UUID freelancer_id) {
        Freelancer freelancer = freelancerRepository.findById(freelancer_id).orElse(null);
        if (freelancer == null) {
            return false;
        }
        freelancerRepository.toggle_profile_visibility(freelancer_id);
        return true;
    }

    @Override
    public boolean addPortfolio(PortfolioDto portfolioDto) {
        Freelancer freelancer = freelancerRepository.findById(portfolioDto.getFreelancer_id()).orElse(null);
        if (freelancer == null) {
            return false;
        }
        portfolioRepository.add_portfolio(
                portfolioDto.getFreelancer_id(),
                portfolioDto.getTitle(),
                portfolioDto.getCover_image_url(),
                portfolioDto.getAttachments()
        );
        return true;
    }

    @Override
    public boolean unpublishPortfolio(UUID portfolio_id) {
        Portfolio portfolio = portfolioRepository.findById(portfolio_id).orElse(null);
        if (portfolio == null) {
            return false;
        }
        portfolioRepository.unpublish_portfolio(portfolio_id);
        return true;
    }

    @Override
    public boolean deletePortfolio(UUID portfolio_id) {
        Portfolio portfolio = portfolioRepository.findById(portfolio_id).orElse(null);
        if (portfolio == null) {
            return false;
        }
        portfolioRepository.delete_portfolio(portfolio_id);
        return true;
    }

}
