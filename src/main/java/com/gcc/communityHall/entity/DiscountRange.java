package com.gcc.communityHall.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DiscountRange {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

	private int id;
	private String usertype;
	private float discountRange;
	private LocalDate cDate;
	private String updtedBy;
    private boolean is_active;
    private boolean is_delete;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public boolean isIs_active() {
		return is_active;
	}
	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}
	public boolean isIs_delete() {
		return is_delete;
	}
	public void setIs_delete(boolean is_delete) {
		this.is_delete = is_delete;
	}

	public float getDiscountRange() {
		return discountRange;
	}
	public void setDiscountRange(float discountRange) {
		this.discountRange = discountRange;
	}
	public LocalDate getcDate() {
		return cDate;
	}
	public void setcDate(LocalDate cDate) {
		this.cDate = cDate;
	}
	public String getUpdtedBy() {
		return updtedBy;
	}
	public void setUpdtedBy(String updtedBy) {
		this.updtedBy = updtedBy;
	}
	
	

}