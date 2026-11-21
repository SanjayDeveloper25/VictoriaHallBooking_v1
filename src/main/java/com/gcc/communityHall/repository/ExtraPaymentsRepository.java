package com.gcc.communityHall.repository;

import com.gcc.communityHall.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.EbDetails;
import com.gcc.communityHall.entity.ExtraPayments;

import java.util.Optional;

@Repository
public interface ExtraPaymentsRepository extends JpaRepository<ExtraPayments,Integer> {

    @Query(value = "SELECT * FROM extra_payments WHERE booking_details_booking_id = ? ",nativeQuery = true)
    Optional<ExtraPayments> getExtraPaymentsByBookingId(int id);

}
