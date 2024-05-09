package com.guru.payment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guru.payment.models.Payment;
import com.guru.payment.repositories.PaymentRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

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
    @CacheEvict(value = {"payments", "payment"}, key = "#id")
    public void deletePaymentById(Long id) {
        paymentRepository.deleteById(id);
    }

}
