package com.gcc.communityHall.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class erpPostData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tx_id;
    private int booking_det_booking_id;
    private boolean status;
    private LocalDate cdate;
    private int pay_status;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTx_id() {
        return tx_id;
    }

    public void setTx_id(String tx_id) {
        this.tx_id = tx_id;
    }

    public int getBooking_det_booking_id() {
        return booking_det_booking_id;
    }

    public void setBooking_det_booking_id(int booking_det_booking_id) {
        this.booking_det_booking_id = booking_det_booking_id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDate getCdate() {
        return cdate;
    }

    public void setCdate(LocalDate cdate) {
        this.cdate = cdate;
    }

    public int getPay_status() {
        return pay_status;
    }

    public void setPay_status(int pay_status) {
        this.pay_status = pay_status;
    }

    @Override
    public String toString() {
        return "erpPostData{" +
                "id=" + id +
                ", tx_id='" + tx_id + '\'' +
                ", booking_det_booking_id=" + booking_det_booking_id +
                ", status=" + status +
                ", cdate=" + cdate +
                ", pay_status=" + pay_status +
                '}';
    }
}
