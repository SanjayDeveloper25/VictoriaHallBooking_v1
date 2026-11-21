package com.gcc.communityHall.paymentService.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity

public class TransactionStatusLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String transactionId;
	private String bookingId;
	private String userId;
	private String status;
	private String errorMessage;
	private String statusMessage;
	private String statusCode;
	private String refundIdentifier;
	@Column(columnDefinition = "TEXT")
	private String response;
	private String amount;
	private String balanceAmt;
	private String receivedDate;

	@Column(name = "cdate",nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime cdate = LocalDateTime.now();

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public Integer getId() {
		return id;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getBookingId() {
		return bookingId;
	}

	public String getUserId() {
		return userId;
	}

	public String getStatus() {
		return status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setStatus(String status) {
		this.status = status;

	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getRefundIdentifier() {
		return refundIdentifier;
	}

	public void setRefundIdentifier(String refundIdentifier) {
		this.refundIdentifier = refundIdentifier;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public LocalDateTime getCdate() {
		return cdate;
	}

	public void setCdate(LocalDateTime cdate) {
		this.cdate = cdate;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBalanceAmt() {
		return balanceAmt;
	}

	public void setBalanceAmt(String balanceAmt) {
		this.balanceAmt = balanceAmt;
	}

	public String getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}

	@Override
	public String toString() {
		return "TransactionStatusLog{" +
				"id=" + id +
				", transactionId='" + transactionId + '\'' +
				", bookingId='" + bookingId + '\'' +
				", userId='" + userId + '\'' +
				", status='" + status + '\'' +
				", errorMessage='" + errorMessage + '\'' +
				", statusMessage='" + statusMessage + '\'' +
				", statusCode='" + statusCode + '\'' +
				", refundIdentifier='" + refundIdentifier + '\'' +
				", cdate='" + cdate + '\'' +
				'}';
	}
}
