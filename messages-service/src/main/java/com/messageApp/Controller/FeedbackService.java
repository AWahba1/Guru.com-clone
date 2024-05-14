package com.messageApp.Controller;

import com.messageApp.DTO.FeedbackByFreelancerDTO;
import com.messageApp.Models.Feedback_By_Freelancer;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class FeedbackService {

    public Feedback_By_Freelancer fromDTO(FeedbackByFreelancerDTO dto) {

        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);

        return new Feedback_By_Freelancer(
                dto.getFreelancer_id(),
                timestamp,
                dto.getFeedback_id(),
                dto.getClient_id(),
                dto.getJob_id(),
                dto.getClient_name(),
                dto.getJob_title(),
                dto.getSatisfied()
        );
    }

}
