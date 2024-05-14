package com.messageApp.Controller;

import com.messageApp.DTO.FeedbackByClientDTO;
import com.messageApp.DTO.FeedbackByFreelancerDTO;
import com.messageApp.DTO.FeedbackByProjectDTO;
import com.messageApp.DTO.MessageInputDTO;
import com.messageApp.Models.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    private Feedback_By_FreelancerRepository feedbackByFreelancerRepository;
    @Autowired
    private Feedback_By_ClientRepository feedbackByClientRepository;

    @Autowired
    private  Feedback_By_ProjectRepository feedbackByProjectRepository;

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/FreelancerFeedback/Add")
    public ResponseEntity<?> createFeedbackByFreelancer(@Valid @RequestBody FeedbackByFreelancerDTO feedbackByFreelancerDTO)
    {
        Feedback_By_Freelancer feedbackByFreelancer =feedbackService.fromDTOFreelancer(feedbackByFreelancerDTO);

        try {
            feedbackByFreelancerRepository.save(feedbackByFreelancer);
            return ResponseEntity.status(HttpStatus.CREATED).body(feedbackByFreelancer);

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the message");
        }
    }

    @GetMapping("/FreelancerFeedback/view")
    public List<Feedback_By_Freelancer> findFreelancerFeedback(@RequestParam UUID freelancer_id) {
        try {
            return feedbackByFreelancerRepository.findByCompositeKey(freelancer_id);
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }

    }

    @PostMapping("/ClientFeedback/Add")
    public ResponseEntity<?> createFeedbackByClient(@Valid @RequestBody FeedbackByClientDTO feedbackByClientDTO)
    {
        Feedback_By_Client feedbackByClient = feedbackService.fromDTOClient(feedbackByClientDTO);

        try {
            feedbackByClientRepository.save(feedbackByClient);
            return ResponseEntity.status(HttpStatus.CREATED).body(feedbackByClientDTO);

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the message");
        }
    }

    @GetMapping("/ClientFeedback/view")
    public List<Feedback_By_Client> findClientFeedback(@RequestParam UUID client_id) {
        try {
            return feedbackByClientRepository.findByCompositeKey(client_id);
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }

    }

    @PostMapping("/ProjectFeedback/Add")
    public ResponseEntity<?> createFeedbackByProject(@Valid @RequestBody FeedbackByProjectDTO feedbackByProjectDTO)
    {
        Feedback_By_Project feedbackByProject = feedbackService.fromDTOProject(feedbackByProjectDTO);

        try {
            feedbackByProjectRepository.save(feedbackByProject);
            return ResponseEntity.status(HttpStatus.CREATED).body(feedbackByProject);

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the message");
        }
    }


    @GetMapping("/ProjectFeedback/view")
    public List<Feedback_By_Project> findMessageByCompositeKey(@RequestParam UUID job_id) {
        try {
            return feedbackByProjectRepository.findByCompositeKey(job_id);
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }

    }



}
