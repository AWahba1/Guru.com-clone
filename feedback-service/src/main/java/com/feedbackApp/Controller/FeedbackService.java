package com.feedbackApp.Controller;

import com.feedbackApp.DTO.FeedbackDTO;
import com.feedbackApp.DTO.TestimonialByFreelancerDTO;
import com.feedbackApp.Models.Feedback_By_Client;
import com.feedbackApp.Models.Feedback_By_Freelancer;
import com.feedbackApp.Models.Feedback_By_Project;
import com.feedbackApp.Models.Testomonial_By_Freelancer;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FeedbackService {

    public Feedback_By_Freelancer fromDTOFreelancer(FeedbackDTO dto,UUID feedback_id, Timestamp timestamp) {
        return Feedback_By_Freelancer.builder()
                .freelancer_id(dto.getFreelancer_id())
                .created_at(timestamp)
                .feedback_id(feedback_id)
                .client_id(dto.getClient_id())
                .job_id(dto.getJob_id())
                .client_name(dto.getClient_name())
                .job_title(dto.getJob_title())
                .satisfied(dto.getSatisfied())
                .build();
    }

    public Feedback_By_Client fromDTOClient(FeedbackDTO dto,UUID feedback_id,Timestamp timestamp) {

        return Feedback_By_Client.builder().
                client_id(dto.getClient_id()).
                created_at(timestamp).
                feedback_id(feedback_id).
                job_id(dto.getJob_id()).
                freelancer_id(dto.getFreelancer_id()).
                client_name(dto.getClient_name()).
                job_title(dto.getJob_title()).
                satisfied(dto.getSatisfied()).
                build();
    }

    public Feedback_By_Project fromDTOProject(FeedbackDTO dto,UUID feedback_id,Timestamp timestamp) {

        return Feedback_By_Project.builder().
                job_id(dto.getJob_id()).
                created_at(timestamp).
                feedback_id(feedback_id).
                client_id(dto.getClient_id()).
                freelancer_id(dto.getFreelancer_id()).
                client_name(dto.getClient_name()).
                job_title(dto.getJob_title()).
                satisfied(dto.getSatisfied()).
                build();
    }

    public Testomonial_By_Freelancer fromDTOTestiomonial(TestimonialByFreelancerDTO dto){
        return  new Testomonial_By_Freelancer(
                dto.getFreelancer_id(),
                UUID.randomUUID(),
                dto.getReviewer_name(),
                dto.getReview()
        );
    }




}
