package com.gcc.communityHall.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.BookingDetails.RefundStatus;
import com.gcc.communityHall.entity.Settlement;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement,Integer> {

		@Query("SELECT s FROM Settlement s JOIN FETCH s.bookingDetails bd WHERE s.settlementId = :settlementId")
	    Optional<Settlement> findSettlementWithBookingDetailsById(@Param("settlementId") int settlementId);
		
		List<Settlement> findByBookingDetailsRefundStatus(RefundStatus refundStatus);
		
		@EntityGraph(attributePaths = {"bookingDetails"})
	    Optional<Settlement> findById(int settlementId);
		
	}

