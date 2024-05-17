package com.guru.jobservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.guru.jobservice.dtos.CreateUpdateRequest;
import com.guru.jobservice.dtos.FiltersRequest;
import com.guru.jobservice.dtos.JobViewRequest;
import com.guru.jobservice.dtos.PaginatedResponse;
import com.guru.jobservice.exceptions.ResourceNotFoundException;
import com.guru.jobservice.exceptions.ValidationException;
import com.guru.jobservice.helpers.AttachmentsHelper;
import com.guru.jobservice.model.Job;
import com.guru.jobservice.repositories.JobRepository;
import com.guru.jobservice.validators.JobRequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class JobService {

    private final JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job getJobById(UUID jobId){
        Job job = jobRepository.getJobById(jobId);
        if (job == null) {
            throw new ResourceNotFoundException();
        }
        return job;
    }

        public Job createJob(CreateUpdateRequest createUpdateRequest) throws JsonProcessingException {
        JobRequestValidator.validatePaymentType(createUpdateRequest);

        String [] attachmentJsonArray = AttachmentsHelper.convertAttachmentsToJson(createUpdateRequest.getAttachments());

        return jobRepository.createJob(
                createUpdateRequest.getTitle(),
                createUpdateRequest.getDescription(),
                createUpdateRequest.getCategoryId(),
                createUpdateRequest.getSubcategoryId(),
                createUpdateRequest.getIsFeatured(),
                createUpdateRequest.getClientId(),
                createUpdateRequest.getPaymentType(),
                createUpdateRequest.getFixedPriceRange(),
                createUpdateRequest.getDuration(),
                createUpdateRequest.getHoursPerWeek(),
                createUpdateRequest.getMinHourlyRate(),
                createUpdateRequest.getMaxHourlyRate(),
                createUpdateRequest.getGetQuotesUntil(),
                createUpdateRequest.getVisibility(),
                createUpdateRequest.getSkills(),
                createUpdateRequest.getTimezones(),
                createUpdateRequest.getLocations(),
                attachmentJsonArray
        );
    }

    public void updateJob(UUID jobId, CreateUpdateRequest createUpdateRequest) throws Exception {

        String [] attachmentJsonArray = AttachmentsHelper.convertAttachmentsToJson(createUpdateRequest.getAttachments());

        Job job = jobRepository.getJobById(jobId);
        if (job == null) {
            throw new ResourceNotFoundException();
        }
        JobRequestValidator.validatePaymentType(createUpdateRequest);
        jobRepository.updateJob(
                jobId,
                createUpdateRequest.getTitle(),
                createUpdateRequest.getDescription(),
                createUpdateRequest.getCategoryId(),
                createUpdateRequest.getSubcategoryId(),
                createUpdateRequest.getIsFeatured(),
                createUpdateRequest.getPaymentType(),
                createUpdateRequest.getFixedPriceRange(),
                createUpdateRequest.getDuration(),
                createUpdateRequest.getHoursPerWeek(),
                createUpdateRequest.getMinHourlyRate(),
                createUpdateRequest.getMaxHourlyRate(),
                createUpdateRequest.getGetQuotesUntil(),
                createUpdateRequest.getVisibility(),
                createUpdateRequest.getStatus(),
                createUpdateRequest.getSkills(),
                createUpdateRequest.getTimezones(),
                createUpdateRequest.getLocations(),
                attachmentJsonArray
        );
    }

    public void deleteJobById(UUID jobId) {
        Job job = jobRepository.getJobById(jobId);
        if (job == null) {
            throw new ResourceNotFoundException();
        }
        jobRepository.deleteJobById(jobId);
    }


    @Cacheable(value = "jobs", key = "#filtersRequest")
    public PaginatedResponse<Job> getAllJobs(FiltersRequest filtersRequest) {

        if ((filtersRequest.getNotApplied() != null || filtersRequest.getNotViewed() != null) && filtersRequest.getFreelancerId() == null) {
            throw new ValidationException("Freelancer ID must be set if not applied or viewed filters are applied");
        }

        List<Job> jobs = jobRepository.getAllJobs(
                filtersRequest.getPage(),
                filtersRequest.getPageSize(),
                filtersRequest.getSearchQuery(),
                filtersRequest.getCategoryId(),
                filtersRequest.getSubcategoryId(),
                filtersRequest.getSkillId(),
                filtersRequest.getFeaturedOnly(),
                filtersRequest.getPaymentType(),
                filtersRequest.getLocationIds(),
                filtersRequest.getSortOrder(),
                filtersRequest.getStatusList(),
                filtersRequest.getVerifiedOnlyClients(),
                filtersRequest.getMinEmployerSpend(),
                filtersRequest.getMaxQuotesReceived(),
                filtersRequest.getNotViewed(),
                filtersRequest.getNotApplied(),
                filtersRequest.getFreelancerId(),
                filtersRequest.getClientId()
        );

        int recordsCount = jobs.size();
        return new PaginatedResponse<Job>(jobs, filtersRequest.getPage(), recordsCount);
    }

    public void markJobAsViewed(JobViewRequest jobViewRequest) {
        UUID jobId = jobViewRequest.getJobId();
        UUID freelancerId = jobViewRequest.getFreelancerId();
        Job job = jobRepository.getJobById(jobId);
        if (job == null) {
            throw new ResourceNotFoundException();
        }

        // TODO: VALIDATE FREELANCER ID USING MESSAGE QUEUE HERE
        jobRepository.markJobAsViewed(jobId, freelancerId);
    }
}
