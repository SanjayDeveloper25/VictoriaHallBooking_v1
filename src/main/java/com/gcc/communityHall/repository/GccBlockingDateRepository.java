package com.gcc.communityHall.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.GccBlockingDate;
import com.gcc.communityHall.entity.GccBlockingDate.BlockedStatus;

@Repository
public interface GccBlockingDateRepository extends JpaRepository<GccBlockingDate,Integer> {

	@Query(value = "SELECT * FROM gcc_blocking_date WHERE hall_id IN (:hallIds) AND blocked_status = 'BLOCKED' AND start_date BETWEEN :fromDate AND :toDate ", nativeQuery = true)
	List<GccBlockingDate> findByFromDateAndToDateBetweenAndHallIdIn(LocalDate fromDate, LocalDate toDate, List<Long> hallIds);

	 @Query("SELECT g FROM GccBlockingDate g WHERE g.hallId = :hallId AND g.blockedStatus = :blockedStatus")
	    List<GccBlockingDate> findAllByHallIdAndBlockedStatus(@Param("hallId") String hallId, @Param("blockedStatus") GccBlockingDate.BlockedStatus blockedStatus);

	 @Query("SELECT g FROM GccBlockingDate g WHERE g.blockedStatus = 'BLOCKED' AND g.startDate >= CURRENT_DATE")
	 List<GccBlockingDate> findAllBlocked();

	@Query("SELECT g FROM GccBlockingDate g WHERE g.blockingReason = :blockingReason AND g.startDate <= :endDate AND g.endDate >= :startDate")
	List<GccBlockingDate> findByBlockingReasonAndDateRange( @Param("blockingReason") String blockingReason,@Param("startDate") Date startDate,@Param("endDate") Date endDate);

	@Query("SELECT COUNT(DISTINCT g.hallId) FROM GccBlockingDate g WHERE g.blockingReason = :blockingReason AND g.startDate <= :endDate AND g.endDate >= :startDate")
	int countBlockedHallsInDateRange(@Param("blockingReason") String blockingReason,@Param("startDate") Date startDate,@Param("endDate") Date endDate);

	@Query("SELECT COUNT(b) FROM GccBlockingDate b WHERE b.hallId = :hallId AND b.blockedStatus = 'BLOCKED' AND b.startDate BETWEEN :startDate AND :endDate")
	int countBlockedBookings(@Param("hallId") int hallId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
	
	   @Query(value = "SELECT * FROM gcc_blocking_date WHERE FIND_IN_SET(:bookingId, booking_ids) > 0", nativeQuery = true)
	    List<GccBlockingDate> getByBookingId(@Param("bookingId") int bookingId);
}
