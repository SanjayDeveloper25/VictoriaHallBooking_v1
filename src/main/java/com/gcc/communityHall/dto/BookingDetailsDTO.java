package com.gcc.communityHall.dto;

import java.time.LocalDate;

public class BookingDetailsDTO {

    private int bookingId;
    private double noOfDays;
    private String startDate;
    private String endDate;
    private int startingSlot;
    private int endingSlot;
    private LocalDate bookingDate;
    private String bookingStatus;
    private double depositAmount;
    private double hallAmount;
    private double amountReceived;
    private double gst;
    private String approvedBy;
    private String refundStatus;
    private String updatedDate;
    private String reason;
    private UsersDTO users;
	public int getBookingId() {
		return bookingId;
	}
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}
	public double getNoOfDays() {
		return noOfDays;
	}
	public void setNoOfDays(double noOfDays) {
		this.noOfDays = noOfDays;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public int getStartingSlot() {
		return startingSlot;
	}
	public void setStartingSlot(int startingSlot) {
		this.startingSlot = startingSlot;
	}
	public int getEndingSlot() {
		return endingSlot;
	}
	public void setEndingSlot(int endingSlot) {
		this.endingSlot = endingSlot;
	}
	public LocalDate getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(LocalDate bookingDate) {
		this.bookingDate = bookingDate;
	}
	public String getBookingStatus() {
		return bookingStatus;
	}
	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
	public double getDepositAmount() {
		return depositAmount;
	}
	public void setDepositAmount(double depositAmount) {
		this.depositAmount = depositAmount;
	}
	public double getHallAmount() {
		return hallAmount;
	}
	public void setHallAmount(double hallAmount) {
		this.hallAmount = hallAmount;
	}
	public double getAmountReceived() {
		return amountReceived;
	}
	public void setAmountReceived(double amountReceived) {
		this.amountReceived = amountReceived;
	}
	public double getGst() {
		return gst;
	}
	public void setGst(double gst) {
		this.gst = gst;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public String getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public UsersDTO getUsers() {
		return users;
	}
	public void setUsers(UsersDTO users) {
		this.users = users;
	}       
    
}
