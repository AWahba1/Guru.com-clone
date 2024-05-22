package com.feedbackApp.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class FeedbackCreatedProducer {
    private final RabbitTemplate rabbitTemplate;

    public FeedbackCreatedProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void sendMessage(FeedbackCreatedDTO message) {
        rabbitTemplate.convertAndSend("feedback_created", message);
    }
}
