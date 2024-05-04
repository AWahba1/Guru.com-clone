package com.guru.jobservice.controllers;

import com.guru.jobservice.dtos.JobRequest;
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

import java.util.List;
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

    @GetMapping("/")
    public PaginatedResponse<Job> getAllJobs(
            @RequestParam(defaultValue = "1")
            @PositiveOrZero(message = "Page number must be positive or zero")
            int page,

            @RequestParam(defaultValue = "10")
            @PositiveOrZero(message = "Page size must be positive or zero")
            int pageSize,

            @RequestParam(required = false) String searchQuery,

            @RequestParam(required = false)
            @com.guru.jobservice.validators.UUID
            String categoryId,
            @RequestParam(required = false)
            @com.guru.jobservice.validators.UUID
            String subcategoryId,
            @RequestParam(required = false)
            @com.guru.jobservice.validators.UUID
            String skillId,
            @RequestParam(required = false) Boolean featuredOnly,
            @RequestParam(required = false) PaymentType paymentType,

            @RequestParam(required = false)
            @com.guru.jobservice.validators.UUID
            String locationId,

            @RequestParam(required = false) SortOrder sortOrder,
            @RequestParam(required = false) JobStatus[] statusList,
            @RequestParam(required = false) Boolean verifiedOnlyClients,

            @RequestParam(required = false)
            @Min(0)
            Integer minEmployerSpend,

            @RequestParam(required = false)
            @Min(0)
            Integer maxQuotesReceived,

            @RequestParam(required = false)
            Boolean notViewed,

            @RequestParam(required = false)
            Boolean notApplied,

            @RequestParam(required = false)
            @com.guru.jobservice.validators.UUID
            String freelancerId

    ) {
        return jobService.getAllJobs(page, pageSize, searchQuery, categoryId, subcategoryId, skillId,
                featuredOnly, paymentType, locationId, sortOrder, statusList, verifiedOnlyClients, minEmployerSpend,
                maxQuotesReceived, notViewed, notApplied, freelancerId);
    }
}

