package com.raj.bookmyshow.repo;

import com.raj.bookmyshow.entity.Booking;
import com.raj.bookmyshow.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(String id);

}
