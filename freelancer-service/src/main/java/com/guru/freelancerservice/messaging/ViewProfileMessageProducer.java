package com.guru.freelancerservice.messaging;

import com.guru.freelancerservice.messages.ViewProfileMessageDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ViewProfileMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public ViewProfileMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void sendMessage(ViewProfileMessageDto message) {
        rabbitTemplate.convertAndSend("view_profile_queue", message);
    }
}
