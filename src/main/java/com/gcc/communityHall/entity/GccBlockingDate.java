package com.gcc.communityHall.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class GccBlockingDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blocking_id")
    private int blockingId;

   public enum BlockedStatus {
        BLOCKED, UNBLOCKED
    }
   private String bookingIds;
   
    private Date startDate;
    private Date endDate;
    private Date cdate;
    private String updatedBy;
    private Date updatedDate;
    private String hallId;
    private String blockingReason;
    @Enumerated(EnumType.STRING)
    private BlockedStatus blockedStatus=BlockedStatus.BLOCKED;




   public GccBlockingDate() {
      super();
      // TODO Auto-generated constructor stub
   }
   
   public GccBlockingDate(int blockingId, Date startDate, Date endDate, Date cdate,
         String updatedBy, String hallId, String blockingReason, BlockedStatus blockedStatus,String bookingIds) {
      super();
      this.blockingId = blockingId;
      this.startDate = startDate;
      this.endDate = endDate;
      this.cdate = cdate;
      this.updatedBy = updatedBy;
      this.hallId = hallId;
      this.blockingReason = blockingReason;
      this.blockedStatus = blockedStatus;
      this.bookingIds = bookingIds;
   }

   public int getBlockingId() {
      return blockingId;
   }
   public void setBlockingId(int blockingId) {
      this.blockingId = blockingId;
   }
   public Date getStartDate() {
      return startDate;
   }
   public void setStartDate(Date startDate) {
      this.startDate = startDate;
   }
   public Date getEndDate() {
      return endDate;
   }
   public void setEndDate(Date endDate) {
      this.endDate = endDate;
   }

   public Date getCdate() {
      return cdate;
   }
   public void setCdate(Date cdate) {
      this.cdate = cdate;
   }
   public String getUpdatedBy() {
      return updatedBy;
   }
   public void setUpdatedBy(String updatedBy) {
      this.updatedBy = updatedBy;
   }
   public String getHallId() {
      return hallId;
   }
   public void setHallId(String hallId) {
      this.hallId = hallId;
   }
   public String getBlockingReason() {
      return blockingReason;
   }
   public void setBlockingReason(String blockingReason) {
      this.blockingReason = blockingReason;
   }
   public BlockedStatus getBlockedStatus() {
      return blockedStatus;
   }
   public void setBlockedStatus(BlockedStatus blockedStatus) {
      this.blockedStatus = blockedStatus;
   }


   public String getBookingIds() {
	return bookingIds;
}

public void setBookingIds(String bookingIds) {
	this.bookingIds = bookingIds;
}
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}