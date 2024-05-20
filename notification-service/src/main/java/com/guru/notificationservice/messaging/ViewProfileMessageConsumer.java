package com.guru.notificationservice.messaging;

import com.guru.notificationservice.messages.NewMessageSentDTO;
import com.guru.notificationservice.messages.ViewProfileMessageDto;
import com.guru.notificationservice.models.Notification;
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

    @RabbitListener(queues = "new_message_sent")
    public void consumeMessage(NewMessageSentDTO message) {
        notificationsRepository.save(new Notifications(message.getUserId(), message.getSenderId(), message.getSenderName()+" has sent a message.",  new Timestamp(System.currentTimeMillis()), false));
    @RabbitListener(queues = "view_profile_queue")
    public void consumeMessage(ViewProfileMessageDto message) {
        notificationsRepository.save(new Notification( message.getUserId() , message.getViewerName()+" has viewed your profile", new Timestamp(System.currentTimeMillis()), false));
    }
    }
}
