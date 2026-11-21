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

import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.BookingDetails.BookingStatus;
import com.gcc.communityHall.entity.HallDetails;

@Repository
public interface BookingDetailsRepository extends JpaRepository<BookingDetails, Integer> {
        @Query(value = "SELECT * FROM booking_details where booking_status='GCC_BLOCKED'", nativeQuery = true)
        List<BookingDetails> getGccBlockedList();

        @Query(value = "SELECT * FROM booking_details where booking_status='APPROVED'", nativeQuery = true)
        List<BookingDetails> getAllApprovedList();

        @Query(value = "SELECT * FROM booking_details where booking_status='REJECTED'", nativeQuery = true)
        List<BookingDetails> getAllRejectingList();

        @Query(value = "SELECT * FROM booking_details where booking_status='WAITING_FOR_NOC_APPROVAL'", nativeQuery = true)
        List<BookingDetails> getAllwaitingList();

        @Query(value = "SELECT * FROM booking_details WHERE hall_details_hall_id IN (:hallIds) AND " +
                        "booking_status IN ('APPROVED','WAITING_FOR_NOC','WAITING_FOR_NOC_APPROVAL') AND " +
                        "((start_date BETWEEN :fromDate AND :toDate) or (end_date BETWEEN :fromDate AND :toDate))", nativeQuery = true)
        List<BookingDetails> findByFromDateBetweenAndToDateBetweenAndHallIdIn(LocalDate fromDate, LocalDate toDate,
                        List<Long> hallIds);

        @Query("SELECT b FROM BookingDetails b JOIN FETCH b.settlement s WHERE s.settlementId = :settlementId")
        BookingDetails findBySettlementIdWithBookingDetails(@Param("settlementId") int settlementId);

        // ak
        @Query("SELECT b.bookingRefId FROM BookingDetails b WHERE b.bookingId = :bookingId")
        String getRefIdByBookingId(@Param("bookingId") int bookingId);

        @Modifying
        @Query("UPDATE BookingDetails b SET b.bookingRefId = :refId WHERE b.bookingId = :bookingId")
        void updateRefId(@Param("refId") String refId, @Param("bookingId") int bookingId);

        // ak
        @Query("SELECT b FROM BookingDetails b WHERE b.bookingRefId = :refId")
        BookingDetails findByRefId(@Param("refId") String refId);

        // ak for migrate
        @Query("SELECT b FROM BookingDetails b WHERE b.bookingRefId IS NULL OR b.bookingRefId = ''")
        List<BookingDetails> findAllOldBookingsWithoutRefId();

        @Query("SELECT bd FROM BookingDetails bd JOIN bd.hallDetails hd WHERE hd.zone = :zone AND bd.bookingStatus IN ('WAITING_FOR_NOC')")
        List<BookingDetails> getWaitinglist(@Param("zone") String zone);

        @Query("SELECT bd FROM BookingDetails bd JOIN bd.hallDetails hd WHERE hd.zone = :zone AND bd.bookingStatus IN ('APPROVED')")
        List<BookingDetails> getApprovedlist(@Param("zone") String zone);

        @Query("SELECT bd FROM BookingDetails bd JOIN bd.hallDetails hd WHERE hd.zone = :zone AND bd.bookingStatus IN ('REJECTED')")
        List<BookingDetails> getrejectedlist(@Param("zone") String zone);

        @Query("SELECT bd FROM BookingDetails bd JOIN bd.hallDetails hd WHERE hd.zone = :zone AND bd.bookingStatus IN ('PENDING', 'PAYMENT_FAILURE')")
        List<BookingDetails> getPendingOrPaymentFailureBookings(@Param("zone") String zone);

        @Query(value = "SELECT * FROM booking_details WHERE DATE(start_date) = CURRENT_DATE() AND hall_details_hall_id = :hallId and booking_status='APPROVED'", nativeQuery = true)
        List<BookingDetails> findPresentsEventsByUserId(Integer hallId);
        // List<Booking> findpresentseventsByHallid(Integer hallId);

        // @Query(value = "SELECT * FROM booking_details WHERE DATE(start_date) <
        // CURRENT_DATE() AND hall_details_hall_id = :hallId and
        // booking_status='APPROVED'", nativeQuery = true)
        // List<BookingDetails> findPastsEventsByUserId(Integer hallId);
        // @Query(value = "SELECT * FROM booking_details WHERE DATE(start_date) <
        // CURRENT_DATE() AND hall_details_hall_id = :hallId AND booking_status =
        // 'APPROVED'", nativeQuery = true)
        // List<BookingDetails> findPastEventsByHallId(Integer hallId);
        /*
         * @Query(value =
         * "SELECT * FROM booking_details WHERE DATE(start_date) BETWEEN DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY) AND CURRENT_DATE() - INTERVAL 1 DAY AND hall_details_hall_id = :hallId AND booking_status = 'APPROVED'"
         * , nativeQuery = true)
         * List<BookingDetails> findPastEventsByHallId(@Param("hallId") Integer hallId);
         */
        @Query(value = "SELECT * FROM booking_details WHERE DATE(start_date) < CURRENT_DATE()  AND hall_details_hall_id = :hallId AND booking_status = 'APPROVED'", nativeQuery = true)
        List<BookingDetails> findPastEventsByHallId(@Param("hallId") Integer hallId);

        @Query(value = "SELECT * FROM booking_details " +
                        "WHERE DATE(start_date) BETWEEN DATE_ADD(CURRENT_DATE(), INTERVAL 1 DAY) AND DATE_ADD(CURRENT_DATE(), INTERVAL 2 DAY) "
                        +
                        "AND hall_details_hall_id = :hallId " +
                        "AND booking_status = 'APPROVED'", nativeQuery = true)
        List<BookingDetails> findFutureEventsByHallId(Integer hallId);
        // @Query(value = "SELECT * FROM booking_details " +
        // "WHERE DATE(start_date) > CURRENT_DATE() " +
        // "AND hall_details_hall_id = :hallId " +
        // "AND booking_status = 'APPROVED'", nativeQuery = true)
        // List<BookingDetails> findFutureEventsByHallId(Integer hallId);

        @Query(value = "select count(users_user_id), us.user_status  from booking_details bd  inner join users us on us.user_id =  bd.users_user_id \r"
                        + "where users_user_id=:userId and booking_status in ('APPROVED','COMPLETED','WAITING_FOR_NOC','WAITING_FOR_NOC_APPROVAL') and  DATE_SUB(CURDATE(), INTERVAL 1 YEAR) \r"
                        + "and us.user_status not in ('PENDING')", nativeQuery = true)
        int checkUserBookinglimitation(int userId);

        @Query(value = "SELECT * FROM booking_details WHERE users_user_id = :userId AND booking_status NOT IN ('PENDING')", nativeQuery = true)
        List<BookingDetails> getAllBookingsByUserId(Integer userId);

        @Query(value = "select * from booking_details WHERE booking_id =:bookID", nativeQuery = true)
        BookingDetails getByBookingId(String bookID);

        // + " WHERE hall_details_hall_id =:hallId and booking_status= 'APPROVED' and
        // start_date < curdate() ", nativeQuery = true)
        @Query(value = "SELECT * FROM booking_details WHERE DATE(start_date) <= CURRENT_DATE() AND hall_details_hall_id = :hallId and booking_status='APPROVED' and refund_status IN ('WAITING','REJECTED','APPROVED')", nativeQuery = true)
        public List<BookingDetails> getSettlementList(Integer hallId);

        @Query(value = "SELECT * FROM booking_details WHERE DATE(start_date) <= CURRENT_DATE() AND booking_status='APPROVED' AND refund_status IN ('WAITING','REJECTED','APPROVED')", nativeQuery = true)
        public List<BookingDetails> getAllSettlementList();

        // yoga
        @Query(value = "SELECT " +
                        "u.user_id AS userId, " +
                        "u.name AS UserName, " +
                        "u.mobile_no AS MobileNumber, " +
                        "CONCAT(u.doorno,',',u.street, ', ', u.area, ', ', u.location, ', ', u.pin_code) AS Address, " +
                        "COUNT(*) AS TotalBookings, " +
                        "SUM(CASE WHEN bd.booking_status = 'APPROVED' THEN 1 ELSE 0 END) AS Payment, " +
                        "SUM(CASE WHEN bd.booking_status = 'REJECTED' THEN 1 ELSE 0 END) AS Rejected, " +
                        "SUM(CASE WHEN bd.booking_status = 'CANCELED' THEN 1 ELSE 0 END) AS Canceled, " +
                        "SUM(CASE WHEN bd.booking_status = 'PAYMENT_FAILURE' THEN 1 ELSE 0 END) AS PaymentFailure " +
                        "FROM " +
                        "booking_details bd " +
                        "JOIN " +
                        "users u ON bd.users_user_id = u.user_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND bd.booking_status NOT IN ('PENDING')" +
                        "GROUP BY " +
                        "bd.users_user_id order by u.user_id asc", nativeQuery = true)
        List<Object[]> getBookingUserDetailsReport(@Param("fromDate") String fromDate, @Param("toDate") String toDate);

        @Query(value = "SELECT " +
                        "h.hall_name AS HallName, " +
                        "h.hall_Incharge_name AS InchargeName, " +
                        "h.hall_Incharge_no AS hallInchargeMobno, " +
                        "CONCAT(h.zone, '/', h.division) AS ZoneDivision, " + // Concatenate zone and division
                        "COUNT(*) AS HallTotalBookings, " +
                        "SUM(p.amount_received) AS AmountReceived, " +
                        "COALESCE(SUM(r.amount - r.balace_amount), 0) AS AmountRefund, " +
                        "COALESCE(SUM(CASE WHEN bd.booking_status IN ('COMPLETED', 'CANCELED') THEN r.balace_amount ELSE 0 END), 0) AS Difference  "
                        +
                        "FROM " +
                        "booking_details bd " +
                        "JOIN " +
                        "hall_details h ON bd.hall_details_hall_id = h.hall_id " +
                        "LEFT JOIN users u ON bd.users_user_id = u.user_id " +
                        "JOIN " +
                        "payments p ON bd.booking_id = p.booking_details_booking_id " +
                        "LEFT JOIN refund_response r ON  bd.booking_id = r.booking_id  " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND bd.booking_status NOT IN ('PENDING')" +
                        "GROUP BY " +
                        "bd.hall_details_hall_id", nativeQuery = true)
        List<Object[]> getBookingHallDetailsReport(@Param("fromDate") String fromDate, @Param("toDate") String toDate);

        // ak
        /*
         * @Query(value = "SELECT  * \r"
         * 
         * + "FROM \r"
         * + "    settlement s\r"
         * +
         * "    JOIN booking_details b ON s.booking_details_booking_id = b.booking_id\r"
         * 
         * + "WHERE \r"
         * + "    DATE(b.booking_date) BETWEEN :fromDate AND :toDate\r"
         * 
         * + "ORDER BY \r"
         * + "    b.booking_date", nativeQuery = true)
         * List<Object[]> getSettlementInitiatedReport(@Param("fromDate") String
         * fromDate, @Param("toDate") String toDate);
         */
        @Query(value = "SELECT distinct(p.booking_id),  " +
        // "b.booking_id, " +
                        "b.booking_date, " +
                        "s.cdate, " +
                        "u.name, " +
                        "u.mobile_no, " +
                        "hd.hall_name, " +
                        "b.end_date, " +
                        "b.start_date, " +
                        "b.amount_received, " +
                        "s.refunded_amount, " +
                        "p.txansaction_id " +
                        "FROM booking_details b " +
                        "INNER JOIN settlement s ON b.booking_id = s.booking_details_booking_id " +
                        "INNER JOIN users u ON u.user_id = b.users_user_id " +
                        "INNER JOIN hall_details hd ON hd.hall_id = b.hall_details_hall_id " +
                        "inner join refund_response p on p.booking_id=b.booking_id " +
                        "WHERE DATE(b.booking_date) BETWEEN :fromDate AND :toDate " +
                        "ORDER BY b.booking_date DESC", nativeQuery = true)
        List<Object[]> getSettlementInitiatedReport(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate);

        @Query(value = "SELECT \r"
                        + "  COALESCE( SUM(\r"
                        + "        CASE \r"
                        + "            WHEN start_date = CURDATE() THEN 1\r"
                        + "            ELSE 0\r"
                        + "        END\r"
                        + "    ),0) AS currente,\r"
                        + "   COALESCE( SUM(\r"
                        + "        CASE \r"
                        + "			\r"
                        + "            WHEN start_date < CURDATE() THEN 1\r"
                        + "            ELSE 0\r"
                        + "        END\r"
                        + "    ), 0)  AS past,\r"
                        + "  COALESCE(   SUM(\r"
                        + "        CASE \r"
                        + "			\r"
                        + "            WHEN start_date > CURDATE() THEN 1\r"
                        + "            ELSE 0\r"
                        + "        END\r"
                        + "    ),0) AS FUTURE\r"
                        + "FROM \r"
                        + "    booking_details hall\r"
                        + "    where  hall_details_hall_id= :hall_id and booking_status='APPROVED'", nativeQuery = true)
        List<Map<String, Object>> pieChartCount(int hall_id);

