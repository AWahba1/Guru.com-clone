package com.guru.jobservice.messaging;

import com.guru.jobservice.messages.JobInvitationDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class JobInvitationMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public JobInvitationMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(JobInvitationDto message) {
        rabbitTemplate.convertAndSend("job_invitations_queue", message);
    }

}