package com.guru.notificationservice.messaging;

import com.guru.notificationservice.messages.ViewProfileMessageDto;
import com.guru.notificationservice.models.Notifications;
import com.guru.notificationservice.repositories.NotificationsRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ViewProfileMessageConsumer {
private final NotificationsRepository notificationsRepository;

    public ViewProfileMessageConsumer(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    @RabbitListener(queues = "view_profile_queue")
    public void consumeMessage(ViewProfileMessageDto message) {
        notificationsRepository.save(new Notifications(message.getUserId(), message.getViewerId(), message.getViewerName()+" has viewed your profile",  new Timestamp(System.currentTimeMillis())));
    }
}
