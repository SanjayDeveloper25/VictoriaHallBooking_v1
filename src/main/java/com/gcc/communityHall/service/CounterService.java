package com.gcc.communityHall.service;


import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.CounterDetails;
import com.gcc.communityHall.entity.CounterRefundList;


public interface CounterService
{
	 Map<String, Object> saveCounterBooking(Map<String, Object> formData);

	
	CounterDetails saveCounterDetails(
			String bankName,
			int bankAccNo,
			int ddNumber,
			double amountReceived,
			MultipartFile counterddphoto, int bookingId);
	
	CounterDetails getDdImageById(int counterId);


	BookingDetails getBookingsById(int BookingId);


	CounterRefundList saveCancellation(int counterId, MultipartFile uploadLetter,String reason) throws IOException;
}
