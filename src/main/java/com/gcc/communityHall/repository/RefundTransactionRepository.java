package com.gcc.communityHall.repository;


import com.gcc.communityHall.entity.RefundTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RefundTransactionRepository extends JpaRepository<RefundTransaction,Integer> {


    @Query(value = "select * from refund_transaction where booking_details_booking_id = ?",nativeQuery = true)
     RefundTransaction findByBookingId(int bookingId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO refund_transaction (transaction_id, amount_refunded, cdate, refund_status, booking_details_booking_id, refund_identifier) " +
            "VALUES (?, ?, ?, ?, ?, ?)", nativeQuery = true)
    void insertRefundTransaction(String transactionId,
                                 Double amountRefunded,
                                 java.util.Date cdate,
                                 String refundStatus,
                                 Integer bookingId,
                                 String refundIdentifier);








}
