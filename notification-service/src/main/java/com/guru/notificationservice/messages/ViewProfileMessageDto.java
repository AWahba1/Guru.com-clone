package com.guru.notificationservice.messages;

import lombok.Data;

import java.util.UUID;

@Data
public class ViewProfileMessageDto {
    private UUID userId;
    private UUID viewerId;
    private String viewerName;
}
