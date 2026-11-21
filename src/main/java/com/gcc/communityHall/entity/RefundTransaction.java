package com.gcc.communityHall.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class RefundTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int refundId;
    private double amountRefunded;
    private String TransactionId;
    private String refundStatus;
    private Date cdate;
    private String refundIdentifier;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private BookingDetails bookingDetails;
	public int getRefundId() {
		return refundId;
	}
	public void setRefundId(int refundId) {
		this.refundId = refundId;
	}
	public double getAmountRefunded() {
		return amountRefunded;
	}
	public void setAmountRefunded(double amountRefunded) {
		this.amountRefunded = amountRefunded;
	}
	public String getTransactionId() {
		return TransactionId;
	}
	public void setTransactionId(String transactionId) {
		TransactionId = transactionId;
	}
	public String getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	public Date getCdate() {
		return cdate;
	}
	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}
	public BookingDetails getBookingDetails() {
		return bookingDetails;
	}
	public void setBookingDetails(BookingDetails bookingDetails) {
		this.bookingDetails = bookingDetails;
	}
	public String getRefundIdentifier() {
		return refundIdentifier;
	}
	public void setRefundIdentifier(String refundIdentifier) {
		this.refundIdentifier = refundIdentifier;
	}

	@Override
	public String toString() {
		return "RefundTransaction{" +
				"refundId=" + refundId +
				", amountRefunded=" + amountRefunded +
				", TransactionId='" + TransactionId + '\'' +
				", refundStatus='" + refundStatus + '\'' +
				", cdate=" + cdate +
				", refundIdentifier='" + refundIdentifier + '\'' +
				", bookingDetails=" + bookingDetails +
				'}';
	}
}
