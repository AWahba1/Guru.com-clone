package com.guru.jobservice.services;

import com.guru.jobservice.dtos.JobRequest;
import com.guru.jobservice.exceptions.ResourceNotFoundException;
import com.guru.jobservice.model.Job;
import com.guru.jobservice.repositories.JobRepository;
import com.guru.jobservice.validators.JobRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

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



//    public List<JobDTO> getAllJobs(int page, int pageSize, String searchQuery, UUID categoryId,
//                                   UUID subcategoryId, UUID skillId, Boolean featuredOnly,
//                                   String paymentTerms, UUID locationId, String sortOrder,
//                                   List<String> statusList) {
//        return jobRepository.getAllJobs(page, pageSize, searchQuery, categoryId, subcategoryId,
//                skillId, featuredOnly, paymentTerms, locationId, sortOrder, statusList);
//    }
}
