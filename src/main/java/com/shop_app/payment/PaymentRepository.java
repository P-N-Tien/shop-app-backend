package com.shop_app.payment;

import com.shop_app.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findBytransactionId(String txnRef);
}
