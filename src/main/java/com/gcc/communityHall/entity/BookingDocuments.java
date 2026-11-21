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
public class BookingDocuments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingDocId;
    private String bookingDocUrl;
    private String documentName;
    private String documentType;
    private String name;
    private String booking_label;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private BookingDetails bookingDetails;

    public int getBookingDocId() {
        return bookingDocId;
    }

    public void setBookingDocId(int bookingDocId) {
        this.bookingDocId = bookingDocId;
    }

    public String getBookingDocUrl() {
        return bookingDocUrl;
    }

    public void setBookingDocUrl(String bookingDocUrl) {
        this.bookingDocUrl = bookingDocUrl;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBooking_label() {
        return booking_label;
    }

    public void setBooking_label(String booking_label) {
        this.booking_label = booking_label;
    }

    public BookingDetails getBookingDetails() {
        return bookingDetails;
    }

    public void setBookingDetails(BookingDetails bookingDetails) {
        this.bookingDetails = bookingDetails;
    }
}
