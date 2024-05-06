package com.guru.freelancerservice.services.implementation;

import com.guru.freelancerservice.dtos.featuredTeammember.AddFeaturedTeamMembersRequestDto;
import com.guru.freelancerservice.dtos.freelancer.FreelancerAboutSectionDto;
import com.guru.freelancerservice.dtos.freelancer.FreelancerProfileDto;
import com.guru.freelancerservice.dtos.freelancer.FreelancerViewProfileDto;
import com.guru.freelancerservice.dtos.portfolios.PortfolioDetailsDto;
import com.guru.freelancerservice.dtos.portfolios.PortfolioListViewDto;
import com.guru.freelancerservice.dtos.portfolios.PortfolioRequestDto;
import com.guru.freelancerservice.dtos.resources.*;
import com.guru.freelancerservice.dtos.services.*;
import com.guru.freelancerservice.models.*;
import com.guru.freelancerservice.repositories.*;
import com.guru.freelancerservice.response.ResponseHandler;
import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class FreelancerImplementation implements FreelancerService {
    private final FreelancerRepository freelancerRepository;
    private final PortfolioRepository portfolioRepository;
    private final ServiceRepository serviceRepository;
    private final DedicatedResoruceRepository dedicatedResourceRepository;
    private final QuoteRepository quoteRepository;
    private final FeaturedTeamMemberRepository featuredTeamMemberRepository;
    @Autowired
    public FreelancerImplementation(FreelancerRepository freelancerRepository, PortfolioRepository portfolioRepository, ServiceRepository serviceRepository, DedicatedResoruceRepository dedicatedResourceRepository, QuoteRepository quoteRepository, FeaturedTeamMemberRepository featuredTeamMemberRepository) {
        this.freelancerRepository = freelancerRepository;
        this.portfolioRepository = portfolioRepository;
        this.serviceRepository = serviceRepository;
        this.dedicatedResourceRepository = dedicatedResourceRepository;
        this.quoteRepository = quoteRepository;
        this.featuredTeamMemberRepository = featuredTeamMemberRepository;
    }

    @Override
    public ResponseEntity<Object> getFreelancerProfile(UUID freelancer_id) {
        Freelancer freelancer = freelancerRepository.findById(freelancer_id).orElse(null);
        if (freelancer == null) {
            return ResponseHandler.generateErrorResponse("Freelancer not found", HttpStatus.NOT_FOUND);
        }
        FreelancerProfileDto returnedProfile = freelancerRepository.getFreelancerProfile(freelancer_id).getFirst();
        HashSet<String> uniqueSkills = new HashSet<>();
        uniqueSkills.addAll(List.of(returnedProfile.getService_skills()));
        uniqueSkills.addAll(List.of(returnedProfile.getResource_skills()));
        uniqueSkills.addAll(List.of(returnedProfile.getPortfolio_service_skills()));
        uniqueSkills.addAll(List.of(returnedProfile.getPortfolio_resource_skills()));

        FreelancerViewProfileDto freelancerViewProfileDto = FreelancerViewProfileDto.builder()
                .freelancer_id(returnedProfile.getFreelancer_id())
                .freelancer_name(returnedProfile.getFreelancer_name())
                .image_url(returnedProfile.getImage_url())
                .tagline(returnedProfile.getTagline())
                .bio(returnedProfile.getBio())
                .work_terms(returnedProfile.getWork_terms())
                .attachments(returnedProfile.getAttachments())
                .user_type(returnedProfile.getUser_type())
                .website_link(returnedProfile.getWebsite_link())
                .facebook_link(returnedProfile.getFacebook_link())
                .linkedin_link(returnedProfile.getLinkedin_link())
                .professional_video_link(returnedProfile.getProfessional_video_link())
                .company_history(returnedProfile.getCompany_history())
                .operating_since(returnedProfile.getOperating_since())
                .skills(uniqueSkills.stream().toList())
                .build();

        return ResponseHandler.generateGetResponse("Freelancer profile retrieved successfully", HttpStatus.OK, freelancerViewProfileDto,1);
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
    public boolean addPortfolio(PortfolioRequestDto portfolioRequestDto) {
        Freelancer freelancer = freelancerRepository.findById(portfolioRequestDto.getFreelancer_id()).orElse(null);
        if (freelancer == null) {
            return false;
        }
        portfolioRepository.add_portfolio(
                portfolioRequestDto.getFreelancer_id(),
                portfolioRequestDto.getTitle(),
                portfolioRequestDto.getCover_image_url(),
                portfolioRequestDto.getPortfolio_skills(),
                portfolioRequestDto.getAttachments()
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
    public ResponseEntity<Object> publishPortfolio(UUID portfolio_id) {
        Portfolio portfolio = portfolioRepository.findById(portfolio_id).orElse(null);
        if (portfolio == null) {
            return  ResponseHandler.generateErrorResponse("Portfolio not found", HttpStatus.NOT_FOUND);
        }
        portfolioRepository.publish_portfolio(portfolio_id);
        return ResponseHandler.generateGeneralResponse("Portfolio published successfully", HttpStatus.OK);
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

    @Override
    public boolean updatePortfolio(PortfolioRequestDto portfolioRequestDto) {
        Portfolio portfolio = portfolioRepository.findById(portfolioRequestDto.getPortfolio_id()).orElse(null);
        if (portfolio == null) {
            return false;
        }
        portfolioRepository.update_portfolio(
                portfolioRequestDto.getPortfolio_id(),
                portfolioRequestDto.getTitle(),
                portfolioRequestDto.getCover_image_url(),
                portfolioRequestDto.getPortfolio_skills(),
                portfolioRequestDto.getAttachments()
        );
        return true;
    }

    @Override
    public ResponseEntity<Object> addService(ServiceDto serviceDto) {
        Freelancer freelancer = freelancerRepository.findById(serviceDto.getFreelancer_id()).orElse(null);
        if (freelancer == null) {
            return  ResponseHandler.generateErrorResponse("Freelancer not found", HttpStatus.NOT_FOUND);
        }
        List<UUID> freelancerPortfolioIds = portfolioRepository.getPortfolioIds(serviceDto.getFreelancer_id());
        if (!new HashSet<>(freelancerPortfolioIds).containsAll(List.of(serviceDto.getPortfolio_id()))) {
            return  ResponseHandler.generateErrorResponse("Some or all of these portfolios don't belong to this freelancer", HttpStatus.BAD_REQUEST);
        }
        serviceRepository.add_service(
                serviceDto.getFreelancer_id(),
                serviceDto.getService_title(),
                serviceDto.getService_description(),
                serviceDto.getService_skills(),
                serviceDto.getService_rate(),
                serviceDto.getMinimum_budget(),
                serviceDto.getService_thumbnail(),
                serviceDto.getPortfolio_id()
        );
        return ResponseHandler.generateGeneralResponse("Freelancer service added successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> unpublishService(UUID service_id) {
        ServiceModel service = serviceRepository.findById(service_id).orElse(null);
        if (service == null) {
            return  ResponseHandler.generateErrorResponse("Service not found", HttpStatus.NOT_FOUND);
        }
        serviceRepository.unpublish_service(service_id);
        return ResponseHandler.generateGeneralResponse("Freelancer service unpublished successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> publishService(UUID service_id) {
        ServiceModel service = serviceRepository.findById(service_id).orElse(null);
        if (service == null) {
            return  ResponseHandler.generateErrorResponse("Service not found", HttpStatus.NOT_FOUND);
        }
        serviceRepository.publish_service(service_id);
        return ResponseHandler.generateGeneralResponse("Freelancer service published successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> deleteService(UUID service_id) {
        ServiceModel service = serviceRepository.findById(service_id).orElse(null);
        if (service == null) {
            return  ResponseHandler.generateErrorResponse("Service not found", HttpStatus.NOT_FOUND);
        }
        serviceRepository.delete_service(service_id);
        return ResponseHandler.generateGeneralResponse("Freelancer service deleted successfully", HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Object> updateService(ServiceDto serviceDto) {
        ServiceModel service = serviceRepository.findById(serviceDto.getService_id()).orElse(null);
        if (service == null) {
            return  ResponseHandler.generateErrorResponse("Service not found", HttpStatus.NOT_FOUND);
        }
        List<UUID> freelancerPortfolioIds = portfolioRepository.getPortfolioIds(serviceDto.getFreelancer_id());
        if(serviceDto.getPortfolio_id() != null){
            for(UUID portfolioId: serviceDto.getPortfolio_id()){
                if (portfolioId.toString().matches("^[0-9a-fA-F]{8}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{12}$")){
                    return  ResponseHandler.generateErrorResponse("Some or all of these portfolio ids are not valid uuids", HttpStatus.BAD_REQUEST);
                }
                if(!freelancerPortfolioIds.contains(portfolioId)){
                    return  ResponseHandler.generateErrorResponse("Some or all of these portfolios don't belong to this freelancer", HttpStatus.BAD_REQUEST);
                }
            }
        }
        serviceRepository.update_service(
                serviceDto.getService_id(),
                serviceDto.getService_title(),
                serviceDto.getService_description(),
                serviceDto.getService_skills(),
                serviceDto.getService_rate(),
                serviceDto.getMinimum_budget(),
                serviceDto.getService_thumbnail(),
                serviceDto.getPortfolio_id()
        );
        return ResponseHandler.generateGeneralResponse("Freelancer service updated successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> addDedicatedResource(ResourceDto resourceDto) {
        Freelancer freelancer = freelancerRepository.findById(resourceDto.getFreelancer_id()).orElse(null);
        if (freelancer == null) {
            return  ResponseHandler.generateErrorResponse("Freelancer not found", HttpStatus.NOT_FOUND);
        }
        dedicatedResourceRepository.add_dedicated_resource(
                resourceDto.getFreelancer_id(),
                resourceDto.getResource_name(),
                resourceDto.getResource_title(),
                resourceDto.getResource_summary(),
                resourceDto.getResource_skills(),
                resourceDto.getResource_rate(),
                resourceDto.getMinimum_duration(),
                resourceDto.getResource_image(),
                resourceDto.getPortfolio_ids()
        );
        return ResponseHandler.generateGeneralResponse("Freelancer dedicated resource added successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> unpublishDedicatedResource(UUID resource_id) {
        DedicatedResource resource = dedicatedResourceRepository.findById(resource_id).orElse(null);
        if (resource == null) {
            return  ResponseHandler.generateErrorResponse("Resource not found", HttpStatus.NOT_FOUND);
        }
        dedicatedResourceRepository.unpublish_dedicated_resource(resource_id);
        return ResponseHandler.generateGeneralResponse("Freelancer dedicated resource unpublished successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> publishDedicatedResource(UUID resource_id) {
        DedicatedResource resource = dedicatedResourceRepository.findById(resource_id).orElse(null);
        if (resource == null) {
            return  ResponseHandler.generateErrorResponse("Resource not found", HttpStatus.NOT_FOUND);
        }
        dedicatedResourceRepository.publish_dedicated_resource(resource_id);
        return ResponseHandler.generateGeneralResponse("Freelancer dedicated resource published successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> deleteDedicatedResource(UUID resource_id) {
        DedicatedResource resource = dedicatedResourceRepository.findById(resource_id).orElse(null);
        if (resource == null) {
            return  ResponseHandler.generateErrorResponse("Resource not found", HttpStatus.NOT_FOUND);
        }
        dedicatedResourceRepository.delete_dedicated_resource(resource_id);
        return ResponseHandler.generateGeneralResponse("Freelancer dedicated resource deleted successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> updateDedicatedResource(ResourceDto resourceDto) {
        DedicatedResource resource = dedicatedResourceRepository.findById(resourceDto.getResource_id()).orElse(null);
        if (resource == null) {
            return  ResponseHandler.generateErrorResponse("Resource not found", HttpStatus.NOT_FOUND);
        }
        List<UUID> freelancerPortfolioIds = portfolioRepository.getPortfolioIds(resourceDto.getFreelancer_id());
        if(resourceDto.getPortfolio_ids() != null){
            for (UUID portfolioId : resourceDto.getPortfolio_ids()) {
                if (portfolioId.toString().matches("^[0-9a-fA-F]{8}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{12}$")){
                    return  ResponseHandler.generateErrorResponse("Some or all of these portfolio ids are not valid uuids", HttpStatus.BAD_REQUEST);
                }
                if(!freelancerPortfolioIds.contains(portfolioId)){
                    return  ResponseHandler.generateErrorResponse("Some or all of these portfolios don't belong to this freelancer", HttpStatus.BAD_REQUEST);
                }
            }
        }
        dedicatedResourceRepository.update_dedicated_resource(
                resourceDto.getResource_id(),
                resourceDto.getResource_name(),
                resourceDto.getResource_title(),
                resourceDto.getResource_summary(),
                resourceDto.getResource_skills(),
                resourceDto.getResource_rate(),
                resourceDto.getMinimum_duration(),
                resourceDto.getResource_image(),
                resourceDto.getPortfolio_ids()
        );
        return ResponseHandler.generateGeneralResponse("Freelancer dedicated resource updated successfully", HttpStatus.OK);
    }

//    @Override
//    public ResponseEntity<Object> addQuote(Quote quote) {
//        Freelancer freelancer = freelancerRepository.findById(quote.getFreelancer_id()).orElse(null);
//        if (freelancer == null) {
//            return  ResponseHandler.generateErrorResponse("Freelancer not found", HttpStatus.NOT_FOUND);
//        }
//        // message queue to check if the job exists
//        quoteRepository.add_quote(
//                quote.getFreelancer_id(),
//                quote.getJob_id(),
//                quote.getProposal(),
//                quote.getBids_used(),
//                quote.getBid_date()
//        );
//        return ResponseHandler.generateGeneralResponse("Freelancer quote added successfully", HttpStatus.OK);
//    }

    @Override
    public ResponseEntity<Object> getPortfolio(UUID portfolio_id) {
        //handle the portfolio view when integration with authentication
        Portfolio portfolio = portfolioRepository.findById(portfolio_id).orElse(null);
        if (portfolio == null) {
            return  ResponseHandler.generateErrorResponse("Portfolio not found", HttpStatus.NOT_FOUND);
        }
        PortfolioDetailsDto portfolioDetailsDto= portfolioRepository.get_portfolio_by_id(portfolio_id).getFirst();
        return ResponseHandler.generateGetResponse("Portfolio retrieved successfully", HttpStatus.OK, portfolioDetailsDto,1);
    }

    @Override
    public ResponseEntity<Object> getAllFreelancerPortfolios(UUID freelancer_id) {
        Freelancer freelancer = freelancerRepository.findById(freelancer_id).orElse(null);
        List<PortfolioListViewDto> portfolios = portfolioRepository.get_all_freelancer_portfolios(freelancer_id);
        if (freelancer == null) {
            return  ResponseHandler.generateErrorResponse("Freelancer not found", HttpStatus.NOT_FOUND);
        }
        return ResponseHandler.generateGetResponse("Portfolios retrieved successfully", HttpStatus.OK, portfolios,portfolios.size());
    }

    @Override
    public ResponseEntity<Object> getService(UUID service_id) {
        ServiceModel service = serviceRepository.findById(service_id).orElse(null);
        if (service == null) {
            return  ResponseHandler.generateErrorResponse("Service not found", HttpStatus.NOT_FOUND);
        }
        ServiceDetailsDto serviceDetailsDto = serviceRepository.get_service_details(service_id).getFirst();
        List<PortfolioServiceDetailsDto> portfolioServiceDetailsDtos = new ArrayList<>();
        if(serviceDetailsDto.getPortfolio_ids() != null){
            for (UUID portfolioId: serviceDetailsDto.getPortfolio_ids()){
                PortfolioListViewDto portfolio = portfolioRepository.get_portfolio_list_view_by_id(portfolioId).getFirst();
                PortfolioServiceDetailsDto portfolioServiceDetailsDto = new PortfolioServiceDetailsDto();
                portfolioServiceDetailsDto.setPortfolio_id(portfolio.getPortfolio_id());
                portfolioServiceDetailsDto.setTitle(portfolio.getTitle());
                portfolioServiceDetailsDto.setCover_image_url(portfolio.getCover_image_url());
                portfolioServiceDetailsDtos.add(portfolioServiceDetailsDto);
            }
        }
        ServiceWithEmbeddedPortfoliosDto serviceWithEmbeddedPortfoliosDto = getServiceWithEmbeddedPortfoliosDto(serviceDetailsDto, portfolioServiceDetailsDtos);
        return ResponseHandler.generateGetResponse("Service retrieved successfully", HttpStatus.OK, serviceWithEmbeddedPortfoliosDto,1);
    }

    private static ServiceWithEmbeddedPortfoliosDto getServiceWithEmbeddedPortfoliosDto(ServiceDetailsDto serviceDetailsDto, List<PortfolioServiceDetailsDto> portfolioServiceDetailsDtos) {
        ServiceWithEmbeddedPortfoliosDto serviceWithEmbeddedPortfoliosDto = new ServiceWithEmbeddedPortfoliosDto();
        serviceWithEmbeddedPortfoliosDto.setService_id(serviceDetailsDto.getService_id());
        serviceWithEmbeddedPortfoliosDto.setFreelancer_id(serviceDetailsDto.getFreelancer_id());
        serviceWithEmbeddedPortfoliosDto.setService_title(serviceDetailsDto.getService_title());
        serviceWithEmbeddedPortfoliosDto.setService_description(serviceDetailsDto.getService_description());
        serviceWithEmbeddedPortfoliosDto.setService_skills(serviceDetailsDto.getService_skills());
        serviceWithEmbeddedPortfoliosDto.setService_rate(serviceDetailsDto.getService_rate());
        serviceWithEmbeddedPortfoliosDto.setMinimum_budget(serviceDetailsDto.getMinimum_budget());
        serviceWithEmbeddedPortfoliosDto.setService_thumbnail(serviceDetailsDto.getService_thumbnail());
        serviceWithEmbeddedPortfoliosDto.setService_views(serviceDetailsDto.getService_views());
        serviceWithEmbeddedPortfoliosDto.setPortfolios(portfolioServiceDetailsDtos);
        return serviceWithEmbeddedPortfoliosDto;
    }

    @Override
    public ResponseEntity<Object> getAllFreelancerServices(UUID freelancer_id) {
        Freelancer freelancer = freelancerRepository.findById(freelancer_id).orElse(null);
        if (freelancer == null) {
            return  ResponseHandler.generateErrorResponse("Freelancer not found", HttpStatus.NOT_FOUND);
        }
        List<ServiceListViewDto> services = serviceRepository.get_all_freelancer_services(freelancer_id);
        return ResponseHandler.generateGetResponse("Services retrieved successfully", HttpStatus.OK, services,services.size());
    }

    @Override
    public ResponseEntity<Object> getDedicatedResource(UUID resource_id) {
        DedicatedResource resource = dedicatedResourceRepository.findById(resource_id).orElse(null);
        if (resource == null) {
            return  ResponseHandler.generateErrorResponse("Resource not found", HttpStatus.NOT_FOUND);
        }
        ResourceDetailsDto resourceDetailsDto = dedicatedResourceRepository.get_resource_details(resource_id).getFirst();
        List<PortfolioResourceDetailsDto> portfolioResourceDetailsDtos = new ArrayList<>();
        if(resourceDetailsDto.getPortfolio_ids() != null){
            for (UUID portfolioId: resourceDetailsDto.getPortfolio_ids()){
                PortfolioListViewDto portfolio = portfolioRepository.get_portfolio_list_view_by_id(portfolioId).getFirst();
                PortfolioResourceDetailsDto portfolioResourceDetailsDto = new PortfolioResourceDetailsDto();
                portfolioResourceDetailsDto.setPortfolio_id(portfolio.getPortfolio_id());
                portfolioResourceDetailsDto.setTitle(portfolio.getTitle());
                portfolioResourceDetailsDto.setCover_image_url(portfolio.getCover_image_url());
                portfolioResourceDetailsDtos.add(portfolioResourceDetailsDto);
            }
        }
        ResourceWithEmbeddedPortfoliosDto resourceWithEmbeddedPortfoliosDto = getResourceWithEmbeddedPortfoliosDto(resourceDetailsDto, portfolioResourceDetailsDtos);
        return ResponseHandler.generateGetResponse("Resource retrieved successfully", HttpStatus.OK, resourceWithEmbeddedPortfoliosDto,1);
    }

    private ResourceWithEmbeddedPortfoliosDto getResourceWithEmbeddedPortfoliosDto(ResourceDetailsDto resourceDetailsDto, List<PortfolioResourceDetailsDto> portfolioResourceDetailsDtos) {
        ResourceWithEmbeddedPortfoliosDto resourceWithEmbeddedPortfoliosDto = new ResourceWithEmbeddedPortfoliosDto();
        resourceWithEmbeddedPortfoliosDto.setResource_id(resourceDetailsDto.getResource_id());
        resourceWithEmbeddedPortfoliosDto.setFreelancer_id(resourceDetailsDto.getFreelancer_id());
        resourceWithEmbeddedPortfoliosDto.setResource_name(resourceDetailsDto.getResource_name());
        resourceWithEmbeddedPortfoliosDto.setResource_title(resourceDetailsDto.getResource_title());
        resourceWithEmbeddedPortfoliosDto.setResource_summary(resourceDetailsDto.getResource_summary());
        resourceWithEmbeddedPortfoliosDto.setResource_skills(resourceDetailsDto.getResource_skills());
        resourceWithEmbeddedPortfoliosDto.setResource_rate(resourceDetailsDto.getResource_rate());
        resourceWithEmbeddedPortfoliosDto.setMinimum_duration(resourceDetailsDto.getMinimum_duration());
        resourceWithEmbeddedPortfoliosDto.setResource_image(resourceDetailsDto.getResource_image());
        resourceWithEmbeddedPortfoliosDto.setPortfolios(portfolioResourceDetailsDtos);
        return resourceWithEmbeddedPortfoliosDto;
    }

    @Override
    public ResponseEntity<Object> getAllFreelancerDedicatedResources(UUID freelancer_id) {
        Freelancer freelancer = freelancerRepository.findById(freelancer_id).orElse(null);
        if (freelancer == null) {
            return  ResponseHandler.generateErrorResponse("Freelancer not found", HttpStatus.NOT_FOUND);
        }
        List<ResourceListViewDto> resources = dedicatedResourceRepository.get_all_freelancer_dedicated_resources(freelancer_id);
        return ResponseHandler.generateGetResponse("Dedicated resources retrieved successfully", HttpStatus.OK, resources,resources.size());
    }

    @Override
    public ResponseEntity<Object> addFeaturedTeamMembers(AddFeaturedTeamMembersRequestDto featuredTeamMembersRequestDto) {
        Freelancer freelancer = freelancerRepository.findById(featuredTeamMembersRequestDto.getFreelancer_id()).orElse(null);
        if( freelancer== null){
            return  ResponseHandler.generateErrorResponse("Freelancer not found", HttpStatus.NOT_FOUND);
        }
            featuredTeamMemberRepository.add_featured_team_members(
                    featuredTeamMembersRequestDto.getFreelancer_id(),
                    featuredTeamMembersRequestDto.getMember_names(),
                    featuredTeamMembersRequestDto.getTitles(),
                    featuredTeamMembersRequestDto.getMember_types(),
                    featuredTeamMembersRequestDto.getMember_emails()
            );

        return ResponseHandler.generateGeneralResponse("Featured team members added successfully", HttpStatus.OK);

    }
}
