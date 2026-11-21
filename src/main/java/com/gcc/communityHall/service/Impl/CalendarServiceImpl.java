package com.gcc.communityHall.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcc.communityHall.entity.BookingCalendar;
import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.repository.BookingCalendarRepository;
import com.gcc.communityHall.repository.BookingDetailsRepository;
import com.gcc.communityHall.service.CalendarService;

@Service
public class CalendarServiceImpl implements CalendarService {

    @Autowired
    BookingCalendarRepository bookingCalendarRepository;
    
    @Autowired
    private BookingDetailsRepository bookingDetailsRepository;
    
    @Override
    public List<BookingCalendar> getCalendarByDateAndHallIdAndSlot(
            String fromDate, String toDate, String startingSlot, String endingSlot, int hallId) {
        if(hallId!=0){
            return bookingCalendarRepository.getCalendarByBDateAndHAllId(fromDate,toDate,hallId);
        }else{
            return bookingCalendarRepository.getCalendarByBDate(fromDate,toDate);
        }

    }

    @Override
    public List<BookingCalendar> getCalendarByBookingId(int bookingId) {
        if(bookingId!=0){
            return bookingCalendarRepository.getCalendarByBookingId(bookingId);
        }
        return null;
    }
}