package com.guru.notificationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.guru.notificationservice.service.NotificationService;

import java.util.UUID;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    private NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

     @GetMapping("/{userId}")
     public ResponseEntity<Object> getAllNotificationsByUserId(@PathVariable UUID userId) {
         return notificationService.getAllNotificationsByUserId(userId);
     }

     @PatchMapping("/read/{id}")
        public ResponseEntity<Object> markNotificationAsRead(@PathVariable UUID id) {
            return notificationService.markNotificationAsRead(id);
        }

     @DeleteMapping("/{id}")
        public ResponseEntity<Object> deleteNotification(@PathVariable UUID id) {
            return notificationService.deleteNotification(id);
        }
}
