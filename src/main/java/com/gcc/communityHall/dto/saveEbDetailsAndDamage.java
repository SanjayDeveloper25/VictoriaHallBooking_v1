package com.gcc.communityHall.dto;

import java.util.List;

import com.gcc.communityHall.entity.EbDetails;

public class saveEbDetailsAndDamage {

    private int bookingId;
    private EbDetails ebDetails;

    private List<DamageDTO> damage;

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public EbDetails getEbDetails() {
        return ebDetails;
    }

    public void setEbDetails(EbDetails ebDetails) {
        this.ebDetails = ebDetails;
    }

    public List<DamageDTO> getDamage() {
        return damage;
    }

    public void setDamage(List<DamageDTO> damage) {
        this.damage = damage;
    }
}