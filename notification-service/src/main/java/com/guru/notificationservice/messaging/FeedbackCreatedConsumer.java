package com.guru.notificationservice.messaging;

import com.guru.notificationservice.messages.FeedbackCreatedDTO;
import com.guru.notificationservice.messages.NewMessageSentDTO;
import com.guru.notificationservice.messages.ViewProfileMessageDto;
import com.guru.notificationservice.models.Notification;
import com.guru.notificationservice.repositories.NotificationsRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class FeedbackCreatedConsumer {

    private final NotificationsRepository notificationsRepository;

    public FeedbackCreatedConsumer(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    @RabbitListener(queues = "feedback_created")
    public void consumeMessage(FeedbackCreatedDTO message){
        notificationsRepository.save(new Notification(message.getUserId(), message.getSenderId(),"feedback created on your job" +message.getJob_title(),  new Timestamp(System.currentTimeMillis()), false));
    }
}
