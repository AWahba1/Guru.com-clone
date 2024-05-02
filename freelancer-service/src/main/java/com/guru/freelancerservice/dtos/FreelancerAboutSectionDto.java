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
