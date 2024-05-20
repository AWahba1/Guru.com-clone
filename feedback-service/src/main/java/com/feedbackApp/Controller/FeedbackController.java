package com.feedbackApp.Controller;

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
    @Autowired
    private Feedback_By_FreelancerRepository feedbackByFreelancerRepository;
    @Autowired
    private Feedback_By_ClientRepository feedbackByClientRepository;

    @Autowired
    private  Feedback_By_ProjectRepository feedbackByProjectRepository;

    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private Testomonial_By_FreelancerRepository testomonialByFreelancerRepository;

    @Autowired
    private Reports_By_JobRepository reportsByJobRepository;

    @PostMapping("/Add")
    public ResponseEntity<?> CreateFeedback(@Valid @RequestBody FeedbackDTO feedbackDTO)
    {
        UUID feedback =UUID.randomUUID();
        Feedback_By_Freelancer feedbackByFreelancer =feedbackService.fromDTOFreelancer(feedbackDTO,feedback);
        Feedback_By_Client feedbackByClient = feedbackService.fromDTOClient(feedbackDTO,feedback);
        Feedback_By_Project feedbackByProject = feedbackService.fromDTOProject(feedbackDTO,feedback);

        try {
            feedbackByFreelancerRepository.save(feedbackByFreelancer);
            feedbackByClientRepository.save(feedbackByClient);
            feedbackByProjectRepository.save(feedbackByProject);
            return ResponseEntity.status(HttpStatus.CREATED).body("Feedback added");

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the feedback");
        }
    }

    @PutMapping("/Edit")
    public ResponseEntity<?> EditFeedback(@Valid @RequestBody FeedbackEditDTO feedbackEditDTO)
    {
        if(feedbackEditDTO.getSatisfied()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NO satisfied input is given");
        }

        System.out.println(feedbackEditDTO.getFreelancer_id());
        System.out.println(feedbackEditDTO.getClient_id());
        System.out.println(feedbackEditDTO.getJob_id());

        try {
            feedbackByFreelancerRepository.updateFeedbackFreelancer(feedbackEditDTO.getFreelancer_id(),feedbackEditDTO.getCreated_at(),feedbackEditDTO.getFeedback_id(),feedbackEditDTO.getSatisfied());
            feedbackByClientRepository.updatefeedbackClient(feedbackEditDTO.getClient_id(),feedbackEditDTO.getCreated_at(),feedbackEditDTO.getFeedback_id(),feedbackEditDTO.getSatisfied());
            feedbackByProjectRepository.updateFeedbackProject(feedbackEditDTO.getJob_id(),feedbackEditDTO.getCreated_at(),feedbackEditDTO.getFeedback_id(),feedbackEditDTO.getSatisfied());
            return ResponseEntity.status(HttpStatus.CREATED).body("Feedback updated");

        } catch (Exception e){
            //System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the feedback");
        }
    }

    @DeleteMapping("/Delete")
    public ResponseEntity<?> DeleteFeedback(@Valid @RequestBody FeedbackEditDTO feedbackEditDTO)
    {

        try {
            feedbackByFreelancerRepository.deleteFeedbackFreelancer(feedbackEditDTO.getFreelancer_id(),feedbackEditDTO.getCreated_at(),feedbackEditDTO.getFeedback_id());
            feedbackByClientRepository.deletefeedbackClient(feedbackEditDTO.getClient_id(),feedbackEditDTO.getCreated_at(),feedbackEditDTO.getFeedback_id());
            feedbackByProjectRepository.deleteFeedbackProject(feedbackEditDTO.getJob_id(),feedbackEditDTO.getCreated_at(),feedbackEditDTO.getFeedback_id());
            return ResponseEntity.status(HttpStatus.CREATED).body("Feedback Deleted");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the feedback");
        }
    }

    @GetMapping("/FreelancerFeedback/view")
    @Cacheable(value = "feedback",key = "#freelancer_id")
    public List<Feedback_By_Freelancer> findFreelancerFeedback(@RequestParam UUID freelancer_id) {
        try {
            System.out.println("get data from db");
            return feedbackByFreelancerRepository.findByCompositeKey(freelancer_id);
        }
        catch (Exception e){
            //System.out.println(e);
            return null;
        }

    }


    @GetMapping("/ClientFeedback/view")
    @Cacheable(value = "feedback",key = "#client_id")
    public List<Feedback_By_Client> findClientFeedback(@RequestParam UUID client_id) {
        try {
            System.out.println("get data from db");
            return feedbackByClientRepository.findByCompositeKey(client_id);
        }
        catch (Exception e){
            //System.out.println(e);
            return null;
        }

    }




    @GetMapping("/ProjectFeedback/view")
    public List<Feedback_By_Project> findMessageByCompositeKey(@RequestParam UUID job_id) {
        try {
            return feedbackByProjectRepository.findByCompositeKey(job_id);
        }
        catch (Exception e){
            //System.out.println(e);
            return null;
        }

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
