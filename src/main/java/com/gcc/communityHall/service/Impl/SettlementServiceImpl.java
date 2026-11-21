package com.gcc.communityHall.service.Impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.BookingHistory;
import com.gcc.communityHall.entity.BookingDetails.RefundStatus;
import com.gcc.communityHall.entity.Settlement;
import com.gcc.communityHall.entity.SettlementHistory;
import com.gcc.communityHall.repository.BookingDetailsRepository;
import com.gcc.communityHall.repository.BookingHistoryRepository;
import com.gcc.communityHall.repository.SettlementHistoryRepository;
import com.gcc.communityHall.repository.SettlementRepository;
import com.gcc.communityHall.service.SettlementService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

@Service
public class SettlementServiceImpl implements SettlementService {
	// ak
	@Autowired
	private SettlementHistoryRepository settlementHistoryRepository;

	@Autowired
	SettlementRepository settlementRepository;

	@Autowired
	BookingDetailsRepository bookingDetailsRepository;

	@Autowired
	private BookingHistoryRepository bookingHistoryRepository;

	@Autowired
	private HttpSession session;

	@Value("${file.upload.directory}")
	private String UploadDirectory;

	@Value("${file.fullpath}")
	private String serverPath;

	private final String settlementDirectory;

	private final String settlementPhotoUrl;

	@Autowired
	SettlementServiceImpl(SettlementRepository settlementRepository,
			@Value("${file.settlement.upload.directory}") String settlementDirectory,
			@Value("${file.settlement.fullpath}") String settlementPhotoUrl) {
		this.settlementDirectory = settlementDirectory;
		this.settlementPhotoUrl = settlementPhotoUrl;
	}

	// @Override
	// public Settlement getSettlementById(int settlementId) {
	// Optional<Settlement> settlement= settlementRepository.findById(settlementId);
	// if(settlement.isPresent()){
	// return settlement.get();
	// }
	// return null;
	// }

	// @Override
	// public Settlement getSettlementById(int settlementId) {
	// return settlementRepository.findByIdWithBookingDetails(settlementId);
	// }

	@Override
	public Settlement getSettlementById(int settlementId) {
		Optional<Settlement> settlementOptional = settlementRepository.findById(settlementId);
		if (settlementOptional.isPresent()) {
			Settlement settlement = settlementOptional.get();
			// Initialize the BookingDetails object
			settlement.getBookingDetails();
			// System.out.println("✅ Approved flag reset after 4 days for Settlement ID: " +
			// settlementId);
			return settlement;
		}
		return null;
	}
	// ak
	/*
	 * @Override
	 * public Settlement getSettlementById(int settlementId) {
	 * Optional<Settlement> settlementOptional =
	 * settlementRepository.findById(settlementId);
	 * 
	 * if (settlementOptional.isPresent()) {
	 * Settlement settlement = settlementOptional.get();
	 * 
	 * // Initialize BookingDetails
	 * settlement.getBookingDetails();
	 * System.out.println("settlement.getBookingDetails(): " +
	 * settlement.getBookingDetails());
	 * 
	 * // ✅ Auto-reset logic: if approved for 4+ days, reset flag
	 * if (settlement.getApprovedFlag() == 1 && settlement.getApprovedFlagDate() !=
	 * null) {
	 * long millisDiff = System.currentTimeMillis() -
	 * settlement.getApprovedFlagDate().getTime();
	 * long daysDiff = millisDiff / (1000 * 60 * 60 * 24);
	 * System.out.println("Approved Flag Date: " +
	 * settlement.getApprovedFlagDate());
	 * System.out.println("Current Java Date: " + new Date());
	 * System.out.println("Days Difference: " + daysDiff);
	 * 
	 * 
	 * if (daysDiff >= 4) {
	 * settlement.setApprovedFlag(0);
	 * settlement.setApprovedFlagDate(null);
	 * settlementRepository.save(settlement);
	 * System.out.println("✅ Approved flag reset after 4 days for Settlement ID: " +
	 * settlementId);
	 * }
	 * }
	 * 
	 * return settlement;
	 * }
	 * 
	 * return null;
	 * }
	 */

	@Override
	public List<Settlement> getSettlementListWithWaitingRefund() {
		return settlementRepository.findByBookingDetailsRefundStatus(RefundStatus.WAITING);
	}

