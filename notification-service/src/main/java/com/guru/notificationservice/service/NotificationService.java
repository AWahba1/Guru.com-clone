package com.guru.notificationservice.service;

import com.guru.notificationservice.response.ResponseHandler;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface NotificationService {

    ResponseEntity<Object> getAllNotificationsByUserId(UUID userId);

    ResponseEntity<Object> markNotificationAsRead(UUID id);

    ResponseEntity<Object> deleteNotification(UUID id);
}
