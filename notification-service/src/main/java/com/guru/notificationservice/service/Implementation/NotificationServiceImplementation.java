package com.guru.notificationservice.service.Implementation;
import com.guru.notificationservice.models.Notification;
import com.guru.notificationservice.repositories.NotificationsRepository;
import com.guru.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.guru.notificationservice.response.ResponseHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImplementation implements NotificationService{
    private NotificationsRepository notificationsRepository;

    @Autowired
    public NotificationServiceImplementation(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    @Override
    public ResponseEntity<Object> getAllNotificationsByUserId(UUID userId) {
        List<Notification> notifications= notificationsRepository.findAllNotificationsByUserId(userId);
        return ResponseHandler.generateGetResponse("All Notifications", HttpStatus.OK, notifications, notifications.size());
    }

    @Override
    public ResponseEntity<Object> markNotificationAsRead(UUID id) {
        Notification notification = notificationsRepository.findById(id).orElse(null);
        if(notification == null){
            return ResponseHandler.generateGeneralResponse("Notification not found", HttpStatus.NOT_FOUND);
        }
        notificationsRepository.markNotificationAsRead(id);
        return ResponseHandler.generateGeneralResponse("Notification marked as read", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> deleteNotification(UUID id) {
        notificationsRepository.deleteById(id);
        return ResponseHandler.generateGeneralResponse("Notification deleted", HttpStatus.OK);
    }


}
