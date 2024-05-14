package com.messageApp.Controller;

import com.messageApp.DTO.FeedbackDTO;
import com.messageApp.DTO.TestimonialByFreelancerDTO;
import com.messageApp.Models.Feedback_By_Client;
import com.messageApp.Models.Feedback_By_Freelancer;
import com.messageApp.Models.Feedback_By_Project;
import com.messageApp.Models.Testomonial_By_Freelancer;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FeedbackService {

    public Feedback_By_Freelancer fromDTOFreelancer(FeedbackDTO dto,UUID feedback_id) {

        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);

        return new Feedback_By_Freelancer(
                dto.getFreelancer_id(),
                timestamp,
                feedback_id,
                dto.getClient_id(),
                dto.getJob_id(),
                dto.getClient_name(),
                dto.getJob_title(),
                dto.getSatisfied()
        );
    }

    public Feedback_By_Client fromDTOClient(FeedbackDTO dto,UUID feedback_id) {
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);

        return new Feedback_By_Client(
                dto.getClient_id(),
                timestamp,
                feedback_id,
                dto.getJob_id(),
                dto.getFreelancer_id(),
                dto.getClient_name(),
                dto.getJob_title(),
                dto.getSatisfied()
        );
    }

    public Feedback_By_Project fromDTOProject(FeedbackDTO dto,UUID feedback_id) {
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);

        return new Feedback_By_Project(
                dto.getJob_id(),
                timestamp,
                feedback_id,
                dto.getClient_id(),
                dto.getFreelancer_id(),
                dto.getClient_name(),
                dto.getJob_title(),
                dto.getSatisfied()
        );
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
