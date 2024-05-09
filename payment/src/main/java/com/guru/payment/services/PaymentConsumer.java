package com.guru.payment.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {
    @KafkaListener(topics = "payments", groupId = "guru-payment-group")
    public void listen(String message) {
        // Process message
    }
}
