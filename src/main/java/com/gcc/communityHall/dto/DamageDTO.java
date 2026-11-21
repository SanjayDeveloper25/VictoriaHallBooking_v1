package com.gcc.communityHall.dto;

import org.springframework.web.multipart.MultipartFile;

public class DamageDTO {

    private String assetName;
    private String comments;
    private MultipartFile assetImage;
    private int quantity;
    private float assetAmount;

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public MultipartFile getAssetImage() {
        return assetImage;
    }

    public void setAssetImage(MultipartFile assetImage) {
        this.assetImage = assetImage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getAssetAmount() {
        return assetAmount;
    }

    public void setAssetAmount(float assetAmount) {
        this.assetAmount = assetAmount;
    }
}