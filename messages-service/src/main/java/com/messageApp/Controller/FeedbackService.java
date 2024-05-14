package com.messageApp.Controller;

import com.messageApp.DTO.FeedbackByClientDTO;
import com.messageApp.DTO.FeedbackByFreelancerDTO;
import com.messageApp.DTO.FeedbackByProjectDTO;
import com.messageApp.Models.Feedback_By_Client;
import com.messageApp.Models.Feedback_By_Freelancer;
import com.messageApp.Models.Feedback_By_Project;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class FeedbackService {

    public Feedback_By_Freelancer fromDTOFreelancer(FeedbackByFreelancerDTO dto) {

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

    public Feedback_By_Client fromDTOClient(FeedbackByClientDTO dto) {
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);

        return new Feedback_By_Client(
                dto.getClient_id(),
                timestamp,
                dto.getFeedback_id(),
                dto.getJob_id(),
                dto.getFreelancer_id(),
                dto.getClient_name(),
                dto.getJob_title(),
                dto.getSatisfied()
        );
    }

    public Feedback_By_Project fromDTOProject(FeedbackByProjectDTO dto) {
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);

        return new Feedback_By_Project(
                dto.getJob_id(),
                timestamp,
                dto.getFeedback_id(),
                dto.getClient_id(),
                dto.getFreelancer_id(),
                dto.getClient_name(),
                dto.getJob_title(),
                dto.getSatisfied()
        );
    }

}
