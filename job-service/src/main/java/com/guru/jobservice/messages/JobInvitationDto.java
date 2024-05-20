package com.guru.jobservice.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobInvitationDto {
    private UUID clientId;
    private UUID freelancerId;
    private UUID jobId;
    private String jobTitle;
}