package com.gcc.communityHall.dto;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public class SaveBooking {

    private int hallId;
    private int userId;
    private int reasonId;
    private Map<String,Object> bookingDetails;

    private MultipartFile file;
    private Object obj = file;
    public void check(){
        MultipartFile mf= (MultipartFile) obj;
    }

}
