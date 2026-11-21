package com.gcc.communityHall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.BookingDocuments;

@Repository
public interface BookingDocumentsRepository extends JpaRepository<BookingDocuments,Integer> {
	
	@Transactional
	@Modifying
	@Query(value="delete from booking_documents where booking_details_booking_id = :bookingId",nativeQuery=true)
	public void deleteByBookingId(int bookingId);
}
