package com.messageApp.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ViewProfileMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public ViewProfileMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void sendMessage(NewMessageSentDTO message) {
        rabbitTemplate.convertAndSend("new_message_sent", message);
    }
}
