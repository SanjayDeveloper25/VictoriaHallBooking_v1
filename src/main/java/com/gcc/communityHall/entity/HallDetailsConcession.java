package com.gcc.communityHall.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class HallDetailsConcession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hallDetailsconsession_id;
    private double fullDayCleaningcharges;
    private double fulldayPAsystem;
    private double fulldayEBcharges;
    private double fulldayhallamount;
    private double halfDayCleaningcharges;
    private double halfdayPAsystem;
    private double halfdayEBcharges;
    private double halfdayhallamount;
	private double fullDayDepositAmount;
	private double halfDayDepositAmount;
    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JsonIgnore
    private HallDetails hallDetails;
	public int getHallDetailsconsession_id() {
		return hallDetailsconsession_id;
	}
	public void setHallDetailsconsession_id(int hallDetailsconsession_id) {
		this.hallDetailsconsession_id = hallDetailsconsession_id;
	}
	public double getFullDayCleaningcharges() {
		return fullDayCleaningcharges;
	}
	public void setFullDayCleaningcharges(double fullDayCleaningcharges) {
		this.fullDayCleaningcharges = fullDayCleaningcharges;
	}
	public double getFulldayPAsystem() {
		return fulldayPAsystem;
	}
	public void setFulldayPAsystem(double fulldayPAsystem) {
		this.fulldayPAsystem = fulldayPAsystem;
	}
	public double getFulldayEBcharges() {
		return fulldayEBcharges;
	}
	public void setFulldayEBcharges(double fulldayEBcharges) {
		this.fulldayEBcharges = fulldayEBcharges;
	}
	public double getFulldayhallamount() {
		return fulldayhallamount;
	}
	public void setFulldayhallamount(double fulldayhallamount) {
		this.fulldayhallamount = fulldayhallamount;
	}
	public double getHalfDayCleaningcharges() {
		return halfDayCleaningcharges;
	}
	public void setHalfDayCleaningcharges(double halfDayCleaningcharges) {
		this.halfDayCleaningcharges = halfDayCleaningcharges;
	}
	public double getHalfdayPAsystem() {
		return halfdayPAsystem;
	}
	public void setHalfdayPAsystem(double halfdayPAsystem) {
		this.halfdayPAsystem = halfdayPAsystem;
	}
	public double getHalfdayEBcharges() {
		return halfdayEBcharges;
	}
	public void setHalfdayEBcharges(double halfdayEBcharges) {
		this.halfdayEBcharges = halfdayEBcharges;
	}
	public double getHalfdayhallamount() {
		return halfdayhallamount;
	}
	public void setHalfdayhallamount(double halfdayhallamount) {
		this.halfdayhallamount = halfdayhallamount;
	}

	public double getFullDayDeposit() {
		return fullDayDepositAmount;
	}
	public void setFullDayDeposit(double fullDayDepositAmount) {
		this.fullDayDepositAmount = fullDayDepositAmount;
	}
	public double getHalfDayDeposit() {
		return halfDayDepositAmount;
	}
	public void setHalfDayDeposit(double halfDayDepositAmount) {
		this.halfDayDepositAmount = halfDayDepositAmount;
	}

	public HallDetails getHallDetails() {
		return hallDetails;
	}
	public void setHallDetails(HallDetails hallDetails) {
		this.hallDetails = hallDetails;
	}
}