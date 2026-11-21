package com.gcc.communityHall.service;

import java.util.List;

import com.gcc.communityHall.entity.BookingCalendar;
import com.gcc.communityHall.entity.BookingDetails;

public interface CalendarService {

    List<BookingCalendar> getCalendarByDateAndHallIdAndSlot(
            String fromDate,String toDate,String startingSlot,String endingSlot,
            int hallId);

    List<BookingCalendar> getCalendarByBookingId(int bookingId);

}
