package com.guru.jobservice.services;

import com.guru.jobservice.dtos.CreateUpdateRequest;
import com.guru.jobservice.dtos.FiltersRequest;
import com.guru.jobservice.dtos.PaginatedResponse;
import com.guru.jobservice.enums.JobStatus;
import com.guru.jobservice.enums.PaymentType;
import com.guru.jobservice.enums.SortOrder;
import com.guru.jobservice.exceptions.ResourceNotFoundException;
import com.guru.jobservice.exceptions.ValidationException;
import com.guru.jobservice.model.Job;
import com.guru.jobservice.repositories.JobRepository;
import com.guru.jobservice.validators.JobRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
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

    public void createJob(CreateUpdateRequest createUpdateRequest) {

        JobRequestValidator.validatePaymentType(createUpdateRequest);

        jobRepository.createJob(
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
                createUpdateRequest.getLocations()
        );
}

    public void updateJob(UUID jobId, CreateUpdateRequest createUpdateRequest) {

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
                createUpdateRequest.getLocations()
        );
    }

    public void deleteJobById(UUID jobId) {
        Job job = jobRepository.getJobById(jobId);
        if (job == null) {
            throw new ResourceNotFoundException();
        }
        jobRepository.deleteJobById(jobId);
    }


    public PaginatedResponse<Job> getAllJobs(FiltersRequest filtersRequest) {

        if ((filtersRequest.getNotApplied() != null || filtersRequest.getNotViewed() != null) && filtersRequest.getFreelancerId() == null) {
            throw new ValidationException("Freelancer ID must be set if not applied or viewed filters are applied");
        }

        List<Job> jobs = jobRepository.getAllJobs(
                filtersRequest.getPage(), filtersRequest.getPageSize(),
                filtersRequest.getSearchQuery(),
                filtersRequest.getCategoryId(), filtersRequest.getSubcategoryId(), filtersRequest.getSkillId(),
                filtersRequest.getFeaturedOnly(),
                filtersRequest.getPaymentType(), filtersRequest.getLocationId(), filtersRequest.getSortOrder(),
                filtersRequest.getStatusList(), filtersRequest.getVerifiedOnlyClients(), filtersRequest.getMinEmployerSpend(),
                filtersRequest.getMaxQuotesReceived(), filtersRequest.getNotViewed(), filtersRequest.getNotApplied(), filtersRequest.getFreelancerId()
        );

        int recordsCount = jobs.size();
        return new PaginatedResponse<Job>(jobs, filtersRequest.getPage(), recordsCount);
    }
}