        // @Query(value = "SELECT * FROM booking_details WHERE DATE(booking_date) >=
        // :fromDate AND DATE(booking_date) <= :toDate AND (:bookingStatus IS NULL OR
        // booking_status = :bookingStatus)", nativeQuery = true)
        @Query(value = "SELECT bd.booking_id, bd.booking_date, hd.hall_name, us.name, us.mobile_no, " +
                        "CONCAT(us.doorno, ', ', us.street, ', ', us.area, ', ', us.location, ', ', us.pin_code) AS address, "
                        +
                        "us.usertype, bd.reason, bd.start_date, bd.end_date, " +
                        "(bd.deposit_amount + bd.hall_amount + bd.gst) AS amount_paid, bd.booking_status " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND (:bookingStatus IS NULL OR bd.booking_status = :bookingStatus) order by bd.booking_id asc ", nativeQuery = true)
        List<Object[]> getAllBookingDetailsWithStatus(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate,
                        @Param("bookingStatus") String bookingStatus);

        @Query(value = "SELECT bd.booking_id, bd.booking_date, hd.hall_name, us.name, us.mobile_no, " +
                        "CONCAT(us.doorno, ', ', us.street, ', ', us.area, ', ', us.location, ', ', us.pin_code) AS address, "
                        +
                        "us.usertype, bd.reason, bd.start_date, bd.end_date, " +
                        "bd.deposit_amount, bd.hall_amount, bd.gst, bd.amount_received, bd.booking_status " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND (:bookingId IS NULL OR bd.booking_id = :bookingId) order by bd.booking_id asc", nativeQuery = true)
        List<Object[]> getAllBookingDetailsWithBookingId(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate,
                        @Param("bookingId") Long bookingId);

        @Query(value = "SELECT bd.booking_id, bd.booking_date, hd.hall_name, us.name, us.mobile_no, " +
                        "CONCAT(us.doorno, ', ', us.street, ', ', us.area, ', ', us.location, ', ', us.pin_code) AS address, "
                        +
                        "us.usertype, bd.reason, bd.start_date, bd.end_date, " +
                        "bd.deposit_amount, bd.hall_amount, bd.gst, bd.amount_received, bd.booking_status " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND (:bookingId IS NULL OR bd.booking_id = :bookingId)" +
                        "AND (:bookingStatus IS NULL OR bd.booking_status = :bookingStatus) order by bd.booking_id asc", nativeQuery = true)
        List<Object[]> getAllBookingDetailsWithBookingIdwithbookingStatus(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate,
                        @Param("bookingId") Long bookingId, @Param("bookingStatus") String bookingStatus);

        // @Query(value = "SELECT * FROM booking_details WHERE DATE(booking_date) >=
        // :fromDate AND DATE(booking_date) <= :toDate", nativeQuery = true)
        @Query(value = "SELECT bd.booking_id, bd.booking_date, hd.hall_name, us.name, us.mobile_no, " +
                        "CONCAT(us.doorno, ', ', us.street, ', ', us.area, ', ', us.location, ', ', us.pin_code) AS address, "
                        +
                        "us.usertype, bd.reason, bd.start_date, bd.end_date, " +
                        "(bd.deposit_amount + bd.hall_amount + bd.gst) AS amount_paid, bd.booking_status " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate order by bd.booking_id asc", nativeQuery = true)
        List<Object[]> getAllBookingDetails(@Param("fromDate") String fromDate, @Param("toDate") String toDate);

        // @Query(value = "SELECT bd.booking_id, bd.booking_date, hd.hall_name, us.name,
        // us.usertype, p.transaction_id, p.payment_type, p.amount_received, bd.reason,
        // bd.start_date, bd.end_date, p.payment_status " +
        // "FROM payments p " +
        // "JOIN booking_details bd ON p.booking_details_booking_id =bd.booking_id " +
        // "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
        // "left JOIN users us ON bd.users_user_id = us.user_id " +
        // "WHERE DATE(p.cdate) >= :fromDate " +
        // "AND DATE(p.cdate) <= :toDate " +
        // "AND (:paymentStatus IS NULL OR p.payment_status = :paymentStatus)",
        // nativeQuery = true)
        // List<Object[]> getAllPaymentsDetails(@Param("fromDate") String fromDate,
        // @Param("toDate") String toDate,
        // @Param("paymentStatus") String paymentStatus);

        @Query(value = "SELECT SUM(CASE WHEN bd.booking_status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_booking_count,\r"
                        + "SUM(CASE WHEN bd.booking_status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_booking_count,\r"
                        + "SUM(CASE WHEN bd.refund_status = 'WAITING' THEN 1 ELSE 0 END) AS waiting_settlement_count,\r"
                        + "SUM(CASE WHEN bd.refund_status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_settlement_count,\r"
                        + "SUM(CASE WHEN bd.refund_status = 'REJECTED' THEN 1 ELSE 0 END) AS reject_settlement_count\r"
                        + "FROM booking_details bd "
                        + "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id "
                        + "WHERE hd.zone = :zone", nativeQuery = true)
        List<Map<String, Object>> toatlBookingCount(String zone);

        @Query(value = " SELECT  SUM(CASE WHEN booking_status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_count,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_count,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'PENDING' THEN 1 ELSE 0 END) AS pending_count,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'CANCELED' THEN 1 ELSE 0 END) AS CANCELED,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'PAYMENT_FAILURE' THEN 1 ELSE 0 END) AS PAYMENT_FAILURE,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'GCC_BLOCKED' THEN 1 ELSE 0 END) AS GCC_BLOCKED\r"
                        + "FROM booking_details bd "
                        + "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id "
                        + "WHERE hd.zone = :zone", nativeQuery = true)
        List<Map<String, Object>> getCount(String zone);

        @Query(value = "SELECT  CONCAT(YEAR(bd.start_date), '-', LPAD(MONTH(bd.start_date), 2, '0')) AS months, SUM(CASE WHEN booking_status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_count,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_count,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'PENDING' THEN 1 ELSE 0 END) AS pending_count\r"
                        + "FROM booking_details bd JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id \r"
                        + "WHERE bd.start_date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)\r"
                        + "  AND bd.start_date <= DATE_ADD(CURDATE(), INTERVAL 6 MONTH)  AND hd.zone = :zone \r"
                        + "  group by months   order by months ASC", nativeQuery = true)
        List<Map<String, Object>> getmounthcount(String zone);

        @Query(value = "SELECT  CONCAT(YEAR(bd.start_date), '-', LPAD(MONTH(bd.start_date), 2, '0')) AS months, SUM(CASE WHEN booking_status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_count,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_count,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'PENDING' THEN 1 ELSE 0 END) AS pending_count\r"
                        + "FROM booking_details bd JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id \r"
                        + "WHERE bd.start_date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)\r"
                        + "  AND bd.start_date <= DATE_ADD(CURDATE(), INTERVAL 6 MONTH)  \r"
                        + "AND (:zone = 'all zones' OR hd.zone = :zone)\r "
                        + "  group by months   order by months ASC", nativeQuery = true)
        List<Map<String, Object>> getAllAdminmonthcount(@Param("zone") String zone);

        @Query(value = " SELECT  SUM(CASE WHEN booking_status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_count,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_count,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'PENDING' THEN 1 ELSE 0 END) AS pending_count,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'CANCELED' THEN 1 ELSE 0 END) AS CANCELED,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'PAYMENT_FAILURE' THEN 1 ELSE 0 END) AS PAYMENT_FAILURE,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'GCC_BLOCKED' THEN 1 ELSE 0 END) AS GCC_BLOCKED\r"
                        + "FROM booking_details bd "
                        + "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id "
                        + "WHERE (:zone = 'all zones' OR hd.zone = :zone)", nativeQuery = true)
        List<Map<String, Object>> getAdmincount(@Param("zone") String zone);

        @Query(value = "SELECT  CONCAT(YEAR(bd.start_date), '-', LPAD(MONTH(bd.start_date), 2, '0')) AS months, SUM(CASE WHEN booking_status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_count,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_count,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed_count\r"
                        + "FROM booking_details bd JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id \r"
                        + "WHERE bd.start_date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)\r"
                        + "  AND bd.start_date <= DATE_ADD(CURDATE(), INTERVAL 6 MONTH) AND hd.hall_id=:hall_id \r"
                        + "  group by months   order by months ASC", nativeQuery = true)
        List<Map<String, Object>> getCartakermounthcount(int hall_id);

        @Query(value = " SELECT  SUM(CASE WHEN booking_status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_count,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_count,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed_count,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'CANCELED' THEN 1 ELSE 0 END) AS CANCELED,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'PAYMENT_FAILURE' THEN 1 ELSE 0 END) AS PAYMENT_FAILURE,\r"
                        + "    SUM(CASE WHEN bd.booking_status = 'GCC_BLOCKED' THEN 1 ELSE 0 END) AS GCC_BLOCKED\r"
                        + "FROM booking_details bd "
                        + "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id where   hd.hall_id=:hall_id", nativeQuery = true)
        List<Map<String, Object>> getcartakecount(int hall_id);

        @Query(value = "SELECT \r"
                        + "    bd.*, \r"
                        + "    CASE \r"
                        + "        WHEN bd.start_date < CURDATE() THEN 'Past'\r"
                        + "        WHEN bd.start_date = CURDATE() THEN 'Current'\r"
                        + "        WHEN bd.booking_status = 'WAITING_FOR_NOC' THEN 'waitingfornoc'\r"
                        + "        WHEN bd.booking_status = 'WAITING_FOR_NOC_APPROVAL' THEN 'waitingfornocapproval'\r"
                        + "        WHEN bd.schedule_count = 0 AND bd.start_date > CURDATE() THEN 'Reschedule'\r"
                        + "		   WHEN bd.reason IN ('State Govt Event','Central Govt Event','Commericial & Others') AND bd.booking_status='APPROVED' THEN 'noaction'"
                        + "        ELSE 'Future' \r"
                        + "    END AS event_category,\r"
                        + "    users.*,  \r"
                        + "    hd.*, \r"
                        + "    cd.*, \r"
                        + "    pd.* \r"
                        + "FROM booking_details bd \r"
                        + "INNER JOIN users users ON bd.users_user_id = users.user_id \r"
                        + "INNER JOIN hall_details hd ON hd.hall_id = bd.hall_details_hall_id \r"
                        + "LEFT JOIN counter_details cd ON cd.counter_id = bd.counterdetails_counter_id\r"
                        + "LEFT JOIN payments pd ON bd.booking_id = pd.booking_details_booking_id \r"
                        + "WHERE bd.users_user_id = :userId and booking_status IN ('GCC_BLOCKED', 'APPROVED','CANCELED','REJECTED','COMPLETED','WAITING_FOR_NOC','WAITING_FOR_NOC_APPROVAL') \r"
                        + "ORDER BY bd.booking_id DESC\r", nativeQuery = true)
        List<Map<String, Object>> getAllBookingEventsByUserId(Integer userId);

        // counter
        @Query(value = "SELECT bd.*, " +
                        "hd.zone AS hall_zone, " +
                        "hd.division AS hall_division, " +
                        "users.mobile_no AS mobile_no, " +
                        "CASE " +
                        "WHEN bd.start_date < CURDATE() THEN 'Past' " +
                        "WHEN bd.start_date = CURDATE() THEN 'Current' " +
                        "ELSE 'Future' " +
                        "END AS event_category " +
                        "FROM booking_details bd " +
                        "INNER JOIN users users on bd.users_user_id = users.user_id " +
                        "INNER JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "WHERE bd.booking_type = 'OFFLINE'", nativeQuery = true)
        List<Map<String, Object>> findAllByBookingType();

        @Query(value = "select bdud.* from booking_details bdud inner Join counter_refund_list crl on bdud.counterdetails_counter_id = crl.counterdetails_counter_id where crl.counterdetails_counter_id = :counterId", nativeQuery = true)
        List<BookingDetails> getCounterDetailsbycounterId(int counterId);

        @Query(value = "select *from booking_details where counterdetails_counter_id = :counterId", nativeQuery = true)
        List<BookingDetails> findByCounterId(int counterId);

        @Query("SELECT b FROM BookingDetails b LEFT JOIN FETCH b.bookingHistories WHERE b.bookingId = :bookingId")
        Optional<BookingDetails> findByIdWithHistories(@Param("bookingId") int bookingId);

        @Query("SELECT b FROM BookingDetails b JOIN FETCH b.settlement s WHERE b.refundStatus = 'APPROVED'")
        List<BookingDetails> findAllWithApprovedRefundStatus();

        @Query("SELECT b FROM BookingDetails b JOIN FETCH b.settlement s WHERE b.refundStatus = 'INITIATED'")
        List<BookingDetails> findByBookingDetailsRefundStatus();

        @Query("SELECT b FROM BookingDetails b JOIN FETCH b.settlement s WHERE b.refundStatus = 'COMPLETED' AND b.bookingStatus='COMPLETED'")
        List<BookingDetails> findAllwithCompletedRefundStatus();

        @Query("SELECT b FROM BookingDetails b JOIN FETCH b.settlement s WHERE b.refundStatus = 'WAITING' and b.hallDetails.zone=:zone")
        List<BookingDetails> findSettlementPendingByZone(String zone);

        // ak
        @Query("SELECT b FROM BookingDetails b JOIN FETCH b.settlement s WHERE b.refundStatus = 'FAILURE' and b.hallDetails.zone=:zone")
        List<BookingDetails> findSettlementInitiatedPendingByZone(String zone);

        @Query("SELECT b FROM BookingDetails b JOIN FETCH b.settlement s WHERE b.refundStatus = 'FAILURE'")
        List<BookingDetails> findSettlementInitiatedPending();

        @Query("SELECT b FROM BookingDetails b JOIN FETCH b.settlement s WHERE b.refundStatus = 'APPROVED' and b.hallDetails.zone=:zone")
        List<BookingDetails> findSettlementApprovedByZone(String zone);

        @Query("SELECT b FROM BookingDetails b JOIN FETCH b.settlement s WHERE b.refundStatus = 'REJECTED' and b.hallDetails.zone=:zone")
        List<BookingDetails> findSettlementRejectedByZone(String zone);

        @Query("SELECT b FROM BookingDetails b JOIN FETCH b.settlement s WHERE b.refundStatus = 'REJECTED'")
        List<BookingDetails> findAllSettlementRejected();

        @Query("SELECT b FROM BookingDetails b WHERE b.refundStatus = 'WAITING' AND b.bookingStatus = 'CANCELED' AND b.hallDetails.zone = :zone")
        List<BookingDetails> findCancelledListByZone(@Param("zone") String zone);

        @Query("SELECT b FROM BookingDetails b WHERE b.refundStatus = 'APPROVED' AND b.bookingStatus = 'CANCELED' AND b.hallDetails.zone = :zone")
        List<BookingDetails> findapprovedCancelledListByZone(String zone);

        @Query("SELECT b FROM BookingDetails b WHERE b.refundStatus = 'WAITING' AND b.bookingStatus IN ('GCC_BLOCKED', 'REJECTED') AND b.hallDetails.zone = :zone")
        List<BookingDetails> findRejectedListByZone(@Param("zone") String zone);

        @Query("SELECT b FROM BookingDetails b WHERE b.refundStatus = 'APPROVED' AND b.bookingStatus IN ('GCC_BLOCKED', 'REJECTED') AND b.hallDetails.zone = :zone")
        List<BookingDetails> findapprovedRejectedListByZone(@Param("zone") String zone);

        @Query("SELECT b FROM BookingDetails b WHERE b.refundStatus = 'APPROVED' AND b.bookingStatus = 'CANCELED'")
        List<BookingDetails> findCancelledList();

        @Query(value = "SELECT * FROM booking_details WHERE refund_status = 'APPROVED' AND booking_status IN ('GCC_BLOCKED', 'REJECTED')", nativeQuery = true)
        List<BookingDetails> findRejectedList();

        @Query(value = "SELECT * FROM booking_details WHERE refund_status = 'INITIATED' AND booking_status = 'CANCELED'", nativeQuery = true)
        List<BookingDetails> findInitiatedCancelledList();

        @Query(value = "SELECT * FROM booking_details WHERE refund_status = 'INITIATED' AND booking_status IN ('GCC_BLOCKED', 'REJECTED')", nativeQuery = true)
        List<BookingDetails> findInitiatedRejectedList();

        @Query(value = "SELECT * FROM booking_details WHERE refund_status IN ('COMPLETED','CLOSED') AND booking_status = 'COMPLETED'", nativeQuery = true)
        List<BookingDetails> findSettlementCompletedList();

        @Query(value = "SELECT * FROM booking_details WHERE refund_status = 'COMPLETED' AND booking_status = 'CANCELED'", nativeQuery = true)
        List<BookingDetails> findCancelledCompletedList();

        @Query(value = "SELECT * FROM booking_details WHERE refund_status = 'COMPLETED' AND booking_status IN ('GCC_BLOCKED', 'REJECTED')", nativeQuery = true)
        List<BookingDetails> findRejectedSettlementCompletedList();

        // officer reports
        @Query(value = "SELECT bd.*, hd.hall_name, us.name, us.usertype " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND hd.zone = :zone ", nativeQuery = true)
        List<BookingDetails> getAllBookingDetailsByZone(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("zone") String zone);

        @Query(value = "SELECT bd.*, hd.hall_name, us.name, us.usertype " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND hd.zone = :zone " +
                        "AND (:bookingStatus IS NULL OR bd.booking_status = :bookingStatus)", nativeQuery = true)
        List<BookingDetails> getAllBookingDetailsByZoneWithStatus(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("zone") String zone,
                        @Param("bookingStatus") String bookingStatus);

        @Query(value = "SELECT " +
                        "h.hall_name AS HallName, " +
                        "h.zone AS Zone, " +
                        "h.division AS Division, " +
                        "CONCAT(h.door_no,',',h.street, ', ', h.area, ', ', h.city, ', ', h.pin_code) AS Address, " +
                        "COUNT(*) AS HallTotalBookings, " +
                        "SUM(p.amount_received) AS AmountReceived, " +
                        "COALESCE(SUM(r.amount_refunded), 0) AS AmountRefund, " + // Use COALESCE to handle NULL values
                        "(SUM(p.amount_received) - COALESCE(SUM(r.amount_refunded), 0)) AS Difference " + // Calculate
                                                                                                          // the
                                                                                                          // difference
                        "FROM " +
                        "booking_details bd " +
                        "JOIN " +
                        "hall_details h ON bd.hall_details_hall_id = h.hall_id " +
                        "JOIN " +
                        "payments p ON bd.booking_id = p.booking_details_booking_id " +
                        "LEFT JOIN " +
                        "refund_transaction r ON p.transaction_id = r.transaction_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND h.zone = :zone " +
                        "GROUP BY " +
                        "bd.hall_details_hall_id", nativeQuery = true)
        List<Object[]> getBookingHallDetailsReportByZone(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("zone") String zone);

        @Query(value = "SELECT bd.*, hd.hall_name, us.name, us.usertype " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "LEFT JOIN users us ON bd.users_user_id = us.user_id " +
                        "LEFT JOIN settlement st ON bd.booking_id=st.booking_details_booking_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND bd.booking_status NOT IN ('PENDING')" +
                        "AND hd.zone = :zone ", nativeQuery = true)
        List<BookingDetails> getAllBookingsettlementDetailsByZone(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("zone") String zone);

        @Query(value = "SELECT bd.*, hd.hall_name, us.name, us.usertype " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "LEFT JOIN users us ON bd.users_user_id = us.user_id " +
                        "LEFT JOIN settlement st ON bd.booking_id=st.booking_details_booking_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND bd.booking_status NOT IN ('PENDING')" +
                        "AND hd.zone = :zone " +
                        "AND (:refundStatus IS NULL OR bd.refund_status = :refundStatus) ", nativeQuery = true)
        List<BookingDetails> getAllBookingsettlementDetailsByZoneWithStatus(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("zone") String zone,
                        @Param("refundStatus") String refundStatus);

        @Query(value = "SELECT " +
                        "h.hall_name AS HallName, " +
                        "h.hall_Incharge_name AS InchargeName, " +
                        "h.hall_Incharge_no AS hallInchargeMobno, " +
                        "CONCAT(h.zone, '/', h.division) AS ZoneDivision, " + // Concatenate zone and division
                        "COUNT(*) AS HallTotalBookings, " +
                        "SUM(p.amount_received) AS AmountReceived, " +
                        "COALESCE(SUM(r.amount - r.balace_amount), 0) AS AmountRefund, " +
                        "COALESCE(SUM(CASE WHEN bd.booking_status IN ('COMPLETED', 'CANCELED') THEN r.balace_amount ELSE 0 END), 0) AS Difference  "
                        +
                        "FROM " +
                        "booking_details bd " +
                        "JOIN " +
                        "hall_details h ON bd.hall_details_hall_id = h.hall_id " +
                        "LEFT JOIN users u ON bd.users_user_id = u.user_id " +
                        "JOIN " +
                        "payments p ON bd.booking_id = p.booking_details_booking_id " +
                        "LEFT JOIN refund_response r ON  bd.booking_id = r.booking_id  " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND bd.booking_status NOT IN ('PENDING')" +
                        "AND h.zone = :zone " +
                        "AND bd.hall_details_hall_id = :hall_id " +
                        "GROUP BY " +
                        "bd.hall_details_hall_id", nativeQuery = true)
        List<Object[]> getBookingHallDetailsReportByZone1(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("zone") String zone, @Param("hall_id") Integer hallId);

        List<BookingDetails> findByHallDetailsAndBookingStatus(HallDetails hallDetails, BookingStatus gccBlocked);

        @Query(value = "SELECT COUNT(*) FROM booking_details WHERE hall_details_hall_id = :hallId AND start_date BETWEEN :startDate AND :endDate AND booking_status='GCC_BLOCKED'", nativeQuery = true)
        int countByHallIdAndBookingDateBetween(
                        @Param("hallId") int hallId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query(value = "SELECT * FROM booking_details WHERE booking_id=:bookingId", nativeQuery = true)
        List<BookingDetails> findBookingDetailsByBookingId(@Param("bookingId") Long bookingId);

        @Query(value = "SELECT booking_id FROM booking_details WHERE hall_details_hall_id = :hallId AND start_date BETWEEN :startDate AND :endDate AND booking_status='GCC_BLOCKED'", nativeQuery = true)
        List<Integer> findBookingIdsByHallIdAndBookingDateBetween(@Param("hallId") int hallId,
                        @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        @Query(value = "SELECT bd.*, hd.hall_name, us.name, us.usertype " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "LEFT JOIN users us ON bd.users_user_id = us.user_id " +
                        "LEFT JOIN settlement st ON bd.booking_id=st.booking_details_booking_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND bd.booking_status NOT IN ('PENDING')" +
                        "AND (:refundStatus IS NULL OR bd.refund_status = :refundStatus) ", nativeQuery = true)
        List<BookingDetails> getAllBookingsettlementDetailsWithStatus(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("refundStatus") String refundStatus);

        @Query(value = "SELECT bd.*, hd.hall_name, us.name, us.usertype " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "LEFT JOIN users us ON bd.users_user_id = us.user_id " +
                        "LEFT JOIN settlement st ON bd.booking_id=st.booking_details_booking_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND bd.booking_status NOT IN ('PENDING')" +
                        "AND DATE(bd.booking_date) <= :toDate ", nativeQuery = true)
        List<BookingDetails> getAllBookingsettlementDetails(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate);

        // admin dashboard
        @Query(nativeQuery = true, value =

        "SELECT 'main' AS category, NULL AS `key` , " +

                        "COUNT(DISTINCT CASE WHEN b.booking_status NOT IN ('GCC_BLOCKED', 'GCC_UNBLOCKED') THEN b.booking_id END) AS totalCount,"
                        +

                        "COALESCE(SUM(CASE WHEN b.booking_status NOT IN ('PENDING') THEN b.amount_received ELSE 0 END), 0) AS main_total_amount, "
                        +

                        "COALESCE(SUM(r.amount - r.balace_amount), 0) AS main_refunded_amount," +

                        "COALESCE(SUM(CASE  WHEN b.booking_status NOT IN ('PENDING', 'REFUNDED', 'GCC_BLOCKED','REJECTED') THEN b.hall_amount ELSE 0 END), 0) AS main_net_amount, "
                        +

                        "COALESCE(SUM(CASE WHEN b.booking_status NOT IN ('PENDING', 'REFUNDED', 'GCC_BLOCKED','REJECTED') THEN b.gst ELSE 0 END), 0) AS main_gst_amount, NULL AS PAST_Events, NULL AS FUTURE_Events "
                        +

                        "FROM booking_details b " +

                        "JOIN hall_details h ON h.hall_id = b.hall_details_hall_id " +

                        "LEFT JOIN refund_response r ON b.booking_id = r.booking_id " +

                        "LEFT JOIN users u ON b.users_user_id = u.user_id " +

                        "WHERE (b.start_date >= CASE WHEN :fromDate IS NOT NULL THEN :fromDate ELSE (SELECT MIN(start_date) FROM booking_details) END) "
                        +

                        "AND (b.start_date <= CASE WHEN :toDate IS NOT NULL THEN :toDate ELSE (SELECT MAX(end_date) FROM booking_details) END) "
                        +

                        "AND (:zone IS NULL OR :zone = 'ALL' OR h.zone = :zone) " +

                        "AND (:division IS NULL OR :division = 'ALL' OR h.division = :division) " +

                        "AND (:hall_id IS NULL OR :hall_id = '1000' OR h.hall_id = :hall_id) " +

                        "AND (:userType IS NULL OR :userType = 'ALL' OR u.usertype = :userType) " +

                        "AND b.booking_status!='PENDING'" +

                        "UNION ALL " +

                        "SELECT 'bookingStatus' AS category, b.booking_status AS `key`, COUNT(*) AS totalCount, NULL AS main_total_amount, NULL AS main_refunded_amount, NULL AS main_net_amount,NULL AS main_gst_amount, NULL AS PAST_Events, NULL AS FUTURE_Events "
                        +

                        "FROM booking_details b " +

                        "JOIN hall_details h ON b.hall_details_hall_id = h.hall_id " +

                        "LEFT JOIN users u ON b.users_user_id = u.user_id " +

                        "WHERE (b.start_date >= CASE WHEN :fromDate IS NOT NULL THEN :fromDate ELSE (SELECT MIN(start_date) FROM booking_details) END) "
                        +

                        "AND (b.start_date <= CASE WHEN :toDate IS NOT NULL THEN :toDate ELSE (SELECT MAX(end_date) FROM booking_details) END) "
                        +

                        "AND (:zone IS NULL OR :zone = 'ALL' OR h.zone = :zone) " +

                        "AND (:division IS NULL OR :division = 'ALL' OR h.division = :division) " +

                        "AND (:hall_id IS NULL OR :hall_id = '1000' OR h.hall_id = :hall_id) " +

                        "AND (:userType IS NULL OR :userType = 'ALL' OR u.usertype = :userType) " +

                        "AND b.booking_status != 'PENDING'" +

                        "GROUP BY b.booking_status " +

                        "UNION ALL " +

                        "SELECT 'refundStatus' AS category, b.refund_status AS `key`, COUNT(*) AS totalCount, NULL AS main_total_amount, NULL AS main_refunded_amount, NULL AS main_net_amount,NULL AS main_gst_amount, NULL AS PAST_Events, NULL AS FUTURE_Events "
                        +

                        "FROM booking_details b " +

                        "JOIN hall_details h ON b.hall_details_hall_id = h.hall_id " +

                        "LEFT JOIN users u ON b.users_user_id = u.user_id " +

                        "WHERE (b.start_date >= CASE WHEN :fromDate IS NOT NULL THEN :fromDate ELSE (SELECT MIN(start_date) FROM booking_details) END) "
                        +

                        "AND (b.start_date <= CASE WHEN :toDate IS NOT NULL THEN :toDate ELSE (SELECT MAX(end_date) FROM booking_details) END) "
                        +

                        "AND (:zone IS NULL OR :zone = 'ALL' OR h.zone = :zone) " +

                        "AND (:division IS NULL OR :division = 'ALL' OR h.division = :division) " +

                        "AND (:hall_id IS NULL OR :hall_id = '1000' OR h.hall_id = :hall_id) " +

                        "AND (:userType IS NULL OR :userType = 'ALL' OR u.usertype = :userType) " +

                        "AND b.booking_status != 'PENDING'" +

                        "GROUP BY b.refund_status " +

                        "UNION ALL " +

                        "SELECT 'reason' AS category, b.reason AS `key`, COUNT(*) AS totalCount, NULL AS main_total_amount, NULL AS main_net_amount, NULL AS main_refunded_amount,NULL AS main_gst_amount, NULL AS PAST_Events, NULL AS FUTURE_Events "
                        +

                        "FROM booking_details b " +

                        "JOIN hall_details h ON b.hall_details_hall_id = h.hall_id " +

                        "LEFT JOIN users u ON b.users_user_id = u.user_id " +

                        "WHERE (b.start_date >= CASE WHEN :fromDate IS NOT NULL THEN :fromDate ELSE (SELECT MIN(start_date) FROM booking_details) END) "
                        +

                        "AND (b.start_date <= CASE WHEN :toDate IS NOT NULL THEN :toDate ELSE (SELECT MAX(end_date) FROM booking_details) END) "
                        +

                        "AND (:zone IS NULL OR :zone = 'ALL' OR h.zone = :zone) " +

                        "AND (:division IS NULL OR :division = 'ALL' OR h.division = :division) " +

                        "AND (:hall_id IS NULL OR :hall_id = '1000' OR h.hall_id = :hall_id) " +

                        "AND (:userType IS NULL OR :userType = 'ALL' OR u.usertype = :userType) " +

                        "AND b.booking_status != 'PENDING'" +

                        "GROUP BY b.reason " +

                        "UNION ALL " +

                        "SELECT 'zonewise' AS category, h.zone AS `key`, SUM(CASE WHEN b.hall_details_hall_id IS NOT NULL AND b.booking_status != 'PENDING' THEN 1 ELSE 0 END) AS totalCount, "
                        +

                        "NULL AS main_total_amount, NULL AS main_refunded_amount, NULL AS main_net_amount,NULL AS main_gst_amount, NULL AS PAST_Events, NULL AS FUTURE_Events "
                        +

                        "FROM hall_details h " +

                        "LEFT JOIN booking_details b ON b.hall_details_hall_id = h.hall_id " +

                        "LEFT JOIN users u ON b.users_user_id = u.user_id " +

                        "WHERE (b.start_date IS NULL OR b.start_date BETWEEN COALESCE(:fromDate, (SELECT MIN(start_date) FROM booking_details)) AND COALESCE(:toDate, (SELECT MAX(end_date) FROM booking_details))) "
                        +

                        "AND (:zone IS NULL OR :zone = 'ALL' OR h.zone = :zone) " +

                        "AND (:division IS NULL OR :division = 'ALL' OR h.division = :division) " +

                        "AND (:hall_id IS NULL OR :hall_id = '1000' OR h.hall_id = :hall_id) " +

                        "AND (:userType IS NULL OR :userType = 'ALL' OR u.usertype = :userType) " +

                        "GROUP BY h.zone " +

                        "UNION ALL " +

                        "SELECT 'paymentStatus' AS category, " +

                        "       p.payment_status AS `key`, " +

                        "       COUNT(*) AS totalCount, " +

                        "       NULL AS main_total_amount, " +

                        "       NULL AS main_refunded_amount, " +

                        "       NULL AS main_net_amount, " +

                        "		NULL AS main_gst_amount," +

                        "       NULL AS PAST_Events, " +

                        "       NULL AS FUTURE_Events " +

                        "FROM payments p " +

                        "JOIN booking_details b ON p.booking_details_booking_id = b.booking_id " +

                        "JOIN hall_details h ON b.hall_details_hall_id = h.hall_id " +

                        "LEFT JOIN users u ON b.users_user_id = u.user_id " +

                        "WHERE (b.start_date >= CASE WHEN :fromDate IS NOT NULL THEN :fromDate ELSE (SELECT MIN(start_date) FROM booking_details) END) "
                        +

                        "AND (b.start_date <= CASE WHEN :toDate IS NOT NULL THEN :toDate ELSE (SELECT MAX(end_date) FROM booking_details) END) "
                        +

                        "AND (:zone IS NULL OR :zone = 'ALL' OR h.zone = :zone) " +

                        "AND (:division IS NULL OR :division = 'ALL' OR h.division = :division) " +

                        "AND (:hall_id IS NULL OR :hall_id = '1000' OR h.hall_id = :hall_id) " +

                        "AND (:userType IS NULL OR :userType = 'ALL' OR u.usertype = :userType) " +

                        "AND b.booking_status!='PENDING'" +

                        "GROUP BY p.payment_status " +

                        "UNION ALL " +

                        "SELECT 'past_future' AS category, NULL AS `key`, NULL AS totalCount, NULL AS main_total_amount, NULL AS main_refunded_amount, NULL AS main_net_amount, "
                        +
                        "NULL AS main_gst_amount," +
                        "COALESCE(SUM(PAST_Booking), 0) AS PAST_Events, " +
                        "COALESCE(SUM(FUTURE_Booking), 0) AS FUTURE_Events " +
                        "FROM ( " +
                        "SELECT " +
                        "CASE WHEN DATE(b.start_date) < CURRENT_DATE() THEN 1 ELSE 0 END AS PAST_Booking, " +
                        "CASE WHEN DATE(b.start_date) BETWEEN CURRENT_DATE() AND DATE_ADD(CURRENT_DATE(), INTERVAL 3 MONTH) THEN 1 ELSE 0 END AS FUTURE_Booking "
                        +
                        "FROM booking_details b " +
                        "JOIN hall_details h ON b.hall_details_hall_id = h.hall_id " +
                        "LEFT JOIN users u ON b.users_user_id = u.user_id " +
                        "WHERE " +
                        " b.booking_status!='PENDING'" +
                        "AND (b.start_date >= CASE WHEN :fromDate IS NOT NULL THEN :fromDate ELSE (SELECT MIN(start_date) FROM booking_details) END) AND "
                        +
                        "(b.start_date <= CASE WHEN :toDate IS NOT NULL THEN :toDate ELSE (SELECT MAX(end_date) FROM booking_details) END) AND "
                        +
                        "(:zone IS NULL OR :zone = 'ALL' OR h.zone = :zone) AND " +
                        "(:division IS NULL OR :division = 'ALL' OR h.division = :division) AND " +
                        "(:hall_id IS NULL OR :hall_id = '1000' OR h.hall_id = :hall_id) AND " +
                        "(:userType IS NULL OR :userType = 'ALL' OR u.usertype = :userType) " +
                        ") AS combined"

        )
        List<Map<String, Object>> getBookingStatistics(
                        @Param("fromDate") LocalDate fromDate,
                        @Param("toDate") LocalDate toDate,
                        @Param("zone") String zone,
                        @Param("division") String division,
                        @Param("hall_id") Integer hallid,
                        @Param("userType") String userType);

        @Query(value = "SELECT \r"
                        + "    b.booking_date as booking_date,\r"
                        + "    u.name as name,\r"
                        + "    u.usertype as usertype,\r"
                        + "    b.reason as reason,\r"
                        + "    b.start_date as start_date,\r"
                        + "    b.end_date as end_date,\r"
                        + "    b.no_of_days as no_of_days,\r"
                        + "    b.booking_status as booking_status,\r"
                        + "    b.amount_received as amount_received,\r"
                        + "    COALESCE(rt.amount_refunded, 0) as amount_refunded,\r"
                        + "    COALESCE(r.balace_amount, 0) as balance_amount\r"
                        + "FROM booking_details b\r"
                        + "JOIN hall_details h ON b.hall_details_hall_id = h.hall_id \r"
                        + "LEFT JOIN users u ON b.users_user_id = u.user_id\r"
                        + "LEFT JOIN refund_response r ON b.booking_id = r.booking_id\r"
                        + "LEFT JOIN refund_transaction rt ON b.booking_id = rt.booking_details_booking_id\r"
                        + "WHERE h.hall_id = :hallId AND b.booking_status != 'PENDING' ", nativeQuery = true)
        List<Map<String, Object>> getBookingDetailsByHallId(@Param("hallId") int hallId);

        // officer Dashboard
        @Query(nativeQuery = true, value = "SELECT 'main' AS category, NULL AS `key` , " +
                        "COUNT(DISTINCT b.booking_id) AS totalCount," +
                        "SUM(b.amount_received) AS main_total_amount, " +
                        "COALESCE(SUM(r.amount-r.balace_amount),0) AS main_refunded_amount, " +
                        "COALESCE(SUM(CASE WHEN b.booking_status IN ('COMPLETED', 'CANCELED') THEN r.balace_amount ELSE 0 END), 0)  AS main_net_amount, NULL AS PAST_Events, NULL AS FUTURE_Events "
                        +
                        "FROM booking_details b " +
                        "JOIN hall_details h ON h.hall_id = b.hall_details_hall_id " +
                        "LEFT JOIN refund_response r ON b.booking_id = r.booking_id " +
                        "LEFT JOIN users u ON b.users_user_id = u.user_id " +
                        "WHERE (b.start_date >= CASE WHEN :fromDate IS NOT NULL THEN :fromDate ELSE (SELECT MIN(start_date) FROM booking_details) END) "
                        +
                        "AND (b.start_date <= CASE WHEN :toDate IS NOT NULL THEN :toDate ELSE (SELECT MAX(end_date) FROM booking_details) END) "
                        +
                        "AND (:zone IS NULL OR :zone = 'ALL' OR h.zone = :zone) " +
                        "AND (:division IS NULL OR :division = 'ALL' OR h.division = :division) " +
                        "AND (:hall_id IS NULL OR :hall_id = '1000' OR h.hall_id = :hall_id) " +
                        "AND (:userType IS NULL OR :userType = 'ALL' OR u.usertype = :userType) " +
                        "AND b.booking_status!='PENDING'" +
                        "UNION ALL " +

                        "SELECT 'bookingStatus' AS category, b.booking_status AS `key`, COUNT(*) AS totalCount, NULL AS main_total_amount, NULL AS main_refunded_amount, NULL AS main_net_amount, NULL AS PAST_Events, NULL AS FUTURE_Events "
                        +
                        "FROM booking_details b " +
                        "JOIN hall_details h ON b.hall_details_hall_id = h.hall_id " +
                        "LEFT JOIN users u ON b.users_user_id = u.user_id " +
                        "WHERE (b.start_date >= CASE WHEN :fromDate IS NOT NULL THEN :fromDate ELSE (SELECT MIN(start_date) FROM booking_details) END) "
                        +
                        "AND (b.start_date <= CASE WHEN :toDate IS NOT NULL THEN :toDate ELSE (SELECT MAX(end_date) FROM booking_details) END) "
                        +
                        "AND (:zone IS NULL OR :zone = 'ALL' OR h.zone = :zone) " +
                        "AND (:division IS NULL OR :division = 'ALL' OR h.division = :division) " +
                        "AND (:hall_id IS NULL OR :hall_id = '1000' OR h.hall_id = :hall_id) " +
                        "AND (:userType IS NULL OR :userType = 'ALL' OR u.usertype = :userType) " +
                        "AND b.booking_status != 'PENDING'" +
                        "GROUP BY b.booking_status " +
                        "UNION ALL " +

                        "SELECT 'refundStatus' AS category, b.refund_status AS `key`, COUNT(*) AS totalCount, NULL AS main_total_amount, NULL AS main_refunded_amount, NULL AS main_net_amount, NULL AS PAST_Events, NULL AS FUTURE_Events "
                        +
                        "FROM booking_details b " +
                        "JOIN hall_details h ON b.hall_details_hall_id = h.hall_id " +
                        "LEFT JOIN users u ON b.users_user_id = u.user_id " +
                        "WHERE (b.start_date >= CASE WHEN :fromDate IS NOT NULL THEN :fromDate ELSE (SELECT MIN(start_date) FROM booking_details) END) "
                        +
                        "AND (b.start_date <= CASE WHEN :toDate IS NOT NULL THEN :toDate ELSE (SELECT MAX(end_date) FROM booking_details) END) "
                        +
                        "AND (:zone IS NULL OR :zone = 'ALL' OR h.zone = :zone) " +
                        "AND (:division IS NULL OR :division = 'ALL' OR h.division = :division) " +
                        "AND (:hall_id IS NULL OR :hall_id = '1000' OR h.hall_id = :hall_id) " +
                        "AND (:userType IS NULL OR :userType = 'ALL' OR u.usertype = :userType) " +
                        "AND b.booking_status != 'PENDING'" +
                        "GROUP BY b.refund_status " +
                        "UNION ALL " +

                        "SELECT 'reason' AS category, b.reason AS `key`, COUNT(*) AS totalCount, NULL AS main_total_amount, NULL AS main_net_amount, NULL AS main_refunded_amount, NULL AS PAST_Events, NULL AS FUTURE_Events "
                        +
                        "FROM booking_details b " +
                        "JOIN hall_details h ON b.hall_details_hall_id = h.hall_id " +
                        "LEFT JOIN users u ON b.users_user_id = u.user_id " +
                        "WHERE (b.start_date >= CASE WHEN :fromDate IS NOT NULL THEN :fromDate ELSE (SELECT MIN(start_date) FROM booking_details) END) "
                        +
                        "AND (b.start_date <= CASE WHEN :toDate IS NOT NULL THEN :toDate ELSE (SELECT MAX(end_date) FROM booking_details) END) "
                        +
                        "AND (:zone IS NULL OR :zone = 'ALL' OR h.zone = :zone) " +
                        "AND (:division IS NULL OR :division = 'ALL' OR h.division = :division) " +
                        "AND (:hall_id IS NULL OR :hall_id = '1000' OR h.hall_id = :hall_id) " +
                        "AND (:userType IS NULL OR :userType = 'ALL' OR u.usertype = :userType) " +
                        "AND b.booking_status != 'PENDING'" +
                        "GROUP BY b.reason " +
                        "UNION ALL " +

                        "SELECT 'zonewise' AS category, h.zone AS `key`, SUM(CASE WHEN b.hall_details_hall_id IS NOT NULL AND b.booking_status != 'PENDING' THEN 1 ELSE 0 END) AS totalCount, "
                        +
                        "NULL AS main_total_amount, NULL AS main_refunded_amount, NULL AS main_net_amount, NULL AS PAST_Events, NULL AS FUTURE_Events "
                        +
                        "FROM hall_details h " +
                        "LEFT JOIN booking_details b ON b.hall_details_hall_id = h.hall_id " +
                        "LEFT JOIN users u ON b.users_user_id = u.user_id " +
                        "WHERE (b.start_date IS NULL OR b.start_date BETWEEN COALESCE(:fromDate, (SELECT MIN(start_date) FROM booking_details)) AND COALESCE(:toDate, (SELECT MAX(end_date) FROM booking_details))) "
                        +
                        "AND (:zone IS NULL OR :zone = 'ALL' OR h.zone = :zone) " +
                        "AND (:division IS NULL OR :division = 'ALL' OR h.division = :division) " +
                        "AND (:hall_id IS NULL OR :hall_id = '1000' OR h.hall_id = :hall_id) " +
                        "AND (:userType IS NULL OR :userType = 'ALL' OR u.usertype = :userType) " +
                        "GROUP BY h.zone " +
                        "UNION ALL " +

                        "SELECT 'paymentStatus' AS category, " +
                        "       p.payment_status AS `key`, " +
                        "       COUNT(*) AS totalCount, " +
                        "       NULL AS main_total_amount, " +
                        "       NULL AS main_refunded_amount, " +
                        "       NULL AS main_net_amount, " +
                        "       NULL AS PAST_Events, " +
                        "       NULL AS FUTURE_Events " +
                        "FROM payments p " +
                        "JOIN booking_details b ON p.booking_details_booking_id = b.booking_id " +
                        "JOIN hall_details h ON b.hall_details_hall_id = h.hall_id " +
                        "LEFT JOIN users u ON b.users_user_id = u.user_id " +
                        "WHERE (b.start_date >= CASE WHEN :fromDate IS NOT NULL THEN :fromDate ELSE (SELECT MIN(start_date) FROM booking_details) END) "
                        +
                        "AND (b.start_date <= CASE WHEN :toDate IS NOT NULL THEN :toDate ELSE (SELECT MAX(end_date) FROM booking_details) END) "
                        +
                        "AND (:zone IS NULL OR :zone = 'ALL' OR h.zone = :zone) " +
                        "AND (:division IS NULL OR :division = 'ALL' OR h.division = :division) " +
                        "AND (:hall_id IS NULL OR :hall_id = '1000' OR h.hall_id = :hall_id) " +
                        "AND (:userType IS NULL OR :userType = 'ALL' OR u.usertype = :userType) " +
                        "AND b.booking_status!='PENDING'" +
                        "GROUP BY p.payment_status " +
                        "UNION ALL " +

                        "SELECT 'past_future' AS category, NULL AS `key`, NULL AS totalCount, NULL AS main_total_amount, NULL AS main_refunded_amount, NULL AS main_net_amount, "
                        +
                        "COALESCE(SUM(PAST_Booking), 0) AS PAST_Events, " +
                        "COALESCE(SUM(FUTURE_Booking), 0) AS FUTURE_Events " +
                        "FROM ( " +
                        "SELECT " +
                        "CASE WHEN DATE(b.start_date) < CURRENT_DATE() THEN 1 ELSE 0 END AS PAST_Booking, " +
                        "CASE WHEN DATE(b.start_date) BETWEEN CURRENT_DATE() AND DATE_ADD(CURRENT_DATE(), INTERVAL 3 MONTH) THEN 1 ELSE 0 END AS FUTURE_Booking "
                        +
                        "FROM booking_details b " +
                        "JOIN hall_details h ON b.hall_details_hall_id = h.hall_id " +
                        "LEFT JOIN users u ON b.users_user_id = u.user_id " +
                        "WHERE " +
                        " b.booking_status!='PENDING'" +
                        "AND (b.start_date >= CASE WHEN :fromDate IS NOT NULL THEN :fromDate ELSE (SELECT MIN(start_date) FROM booking_details) END) AND "
                        +
                        "(b.start_date <= CASE WHEN :toDate IS NOT NULL THEN :toDate ELSE (SELECT MAX(end_date) FROM booking_details) END) AND "
                        +
                        "(:zone IS NULL OR :zone = 'ALL' OR h.zone = :zone) AND " +
                        "(:division IS NULL OR :division = 'ALL' OR h.division = :division) AND " +
                        "(:hall_id IS NULL OR :hall_id = '1000' OR h.hall_id = :hall_id) AND " +
                        "(:userType IS NULL OR :userType = 'ALL' OR u.usertype = :userType) " +
                        ") AS combined")
        List<Map<String, Object>> getOfficerDashboard(
                        @Param("fromDate") LocalDate fromDate,
                        @Param("toDate") LocalDate toDate,
                        @Param("zone") String zone,
                        @Param("division") String division,
                        @Param("hall_id") Integer hallid,
                        @Param("userType") String userType);

        @Query(value = "SELECT \r"
                        + "    b.booking_date as booking_date,\r"
                        + "    u.name as name,\r"
                        + "    u.usertype as usertype,\r"
                        + "    b.reason as reason,\r"
                        + "    b.start_date as start_date,\r"
                        + "    b.end_date as end_date,\r"
                        + "    b.no_of_days as no_of_days,\r"
                        + "    b.booking_status as booking_status,\r"
                        + "    b.amount_received as amount_received,\r"
                        + "    COALESCE(rt.amount_refunded, 0) as amount_refunded,\r"
                        + "    COALESCE(r.balace_amount, 0) as balance_amount\r"
                        + "FROM booking_details b\r"
                        + "JOIN hall_details h ON b.hall_details_hall_id = h.hall_id \r"
                        + "LEFT JOIN users u ON b.users_user_id = u.user_id\r"
                        + "LEFT JOIN refund_response r ON b.booking_id = r.booking_id\r"
                        + "LEFT JOIN refund_transaction rt ON b.booking_id = rt.booking_details_booking_id\r"
                        + "WHERE h.hall_id = :hallId AND b.booking_status != 'PENDING' ", nativeQuery = true)
        List<Map<String, Object>> getHallDetailsForOfficer(@Param("hallId") Integer hallIid);

        @Query(value = "SELECT bd.*, hd.hall_name, us.name, us.usertype " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND hd.zone = :zone ", nativeQuery = true)
        List<BookingDetails> getAllBookingDetailsBookingDateByZone(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("zone") String zone);

        @Query(value = "SELECT bd.*, hd.hall_name, us.name, us.usertype " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "WHERE DATE(bd.start_date) >= :fromDate " +
                        "AND DATE(bd.end_date) <= :toDate " +
                        "AND hd.zone = :zone ", nativeQuery = true)
        List<BookingDetails> getAllBookingDetailsEventDateByZone(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("zone") String zone);

        @Query(value = "SELECT bd.*, hd.hall_name, us.name, us.usertype " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND hd.zone = :zone " +
                        "AND (:bookingStatus IS NULL OR bd.booking_status = :bookingStatus)", nativeQuery = true)
        List<BookingDetails> getAllBookingDetailsBookingDateByZoneWithStatus(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("zone") String zone,
                        @Param("bookingStatus") String bookingStatus);

        @Query(value = "SELECT bd.*, hd.hall_name, us.name, us.usertype " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "WHERE DATE(bd.start_date) >= :fromDate " +
                        "AND DATE(bd.end_date) <= :toDate " +
                        "AND hd.zone = :zone " +
                        "AND (:bookingStatus IS NULL OR bd.booking_status = :bookingStatus)", nativeQuery = true)
        List<BookingDetails> getAllBookingDetailsEventDateByZoneWithStatus(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("zone") String zone,
                        @Param("bookingStatus") String bookingStatus);

        @Query(value = "SELECT bd.*, hd.hall_name, us.name, us.usertype " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "JOIN settlement st ON bd.booking_id=st.booking_details_booking_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND hd.zone = :zone ", nativeQuery = true)
        List<BookingDetails> getAllBookingsettlementDetailsByZoneByBookingDate(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("zone") String zone);

        @Query(value = "SELECT bd.*, hd.hall_name, us.name, us.usertype " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "JOIN settlement st ON bd.booking_id=st.booking_details_booking_id " +
                        "WHERE DATE(bd.start_date) >= :fromDate " +
                        "AND DATE(bd.end_date) <= :toDate " +
                        "AND hd.zone = :zone ", nativeQuery = true)
        List<BookingDetails> getAllBookingsettlementDetailsByZoneByEventDate(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("zone") String zone);

        @Query(value = "SELECT bd.*, hd.hall_name, us.name, us.usertype " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "JOIN settlement st ON bd.booking_id=st.booking_details_booking_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND hd.zone = :zone " +
                        "AND (:refundStatus IS NULL OR bd.refund_status = :refundStatus) ", nativeQuery = true)
        List<BookingDetails> getAllBookingsettlementDetailsByZoneByBookingDateWithStatus(
                        @Param("fromDate") String fromDate, @Param("toDate") String toDate, @Param("zone") String zone,
                        @Param("refundStatus") String refundStatus);

        @Query(value = "SELECT bd.*, hd.hall_name, us.name, us.usertype " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "JOIN settlement st ON bd.booking_id=st.booking_details_booking_id " +
                        "WHERE DATE(bd.start_date) >= :fromDate " +
                        "AND DATE(bd.end_date) <= :toDate " +
                        "AND hd.zone = :zone " +
                        "AND (:refundStatus IS NULL OR bd.refund_status = :refundStatus) ", nativeQuery = true)
        List<BookingDetails> getAllBookingsettlementDetailsByZoneByEventDateWithStatus(
                        @Param("fromDate") String fromDate, @Param("toDate") String toDate, @Param("zone") String zone,
                        @Param("refundStatus") String refundStatus);

        @Query(value = "SELECT bd.booking_id, bd.booking_date, hd.hall_name, us.name, us.usertype, p.transaction_id, p.payment_type, p.amount_received, bd.reason, bd.start_date, bd.end_date, p.payment_status "
                        +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "JOIN payments p ON bd.booking_id = p.booking_details_booking_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND hd.zone = :zone " +
                        "AND p.payment_status='Success' ", nativeQuery = true)
        List<Object[]> getAllPaymentsDetailsByZone(@Param("fromDate") String fromDate, @Param("toDate") String toDate,
                        @Param("zone") String zone);

        @Query(value = "SELECT " +
                        "h.hall_name AS HallName, " +
                        "h.zone AS Zone, " +
                        "h.division AS Division, " +
                        "CONCAT(h.door_no,',',h.street, ', ', h.area, ', ', h.city, ', ', h.pin_code) AS Address, " +
                        "COUNT(*) AS HallTotalBookings, " +
                        "SUM(p.amount_received) AS AmountReceived, " +
                        "COALESCE(SUM(r.amount_refunded), 0) AS AmountRefund, " + // Use COALESCE to handle NULL values
                        "(SUM(p.amount_received) - COALESCE(SUM(r.amount_refunded), 0)) AS Difference " + // Calculate
                                                                                                          // the
                                                                                                          // difference
                        "FROM " +
                        "booking_details bd " +
                        "JOIN " +
                        "hall_details h ON bd.hall_details_hall_id = h.hall_id " +
                        "JOIN " +
                        "payments p ON bd.booking_id = p.booking_details_booking_id " +
                        "LEFT JOIN " +
                        "refund_transaction r ON p.transaction_id = r.transaction_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND h.zone = :zone " +
                        "GROUP BY " +
                        "h.hall_id", nativeQuery = true)
        List<Object[]> getBookingHallDetailsReportByZoneByBookingDate(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("zone") String zone);

        @Query(value = "SELECT " +
                        "h.hall_name AS HallName, " +
                        "h.zone AS Zone, " +
                        "h.division AS Division, " +
                        "CONCAT(h.door_no,',',h.street, ', ', h.area, ', ', h.city, ', ', h.pin_code) AS Address, " +
                        "COUNT(*) AS HallTotalBookings, " +
                        "SUM(p.amount_received) AS AmountReceived, " +
                        "COALESCE(SUM(r.amount_refunded), 0) AS AmountRefund, " + // Use COALESCE to handle NULL values
                        "(SUM(p.amount_received) - COALESCE(SUM(r.amount_refunded), 0)) AS Difference " + // Calculate
                                                                                                          // the
                                                                                                          // difference
                        "FROM " +
                        "booking_details bd " +
                        "JOIN " +
                        "hall_details h ON bd.hall_details_hall_id = h.hall_id " +
                        "JOIN " +
                        "payments p ON bd.booking_id = p.booking_details_booking_id " +
                        "LEFT JOIN " +
                        "refund_transaction r ON p.transaction_id = r.transaction_id " +
                        "WHERE DATE(bd.start_date) >= :fromDate " +
                        "AND DATE(bd.end_date) <= :toDate " +
                        "AND h.zone = :zone " +
                        "GROUP BY " +
                        "h.hall_id", nativeQuery = true)
        List<Object[]> getBookingHallDetailsReportByZoneByEventDate(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("zone") String zone);

        @Query(value = "SELECT " +
                        "h.hall_name AS HallName, " +
                        "h.zone AS Zone, " +
                        "h.division AS Division, " +
                        "CONCAT(h.door_no,',',h.street, ', ', h.area, ', ', h.city, ', ', h.pin_code) AS Address, " +
                        "COUNT(*) AS HallTotalBookings, " +
                        "SUM(p.amount_received) AS AmountReceived, " +
                        "COALESCE(SUM(r.amount_refunded), 0) AS AmountRefund, " + // Use COALESCE to handle NULL values
                        "(SUM(p.amount_received) - COALESCE(SUM(r.amount_refunded), 0)) AS Difference " + // Calculate
                                                                                                          // the
                                                                                                          // difference
                        "FROM " +
                        "booking_details bd " +
                        "JOIN " +
                        "hall_details h ON bd.hall_details_hall_id = h.hall_id " +
                        "JOIN " +
                        "payments p ON bd.booking_id = p.booking_details_booking_id " +
                        "LEFT JOIN " +
                        "refund_transaction r ON p.transaction_id = r.transaction_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND h.zone = :zone " +
                        "AND (:hall_id = '0' OR h.hall_id = :hall_id) " +
                        "GROUP BY " +
                        "h.hall_id", nativeQuery = true)
        List<Object[]> getBookingHallDetailsReportByZoneByBookingDateHallname(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("zone") String zone, @Param("hall_id") Integer hallId);

        @Query(value = "SELECT " +
                        "h.hall_name AS HallName, " +
                        "h.zone AS Zone, " +
                        "h.division AS Division, " +
                        "CONCAT(h.door_no,',',h.street, ', ', h.area, ', ', h.city, ', ', h.pin_code) AS Address, " +
                        "COUNT(*) AS HallTotalBookings, " +
                        "SUM(p.amount_received) AS AmountReceived, " +
                        "COALESCE(SUM(r.amount_refunded), 0) AS AmountRefund, " + // Use COALESCE to handle NULL values
                        "(SUM(p.amount_received) - COALESCE(SUM(r.amount_refunded), 0)) AS Difference " + // Calculate
                                                                                                          // the
                                                                                                          // difference
                        "FROM " +
                        "booking_details bd " +
                        "JOIN " +
                        "hall_details h ON bd.hall_details_hall_id = h.hall_id " +
                        "JOIN " +
                        "payments p ON bd.booking_id = p.booking_details_booking_id " +
                        "LEFT JOIN " +
                        "refund_transaction r ON p.transaction_id = r.transaction_id " +
                        "WHERE DATE(bd.start_date) >= :fromDate " +
                        "AND DATE(bd.end_date) <= :toDate " +
                        "AND h.zone = :zone " +
                        "AND (:hall_id = '0' OR h.hall_id = :hall_id) " +
                        "GROUP BY " +
                        "h.hall_id", nativeQuery = true)
        List<Object[]> getBookingHallDetailsReportByZoneByEventDateHallname(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("zone") String zone, @Param("hall_id") Integer hallId);

        // Present Events: Fetch today's events
        @Query(value = "SELECT * FROM booking_details WHERE DATE(start_date) = CURRENT_DATE() AND booking_status='APPROVED'", nativeQuery = true)
        List<BookingDetails> findPresentBookingList();

        // Past Events: Fetch events
        @Query(value = "SELECT * FROM booking_details " +
                        "WHERE DATE(start_date) < CURRENT_DATE() " +
                        "AND booking_status='APPROVED'", nativeQuery = true)
        List<BookingDetails> findPastEventsList();

        // Future Events: Fetch events for the next two days (tomorrow and the day
        // after)
        @Query(value = "SELECT * FROM booking_details " +
                        "WHERE start_date BETWEEN CURDATE() + INTERVAL 1 DAY AND CURDATE() + INTERVAL 2 DAY " +
                        "AND booking_status = 'APPROVED'", nativeQuery = true)
        List<BookingDetails> findFutureEventsList();

        // admin reports
        @Query(value = "SELECT bd.booking_id, bd.booking_date, hd.hall_name, us.name, us.mobile_no, " +
                        "CONCAT(us.doorno, ', ', us.street, ', ', us.area, ', ', us.location, ', ', us.pin_code) AS address, "
                        +
                        "us.usertype, bd.reason, bd.start_date, bd.end_date, " +
                        "bd.deposit_amount, bd.hall_amount, bd.gst, bd.amount_received, bd.booking_status " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND (:zone IS NULL OR :zone = 'ALL' OR hd.zone = :zone) " +
                        "AND (:division IS NULL OR :division = 'ALL' OR hd.division = :division) " +
                        "AND (:hallid IS NULL OR :hallid = '1000' OR hd.hall_id = :hallid) " +
                        "AND bd.booking_status!='PENDING' order by bd.booking_id asc", nativeQuery = true)
        List<Object[]> getBookingDetailsByBookingDate(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("zone") String zone, @Param("division") String division,
                        @Param("hallid") Integer hallid);

        @Query(value = "SELECT bd.booking_id, bd.booking_date, hd.hall_name, us.name, us.mobile_no, " +
                        "CONCAT(us.doorno, ', ', us.street, ', ', us.area, ', ', us.location, ', ', us.pin_code) AS address, "
                        +
                        "us.usertype, bd.reason, bd.start_date, bd.end_date, " +
                        "bd.deposit_amount, bd.hall_amount, bd.gst, bd.amount_received, bd.booking_status " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND (:zone IS NULL OR :zone = 'ALL' OR hd.zone = :zone) " +
                        "AND (:division IS NULL OR :division = 'ALL' OR hd.division = :division) " +
                        "AND (:hallid IS NULL OR :hallid = '1000' OR hd.hall_id = :hallid) " +
                        "AND bd.booking_status = :bookingStatus order by bd.booking_id asc", nativeQuery = true)
        List<Object[]> getBookingDetailsByBookingDateWithStatus(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate,
                        @Param("bookingStatus") String bookingStatus, @Param("zone") String zone,
                        @Param("division") String division, @Param("hallid") Integer hallid);

        @Query(value = "SELECT bd.booking_id, bd.booking_date, hd.hall_name, us.name, us.mobile_no, " +
                        "CONCAT(us.doorno, ', ', us.street, ', ', us.area, ', ', us.location, ', ', us.pin_code) AS address, "
                        +
                        "us.usertype, bd.reason, bd.start_date, bd.end_date, " +
                        "bd.deposit_amount, bd.hall_amount, bd.gst, bd.amount_received, bd.booking_status " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "WHERE DATE(bd.start_date) >= :fromDate " +
                        "AND DATE(bd.end_date) <= :toDate " +
                        "AND (:zone IS NULL OR :zone = 'ALL' OR hd.zone = :zone) " +
                        "AND (:division IS NULL OR :division = 'ALL' OR hd.division = :division) " +
                        "AND (:hallid IS NULL OR :hallid = '1000' OR hd.hall_id = :hallid) " +
                        "AND bd.booking_status!='PENDING' order by bd.booking_id asc", nativeQuery = true)
        List<Object[]> getBookingDetailsByEventDate(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("zone") String zone, @Param("division") String division,
                        @Param("hallid") Integer hallid);

        @Query(value = "SELECT bd.booking_id, bd.booking_date, hd.hall_name, us.name, us.mobile_no, " +
                        "CONCAT(us.doorno, ', ', us.street, ', ', us.area, ', ', us.location, ', ', us.pin_code) AS address, "
                        +
                        "us.usertype, bd.reason, bd.start_date, bd.end_date, " +
                        "bd.deposit_amount, bd.hall_amount, bd.gst, bd.amount_received, bd.booking_status " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "WHERE DATE(bd.start_date) >= :fromDate " +
                        "AND DATE(bd.end_date) <= :toDate " +
                        "AND (:zone IS NULL OR :zone = 'ALL' OR hd.zone = :zone) " +
                        "AND (:division IS NULL OR :division = 'ALL' OR hd.division = :division) " +
                        "AND (:hallid IS NULL OR :hallid = '1000' OR hd.hall_id = :hallid) " +
                        "AND bd.booking_status = :bookingStatus order by bd.booking_id asc", nativeQuery = true)
        List<Object[]> getBookingDetailsByEventDateWithStatus(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate,
                        @Param("bookingStatus") String bookingStatus, @Param("zone") String zone,
                        @Param("division") String division, @Param("hallid") Integer hallid);

        @Query(value = "SELECT bd.booking_id, bd.booking_date, hd.hall_name, us.name, us.usertype, p.transaction_id, p.payment_type, bd.deposit_amount, bd.hall_amount, bd.gst, p.amount_received, bd.reason, bd.start_date, bd.end_date, p.payment_status "
                        +
                        "FROM booking_details bd  " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "JOIN payments p ON bd.booking_id = p.booking_details_booking_id " +
                        "WHERE DATE(p.cdate) >= :fromDate " +
                        "AND DATE(p.cdate) <= :toDate " +
                        "AND (:paymentStatus IS NULL OR p.payment_status = :paymentStatus)" +
                        "AND (:zone IS NULL OR :zone = 'ALL' OR hd.zone = :zone) " +
                        "AND (:division IS NULL OR :division = 'ALL' OR hd.division = :division) " +
                        "AND (:hallid IS NULL OR :hallid = '1000' OR hd.hall_id = :hallid) order by bd.booking_id asc", nativeQuery = true)
        List<Object[]> getAllPaymentsDetails(@Param("fromDate") String fromDate, @Param("toDate") String toDate,
                        @Param("paymentStatus") String paymentStatus, @Param("zone") String zone,
                        @Param("division") String division, @Param("hallid") Integer hallid);

        @Query(value = "SELECT bd.booking_id, bd.booking_date, hd.hall_name, us.name, us.usertype, p.transaction_id, p.payment_type, bd.deposit_amount, bd.hall_amount, bd.gst, p.amount_received, bd.reason, bd.start_date, bd.end_date, p.payment_status "
                        +
                        "FROM booking_details bd  " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "JOIN extra_payments p ON bd.booking_id = p.booking_details_booking_id " +
                        "WHERE DATE(p.cdate) >= :fromDate " +
                        "AND DATE(p.cdate) <= :toDate " +
                        "AND (:paymentStatus IS NULL OR p.payment_status = :paymentStatus)" +
                        "AND (:zone IS NULL OR :zone = 'ALL' OR hd.zone = :zone) " +
                        "AND (:division IS NULL OR :division = 'ALL' OR hd.division = :division) " +
                        "AND (:hallid IS NULL OR :hallid = '1000' OR hd.hall_id = :hallid) order by bd.booking_id asc ", nativeQuery = true)
        List<Object[]> getAllExtraPaymentsDetails(@Param("fromDate") String fromDate, @Param("toDate") String toDate,
                        @Param("paymentStatus") String paymentStatus, @Param("zone") String zone,
                        @Param("division") String division, @Param("hallid") Integer hallid);

        @Query(value = "SELECT " +
                        "h.hall_name AS HallName, " +
                        "h.hall_Incharge_name AS InchargeName, " +
                        "h.hall_Incharge_no AS hallInchargeMobno, " +
                        "CONCAT(h.zone, '/', h.division) AS ZoneDivision, " + // Concatenate zone and division
                        "COUNT(*) AS HallTotalBookings, " +
                        "SUM(bd.deposit_amount) AS depositAmount, " +
                        "SUM(bd.hall_amount) AS hallAmount, " +
                        "SUM(bd.gst) AS gst, " +
                        "SUM(p.amount_received) AS AmountReceived, " +
                        "COALESCE(SUM(r.amount - r.balace_amount), 0) AS AmountRefund, " +
                        "COALESCE(SUM(CASE  WHEN bd.booking_status IN ('COMPLETED', 'CANCELED') THEN r.balace_amount - bd.gst ELSE 0 END), 0) AS NetRevenue ,"
                        +
                        "h.hall_id " +
                        "FROM " +
                        "booking_details bd " +
                        "JOIN " +
                        "hall_details h ON bd.hall_details_hall_id = h.hall_id " +
                        "JOIN " +
                        "payments p ON bd.booking_id = p.booking_details_booking_id " +
                        "LEFT JOIN refund_response r ON bd.booking_id = r.booking_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND (:zone IS NULL OR :zone = 'ALL' OR h.zone = :zone) " +
                        "AND (:division IS NULL OR :division = 'ALL' OR h.division = :division) " +
                        "AND (:hallid IS NULL OR :hallid = '1000' OR h.hall_id = :hallid) " +
                        "GROUP BY " +
                        "bd.hall_details_hall_id order by COUNT(*) desc", nativeQuery = true)
        List<Object[]> getBookingHallDetailsReportByBookingDate(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("zone") String zone, @Param("division") String division,
                        @Param("hallid") Integer hallid);

        @Query(value = "SELECT " +
                        "h.hall_name AS HallName, " +
                        "h.hall_Incharge_name AS InchargeName, " +
                        "h.hall_Incharge_no AS hallInchargeMobno, " +
                        "CONCAT(h.zone, '/', h.division) AS ZoneDivision, " + // Concatenate zone and division
                        "COUNT(*) AS HallTotalBookings, " +
                        "SUM(bd.deposit_amount) AS depositAmount, " +
                        "SUM(bd.hall_amount) AS hallAmount, " +
                        "SUM(bd.gst) AS gst, " +
                        "SUM(p.amount_received) AS AmountReceived, " +
                        "COALESCE(SUM(r.amount - r.balace_amount), 0) AS AmountRefund, " +
                        "COALESCE(SUM(CASE  WHEN bd.booking_status IN ('COMPLETED', 'CANCELED') THEN r.balace_amount - bd.gst ELSE 0 END), 0) AS NetRevenue "
                        +
                        "FROM " +
                        "booking_details bd " +
                        "JOIN " +
                        "hall_details h ON bd.hall_details_hall_id = h.hall_id " +
                        "JOIN " +
                        "payments p ON bd.booking_id = p.booking_details_booking_id " +
                        "LEFT JOIN refund_response r ON bd.booking_id = r.booking_id " +
                        "WHERE DATE(bd.start_date) >= :fromDate " +
                        "AND DATE(bd.end_date) <= :toDate " +
                        "AND (:zone IS NULL OR :zone = 'ALL' OR h.zone = :zone) " +
                        "AND (:division IS NULL OR :division = 'ALL' OR h.division = :division) " +
                        "AND (:hallid IS NULL OR :hallid = '1000' OR h.hall_id = :hallid) " +
                        "GROUP BY " +
                        "bd.hall_details_hall_id", nativeQuery = true)
        List<Object[]> getBookingHallDetailsReportByEventDate(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("zone") String zone, @Param("division") String division,
                        @Param("hallid") Integer hallid);

        @Query(value = "SELECT bd.*, hd.hall_name, us.name, us.usertype " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "left JOIN settlement st ON bd.booking_id=st.booking_details_booking_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate order by bd.booking_id asc ", nativeQuery = true)
        List<BookingDetails> getAllBookingsettlementDetailsByBookingDate(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate);

        @Query(value = "SELECT bd.*, hd.hall_name, us.name, us.usertype " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "left JOIN settlement st ON bd.booking_id=st.booking_details_booking_id " +
                        "WHERE DATE(bd.booking_date) >= :fromDate " +
                        "AND DATE(bd.booking_date) <= :toDate " +
                        "AND (:refundStatus IS NULL OR bd.refund_status = :refundStatus) order by bd.booking_id asc", nativeQuery = true)
        List<BookingDetails> getAllBookingsettlementDetailsByBookingDateWithStatus(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("refundStatus") String refundStatus);

        @Query(value = "SELECT bd.*, hd.hall_name, us.name, us.usertype " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "left JOIN settlement st ON bd.booking_id=st.booking_details_booking_id " +
                        "WHERE DATE(bd.start_date) >= :fromDate " +
                        "AND DATE(bd.end_date) <= :toDate order by bd.booking_id asc", nativeQuery = true)
        List<BookingDetails> getAllBookingsettlementDetailsByEventDate(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate);

        @Query(value = "SELECT bd.*, hd.hall_name, us.name, us.usertype " +
                        "FROM booking_details bd " +
                        "JOIN hall_details hd ON bd.hall_details_hall_id = hd.hall_id " +
                        "JOIN users us ON bd.users_user_id = us.user_id " +
                        "left JOIN settlement st ON bd.booking_id=st.booking_details_booking_id " +
                        "WHERE DATE(bd.start_date) >= :fromDate " +
                        "AND DATE(bd.end_date) <= :toDate " +
                        "AND (:refundStatus IS NULL OR bd.refund_status = :refundStatus) order by bd.booking_id asc", nativeQuery = true)
        List<BookingDetails> getAllBookingsettlementDetailsByEventDateWithStatus(@Param("fromDate") String fromDate,
                        @Param("toDate") String toDate, @Param("refundStatus") String refundStatus);

        // In BookingDetailsRepository.java
        // In BookingDetailsRepository.java
        @Query("SELECT h.zone, COUNT(b) FROM BookingDetails b JOIN b.hallDetails h WHERE b.bookingStatus = 'APPROVED' GROUP BY h.zone")
        List<Object[]> countBookingsByZone();

        @Query("SELECT h.division, COUNT(b) FROM BookingDetails b JOIN b.hallDetails h WHERE b.bookingStatus = 'APPROVED' GROUP BY h.division")
        List<Object[]> countBookingsByDivisions();

        @Query("SELECT b FROM BookingDetails b JOIN b.hallDetails h WHERE h.zone = :zone AND b.bookingStatus = 'APPROVED'")
        List<BookingDetails> findByZone(@Param("zone") String zone);

        // gccapps
        List<BookingDetails> findByBookingStatus(BookingDetails.BookingStatus bookingStatus);

        @Query("SELECT b FROM BookingDetails b JOIN FETCH b.settlement s WHERE b.refundStatus = 'WAITING'")
        List<BookingDetails> findAllWithPendingRefundStatus();

        @Query("SELECT b FROM BookingDetails b WHERE b.refundStatus = 'WAITING' AND b.bookingStatus = 'CANCELED'")
        List<BookingDetails> findPendingCancelledList();

        @Query(value = "SELECT * FROM booking_details WHERE refund_status = 'WAITING' AND booking_status IN ('GCC_BLOCKED', 'REJECTED')", nativeQuery = true)
        List<BookingDetails> findPendingRejectedList();

        @Query(value = "SELECT * FROM booking_details WHERE refund_status = 'COMPLETED' AND booking_status = 'CANCELED'", nativeQuery = true)
        List<BookingDetails> findCompletedCancelledList();

        @Query(value = "SELECT * FROM booking_details WHERE refund_status = 'COMPLETED' AND booking_status IN ('GCC_BLOCKED', 'REJECTED')", nativeQuery = true)
        List<BookingDetails> findCompletedRejectedList();

        @Query("SELECT b FROM BookingDetails b JOIN FETCH b.settlement s WHERE b.refundStatus = 'REJECTED'")
        List<BookingDetails> findSettlementRejected();

        @Query(value = "SELECT * FROM booking_details WHERE DATE(start_date) < CURRENT_DATE() AND booking_status='APPROVED'", nativeQuery = true)
        List<BookingDetails> findPastsEvents();

        @Query(value = "SELECT * FROM booking_details WHERE DATE(start_date) = CURRENT_DATE() AND booking_status='APPROVED'", nativeQuery = true)
        List<BookingDetails> findPresentsEvents();

        @Query(value = "SELECT * FROM booking_details WHERE DATE(start_date) BETWEEN CURRENT_DATE() AND DATE_ADD(CURRENT_DATE(), INTERVAL 2 DAY) AND booking_status = 'APPROVED'", nativeQuery = true)
        List<BookingDetails> findFuturesEvents();

        @Query(value = "SELECT * FROM booking_details WHERE DATE(start_date) < CURRENT_DATE() AND booking_status='APPROVED'", nativeQuery = true)
        public List<BookingDetails> getSettlementApprovedList();

        @Query(value = "SELECT * FROM booking_details WHERE DATE(start_date) <= CURRENT_DATE() AND booking_status='APPROVED'", nativeQuery = true)
        public List<BookingDetails> getSettlementsList();

        @Query("SELECT b FROM BookingDetails b WHERE b.refundStatus = 'FAILURE' AND b.bookingStatus = 'APPROVED'  AND b.hallDetails.zone = :zone")
        List<BookingDetails> findSettlementRefundFailedByZone(String zone);

        @Query("SELECT b FROM BookingDetails b WHERE b.refundStatus = 'FAILURE' AND b.bookingStatus = 'CANCELED'  AND b.hallDetails.zone = :zone")
        List<BookingDetails> findRefundFaileCancelledListByZone(String zone);

        @Query("SELECT b FROM BookingDetails b WHERE b.refundStatus = 'FAILURE' AND b.bookingStatus IN ('REJECTED','GCC_BLOCKED')  AND b.hallDetails.zone = :zone")
        List<BookingDetails> findRefundFaileRejectedListByZone(String zone);

        @Query("SELECT b FROM BookingDetails b WHERE b.bookingStatus = 'COMPLETED' AND b.refundStatus IN ('COMPLETED','CLOSED') AND b.hallDetails.zone = :zone")
        List<BookingDetails> findSettlementCompletedListbyzone(String zone);

        @Query("SELECT b FROM BookingDetails b WHERE b.bookingStatus = 'CANCELED' AND b.refundStatus = 'COMPLETED' AND b.hallDetails.zone = :zone")
        List<BookingDetails> findCancelledCompletedListbyzone(String zone);

        @Query("SELECT b FROM BookingDetails b WHERE b.bookingStatus IN ('REJECTED','GCC_BLOCKED') AND b.refundStatus = 'COMPLETED' AND b.hallDetails.zone = :zone")
        List<BookingDetails> findRejectedSettlementCompletedListbyzone(String zone);

        @Query("SELECT b FROM BookingDetails b JOIN FETCH b.settlement s WHERE b.refundStatus = 'WAITING'")
        List<BookingDetails> findAllSettlementPending();

        @Query("SELECT b FROM BookingDetails b WHERE b.refundStatus = 'WAITING' AND b.bookingStatus = 'CANCELED'")
        List<BookingDetails> findAllSettlementCancelledList();

        @Query("SELECT b FROM BookingDetails b WHERE b.refundStatus = 'WAITING' AND b.bookingStatus IN ('REJECTED','GCC_BLOCKED')")
        List<BookingDetails> findAllSettlementRejectedList();

        @Query("SELECT b FROM BookingDetails b WHERE b.refundStatus = 'FAILURE' AND b.bookingStatus = 'APPROVED'")
        List<BookingDetails> findAllSettlementRefundFailed();

        @Query("SELECT b FROM BookingDetails b WHERE b.refundStatus = 'FAILURE' AND b.bookingStatus = 'CANCELED'")
        List<BookingDetails> findAllRefundFaileCancelledList();

        @Query("SELECT b FROM BookingDetails b WHERE b.refundStatus = 'FAILURE' AND b.bookingStatus IN ('REJECTED','GCC_BLOCKED')")
        List<BookingDetails> findAllRefundFaileRejectedList();

        @Query(value = "SELECT * FROM booking_details WHERE booking_date = :bookingDate AND hall_details_hall_id = :hallId", nativeQuery = true)
        List<BookingDetails> findByBookingDateAndHallId(@Param("bookingDate") LocalDate bookingDate,
                        @Param("hallId") Long hallId);

        @Query(value = "SELECT * FROM booking_details where booking_status='WAITING_FOR_NOC' order by booking_id", nativeQuery = true)
        List<BookingDetails> getAllwaitingNOC();

        @Query(value = "SELECT bd.counter_id, COUNT(*) AS total_count " +
                        "FROM booking_details bd " +
                        "WHERE DATE(bd.booking_date) BETWEEN :fromDate AND :toDate " +
                        "AND bd.booking_status <> 'PENDING' " +
                        "AND bd.counter_id IS NOT NULL " +
                        "GROUP BY bd.counter_id", nativeQuery = true)
        List<Object[]> getCounterWiseBookingCount(
                        @Param("fromDate") String fromDate,
                        @Param("toDate") String toDate);

        /*
         * @Query(value =
         * "SELECT bd.counter_id, h.zone, h. division, h.hall_name, COUNT(*) as total_bookings "
         * +
         * "FROM booking_details bd " +
         * "JOIN hall_details h ON h.hall_id = bd.hall_details_hall_id " +
         * "WHERE DATE(bd.booking_date) BETWEEN :fromDate AND :toDate " +
         * "AND bd.counter_id IS NOT NULL " +
         * "GROUP BY bd.counter_id,h.zone, h. division, h.hall_name", nativeQuery =
         * true)
         * List<Object[]> getHallWiseBookingDetails(@Param("fromDate") String
         * fromDate, @Param("toDate") String toDate);
         * 
         * @Query(value =
         * "SELECT bd.booking_id,h.hall_name,  bd.booking_date, bd.start_date, bd.end_date,"
         * +
         * "bd.starting_slot,bd.ending_slot,bd.no_of_days,bd.reason,bd.amount_received, "
         * +
         * "bd.hall_amount,bd.deposit_amount,bd.gst, " +
         * "bd.booking_status,bd.booking_type " +
         * "FROM booking_details bd " +
         * "JOIN hall_details h ON h.hall_id = bd.hall_details_hall_id " +
         * "WHERE DATE(bd.booking_date) BETWEEN :fromDate AND :toDate " +
         * "AND bd.counter_id IS NOT NULL", nativeQuery = true)
         * List<Object[]> getBookingDetailsForAllHalls(@Param("fromDate") String
         * fromDate, @Param("toDate") String toDate);
         */

        @Query(value = "SELECT bd.counter_id, h.zone, h.division, h.hall_name, COUNT(*) AS total_bookings " +
                        "FROM booking_details bd " +
                        "JOIN hall_details h ON h.hall_id = bd.hall_details_hall_id " +
                        "WHERE DATE(bd.booking_date) BETWEEN :fromDate AND :toDate " +
                        "AND bd.counter_id IS NOT NULL " +
                        "AND bd.booking_status <> 'PENDING' " +
                        "AND (:counterId IS NULL OR bd.counter_id = :counterId) " +
                        "GROUP BY bd.counter_id, h.zone, h.division, h.hall_name", nativeQuery = true)
        List<Object[]> getHallWiseBookingDetails(
                        @Param("fromDate") String fromDate,
                        @Param("toDate") String toDate,
                        @Param("counterId") String counterId);

        @Query(value = "  SELECT bd.booking_id, h.hall_name, bd.booking_date, bd.start_date, " +
                        "bd.end_date, bd.starting_slot, bd.ending_slot, bd.no_of_days, bd.reason, " +
                        "bd.amount_received, bd.hall_amount, bd.deposit_amount, bd.gst, bd.booking_status, " +
                        "bd.booking_type  FROM booking_details bd " +
                        "JOIN hall_details h ON h.hall_id = bd.hall_details_hall_id " +
                        "WHERE DATE(bd.booking_date) BETWEEN :fromDate AND :toDate " +
                        "AND bd.counter_id IS NOT NULL " +
                        "AND ( :counterId IS NULL OR bd.counter_id = :counterId ) " +
                        "AND ( :hallid    IS NULL OR bd.hall_details_hall_id = :hallid ) " +
                        "AND bd.booking_status <> 'PENDING' ", nativeQuery = true)
        List<Object[]> getBookingDetailsForAllHalls(
                        @Param("fromDate") String fromDate,
                        @Param("toDate") String toDate,
                        @Param("counterId") String counterId,
                        @Param("hallid") Integer hallid);

        @Query(value = "  SELECT bd.booking_id, h.hall_name, bd.booking_date, bd.start_date, " +
                        "bd.end_date, bd.starting_slot, bd.ending_slot, bd.no_of_days, bd.reason, " +
                        "bd.amount_received, bd.hall_amount, bd.deposit_amount, bd.gst, bd.booking_status, " +
                        "bd.booking_type, bd.counter_id FROM booking_details bd " +
                        "JOIN hall_details h ON h.hall_id = bd.hall_details_hall_id " +
                        "WHERE DATE(bd.booking_date) BETWEEN :fromDate AND :toDate " +
                        "AND bd.counter_id IS NOT NULL " +
                        "AND bd.booking_status <> 'PENDING' GROUP BY bd.counter_id, bd.booking_id ", nativeQuery = true)
        List<Object[]> getCounterWiseBookingDetails(String fromDate, String toDate);

        @Query(value = " SELECT hd.zone, " +
                        "    COUNT(DISTINCT hd.hall_id) AS hall_count, " +
                        "    COUNT(bd.booking_id) AS total_count, " +
                        "    SUM(CASE " +
                        "            WHEN bd.booking_type = 'ONLINE' AND bd.counter_id IS NULL THEN 1 " +
                        "            ELSE 0 " +
                        "        END) AS online_count, " +
                        "    SUM(CASE " +
                        "            WHEN bd.booking_type = 'ONLINE' AND bd.counter_id IS NOT NULL THEN 1 " +
                        "            ELSE 0 " +
                        "        END) AS counter_count " +
                        "FROM hall_details hd " +
                        "LEFT JOIN booking_details bd " +
                        "    ON hd.hall_id = bd.hall_details_hall_id " +
                        "    AND DATE(bd.booking_date) BETWEEN :fromDate AND :toDate " +
                        "    AND bd.booking_status NOT IN ('PENDING','GCC_UNBLOCKED','REFUNDED','GCC_BLOCKED') " +
                        "    AND bd.booking_type IS NOT NULL " +
                        "GROUP BY hd.zone " +
                        "ORDER BY hd.zone", nativeQuery = true)
        List<Object> getZoneWiseBookingList(String fromDate, String toDate);

        @Query(value = " select hd.hall_id, hd.hall_name,  " +
                        "COUNT(bd.booking_id) AS total_count, " +
                        "    SUM(CASE " +
                        "            WHEN bd.booking_type = 'ONLINE' AND bd.counter_id IS NULL THEN 1 " +
                        "            ELSE 0 " +
                        "        END) AS online_count, " +
                        "    SUM(CASE " +
                        "            WHEN bd.booking_type = 'ONLINE' AND bd.counter_id IS NOT NULL THEN 1 " +
                        "            ELSE 0 " +
                        "        END) AS counter_count, hd.zone from hall_details hd " +
                        "left join booking_details bd on hd.hall_id = bd.hall_details_hall_id " +
                        "where DATE(bd.booking_date) BETWEEN :fromDate AND :toDate  and " +
                        "bd.booking_status not in ('PENDING','GCC_UNBLOCKED','REFUNDED','GCC_BLOCKED') and bd.booking_type is not null "
                        +
                        "group by hd.hall_name, hd.hall_id ", nativeQuery = true)
        List<Object> getHallWiseBookingList(String fromDate, String toDate);

        @Query(value = " SELECT " +
                        "    bd.counter_id, bd.hall_details_hall_id, " +
                        "    COUNT(bd.booking_id) AS booking_count " +
                        "FROM booking_details bd " +
                        "WHERE DATE(bd.booking_date) BETWEEN :fromDate AND :toDate " +
                        "  AND bd.booking_status NOT IN ('PENDING','GCC_UNBLOCKED','REFUNDED','GCC_BLOCKED') " +
                        "  AND bd.booking_type IS NOT NULL " +
                        "  AND bd.counter_id IS NOT NULL " +
                        "GROUP BY bd.counter_id, bd.hall_details_hall_id ", nativeQuery = true)
        List<Object> getCounterWiseBookingList(String fromDate, String toDate);

        @Query(value = "SELECT  "
                        + " u.user_id as userId,"
                        + " u.name as UserName,"
                        + " u.mobile_no as UserMobile,"
                        + " u. usertype as UserType,"
                        + " CONCAT("
                        + "   IFNULL(u.doorno,''), ', ', "
                        + "   IFNULL(u.street,''), ', ', "
                        + "   IFNULL(u.area,''), ', ', "
                        + "   IFNULL(u.location,''), ' - ', "
                        + "   IFNULL(u.pin_code,'') "
                        + " ) AS UserAddress, "
                        + "hd.hall_name as hallName,"
                        + " CONCAT("
                        + "   IFNULL(hd.door_no,''), ', ', "
                        + "   IFNULL(hd.street,''), ', ', "
                        + "   IFNULL(hd.area,''), ', ', "
                        + "   IFNULL(hd.city,''), ' - ', "
                        + "   IFNULL(hd.pin_code,'') "
                        + " ) AS HallAddress, "
                        + " hd.hall_incharge_name as CareTakerName,"
                        + " hd.hall_incharge_no as CareTakerMobile,"
                        + " bd.booking_id as bookingId,"
                        + " bd.booking_date as bookingDate,"
                        + " bd.start_date as start_date,"
                        + " bd.end_date as end_date,"
                        + "bd.deposit_amount as depositAmount,"
                        + "bd.gst as gst,"
                        + "bd.hall_amount as hallAmount,"
                        + " bd.amount_received as totalAmount "
                        + "FROM booking_details bd "
                        + "INNER JOIN hall_request_details hrd "
                        + "ON hrd.booking_id = bd.booking_id "
                        + "LEFT JOIN hall_details hd ON hd.hall_id=bd.hall_details_hall_id "
                        + "LEFT JOIN users u ON u.user_id=bd.users_user_id "
                        + "WHERE bd.booking_id = :bookingId "
                        + "AND hrd.booking_status = 'APPROVED'", nativeQuery = true)
        List<Map<String, Object>> findApprovedBookingWithHallRequest(@Param("bookingId") int bookingId);
}
