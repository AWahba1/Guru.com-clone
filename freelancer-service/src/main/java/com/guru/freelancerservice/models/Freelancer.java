package com.guru.freelancerservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Freelancer {
    @Id
    @GeneratedValue(
            strategy = GenerationType.UUID
    )
    private UUID freelancer_id;
    private String freelancer_name;
    private String image_url;
    private boolean visibility;
    private int profile_views;
    private int job_invitations_num;
    private int available_bids;
    private double all_time_earnings;
    private int employers_num;
    private double highest_paid;
    private String membership_date;
    private String tagline;
    private String bio;
    private String work_terms;
    private List<String> attachments;
    private String user_type;
    private String website_link;
    private String facebook_link;
    private String linkedin_link;
    private String professional_video_link;
    private String company_history;
    private String operating_since;

//    public Freelancer() {
//    }

//    public Freelancer(UUID freelancer_id, String freelancer_name, String image_url, boolean visibility, int profile_views, int job_invitations_num, int available_bids, double all_time_earnings, int employers_num, double highest_paid, String membership_date, String tagline, String bio, String work_terms, String[] attachments, String user_type, String website_link, String facebook_link, String linkedin_link, String professional_video_link, String company_history, String operating_since) {
//        this.freelancer_id = freelancer_id;
//        this.freelancer_name = freelancer_name;
//        this.image_url = image_url;
//        this.visibility = visibility;
//        this.profile_views = profile_views;
//        this.job_invitations_num = job_invitations_num;
//        this.available_bids = available_bids;
//        this.all_time_earnings = all_time_earnings;
//        this.employers_num = employers_num;
//        this.highest_paid = highest_paid;
//        this.membership_date = membership_date;
//        this.tagline = tagline;
//        this.bio = bio;
//        this.work_terms = work_terms;
//        this.attachments = List.of(attachments);
//        this.user_type = user_type;
//        this.website_link = website_link;
//        this.facebook_link = facebook_link;
//        this.linkedin_link = linkedin_link;
//        this.professional_video_link = professional_video_link;
//        this.company_history = company_history;
//        this.operating_since = operating_since;
//    }




}
