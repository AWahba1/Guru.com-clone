package com.guru.jobservice.services;

import com.guru.jobservice.dtos.JobRequest;
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

    public void createJob(JobRequest jobRequest) {

        JobRequestValidator.validatePaymentType(jobRequest);

        jobRepository.createJob(
                jobRequest.getTitle(),
                jobRequest.getDescription(),
                jobRequest.getCategoryId(),
                jobRequest.getSubcategoryId(),
                jobRequest.getIsFeatured(),
                jobRequest.getClientId(),
                jobRequest.getPaymentType(),
                jobRequest.getFixedPriceRange(),
                jobRequest.getDuration(),
                jobRequest.getHoursPerWeek(),
                jobRequest.getMinHourlyRate(),
                jobRequest.getMaxHourlyRate(),
                jobRequest.getGetQuotesUntil(),
                jobRequest.getVisibility(),
                jobRequest.getSkills(),
                jobRequest.getTimezones(),
                jobRequest.getLocations()
        );
}

    public void updateJob(UUID jobId, JobRequest jobRequest) {

        Job job = jobRepository.getJobById(jobId);
        if (job == null) {
            throw new ResourceNotFoundException();
        }
        JobRequestValidator.validatePaymentType(jobRequest);
        jobRepository.updateJob(
                jobId,
                jobRequest.getTitle(),
                jobRequest.getDescription(),
                jobRequest.getCategoryId(),
                jobRequest.getSubcategoryId(),
                jobRequest.getIsFeatured(),
                jobRequest.getPaymentType(),
                jobRequest.getFixedPriceRange(),
                jobRequest.getDuration(),
                jobRequest.getHoursPerWeek(),
                jobRequest.getMinHourlyRate(),
                jobRequest.getMaxHourlyRate(),
                jobRequest.getGetQuotesUntil(),
                jobRequest.getVisibility(),
                jobRequest.getStatus(),
                jobRequest.getSkills(),
                jobRequest.getTimezones(),
                jobRequest.getLocations()
        );
    }

    public void deleteJobById(UUID jobId) {
        Job job = jobRepository.getJobById(jobId);
        if (job == null) {
            throw new ResourceNotFoundException();
        }
        jobRepository.deleteJobById(jobId);
    }


    public PaginatedResponse<Job> getAllJobs(int page, int pageSize, String searchQuery, String categoryId,
                                             String subcategoryId, String skillId, Boolean featuredOnly,
                                             PaymentType paymentType, String locationId, SortOrder sortOrder,
                                             JobStatus[] statusList, Boolean verifiedOnlyClients, Integer minEmployerSpend,
                                             Integer maxQuotesReceived, Boolean notViewed, Boolean notApplied, String freelancerId) {

        String typeOfPayment  = paymentType != null ? paymentType.getValue() : null;
        String sortingOrder  = sortOrder != null ? sortOrder.getValue() : SortOrder.NEWEST.getValue();

        UUID categoryUUID = categoryId!=null ? UUID.fromString(categoryId) : null;
        UUID subcategoryUUID = subcategoryId!=null ? UUID.fromString(subcategoryId) : null;
        UUID skillUUID = skillId!=null ? UUID.fromString(skillId) : null;
        UUID locationUUID = locationId!=null ? UUID.fromString(locationId) : null;

        if ((notApplied != null || notViewed != null) && freelancerId == null) {
            throw new ValidationException("Freelancer ID must be set if not applied or viewed filters are applied");
        }

        UUID freelancerUUID = freelancerId!=null ? UUID.fromString(freelancerId) : null;

        String[] statuses = (statusList==null || statusList.length==0)? null : Stream.of(statusList).map(JobStatus::getValue).toArray(String[]::new);
        List<Job> jobs = jobRepository.getAllJobs(
                page, pageSize,
                searchQuery,
                categoryUUID, subcategoryUUID, skillUUID,
                featuredOnly,
                typeOfPayment, locationUUID, sortingOrder,
                statuses, verifiedOnlyClients, minEmployerSpend,
                maxQuotesReceived, notViewed, notApplied, freelancerUUID
        );

        int recordsCount = jobs.size();
        return new PaginatedResponse<Job>(jobs, page, recordsCount);
    }
}
