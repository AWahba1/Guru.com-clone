package com.guru.payment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.guru.payment.models.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}

