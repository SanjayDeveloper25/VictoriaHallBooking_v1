package com.gcc.communityHall.dto;

import com.gcc.communityHall.entity.BookingDetails;

public class BookingResponseDTO {
    private BookingDetails bookingDetails;
    private Integer blockingId;

    public BookingResponseDTO(BookingDetails bookingDetails, Integer blockingId) {
        this.bookingDetails = bookingDetails;
        this.blockingId = blockingId;
    }

    // Getters and Setters
    public BookingDetails getBookingDetails() {
        return bookingDetails;
    }

    public void setBookingDetails(BookingDetails bookingDetails) {
        this.bookingDetails = bookingDetails;
    }

    public Integer getBlockingId() {
        return blockingId;
    }

    public void setBlockingId(Integer blockingId) {
        this.blockingId = blockingId;
    }
}

