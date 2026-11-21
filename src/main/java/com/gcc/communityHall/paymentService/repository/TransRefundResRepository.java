package com.gcc.communityHall.paymentService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.RefundTransaction;
import com.gcc.communityHall.paymentService.entity.RefundResponse;





@Repository
public interface TransRefundResRepository  extends JpaRepository<RefundResponse,Integer>{

	@Query(value = "SELECT * FROM refund_response where user_id=:userId", nativeQuery = true)
	List<RefundResponse> getRefundResponseByuserId(String userId);

	//List<Object[]> getRefundResponseByBookingId(String fromDate, String toDate, Long bookingId);
	
	 @Query(value = "SELECT rr.booking_id, bd.booking_date, bd.refund_status, rr.amount, rr.txansaction_id, rr.user_id, rr.balace_amount " +
             "FROM refund_response rr " +
             "JOIN booking_details bd ON rr.booking_id = bd.booking_id " +
             "WHERE rr.date >= :fromDate " +
             "AND rr.date <= :toDate " +
             "AND (rr.booking_id = :bookingId OR :bookingId IS NULL)", nativeQuery = true)
List<Object[]> getRefundResponseByBookingId(@Param("fromDate") String fromDate, 
                                          @Param("toDate") String toDate, 
                                          @Param("bookingId") Long bookingId);

}
