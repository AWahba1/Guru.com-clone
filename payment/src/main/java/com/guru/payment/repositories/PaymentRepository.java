package com.guru.payment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.guru.payment.models.Payment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Procedure("CreatePayment")
    void createPayment(
            @Param("user_id") String userId,
            @Param("amount") Double amount,
            @Param("status") String status);

    @Procedure("UpdatePayment")
    void updatePayment(
            @Param("payment_id") Long paymentId,
            @Param("user_id") String userId,
            @Param("amount") Double amount,
            @Param("status") String status,
            @Param("created_at") Date createdAt);

    @Procedure("DeletePaymentById")
    void deletePaymentById(@Param("payment_id") Long paymentId);

    @Query(value = "SELECT * FROM GetAllPayments(?1, ?2, ?3, ?4)", nativeQuery = true)
    List<Payment> getAllPayments(
            @Param("user_id") String userId,
            @Param("status") String status,
            @Param("min_amount") Double minAmount,
            @Param("max_amount") Double maxAmount);
}

