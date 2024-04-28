package com.guru.freelancerservice.services.implementation;

import com.guru.freelancerservice.dtos.FreelancerProfileDto;
import com.guru.freelancerservice.models.Freelancer;
import com.guru.freelancerservice.models.user_type_enum;
import com.guru.freelancerservice.repositories.FreelancerRepository;
import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FreelancerImplementation implements FreelancerService {
    private final FreelancerRepository freelancerRepository;
    //private final FreelancerService freelancerService ;

    @Autowired
    public FreelancerImplementation(FreelancerRepository freelancerRepository) {
        this.freelancerRepository = freelancerRepository;
        //this.freelancerService = freelancerService;
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
    private Freelancer getFreelancerFromObject(Object[] object) {
        Freelancer freelancer = Freelancer.builder()
                .freelancer_id((UUID) object[0])
                .freelancer_name((String) object[1])
                .image_url((String) object[2])
                .visibility((Boolean) object[3])
                .profile_views((Integer) object[4])
                .job_invitations_num((Integer) object[5])
                .available_bids((Integer) object[6])
                .all_time_earnings((BigDecimal) object[7])
                .employers_num((Integer) object[8])
                .highest_paid((BigDecimal) object[9])
                .membership_date((Timestamp) object[10])
                .tagline((String) object[11])
                .bio((String) object[12])
                .work_terms((String) object[13])
                .attachments((populateAttachments((Object[]) object[14])))
                .user_type(user_type_enum.valueOf((String) object[15]))
                .website_link((String) object[16])
                .facebook_link((String) object[17])
                .linkedin_link((String) object[18])
                .professional_video_link((String) object[19])
                .company_history((String) object[20])
                .operating_since((Timestamp) object[21])
                .build();
        return freelancer;
    }
    public List<String> populateSkills(Object[] object){
        List<String> resourceSkills = new ArrayList<>();
        for(int i=0; i<object.length; i++){
            resourceSkills.add((String) object[i]);
        }
        return resourceSkills;
    }
    public List<String> populateAttachments(Object[] object){
        List<String> attachments = new ArrayList<>();
        for(int i=0; i<object.length; i++){
            attachments.add((String) object[i]);
        }
        return attachments;
    }
}
