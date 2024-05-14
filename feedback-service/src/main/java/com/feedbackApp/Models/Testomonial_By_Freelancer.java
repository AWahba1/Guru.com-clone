package com.feedbackApp.Models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "Testomonial_By_Freelancer")
@Data
public class Testomonial_By_Freelancer {

    @Id
    private UUID freelancer_id;

    private UUID testemonial_id;

    private String reviewer_name;

    private String review;

    public Testomonial_By_Freelancer(UUID freelancer_id, UUID testemonial_id , String reviewer_name, String review) {
        this.freelancer_id = freelancer_id;
        this.testemonial_id = testemonial_id ;
        this.reviewer_name = reviewer_name;
        this.review = review;
    }

    public UUID getFreelancer_id() {
        return freelancer_id;
    }

    public void setFreelancer_id(UUID freelancer_id) {
        this.freelancer_id = freelancer_id;
    }

    public UUID getTestemonial_id() {
        return testemonial_id;
    }

    public void setTestemonial_id(UUID testemonial_id) {
        this.testemonial_id = testemonial_id;
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
