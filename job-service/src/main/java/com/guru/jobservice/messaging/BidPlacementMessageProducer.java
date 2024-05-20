package com.guru.jobservice.messaging;

import com.guru.jobservice.messages.BidPlacementDto;
import com.guru.jobservice.messages.JobInvitationDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class BidPlacementMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public BidPlacementMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(BidPlacementDto message) {
        rabbitTemplate.convertAndSend("bid_placement_queue", message);
    }

}
