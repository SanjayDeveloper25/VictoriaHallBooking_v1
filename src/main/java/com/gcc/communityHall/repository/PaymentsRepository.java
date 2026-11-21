package com.gcc.communityHall.repository;

import com.gcc.communityHall.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface PaymentsRepository extends JpaRepository<Payments, Integer> {

	@Query(value = "SELECT * FROM payments WHERE payment_status in ('paid') ", nativeQuery = true)
	List<Payments> getPaymentlist();

	@Query(value = "SELECT p.* "
			+ "FROM payments p "
			+ "JOIN booking_details b ON p.booking_details_booking_id = b.booking_id "
			+ "JOIN hall_details h ON b.hall_details_hall_id = h.hall_id "
			+ "WHERE h.zone = :zone", nativeQuery = true)
	List<Payments> getPaymentListByzone(@Param("zone") String zone);

	// List<Payments> findAllWithBookingDetailsAndUsers();
	@Query(value = "select * from payments where booking_details_booking_id=?", nativeQuery = true)
	Payments findByBookingId(int bookingId);

	boolean existsByBookingDetails_BookingId(int bookingId);
}