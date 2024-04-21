package com.guru.jobservice.services;

import com.guru.jobservice.dtos.JobDTO;
//import com.guru.jobservice.repositories.JobRepository;
import com.guru.jobservice.dtos.JobResponse;
import com.guru.jobservice.exceptions.ResourceNotFoundException;
import com.guru.jobservice.model.Job;
import com.guru.jobservice.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JobService {

    private final JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

//    public void createJob(JobDTO jobDTO) {
//        //jobRepository.createJob(jobDTO);
//    }
//
//    public void updateJob(UUID jobId, JobDTO jobDTO) {
//        //jobRepository.updateJob(jobId, jobDTO);
//    }
//
    public void deleteJobById(UUID jobId) {
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
