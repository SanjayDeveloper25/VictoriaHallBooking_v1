package com.gcc.communityHall.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

@Entity
public class BookingCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate bdate;
    private boolean morningSlot;
    private boolean eveningSlot;
    private String bookingId_1;
    private String bookingId_2;
    private boolean is_active;
    private boolean is_delete;
    private int is_booked;
    private boolean is_blocked = false;


    private int hallId;

    @Column(name = "unique_id", nullable = false, unique = true)
    private String uniqueId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public LocalDate getBdate() {
		return bdate;
	}

	public void setBdate(LocalDate bdate) {
		this.bdate = bdate;
	}

	public boolean isMorningSlot() {
        return morningSlot;
    }

    public void setMorningSlot(boolean morningSlot) {
        this.morningSlot = morningSlot;
    }

    public boolean isEveningSlot() {
        return eveningSlot;
    }

    public void setEveningSlot(boolean eveningSlot) {
        this.eveningSlot = eveningSlot;
    }

    public String getBookingId_1() {
        return bookingId_1;
    }

    public void setBookingId_1(String bookingId_1) {
        this.bookingId_1 = bookingId_1;
    }

    public String getBookingId_2() {
        return bookingId_2;
    }

    public void setBookingId_2(String bookingId_2) {
        this.bookingId_2 = bookingId_2;
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

    public int getIs_booked() {
        return is_booked;
    }

    public void setIs_booked(int is_booked) {
        this.is_booked = is_booked;
    }

    public int getHallId() {
        return hallId;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    public boolean getIs_blocked() {
        return is_blocked;
    }

    public void setIs_blocked(boolean is_blocked) {
        this.is_blocked = is_blocked;
    }

    public String getUnique_id() {
        return uniqueId;
    }

    public void setUnique_id(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public BookingCalendar() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BookingCalendar(Integer id, LocalDate bdate, boolean morningSlot, boolean eveningSlot, String bookingId_1,
			String bookingId_2, boolean is_active, boolean is_delete, int is_booked, boolean is_blocked, int hallId, String uniqueId) {
		super();
		this.id = id;
		this.bdate = bdate;
		this.morningSlot = morningSlot;
		this.eveningSlot = eveningSlot;
		this.bookingId_1 = bookingId_1;
		this.bookingId_2 = bookingId_2;
		this.is_active = is_active;
		this.is_delete = is_delete;
		this.is_booked = is_booked;
        this.is_blocked = is_blocked;
		this.hallId = hallId;
        this.uniqueId = uniqueId;
	}



}
