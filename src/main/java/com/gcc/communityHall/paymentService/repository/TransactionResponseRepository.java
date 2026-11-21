package com.gcc.communityHall.paymentService.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.paymentService.entity.TransactionResponse;

@Repository
public interface TransactionResponseRepository extends JpaRepository<TransactionResponse, Integer> {

	Optional<TransactionResponse> findByBookingId(int bookingId);

	List<TransactionResponse> findByuserId(String userId);

	Optional<TransactionResponse> findByTxansactionId(String txansactionId);

	Optional<TransactionResponse> findByTxansactionIdAndStatusCode(String txansactionId, String statusCode);

	Optional<TransactionResponse> findByBookingIdAndStatusCode(int bookingId, String statusCode);

	Optional<TransactionResponse> findByBookingIdAndTxansactionIdAndStatusCode(int bookingId, String txansactionId,
			String statusCode);

	@Query(value = "SELECT tr.booking_id, tr.hall_id, tr.amount, tr.status_code, tr.txansaction_id, tr.user_id " +
	// ", p.payment_status " +
			"FROM transaction_response tr " +
			"JOIN hall_details hd ON tr.hall_id = hd.hall_id " +
			// "LEFT JOIN payments p ON tr.booking_id = p.booking_details_booking_id " +
			"LEFT JOIN booking_details bd ON tr.booking_id = bd.booking_id " +
			"WHERE DATE(STR_TO_DATE(tr.cdate, '%Y-%m-%d')) >= STR_TO_DATE(:fromDate, '%Y-%m-%d') " +
			// "WHERE DATE(STR_TO_DATE(tr.cdate, '%d-%m-%Y %H:%i:%s')) >=
			// STR_TO_DATE(:fromDate, '%Y-%m-%d') " +
			"AND DATE(STR_TO_DATE(tr.cdate, '%Y-%m-%d')) <= STR_TO_DATE(:toDate, '%Y-%m-%d') " +
			"AND (:bookingId IS NULL OR tr.booking_id = :bookingId) " +
			"AND (:hallId IS NULL OR tr.hall_id = :hallId)", nativeQuery = true)
	List<Object[]> getTransactionResponseByBookingId(@Param("fromDate") String fromDate,
			@Param("toDate") String toDate,
			@Param("bookingId") Long bookingId,
			@Param("hallId") Long hallId);

	// @Transactional
	// @Query (value = "UPDATE transaction_response SET amount = :amount, booking_id
	// =:booking_id , date = :date, hall_id = :hall_id, response = :response,
	// status_code = :status_code, tpsl_txn_id = :tpsl_txn_id, txansaction_id =
	// :txansaction_id, user_id = :user_id WHERE (id = :id)",nativeQuery = true)
	// void updateTransactionResponse ( @Param("id") Integer id, @Param("amount")
	// String amount, @Param("booking_id") int booking_id ,@Param("date" ) String
	// date,@Param("hall_id" ) int hall_id ,@Param("response")String
	// response,@Param("status_code")String status_code,@Param("tpsl_txn_id")String
	// tpsl_txn_id,@Param("txansaction_id")String
	// txansaction_id,@Param("user_id")String user_id );

	// TransactionResponse saveTransactionResponse(Integer id, String amount, int
	// bookingId, String date, int hallId, String response,
	// String statusCode, String tpslTxnId, String txansactionId, String userId);

	// @Query(value = "INSERT INTO transaction_response
	// ('id','amount','booking_id','date','hall_id','response','status_code','tpsl_txn_id','txansaction_id','user_id')
	// VALUES (:id, :amount, :booking_id, :date, :hall_id, :response, :status_code,
	// :tpsl_txn_id, :txansaction_id, :user_id)", nativeQuery = true)
	// void saveTransactionResponse(@Param("id") int id, @Param("amount") String
	// amount, @Param("booking_id") int booking_id ,@Param("date" ) String
	// date,@Param("hall_id" ) int hall_id ,@Param("response")String
	// response,@Param("status_code")String status_code,@Param("tpsl_txn_id")String
	// tpsl_txn_id,@Param("txansaction_id")String
	// txansaction_id,@Param("user_id")String user_id );
	//
	// @Query (value = "UPDATE transaction_response SET 'amount' = :amount,
	// 'booking_id' =:booking_id , 'date' = :date, 'hall_id' = :hall_id, 'response'
	// = :response, 'status_code' = :status_code, 'tpsl_txn_id' = :tpsl_txn_id,
	// 'txansaction_id' = :txansaction_id, 'user_id' = :user_id WHERE ('id' = :id)",
	// nativeQuery = true)

	@Query(value = "SELECT tr.id, tr.amount, tr.booking_id, tr.hall_id, tr.response, tr.status_code, " +
			"tr.tpsl_txn_id, tr.txansaction_id, tr.user_id, bd.booking_date " +
			"FROM transaction_response tr " +
			"INNER JOIN booking_details bd ON tr.booking_id = bd.booking_id " +
			"WHERE tr.status_code = ? AND bd.booking_date < DATE_SUB(CURDATE(), INTERVAL 2 DAY", nativeQuery = true)
	List<Map<String, Object>> getTransactionResponseByStatus(String status);

	@Query(value = "SELECT tr.id, tr.amount, tr.booking_id, tr.hall_id, tr.response, tr.tpsl_txn_id, tr.txansaction_id, tr.cdate, "
			+
			"tr.user_id, bd.booking_date FROM transaction_response tr " +
			"INNER JOIN booking_details bd ON tr.booking_id = bd.booking_id " +
			"WHERE tr.status_code = 'temp' AND tr.cdate < DATE_SUB(NOW(), INTERVAL 1 HOUR);", nativeQuery = true)
	List<Map<String, Object>> getTransResBycdateandStatus(String status);

	boolean existsByBookingId(int bookingId);
}
