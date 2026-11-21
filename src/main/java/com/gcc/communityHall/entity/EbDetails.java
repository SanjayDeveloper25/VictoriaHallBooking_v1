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
public class EbDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ebId;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private BookingDetails bookingDetails;

    private double startingReading;
    private double endingReading;
    private float usedUnit;
    private double ebAmount;

    private Date cdate;
    private Date updatedDate;
    private String ebImageUrl;

    public int getEbId() {
        return ebId;
    }

    public void setEbId(int ebId) {
        this.ebId = ebId;
    }

    public BookingDetails getBookingDetails() {
        return bookingDetails;
    }

    public void setBookingDetails(BookingDetails bookingDetails) {
        this.bookingDetails = bookingDetails;
    }

    public double getStartingReading() {
        return startingReading;
    }

    public void setStartingReading(double startingReading) {
        this.startingReading = startingReading;
    }

    public double getEndingReading() {
        return endingReading;
    }

    public void setEndingReading(double endingReading) {
        this.endingReading = endingReading;
    }

    public float getUsedUnit() {
        return usedUnit;
    }

    public void setUsedUnit(float usedUnit) {
        this.usedUnit = usedUnit;
    }

    public double getEbAmount() {
        return ebAmount;
    }

    public void setEbAmount(double ebAmount) {
        this.ebAmount = ebAmount;
    }

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

	public String getEbImageUrl() {
		return ebImageUrl;
	}

	public void setEbImageUrl(String ebImageUrl) {
		this.ebImageUrl = ebImageUrl;
	}
}
