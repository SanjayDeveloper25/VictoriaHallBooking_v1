package com.gcc.communityHall.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcc.communityHall.entity.ApplicationLogs;
import com.gcc.communityHall.repository.ApplicationLogsRepository;
import com.gcc.communityHall.service.ApplicationLogService;

@Service
public class ApplicationLogServiceImpl implements ApplicationLogService {

    @Autowired
    private ApplicationLogsRepository applicationLogsRepository;
    @Override
    public void saveLog(int bookingId, String logType, String comments) {
        ApplicationLogs applicationLogs = new ApplicationLogs();
        applicationLogs.setBookingId(bookingId);
        applicationLogs.setLogType(logType);
        applicationLogs.setComments(comments);
        applicationLogsRepository.save(applicationLogs);

    }
}
