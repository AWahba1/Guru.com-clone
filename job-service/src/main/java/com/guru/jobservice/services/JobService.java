package com.guru.jobservice.services;

//import com.guru.jobservice.dtos.JobDTO;
//import com.guru.jobservice.repositories.JobRepository;
//import com.guru.jobservice.dtos.JobResponse;
import com.guru.jobservice.dtos.JobRequest;
import com.guru.jobservice.enums.PaymentType;
import com.guru.jobservice.exceptions.ResourceNotFoundException;
import com.guru.jobservice.exceptions.ValidationException;
import com.guru.jobservice.model.Job;
import com.guru.jobservice.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JobService {

    private final JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }


    public void createJob(JobRequest jobRequest) {

        if (jobRequest.getPaymentType().equals(PaymentType.FIXED.getValue()))
        {
            if (jobRequest.getFixedPriceRange() == null)
            {
                throw new ValidationException("A price range must be set if payment type is fixed");
            }

            if(jobRequest.getDuration()!=null || jobRequest.getHoursPerWeek()!=null || jobRequest.getMinHourlyRate()!=null || jobRequest.getMaxHourlyRate()!=null);
            {
                throw new ValidationException("Job Duration, Hours per week, min and max hourly rates should be null");
            }
        }

        if (jobRequest.getPaymentType().equals(PaymentType.HOURLY.getValue()))
        {
            if (jobRequest.getFixedPriceRange() != null)
            {
                throw new ValidationException("A price range must be null if payment type is hourly");
            }

            if(jobRequest.getDuration()==null || jobRequest.getHoursPerWeek()==null || jobRequest.getMinHourlyRate()==null || jobRequest.getMaxHourlyRate()==null);
            {
                throw new ValidationException("Job Duration, Hours per week, min and max hourly rates should be SET if payment type is hourly");
            }
        }

        if (jobRequest.getMinHourlyRate()!= null && jobRequest.getMaxHourlyRate()!= null )
        {
            if (jobRequest.getMinHourlyRate().compareTo(jobRequest.getMaxHourlyRate()) > 0)
                throw new ValidationException("Minimum Hourly Rate cannot be greater than max Hourly Rate");
        }
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

//    public void updateJob(UUID jobId, JobDTO jobDTO) {
//        //jobRepository.updateJob(jobId, jobDTO);
//    }

    public void deleteJobById(UUID jobId) {
        Job job = jobRepository.getJobById(jobId);
        if (job == null) {
            throw new ResourceNotFoundException();
        }
        jobRepository.deleteJobById(jobId);
    }

    public Job getJobById(UUID jobId){
        Job job = jobRepository.getJobById(jobId);
        if (job == null) {
            throw new ResourceNotFoundException();
        }
        return job;

    }

//    public List<JobDTO> getAllJobs(int page, int pageSize, String searchQuery, UUID categoryId,
//                                   UUID subcategoryId, UUID skillId, Boolean featuredOnly,
//                                   String paymentTerms, UUID locationId, String sortOrder,
//                                   List<String> statusList) {
//        return jobRepository.getAllJobs(page, pageSize, searchQuery, categoryId, subcategoryId,
//                skillId, featuredOnly, paymentTerms, locationId, sortOrder, statusList);
//    }
}
