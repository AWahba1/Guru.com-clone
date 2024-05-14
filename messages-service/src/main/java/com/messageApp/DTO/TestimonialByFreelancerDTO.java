package com.messageApp.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class TestimonialByFreelancerDTO {

    @NotNull
    private UUID freelancer_id;

    @NotNull
    @NotBlank(message = "reviewer_name should not be empty")
    private String reviewer_name;

    @NotNull
    @NotBlank(message = "review should not be empty")
    private String review;


    public TestimonialByFreelancerDTO(UUID freelancer_id, String reviewer_name, String review) {
        this.freelancer_id = freelancer_id;
        this.reviewer_name = reviewer_name;
        this.review = review;
    }

    public UUID getFreelancer_id() {
        return freelancer_id;
    }

    public void setFreelancer_id(UUID freelancer_id) {
        this.freelancer_id = freelancer_id;
    }

    public String getReviewer_name() {
        return reviewer_name;
    }

    public void setReviewer_name(String reviewer_name) {
        this.reviewer_name = reviewer_name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
