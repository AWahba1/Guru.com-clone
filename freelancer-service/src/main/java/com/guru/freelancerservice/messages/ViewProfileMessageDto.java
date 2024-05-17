package com.guru.freelancerservice.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewProfileMessageDto {
    private UUID userId;
    private UUID viewerId;
    private String viewerName;
}
