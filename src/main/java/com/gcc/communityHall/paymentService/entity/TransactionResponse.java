package com.gcc.communityHall.paymentService.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TransactionResponse {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String userId;
	private int bookingId;
	private int hallId;

	private String txansactionId;
	private String statusCode;
	private String tpslTxnId;
	private String date;

	@Column(length = 5000)
	private String response;
	private String Amount;

	@Column(name = "cdate",nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime cdate = LocalDateTime.now();

	@Column(name = "scheduler_update", nullable = false, columnDefinition = "INT DEFAULT 0")
	private Integer schedulerUpdate = 0;

	public LocalDateTime getCdate() {
		return cdate;
	}

	public void setCdate(LocalDateTime cdate) {
		this.cdate = cdate;
	}

	public TransactionResponse() {

	}

	public TransactionResponse(Integer id, String userId, int bookingId, int hallId, String txansactionId, String statusCode,
                               String tpslTxnId, String date, String response, String amount) {
		super();
		this.id = id;
		this.userId = userId;
		this.bookingId = bookingId;
		this.hallId=hallId;
		this.txansactionId = txansactionId;
		this.statusCode = statusCode;
		this.tpslTxnId = tpslTxnId;
		this.date = date;
		this.response = response;
		Amount = amount;
		this.schedulerUpdate = 0;
	}
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
	public String getTpslTxnId() {
		return tpslTxnId;
	}
	public void setTpslTxnId(String tpslTxnId) {
		this.tpslTxnId = tpslTxnId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
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

	public int getHallId() {
		return hallId;
	}

	public void setHallId(int hallId) {
		this.hallId = hallId;
	}

	public Integer getSchedulerUpdate() {
		return schedulerUpdate;
	}

	public void setSchedulerUpdate(Integer schedulerUpdate) {
		this.schedulerUpdate = schedulerUpdate;
	}

	@Override
	public String toString() {
		return "TransactionResponse [id=" + id + ", userId=" + userId + ", bookingId=" + bookingId + ", hallId="
				+ hallId + ", txansactionId=" + txansactionId + ", statusCode=" + statusCode + ", tpslTxnId="
				+ tpslTxnId + ", date=" + date + ", response=" + response + ", Amount=" + Amount 
				+ ", schedulerUpdate=" + schedulerUpdate + "]";
	}





}
