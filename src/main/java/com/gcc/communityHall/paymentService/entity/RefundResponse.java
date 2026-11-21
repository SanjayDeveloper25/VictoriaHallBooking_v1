package com.gcc.communityHall.paymentService.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class RefundResponse {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String userId;
	private int bookingId;
	private String txansactionId;
	private String statusCode;
//	private String tpslTxnId;
	@Column(name = "date")
	private LocalDate date;

	@Column(length = 5000)
	private String response;
	private String Amount;
	private String balaceAmount;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getBookingId() {
		return bookingId;
	}
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}
	public String getTxansactionId() {
		return txansactionId;
	}
	public void setTxansactionId(String txansactionId) {
		this.txansactionId = txansactionId;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getAmount() {
		return Amount;
	}
	public void setAmount(String amount) {
		Amount = amount;
	}
	public String getBalaceAmount() {
		return balaceAmount;
	}
	public void setBalaceAmount(String balaceAmount) {
		this.balaceAmount = balaceAmount;
	}

}
