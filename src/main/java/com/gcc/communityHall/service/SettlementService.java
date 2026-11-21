package com.gcc.communityHall.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.BookingDetails.RefundStatus;
import com.gcc.communityHall.entity.Settlement;

public interface SettlementService {
	
	public Settlement settlement(Settlement settlement);
	public Settlement getSettlementById(int settlementId);
	//ak
	
	
	public Settlement updateSettlementStatus(int settlementId, BookingDetails.RefundStatus status , String comments);
	
	public Settlement updateRejectSettlementStatus(int settlementId, BookingDetails.RefundStatus status , String comments);
	
	List<Settlement> getSettlementListWithWaitingRefund();
	//public Settlement uploadchallondetails(int settlementId, long ddNumber, String ddDate, MultipartFile challonimage);
	Settlement uploadChallonDetails(int settlementId, long ddNumber, String date, long ddamount, MultipartFile challonimage, long challonnumber, long challonamount);

}