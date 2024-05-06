package com.guru.freelancerservice.repositories;

import com.guru.freelancerservice.dtos.freelancer.FreelancerProfileDto;
import com.guru.freelancerservice.models.Freelancer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface FreelancerRepository extends JpaRepository<Freelancer, UUID> {


    @Query(value = "select * from get_freelancer_profile(?1)", nativeQuery = true)
    List<FreelancerProfileDto> getFreelancerProfile(UUID _freelancer_id);

    @Procedure(name = "update_freelancer_profile_about_section")
    void update_freelancer_profile_about_section(UUID freelancer_id, String new_freelancer_name, String new_image_url, String new_tagline, String new_bio, String new_work_terms, String[] new_attachments, String new_user_type, String new_website_link, String new_facebook_link, String new_linkedin_link, String new_professional_video_link, String new_company_history, Timestamp new_operating_since);

    @Procedure(name = "toggle_profile_visibility")
    void toggle_profile_visibility(UUID freelancer_id);

    @Procedure(name = "increment_profile_views")
    void increment_profile_views(UUID freelancer_id, UUID viewer_id);

    @Query(value = "select viewer_id from profile_views where freelancer_id=(?1) and viewer_id=(?2)", nativeQuery = true)
    List<UUID> get_profile_views(UUID freelancer_id, UUID viewer_id);



}






