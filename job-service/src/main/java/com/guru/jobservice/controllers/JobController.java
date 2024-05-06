package com.guru.jobservice.controllers;

import com.guru.jobservice.dtos.CreateUpdateRequest;
import com.guru.jobservice.dtos.FiltersRequest;
import com.guru.jobservice.dtos.JobViewRequest;
import com.guru.jobservice.dtos.PaginatedResponse;
import com.guru.jobservice.model.Job;
import com.guru.jobservice.services.JobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    @PostMapping("/view")
    public void viewJob(@Valid @RequestBody JobViewRequest viewRequest) {
        jobService.markJobAsViewed(viewRequest);
    }

    @PostMapping("/upload")
    public void addAttachment(@RequestParam("file") MultipartFile file) throws IOException {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uploadDir = "../Uploads/" + fileName;

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }
}

