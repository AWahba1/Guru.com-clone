package com.guru.notificationservice.messaging;

import com.guru.notificationservice.messages.JobInvitationDto;
import com.guru.notificationservice.models.Notification;
import com.guru.notificationservice.repositories.NotificationsRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class JobInvitationMessageConsumer {
    private final NotificationsRepository notificationsRepository;

    public JobInvitationMessageConsumer(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    @RabbitListener(queues = "job_invitations_queue")
    public void consumeMessage(JobInvitationDto message) {
        notificationsRepository.save(new Notification( message.getFreelancerId() , message.getClientId()+" has invited you to job " + message.getJobTitle() + " Number: " + message.getJobId(), new Timestamp(System.currentTimeMillis()), false));
    }
}