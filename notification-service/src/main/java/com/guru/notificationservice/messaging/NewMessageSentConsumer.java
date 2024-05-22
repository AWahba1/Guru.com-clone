package com.guru.notificationservice.messaging;

import com.guru.notificationservice.messages.NewMessageSentDTO;
import com.guru.notificationservice.messages.ViewProfileMessageDto;
import com.guru.notificationservice.models.Notification;
import com.guru.notificationservice.repositories.NotificationsRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class NewMessageSentConsumer {
    private final NotificationsRepository notificationsRepository;

    public NewMessageSentConsumer(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    @RabbitListener(queues = "new_message_sent")
    public void consumeMessage(NewMessageSentDTO message) {
        notificationsRepository.save(new Notification(message.getUserId(), message.getSenderId(), message.getSenderName()+" has sent a message.",  new Timestamp(System.currentTimeMillis()), false));
    }
}







