package com.gcc.communityHall.service;

import java.util.List;

import com.gcc.communityHall.dto.PaymentsDTO;
import com.gcc.communityHall.entity.Payments;

public interface PaymentsService {
	
	List<Payments> getPaymentsByZone(String zone);
	
	List<PaymentsDTO> getAllPaymentDetails();

}