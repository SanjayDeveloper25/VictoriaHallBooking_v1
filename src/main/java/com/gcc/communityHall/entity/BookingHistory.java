package com.gcc.communityHall.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class BookingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int HistoryId;
    private Date cdate;

    public enum BookingStatus {
        PENDING, APPROVED, REJECTED, CANCELED, PAYMENT_FAILURE,GCC_BLOCKED,COMPLETED ,WAITING_FOR_NOC , WAITING_FOR_NOC_APPROVAL
    }
    @Column(name="booking_status")
    @Enumerated(EnumType.STRING)
    private BookingDetails.BookingStatus bookingStatus;
    
    public enum RefundStatus {
    	APPROVED, WAITING, INITIATED, REJECTED, COMPLETED, NA
    }
    @Column(name="refund_status")
    @Enumerated(EnumType.STRING)
    private BookingDetails.RefundStatus refundStatus;

    private String comments;
    private  String updatedBy;
    @Column(name="start_date")
    private String startDate;
    @Column(name="end_date")
    private String endDate;

    /*@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private BookingDetails bookingDetails;*/

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private BookingDetails bookingDetails;

    public int getHistoryId() {
        return HistoryId;
    }

    public void setHistoryId(int historyId) {
        HistoryId = historyId;
    }

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    public BookingDetails.BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingDetails.BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public BookingDetails getBookingDetails() {
        return bookingDetails;
    }

    public void setBookingDetails(BookingDetails bookingDetails) {
        this.bookingDetails = bookingDetails;
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

	public BookingDetails.RefundStatus getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(BookingDetails.RefundStatus refundStatus) {
		this.refundStatus = refundStatus;
	}   
}
