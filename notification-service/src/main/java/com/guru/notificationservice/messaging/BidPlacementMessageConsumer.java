package com.guru.notificationservice.messaging;

import com.guru.notificationservice.messages.BidPlacementDto;
import com.guru.notificationservice.models.Notification;
import com.guru.notificationservice.repositories.NotificationsRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class BidPlacementMessageConsumer {

    private final NotificationsRepository notificationsRepository;

    public BidPlacementMessageConsumer(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    @RabbitListener(queues = "bid_placement_queue")
    public void consumeMessage(BidPlacementDto message) {
        notificationsRepository.save(new Notification( message.getClientId() , message.getFreelancerId()+" has placed a bid on " + message.getJobTitle() + " Number: " + message.getJobId() + " on " + message.getBidDate() + " using " + message.getBidsUsed() + " Proposal: " + message.getProposal(), new Timestamp(System.currentTimeMillis()), false));
    }

}
