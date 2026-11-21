package com.gcc.communityHall.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hall_request_details")
public class HallRequestDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "req_id")
    private Integer reqId;

    @Column(name = "booking_id")
    private Integer bookingId;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "starting_slot")
    private Integer startingSlot;

    @Column(name = "ending_slot")
    private Integer endingSlot;

    @Column(name = "reason")
    private String reason;

    @Column(name = "booking_status")
    private String bookingStatus;

    @Column(name = "hall_id")
    private Integer hallId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "org_type", length = 15)
    private String orgType;

    @Column(name = "event_description")
    private String eventDescription;

    @Column(name = "office_details")
    private String officeDetails;

    @Column(name = "bonafide_path")
    private String bonafidePath;

    @Column(name = "cdate", insertable = false, updatable = false)
    private LocalDateTime cdate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private BookingDetails bookingDetails;

    // @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // private Users users;

    // @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // private HallDetails hallDetails;

    // Getters and Setters

    public Integer getReqId() {
        return reqId;
    }

    public void setReqId(Integer reqId) {
        this.reqId = reqId;
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

    public Integer getStartingSlot() {
        return startingSlot;
    }

    public void setStartingSlot(Integer startingSlot) {
        this.startingSlot = startingSlot;
    }

    public Integer getEndingSlot() {
        return endingSlot;
    }

    public void setEndingSlot(Integer endingSlot) {
        this.endingSlot = endingSlot;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public Integer getHallId() {
        return hallId;
    }

    public void setHallId(Integer hallId) {
        this.hallId = hallId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getOfficeDetails() {
        return officeDetails;
    }

    public void setOfficeDetails(String officeDetails) {
        this.officeDetails = officeDetails;
    }

    public String getBonafidePath() {
        return bonafidePath;
    }

    public void setBonafidePath(String bonafidePath) {
        this.bonafidePath = bonafidePath;
    }

    public LocalDateTime getCdate() {
        return cdate;
    }

    // public Users getUsers() {
    //     return users;
    // }

    // public void setUsers(Users users) {
    //     this.users = users;
    // }

    // public HallDetails getHallDetails() {
    //     return hallDetails;
    // }

    // public void setHallDetails(HallDetails hallDetails) {
    //     this.hallDetails = hallDetails;
    // }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public BookingDetails getBookingDetails() {
        return bookingDetails;
    }

    public void setBookingDetails(BookingDetails bookingDetails) {
        this.bookingDetails = bookingDetails;
    }
}
