package com.feedbackApp.Controller;

import com.feedbackApp.Command.*;
import com.feedbackApp.DTO.FeedbackDTO;
import com.feedbackApp.DTO.FeedbackEditDTO;
import com.feedbackApp.DTO.Reports_By_JobDTO;
import com.feedbackApp.DTO.TestimonialByFreelancerDTO;
import com.feedbackApp.Models.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
//    @Autowired
//    private Feedback_By_FreelancerRepository feedbackByFreelancerRepository;
//    @Autowired
//    private Feedback_By_ClientRepository feedbackByClientRepository;
//
//    @Autowired
//    private  Feedback_By_ProjectRepository feedbackByProjectRepository;

    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private Testomonial_By_FreelancerRepository testomonialByFreelancerRepository;

    @Autowired
    private Reports_By_JobRepository reportsByJobRepository;

    @PostMapping("/Add")
    public ResponseEntity<?> createFeedback(@Valid @RequestBody FeedbackDTO feedbackDTO) {
        //return feedbackService.createFeedback(feedbackDTO);
        Command createFeedbackCommand = new CreateFeedbackCommand(feedbackService, feedbackDTO);
        CommandInvoker invoker = new CommandInvoker();
        invoker.setCommand(createFeedbackCommand);

        invoker.executeCommand();
        return invoker.getResponse();
    }

    @PutMapping("/Edit")
    public ResponseEntity<?> editFeedback(@Valid @RequestBody FeedbackEditDTO feedbackEditDTO) {
        // Instantiate the command with the necessary dependencies
        Command editFeedbackCommand = new EditFeedbackCommand(feedbackService, feedbackEditDTO);
        CommandInvoker invoker = new CommandInvoker();
        invoker.setCommand(editFeedbackCommand);
        invoker.executeCommand();
        return invoker.getResponse();
    }

    @DeleteMapping("/Delete")
    public ResponseEntity<?> deleteFeedback(@Valid @RequestBody FeedbackEditDTO feedbackEditDTO) {
        Command deleteFeedbackCommand = new DeleteFeedbackCommand(feedbackService, feedbackEditDTO);
        CommandInvoker invoker = new CommandInvoker();
        invoker.setCommand(deleteFeedbackCommand);
        invoker.executeCommand();
        return invoker.getResponse();
    }

    @GetMapping("/FreelancerFeedback/view")
    public ResponseEntity<List<Feedback_By_Freelancer>> findFreelancerFeedback(@RequestParam UUID freelancer_id) {
        List<Feedback_By_Freelancer> feedbackList = feedbackService.findFreelancerFeedback(freelancer_id);
        if (feedbackList != null) {
            return ResponseEntity.ok(feedbackList);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/ClientFeedback/view")
    public ResponseEntity<List<Feedback_By_Client>> findClientFeedback(@RequestParam UUID client_id) {
        List<Feedback_By_Client> feedbackList = feedbackService.findClientFeedback(client_id);
        if (feedbackList != null) {
            return ResponseEntity.ok(feedbackList);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }




    @GetMapping("/ProjectFeedback/view")
    public ResponseEntity<?> findFeedbackByJobId(@RequestParam UUID job_id) {
        return feedbackService.findFeedbackByJobId(job_id);
    }



    @GetMapping("/Testomonial/view")
    public List<Testomonial_By_Freelancer> findTestomonial(@RequestParam UUID freelancer_id) {
        try {
            return testomonialByFreelancerRepository.findByCompositeKey(freelancer_id);
        }
        catch (Exception e){
           // System.out.println(e);
            return null;
        }

    }

    @PostMapping("/Testomonial/Add")
    public ResponseEntity<?> CreateTestomonial(@Valid @RequestBody TestimonialByFreelancerDTO testimonialByFreelancerDTO)
    {
        Testomonial_By_Freelancer testomonialByFreelancer = feedbackService.fromDTOTestiomonial(testimonialByFreelancerDTO);
        System.out.println(""+testimonialByFreelancerDTO.getFreelancer_id()+ "" +testimonialByFreelancerDTO.getReview()+ " " +testimonialByFreelancerDTO.getReviewer_name());

        try {
            testomonialByFreelancerRepository.save(testomonialByFreelancer);
            return ResponseEntity.status(HttpStatus.CREATED).body("testimonial added");
        } catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the Testomonial");
        }
    }


    @GetMapping("/Report/view")
    public List<Reports_By_Job> findReport(@RequestParam UUID job_id) {
        try {
            return reportsByJobRepository.findByCompositeKey(job_id);
        }
        catch (Exception e){
            // System.out.println(e);
            return null;
        }

    }

    @PostMapping("/Report/Add")
    public ResponseEntity<?> CreateReport(@Valid @RequestBody Reports_By_Job reportsByJob)
    {
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        reportsByJob.setReport_id(UUID.randomUUID());
        reportsByJob.setCreated_at(timestamp);

        try {
            reportsByJobRepository.save(reportsByJob);
            return ResponseEntity.status(HttpStatus.CREATED).body("reportsByJob added");
        } catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the reportsByJob");
        }
    }

    @DeleteMapping("/Report/Delete")
    public ResponseEntity<?> DeleteReport(@Valid @RequestBody Reports_By_JobDTO reportsByJobDTO)
    {
        try {
            reportsByJobRepository.deleteReport(reportsByJobDTO.getJob_id());
            return ResponseEntity.status(HttpStatus.CREATED).body("Report deleted");
        } catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the reportsByJob");
        }
    }

    @PutMapping("/Report/Edit")
    public ResponseEntity<?> EditReport(@Valid @RequestBody Reports_By_JobDTO reportsByJobDTO)
    {
        if(reportsByJobDTO.getReport()==null && reportsByJobDTO.getJobStatus()==null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No edit parameters added");
        }

        try {
            if(reportsByJobDTO.getReport()==null){
                reportsByJobRepository.UpdateReportJobStatus(reportsByJobDTO.getJob_id(),reportsByJobDTO.getJobStatus());
                return ResponseEntity.status(HttpStatus.CREATED).body("Report edited successfully");
            }else if(reportsByJobDTO.getJobStatus()==null){
                reportsByJobRepository.UpdateReportReport(reportsByJobDTO.getJob_id(),reportsByJobDTO.getReport());
                return ResponseEntity.status(HttpStatus.CREATED).body("Report edited successfully");
            }
            else {
                reportsByJobRepository.UpdateReportBoth(reportsByJobDTO.getJob_id(), reportsByJobDTO.getReport(), reportsByJobDTO.getJobStatus());
                return ResponseEntity.status(HttpStatus.CREATED).body("Report edited successfully");
            }
        } catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the reportsByJob");
        }
    }




}
