package com.gcc.communityHall.paymentService.repository;


import com.gcc.communityHall.paymentService.entity.RefundClientInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundClientInfoRepository extends JpaRepository<RefundClientInfo, Integer> {
}
