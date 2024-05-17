package com.guru.freelancerservice.dtos.freelancer;

import com.guru.freelancerservice.models.user_type_enum;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FreelancerViewProfileDto {
    private UUID id;
    private String freelancer_name;
    private String image_url;
    private boolean visibility;
    private int profile_views;
    private int job_invitations_num;
    private int available_bids;
    private BigDecimal all_time_earnings;
    private int employers_num;
    private BigDecimal highest_paid;
    private Timestamp membership_date;
    private String tagline;
    private String bio;
    private String work_terms;
    private List<String> attachments;
    private user_type_enum user_type;
    private String website_link;
    private String facebook_link;
    private String linkedin_link;
    private String professional_video_link;
    private String company_history;
    private Timestamp operating_since;
    private List<String> skills;
}
