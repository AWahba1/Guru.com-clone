package com.guru.payment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.guru.payment.models.Payment;
import com.guru.payment.repositories.PaymentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Cacheable(value = "payments")
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Cacheable(value = "payment", key = "#id")
    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }
}
