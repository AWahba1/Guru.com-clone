package com.guru.jobservice.controllers;

import com.guru.jobservice.dtos.CreateUpdateRequest;
import com.guru.jobservice.dtos.FiltersRequest;
import com.guru.jobservice.dtos.PaginatedResponse;
import com.guru.jobservice.enums.JobStatus;
import com.guru.jobservice.enums.PaymentType;
import com.guru.jobservice.enums.SortOrder;
import com.guru.jobservice.model.Job;
import com.guru.jobservice.services.JobService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
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
    public void createJob(@Valid @RequestBody CreateUpdateRequest createUpdateRequest) {
        jobService.createJob(createUpdateRequest);
    }

    @PutMapping("/{id}")
    public void updateJob(@PathVariable UUID id, @Valid @RequestBody CreateUpdateRequest createUpdateRequest) {
        jobService.updateJob(id, createUpdateRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable UUID id) {
        jobService.deleteJobById(id);
    }

    @GetMapping("/")
    public PaginatedResponse<Job> getAllJobs(@Valid FiltersRequest filtersRequest) {
        return jobService.getAllJobs(filtersRequest);
    }
}

