package com.gcc.communityHall.service.Impl;

import com.gcc.communityHall.entity.RefundTransaction;
import com.gcc.communityHall.repository.RefundTransactionRepository;
import com.gcc.communityHall.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private RefundTransactionRepository refundTransactionRepository;

    @Override
    public RefundTransaction getRefundTrans(int bookingId) {
        return refundTransactionRepository.findByBookingId(bookingId);
    }

    @Override
    public RefundTransaction saveRefundTransaction(RefundTransaction refundTransaction) {
        return refundTransactionRepository.save(refundTransaction);
    }

    @Transactional
    @Override
    public void saveRefundTransactionDetails(RefundTransaction refundTransaction) {
        refundTransactionRepository.insertRefundTransaction(
                refundTransaction.getTransactionId(),
                refundTransaction.getAmountRefunded(),
                refundTransaction.getCdate(),
                refundTransaction.getRefundStatus(),
                refundTransaction.getBookingDetails().getBookingId(),
                refundTransaction.getRefundIdentifier()
        );
//        return refundTransaction; // returning the same object after manual insert
    }

}
