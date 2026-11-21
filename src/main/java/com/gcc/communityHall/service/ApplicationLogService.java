package com.gcc.communityHall.service;

public interface ApplicationLogService {
    void saveLog(int bookingId,String logType,String comments);
}