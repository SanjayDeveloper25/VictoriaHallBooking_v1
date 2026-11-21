package com.gcc.communityHall.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class QueueLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "queue_id")
    private int queue_id;

    private LocalDate startDate;
    private LocalDate endDate;
    private int starting_slot;
    private int ending_slot;
    private int user_id;
    private int hall_id;
    private String slotKey;

    @Column(name = "cdate",nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date cdate = new Date();

    public int getQueue_id() {
        return queue_id;
    }

    public void setQueue_id(int queue_id) {
        this.queue_id = queue_id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getStarting_slot() {
        return starting_slot;
    }

    public void setStarting_slot(int starting_slot) {
        this.starting_slot = starting_slot;
    }

    public int getEnding_slot() {
        return ending_slot;
    }

    public void setEnding_slot(int ending_slot) {
        this.ending_slot = ending_slot;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getHall_id() {
        return hall_id;
    }

    public void setHall_id(int hall_id) {
        this.hall_id = hall_id;
    }

    public String getSlotKey() {
        return slotKey;
    }

    public void setSlotKey(String slotKey) {
        this.slotKey = slotKey;
    }

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }
}
