package com.guru.freelancerservice.dtos.freelancer;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.guru.freelancerservice.models.user_type_enum;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@JsonPropertyOrder({
        "freelancer_id", "freelancer_name", "image_url", "visibility", "profile_views",
        "job_invitations_num", "available_bids", "all_time_earnings", "employers_num",
        "highest_paid", "membership_date", "tagline", "bio", "work_terms", "attachments",
        "user_type", "website_link", "facebook_link", "linkedin_link", "professional_video_link",
        "company_history", "operating_since", "resourceSkills", "serviceSkills"
})
public interface FreelancerProfileDto {
    UUID getFreelancer_id();
    String getFreelancer_name();
    String getImage_url();
    boolean getVisibility();
    int getProfile_views();
    int getJob_invitations_num();
    int getAvailable_bids();
    BigDecimal getAll_time_earnings();
    int getEmployers_num();
    BigDecimal getHighest_paid();
    Timestamp getMembership_date();
    String getTagline();
    String getBio();
    String getWork_terms();
    List<String> getAttachments();
    user_type_enum getUser_type();
    String getWebsite_link();
    String getFacebook_link();
    String getLinkedin_link();
    String getProfessional_video_link();
    String getCompany_history();
    Timestamp getOperating_since();
    String[] getResource_skills();
    String[] getService_skills();
    String[] getPortfolio_service_skills();
    String[] getPortfolio_resource_skills();
}
