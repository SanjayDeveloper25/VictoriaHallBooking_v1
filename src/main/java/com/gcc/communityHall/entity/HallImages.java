package com.gcc.communityHall.entity;

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
public class HallImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hallImageId;

    @Column(name = "hall_Image_url")
    private String hallImage;
    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JsonIgnore
    private HallDetails hallDetails;

    public int getHallImageId() {
        return hallImageId;
    }

    public void setHallImageId(int hallImageId) {
        this.hallImageId = hallImageId;
    }

    public String getHallImage() {
        return hallImage;
    }

    public void setHallImage(String hallImage) {
        this.hallImage = hallImage;
    }

    public HallDetails getHallDetails() {
        return hallDetails;
    }

    public void setHallDetails(HallDetails hallDetails) {
        this.hallDetails = hallDetails;
    }
}
