package com.gcc.communityHall.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class SmsSender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "phone",nullable = false)
    private Long phone;

    @Column(name = "purpose",nullable = false)
    private String purpose;
    @Column(name = "bodyMsg",nullable = false)
    private String bodyMsg;
    @Column(name = "cdate",nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date cdate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getBodyMsg() {
        return bodyMsg;
    }

    public void setBodyMsg(String bodyMsg) {
        this.bodyMsg = bodyMsg;
    }

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }
}
