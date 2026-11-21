package com.gcc.communityHall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

import com.gcc.communityHall.entity.HallRequestDetails;

@Repository
public interface HallRequestDetailsRepository extends JpaRepository<HallRequestDetails, Integer> {

    @Query(value =" SELECT r.*, b.*, h.*, u.* "+
                " FROM hall_request_details r "+
                " JOIN booking_details b ON r.booking_id = b.booking_id "+
                " JOIN hall_details h ON b.hall_details_hall_id = h.hall_id "+
                " JOIN users u ON b.users_user_id = u.user_id "+
                " WHERE b.booking_status = 'PENDING'", nativeQuery = true)
    List<HallRequestDetails> findPendingRequests();

    @Query("""
                SELECT hrd FROM HallRequestDetails hrd
                JOIN FETCH hrd.bookingDetails b
                JOIN FETCH b.hallDetails
                JOIN FETCH b.users
                WHERE b.bookingStatus IN ('APPROVED', 'WAITING_FOR_NOC')
            """)
    List<HallRequestDetails> findApprovedRequests();

    @Query("""
                SELECT hrd FROM HallRequestDetails hrd
                JOIN FETCH hrd.bookingDetails b
                JOIN FETCH b.hallDetails
                JOIN FETCH b.users
                WHERE b.bookingStatus = 'REJECTED'
            """)
    List<HallRequestDetails> findRejectedRequests();
}
