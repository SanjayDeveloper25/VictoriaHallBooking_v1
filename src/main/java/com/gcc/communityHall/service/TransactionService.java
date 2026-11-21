package com.gcc.communityHall.service;

import com.gcc.communityHall.entity.RefundTransaction;

public interface TransactionService {

     RefundTransaction getRefundTrans(int bookingId);

     RefundTransaction saveRefundTransaction(RefundTransaction refundTransaction);

     void saveRefundTransactionDetails(RefundTransaction refundTrans);
}
