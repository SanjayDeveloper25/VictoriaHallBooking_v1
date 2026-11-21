package com.gcc.communityHall.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Damage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int damageId;

    private LocalDate cdate;
    @Column(name = "asset_name")
    private String assetName;
    private String comments;
    private String damageImageUrl;
    private int quantity;
    private double assetAmount;
    private String updatedBy;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private HallDetails hallDetails;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private BookingDetails bookingDetails;





	public Damage(int damageId, LocalDate cdate, String assetName, String comments, String damageImageUrl, int quantity,
			double assetAmount, String updatedBy, HallDetails hallDetails, BookingDetails bookingDetails) {
		super();
		this.damageId = damageId;
		this.cdate = cdate;
		this.assetName = assetName;
		this.comments = comments;
		this.damageImageUrl = damageImageUrl;
		this.quantity = quantity;
		this.assetAmount = assetAmount;
		this.updatedBy = updatedBy;
		this.hallDetails = hallDetails;
		this.bookingDetails = bookingDetails;
	}
	public Damage() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getDamageId() {
		return damageId;
	}
	public void setDamageId(int damageId) {
		this.damageId = damageId;
	}
	public LocalDate getCdate() {
		return cdate;
	}
	public void setCdate(LocalDate cdate) {
		this.cdate = cdate;
	}
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getDamageImageUrl() {
		return damageImageUrl;
	}
	public void setDamageImageUrl(String damageImageUrl) {
		this.damageImageUrl = damageImageUrl;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getAssetAmount() {
		return assetAmount;
	}
	public void setAssetAmount(double assetAmount) {
		this.assetAmount = assetAmount;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public HallDetails getHallDetails() {
		return hallDetails;
	}
	public void setHallDetails(HallDetails hallDetails) {
		this.hallDetails = hallDetails;
	}
	public BookingDetails getBookingDetails() {
		return bookingDetails;
	}
	public void setBookingDetails(BookingDetails bookingDetails) {
		this.bookingDetails = bookingDetails;
	}
}
