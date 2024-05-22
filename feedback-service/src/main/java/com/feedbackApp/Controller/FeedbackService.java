package com.feedbackApp.Controller;

import com.feedbackApp.DTO.FeedbackDTO;
import com.feedbackApp.DTO.FeedbackEditDTO;
import com.feedbackApp.DTO.TestimonialByFreelancerDTO;
import com.feedbackApp.Models.Feedback_By_Client;
import com.feedbackApp.Models.Feedback_By_Freelancer;
import com.feedbackApp.Models.Feedback_By_Project;
import com.feedbackApp.Models.Testomonial_By_Freelancer;
import com.feedbackApp.messaging.FeedbackCreatedDTO;
import com.feedbackApp.messaging.FeedbackCreatedProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FeedbackService {
    @Autowired
    private Feedback_By_FreelancerRepository feedbackByFreelancerRepository;
    @Autowired
    private Feedback_By_ClientRepository feedbackByClientRepository;

    @Autowired
    private  Feedback_By_ProjectRepository feedbackByProjectRepository;

    private final FeedbackCreatedProducer feedbackCreatedProducer;

    @Autowired
    public FeedbackService(FeedbackCreatedProducer feedbackCreatedProducer) {
        this.feedbackCreatedProducer = feedbackCreatedProducer;
    }

    public Feedback_By_Freelancer fromDTOFreelancer(FeedbackDTO dto, UUID feedback_id, Timestamp timestamp) {
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

    public ResponseEntity<?> createFeedback(FeedbackDTO feedbackDTO) {
        UUID feedback = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);

        Feedback_By_Freelancer feedbackByFreelancer = fromDTOFreelancer(feedbackDTO, feedback, timestamp);
        Feedback_By_Client feedbackByClient = fromDTOClient(feedbackDTO, feedback, timestamp);
        Feedback_By_Project feedbackByProject = fromDTOProject(feedbackDTO, feedback, timestamp);

        try {
            feedbackByFreelancerRepository.save(feedbackByFreelancer);
            feedbackByClientRepository.save(feedbackByClient);
            feedbackByProjectRepository.save(feedbackByProject);
            feedbackCreatedProducer.sendMessage(new FeedbackCreatedDTO(feedbackByClient.getClient_id(), feedbackByClient.getFreelancer_id(),feedbackByClient.getJob_title()));
            return ResponseEntity.status(HttpStatus.CREATED).body("Feedback added");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the feedback");
        }
    }

    public ResponseEntity<?> editFeedback(FeedbackEditDTO feedbackEditDTO) {
        if (feedbackEditDTO.getSatisfied() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NO satisfied input is given");
        }

        try {
            feedbackByFreelancerRepository.updateFeedbackFreelancer(feedbackEditDTO.getFreelancer_id(), feedbackEditDTO.getCreated_at(), feedbackEditDTO.getFeedback_id(), feedbackEditDTO.getSatisfied());
            feedbackByClientRepository.updatefeedbackClient(feedbackEditDTO.getClient_id(), feedbackEditDTO.getCreated_at(), feedbackEditDTO.getFeedback_id(), feedbackEditDTO.getSatisfied());
            feedbackByProjectRepository.updateFeedbackProject(feedbackEditDTO.getJob_id(), feedbackEditDTO.getCreated_at(), feedbackEditDTO.getFeedback_id(), feedbackEditDTO.getSatisfied());
            return ResponseEntity.status(HttpStatus.CREATED).body("Feedback updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the feedback");
        }
    }

    public ResponseEntity<?> deleteFeedback(FeedbackEditDTO feedbackEditDTO) {
        try {
            feedbackByFreelancerRepository.deleteFeedbackFreelancer(feedbackEditDTO.getFreelancer_id(), feedbackEditDTO.getCreated_at(), feedbackEditDTO.getFeedback_id());
            feedbackByClientRepository.deletefeedbackClient(feedbackEditDTO.getClient_id(), feedbackEditDTO.getCreated_at(), feedbackEditDTO.getFeedback_id());
            feedbackByProjectRepository.deleteFeedbackProject(feedbackEditDTO.getJob_id(), feedbackEditDTO.getCreated_at(), feedbackEditDTO.getFeedback_id());
            return ResponseEntity.status(HttpStatus.CREATED).body("Feedback Deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the feedback");
        }
    }

    public ResponseEntity<?> findFeedbackByJobId(UUID job_id) {
        try {
            List<Feedback_By_Project> feedbackList = feedbackByProjectRepository.findByCompositeKey(job_id);
            return ResponseEntity.ok(feedbackList);
        } catch (Exception e) {
            // Log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public List<Feedback_By_Freelancer> findFreelancerFeedback(UUID freelancer_id) {
        try {
            System.out.println("get data from db");
            return feedbackByFreelancerRepository.findByCompositeKey(freelancer_id);
        } catch (Exception e) {
            // Log the exception
            return null;
        }
    }

    public List<Feedback_By_Client> findClientFeedback(UUID client_id) {
        try {
            System.out.println("get data from db");
            return feedbackByClientRepository.findByCompositeKey(client_id);
        } catch (Exception e) {
            // Log the exception
            return null;
        }
    }







}
