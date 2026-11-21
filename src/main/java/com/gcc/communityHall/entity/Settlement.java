package com.gcc.communityHall.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;

@Entity
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int settlementId;
    private String comments;
    private LocalDate cdate;
    private double damageAmount;
    private double ebAmount;
    private double depositAmount;
    private double refundedAmount;
    private Date updatedDate;
    private String updatedId;
	@Column(name = "challon_url")
	private String challon;
	private String dddate;
	private long ddnumber;
	private double collectionPending;
	private String collectionStatus;
	private long challonnumber;
	private long challonamount;
	private long ddamount;
	//ak
	@Column(name = "approved_flag")
private Integer approvedFlag = 0;

@Column(name = "approved_flag_date")
private Date approvedFlagDate;

	
	

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JsonIgnore
    private BookingDetails bookingDetails;

	public int getSettlementId() {
		return settlementId;
	}
	public void setSettlementId(int settlementId) {
		this.settlementId = settlementId;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public LocalDate getCdate() {
		return cdate;
	}
	public void setCdate(LocalDate cdate) {
		this.cdate = cdate;
	}
	public double getDamageAmount() {
		return damageAmount;
	}
	public void setDamageAmount(double damageAmount) {
		this.damageAmount = damageAmount;
	}
	public double getEbAmount() {
		return ebAmount;
	}
	public void setEbAmount(double ebAmount) {
		this.ebAmount = ebAmount;
	}
	public double getDepositAmount() {
		return depositAmount;
	}
	public void setDepositAmount(double depositAmount) {
		this.depositAmount = depositAmount;
	}
	public double getRefundedAmount() {
		return refundedAmount;
	}
	public void setRefundedAmount(double refundedAmount) {
		this.refundedAmount = refundedAmount;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getUpdatedId() {
		return updatedId;
	}
	public void setUpdatedId(String updatedId) {
		this.updatedId = updatedId;
	}
	public BookingDetails getBookingDetails() {
		return bookingDetails;
	}
	public void setBookingDetails(BookingDetails bookingDetails) {
		this.bookingDetails = bookingDetails;
	}
	public String getChallon() {
		return challon;
	}
	public void setChallon(String challon) {
		this.challon = challon;
	}
	public String getDddate() {
		return dddate;
	}
	public void setDddate(String dddate) {
		this.dddate = dddate;
	}
	public long getDdnumber() {
		return ddnumber;
	}
	public void setDdnumber(long ddnumber) {
		this.ddnumber = ddnumber;
	}
	
	public double getCollectionPending() {
		return collectionPending;
	}
	public void setCollectionPending(double collectionPending) {
		this.collectionPending = collectionPending;
	}
	
	public long getChallonnumber() {
		return challonnumber;
	}
	public void setChallonnumber(long challonnumber) {
		this.challonnumber = challonnumber;
	}
	public long getDdamount() {
		return ddamount;
	}
	public void setDdamount(long ddamount) {
		this.ddamount = ddamount;
	}
	
	public long getChallonamount() {
		return challonamount;
	}
	public void setChallonamount(long challonamount) {
		this.challonamount = challonamount;
	}	
	public String getCollectionStatus() {
		return collectionStatus;
	}
	
	public void setCollectionStatus(String collectionStatus) {
		this.collectionStatus = collectionStatus;
	}
	// Getter for approvedFlag
public Integer getApprovedFlag() {
    return approvedFlag;
}

// Setter for approvedFlag
public void setApprovedFlag(Integer approvedFlag) {
    this.approvedFlag = approvedFlag;
}

// Getter for approvedFlagDate
public Date getApprovedFlagDate() {
    return approvedFlagDate;
}

// Setter for approvedFlagDate
public void setApprovedFlagDate(Date approvedFlagDate) {
    this.approvedFlagDate = approvedFlagDate;
}

	public Settlement(int settlementId, String comments, LocalDate cdate, double damageAmount, double ebAmount,
			double depositAmount, double refundedAmount, Date updatedDate, String updatedId, String challon,
			String dddate, long ddnumber, BookingDetails bookingDetails,double collectionPending, long ddamount, long challonnumber, String collectionStatus) {
		super();
		this.settlementId = settlementId;
		this.comments = comments;
		this.cdate = cdate;
		this.damageAmount = damageAmount;
		this.ebAmount = ebAmount;
		this.depositAmount = depositAmount;
		this.refundedAmount = refundedAmount;
		this.updatedDate = updatedDate;
		this.updatedId = updatedId;
		this.challon = challon;
		this.dddate = dddate;
		this.ddnumber = ddnumber;
		this.bookingDetails = bookingDetails;
		this.collectionPending=collectionPending;
		this.ddamount=ddamount;
		this.challonnumber=challonnumber;
		this.collectionStatus=collectionStatus;
	}
	public Settlement() {
		super();
	}

}
