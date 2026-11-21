package com.gcc.communityHall.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.BookingCalendar;

@Repository
public interface BookingCalendarRepository extends JpaRepository<BookingCalendar,Integer> {

	@Query(value="SELECT booking_id_1 , booking_id_2 FROM booking_calendar\r\n"
			+ "WHERE hall_id=:hallID AND bdate BETWEEN :fromDate AND :toDate", nativeQuery = true)
	List<String> getCancelling(String fromDate, String toDate, String hallID);

	@Query(value="select * from booking_calendar where hall_id=:hallID and bdate=:aa", nativeQuery = true)
	BookingCalendar getbyDates(LocalDate aa, String hallID);

	@Query(value="SELECT * FROM booking_calendar WHERE (booking_id_1=:bookID or booking_id_2=:bookID)",nativeQuery = true)
	List<BookingCalendar> getCancellingdata1(String bookID);
	
	@Query("SELECT c FROM BookingCalendar c WHERE c.bookingId_1 = :bookingId OR c.bookingId_2 = :bookingId")
	BookingCalendar getSingleCancellingData(@Param("bookingId") String bookingId);

	 BookingCalendar findByBdateAndHallId(LocalDate bdate, int hallId);

	@Query(value=" select * FROM booking_calendar where hall_id =:hall_id  AND bdate BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 6 MONTH)", nativeQuery = true)
	List<BookingCalendar> getPastData(int hall_id);


	@Query(value = "SELECT hall_id FROM booking_calendar where (bdate BETWEEN DATE_FORMAT(:fromDate, '%Y-%m-%d') AND DATE_FORMAT(:toDate, '%Y-%m-%d')) AND hall_id = :hall_id group by hall_id",nativeQuery = true)
	List<Integer> groupByHallIdToRemov(String fromDate, String toDate, int hall_id);

	@Query(value = " select * from booking_calendar where hall_id =:hallId and bdate BETWEEN :fromDate and :toDate order by hall_id", nativeQuery = true)
    List<BookingCalendar> getCalendarByBDateAndHAllId(String fromDate, String toDate, int hallId);

	@Query(value = " select * from booking_calendar where bdate BETWEEN :fromDate and :toDate order by hall_id", nativeQuery = true)
    List<BookingCalendar> getCalendarByBDate(String fromDate, String toDate);
	
	  @Query(value = "SELECT SUM(CASE WHEN booking_status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_booking_count,\r\n"
      		+ "SUM(CASE WHEN booking_status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_booking_count,\r\n"
      		+ "SUM(CASE WHEN refund_status = 'WAITING' THEN 1 ELSE 0 END) AS waiting_settlement_count,\r\n"
      		+ "SUM(CASE WHEN refund_status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_settlement_count,\r\n"
      		+ "SUM(CASE WHEN refund_status = 'REJECTED' THEN 1 ELSE 0 END) AS reject_settlement_count\r\n"
      		+ "FROM booking_details;",nativeQuery = true)
      List<Map<String, Object>> toatlBookingCount();
	  
	  @Query(value = "SELECT * FROM booking_calendar WHERE hall_id IN (:hallIdList) AND bdate BETWEEN :fromDate AND :endDate", nativeQuery = true)
	  List<BookingCalendar> findByHallIdAndDateRange(@Param("fromDate") LocalDate fromDate, @Param("endDate") LocalDate endDate, @Param("hallIdList") List<Long> hallIdList);

	  @Modifying
	    @Query("UPDATE BookingCalendar bc SET bc.bdate = :newDate WHERE (bc.bookingId_1 = :bookingId OR bc.bookingId_2 = :bookingId) AND bc.bdate = :currentDate")
	    void updateBdateByBookingIdAndDate(@Param("newDate") LocalDate newDate, @Param("bookingId") String bookingId, @Param("currentDate") LocalDate currentDate);
	Optional<BookingCalendar> findByBdate(LocalDate bdate);

	@Query(value = "SELECT bc.bdate, bc.booking_id_1, bc.booking_id_2, bc.morning_slot, bc.evening_slot, hd.hall_name, hd.hall_id " +
			"FROM booking_calendar bc " +
			"JOIN hall_details hd ON bc.hall_id = hd.hall_id " +
			"WHERE bc.bdate >= :fromDate " +
			"AND bc.bdate <= :toDate " +
			"AND (:bookingId IS NULL OR (bc.booking_id_1 = :bookingId OR bc.booking_id_2 = :bookingId)) " +
			"AND (:hallName = '' OR hd.hall_name = :hallName)",
			nativeQuery = true)
	List<Object[]> findBookingCalendarDetails(@Param("fromDate") String fromDate,
											  @Param("toDate") String toDate,
											  @Param("bookingId") Long bookingId,
											  @Param("hallName") String hallName);

	@Query(value="SELECT * from booking_calendar where booking_id_1 = :bookingId or booking_id_2 = :bookingId ", nativeQuery = true)
    List<BookingCalendar> getCalendarByBookingId(int bookingId);
}
