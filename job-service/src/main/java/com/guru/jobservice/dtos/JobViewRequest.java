package com.guru.jobservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobViewRequest {

    @com.guru.jobservice.validators.UUID
    private String jobId;

    @com.guru.jobservice.validators.UUID
    private String freelancerId;

    public UUID getJobId() {
        return UUID.fromString(jobId);
    }

    public UUID getFreelancerId() {
        return UUID.fromString(freelancerId);
    }
}

