package com.guru.jobservice.controllers;

import com.guru.jobservice.dtos.JobRequest;
import com.guru.jobservice.model.Job;
import com.guru.jobservice.services.JobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }


    @GetMapping("/{id}")
    public Job getJobById(@PathVariable UUID id) throws Exception {
        return jobService.getJobById(id);
    }

    @PostMapping("/")
    public void createJob(@Valid @RequestBody JobRequest jobRequest) {
        jobService.createJob(jobRequest);
    }

    @PutMapping("/{id}")
    public void updateJob(@PathVariable UUID id, @Valid @RequestBody JobRequest jobRequest) {
        jobService.updateJob(id, jobRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable UUID id) {
        jobService.deleteJobById(id);
    }



//    @GetMapping("/")
//    public List<JobDTO> getAllJobs(@RequestParam int page,
//                                   @RequestParam int pageSize,
//                                   @RequestParam int jobId,
//                                   @RequestParam(required = false) String searchQuery,
//                                   @RequestParam(required = false) UUID categoryId,
//                                   @RequestParam(required = false) UUID subcategoryId,
//                                   @RequestParam(required = false) UUID skillId,
//                                   @RequestParam(required = false) Boolean featuredOnly,
//                                   @RequestParam(required = false) String paymentTerms,
//                                   @RequestParam(required = false) UUID locationId,
//                                   @RequestParam(defaultValue = "newest") String sortOrder,
//                                   @RequestParam(required = false) List<String> statusList) {
//        return jobService.getAllJobs(page, pageSize, searchQuery, categoryId, subcategoryId, skillId,
//                featuredOnly, paymentTerms, locationId, sortOrder, statusList);
//    }
}

