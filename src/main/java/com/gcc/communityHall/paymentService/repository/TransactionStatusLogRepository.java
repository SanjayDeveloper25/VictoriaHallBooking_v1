package com.gcc.communityHall.paymentService.repository;

import com.gcc.communityHall.paymentService.entity.TransactionStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionStatusLogRepository extends JpaRepository<TransactionStatusLog, Integer> {
}