	@Override
	public Settlement updateSettlementStatus(int settlementId, BookingDetails.RefundStatus status, String comments) {
		Optional<Settlement> settlementOptional = settlementRepository.findById(settlementId);
		String adminname = (String) session.getAttribute("usertype");
		if (settlementOptional.isPresent()) {
			Settlement settlement = settlementOptional.get();
			System.out.println("settlement@12345" + settlement);
			BookingDetails bookingDetails = settlement.getBookingDetails(); // Fetch associated BookingDetails

			// Update BookingDetails with new RefundStatus
			// bookingDetails.setRefundStatus(status);
			// If the refund status is COMPLETED, update the BookingStatus to COMPLETED
			// if (status == BookingDetails.RefundStatus.COMPLETED) {
			// bookingDetails.setBookingStatus(BookingDetails.BookingStatus.COMPLETED);
			// }else {
			// bookingDetails.setBookingStatus(BookingDetails.BookingStatus.APPROVED);
			// }

			// to maintain history for settlement

			

			bookingDetailsRepository.save(bookingDetails);

			//ak
			//settlement histoty insert
                       
		
            	if (settlementOptional.isPresent()) {
			
            
            SettlementHistory history = new SettlementHistory();
			history.setSettlementId(settlement.getSettlementId());
			history.setComments(settlement.getComments());
			history.setCdate(settlement.getCdate());
			history.setDamageAmount(settlement.getDamageAmount());
			history.setEbAmount(settlement.getEbAmount());
			history.setDepositAmount(settlement.getDepositAmount());
			history.setRefundedAmount(settlement.getRefundedAmount());
			history.setUpdatedDate(new Date());
			history.setUpdatedId(adminname);
			history.setChallon(settlement.getChallon());
			history.setDddate(settlement.getDddate());
			history.setDdnumber(settlement.getDdnumber());
			history.setBookingDetails(settlement.getBookingDetails());
			history.setCollectionPending(settlement.getCollectionPending());
			history.setDdamount(settlement.getDdamount());
			history.setChallonnumber(settlement.getChallonnumber());
			history.setCollectionStatus(settlement.getCollectionStatus());
			history.setChallonamount(settlement.getChallonamount());
			history.setApprovedFlag(settlement.getApprovedFlag());
			history.setApprovedFlagDate(settlement.getApprovedFlagDate());
			history.setShDate(new Date());

			settlementHistoryRepository.save(history);
			System.out.println("✅ Record inserted into SettlementHistory for Settlement ID: " + settlementId);
                }
		
           
			// checking ak
			//settlement.setApprovedFlag(1);
			//settlement.setApprovedFlagDate(new Date());
			
			settlement.setUpdatedDate(new Date());
			// Update Settlement comments
			settlement.setComments(comments);

			// Save and return updated Settlement
			return settlementRepository.save(settlement);
		}

		return null;
	}
	// ak

	@Override
	public Settlement updateRejectSettlementStatus(int settlementId, BookingDetails.RefundStatus status,
			String comments) {
		Optional<Settlement> settlementOptional = settlementRepository.findById(settlementId);

		if (settlementOptional.isPresent()) {
			Settlement settlement = settlementOptional.get();
			BookingDetails bookingDetails = settlement.getBookingDetails(); // Fetch associated BookingDetails
			bookingDetails.setRefundStatus(status);
			bookingDetailsRepository.save(bookingDetails);

			// Update Settlement comments
			settlement.setComments(comments);
			settlement.setUpdatedDate(new Date());

			// Save and return updated Settlement
			return settlementRepository.save(settlement);
		}

		return null;
	}

	@Override
	public Settlement settlement(Settlement settlement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Settlement uploadChallonDetails(int settlementId, long ddNumber, String ddDate, long ddamount,
			MultipartFile challonimage, long challonnumber, long challonamount) {
		Optional<Settlement> settlementOptional = settlementRepository.findById(settlementId);

		if (settlementOptional.isPresent()) {
			Settlement updateSettlement = settlementOptional.get();
			updateSettlement.setDdnumber(ddNumber);
			updateSettlement.setDddate(ddDate);
			updateSettlement.setChallonamount(challonamount);
			updateSettlement.setChallonnumber(challonnumber);
			updateSettlement.setDdamount(ddamount);

			if (challonimage != null && !challonimage.isEmpty()) {
				String fileName = settlementId + "_" + challonimage.getOriginalFilename();
				String filePath = settlementDirectory + "/" + fileName;

				// Save the file to the specified directory
				try (FileOutputStream fos = new FileOutputStream(filePath)) {
					byte[] bytes = challonimage.getBytes();
					fos.write(bytes);
					updateSettlement.setChallon(settlementPhotoUrl + "challonDetails/" + fileName);
				} catch (Exception e) {
					throw new RuntimeException("Error saving challon image", e);
				}
			}

			return settlementRepository.save(updateSettlement);
		} else {
			return null;
		}
	}

}
