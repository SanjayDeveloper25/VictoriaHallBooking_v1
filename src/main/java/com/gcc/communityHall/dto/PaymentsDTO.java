package com.gcc.communityHall.dto;

import java.util.Date;

public class PaymentsDTO {

    private int paymentId;
    private double amountReceived;
    private String transactionId;
    private String paymentType;
    private String paymentStatus;
    private Date cdate;
    private BookingDetailsDTO bookingDetails;
	public int getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}
	public double getAmountReceived() {
		return amountReceived;
	}
	public void setAmountReceived(double amountReceived) {
		this.amountReceived = amountReceived;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public Date getCdate() {
		return cdate;
	}
	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}
	public BookingDetailsDTO getBookingDetails() {
		return bookingDetails;
	}
	public void setBookingDetails(BookingDetailsDTO bookingDetails) {
		this.bookingDetails = bookingDetails;
	}

}
