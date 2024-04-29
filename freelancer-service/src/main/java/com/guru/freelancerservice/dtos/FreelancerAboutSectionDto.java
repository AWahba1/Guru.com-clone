package com.guru.freelancerservice.dtos;

import com.guru.freelancerservice.models.user_type_enum;
import lombok.*;


import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FreelancerAboutSectionDto {
    /*
    IN freelancer_id uuid,
    IN new_freelancer_name varchar(50),
    IN new_image_url varchar(255),
    IN new_tagline varchar(190),
    IN new_bio varchar(3000),
    IN new_work_terms varchar(2000),
    IN new_attachments TEXT[],
    IN new_user_type user_type_enum,
    IN new_website_link varchar(255),
    IN new_facebook_link varchar(255),
    IN new_linkedin_link varchar(255),
    IN new_professional_video_link varchar(255),
    IN new_company_history varchar(3000),
    IN new_operating_since TIMESTAMP
     */

    private UUID freelancer_id;
    private String freelancer_name;
    private String image_url;
    private String tagline;
    private String bio;
    private String work_terms;
    private String[] attachments;
    private user_type_enum user_type;
    private String website_link;
    private String facebook_link;
    private String linkedin_link;
    private String professional_video_link;
    private String company_history;
    private Timestamp operating_since;


}
