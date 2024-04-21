package com.guru.jobservice.controllers;

import com.guru.jobservice.dtos.JobDTO;
//import com.guru.jobservice.repositories.JobRepository;
import com.guru.jobservice.model.Job;
import com.guru.jobservice.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;
//
//    private final JobRepository jobRepository;
    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
//    @Autowired
//    public JobController(JobRepository jobRepository) {
//        this.jobRepository = jobRepository;
//    }

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return new ResponseEntity<>("Hello", HttpStatus.CREATED);

//        JobDTO createdJob = jobService.createJob(jobDTO);
//        return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }

    @PostMapping("/")
    public ResponseEntity<?> createJob(@RequestBody JobDTO jobDTO) {
        return new ResponseEntity<>("Hello", HttpStatus.CREATED);
//        JobDTO createdJob = jobService.createJob(jobDTO);
//        return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }

//    @PutMapping("/{jobId}")
//    public void updateJob(@PathVariable UUID jobId, @RequestBody JobDTO jobDTO) {
//        jobService.updateJob(jobId, jobDTO);
//    }
//
    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable UUID id) {
        jobService.deleteJobById(id);
    }

    @GetMapping("/{id}")
    public Job getJobById(@PathVariable UUID id) throws Exception {
        return jobService.getJobById(id);
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

