package com.gcc.communityHall.paymentService.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.gcc.communityHall.paymentService.entity.RefundResponse;
import com.gcc.communityHall.paymentService.entity.TransactionResponse;




public interface PaymentGatewayService {

	public String getToken(String merchantId, String txnId, String totalAmt, String accountNo, String consumerId,
			String consumerMobileNo, String consumerEmailId, String debitStartDate, String debitEndDate,
			String maxAmount, String amountType, String frequency, String cardNumber, String expMonth, String expYear,
			String cvvCode, String salt

	) throws NoSuchAlgorithmException;

	public String generateToken(String token)throws NoSuchAlgorithmException;
	public String encryptHxRefund(String text) throws Exception;
	public String ToHexStrRefund(byte[] dataBuf);
	public String decryptHxTRefund(String hxstrr) throws Exception;
	public byte[] FromHexStr (String hxStr);

	public TransactionResponse recordResponse(TransactionResponse transactionResponse);
	public RefundResponse recordRefund(RefundResponse refundReponse);

	public TransactionResponse getTransactionResponseByBookingId(int bookingId);
	public TransactionResponse getTransactionResponseByTxansactionId(String txansactionId,String statusCode);
	public TransactionResponse getTransactionResponseByBookingIdAndStatusCode(int bookingId,String statusCode);

	public String refundTheAmount(int bookingId,int userId) throws Exception;

	public String refundTheDepositAmount(int bookingId,int userId,double amount) throws Exception;

	String manualrefundTheAmount(int bookingId, int userId, String txansactionId, String amount) throws Exception;

	TransactionResponse getTransactionResponseByBookingIdAndTransactionIdStatusCode(int bookingId, String txansactionId,
			String statusCode);

	List<TransactionResponse> getTransactionResponseByuserId(String userId);


}
