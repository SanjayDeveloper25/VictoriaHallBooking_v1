package com.gcc.communityHall.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.BookingHistory;

@Repository
public interface BookingHistoryRepository extends JpaRepository<BookingHistory,Integer> {
	
	@Query(value = "SELECT * FROM booking_history where booking_details_booking_id= :bookingId", nativeQuery = true)
	BookingHistory findbybookingId(int bookingId);
}
