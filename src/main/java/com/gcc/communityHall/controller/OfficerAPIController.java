package com.gcc.communityHall.controller;

import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ch.qos.logback.core.CoreConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.net.InetAddress;
import com.gcc.communityHall.dto.PaymentsDTO;
import com.gcc.communityHall.entity.ApplicationLogs;
import com.gcc.communityHall.entity.BookingCalendar;
import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.BookingDetails.BookingStatus;
import com.gcc.communityHall.entity.BookingDetails.RefundStatus;
import com.gcc.communityHall.entity.Users.userType;
import com.gcc.communityHall.paymentService.entity.TransactionResponse;
import com.gcc.communityHall.entity.BookingHistory;
import com.gcc.communityHall.entity.CounterRefundList;
import com.gcc.communityHall.entity.Settlement;
import com.gcc.communityHall.entity.SettlementHistory;
import com.gcc.communityHall.entity.Users;
import com.gcc.communityHall.repository.ApplicationLogsRepository;
import com.gcc.communityHall.repository.BookingCalendarRepository;
import com.gcc.communityHall.repository.BookingDetailsRepository;
import com.gcc.communityHall.repository.BookingHistoryRepository;
import com.gcc.communityHall.repository.CounterRefundListRepository;
import com.gcc.communityHall.repository.SettlementHistoryRepository;
import com.gcc.communityHall.repository.SettlementRepository;
import com.gcc.communityHall.repository.UsersRepository;
import com.gcc.communityHall.service.BookingService;
import com.gcc.communityHall.service.HallDetailService;
import com.gcc.communityHall.service.PaymentsService;
import com.gcc.communityHall.service.ReasonMasterService;
import com.gcc.communityHall.service.SettlementService;
import com.gcc.communityHall.service.UserService;
import com.gcc.communityHall.service.Impl.BookingServiceImpl;
import com.gcc.communityHall.service.Impl.SettlementServiceImpl;
import com.gcc.communityHall.service.Impl.UserServiceImpl;
import com.gcc.communityHall.service.Impl.WhatsappMsgServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Tag(name = "CRUD REST APIs for Officer in CommunityHall", description = "CRUD REST APIs in CommunityHall to CREATE, UPDATE, FETCH AND DELETE Booking details")
@RestController
@RequestMapping("/victoriahallbooking_uat/api/officer")
@AllArgsConstructor
@Validated
public class OfficerAPIController {
	// ak// ak
	@Autowired
	private SettlementHistoryRepository settlementHistoryRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private SettlementService settlementService;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	private SettlementServiceImpl settlementServiceImpl;

	@Autowired
	private SettlementRepository settlementRepository;

	@Autowired
	private BookingServiceImpl bookingServiceImpl;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private BookingDetailsRepository bookingDetailsRepository;

	@Autowired
	private ReasonMasterService reasonMasterService;

	@Autowired
	private CounterRefundListRepository counterRefundListRepository;
	@Autowired
	private BookingCalendarRepository bookingCalendarRepository;
	@Autowired
	private HallDetailService hallDetailService;
	@Autowired
	private WhatsappMsgServiceImpl whatsappMsgServiceImpl;
	@Autowired
	private PaymentsService paymentsService;
	@Autowired
	private BookingHistoryRepository bookingHistoryRepository;

	@Autowired
	private HttpSession session;

	@Autowired
	private ApplicationLogsRepository applicationLogsRepository;

	@Operation(summary = "Retriving the User Details by userId REST API", description = "REST API for Retriving the User Details by UserId")
	// Getting User Details by User Id
	@GetMapping("/getUserDetails")
	public ResponseEntity<?> getUsersByID(HttpServletRequest request, @RequestParam String usersid) {
		Users user = userServiceImpl.getUserById(Integer.parseInt(usersid));
		return ResponseEntity.ok(user);
	}

	@Operation(summary = "Settlement List where the Refund Status is WAITING REST API", description = "REST API for Retriving the Settlement List where the Refund Status is WAITING")
	// retrieving the Settlement List by Refund Status is WAITING
	@GetMapping("/waiting-refund")
	public List<Settlement> getSettlementsWithWaitingRefund() {
		return settlementService.getSettlementListWithWaitingRefund();
	}

	@Operation(summary = "Settlement List by Settlement Id REST API", description = "REST API for Retriving the Settlement List by Settlement Id")
	// retrieving the Settlement List by Settlement Id
	@GetMapping("/getSettlementList")
	public ResponseEntity<BookingDetails> getBookingDetailsWithSettlement(HttpServletRequest request,
			@RequestParam String settlementsid) {
		BookingDetails bookingDetails = bookingServiceImpl
				.getBookingDetailsWithSettlement(Integer.parseInt(settlementsid));
		System.out.println("bookingDetails:12 " + bookingDetails);
		return ResponseEntity.ok(bookingDetails);
	}

	@GetMapping("/getsettlementListbyid")
	public ResponseEntity<Optional<Settlement>> getsettlementlist(HttpServletRequest request,
			@RequestParam int settlementsid) {
		Optional<Settlement> settlementlist = settlementRepository.findById(settlementsid);
		return ResponseEntity.ok(settlementlist);
	}

	@Operation(summary = "Booking Details by Booking Id REST API", description = "REST API for Retriving the Booking Details by Booking Id")
	// retrieving the Booking Details by Booking Id
	@GetMapping("/getBookingDetails")
	public ResponseEntity<?> getBookingsById(HttpServletRequest request, @RequestParam String bookingsId) {
		BookingDetails book = bookingServiceImpl.getBookingsById(Integer.parseInt(bookingsId));
		return ResponseEntity.ok(book);
	}

	@Operation(summary = "Settlement APPROVED REST API", description = "REST API for Updating the Refund Status to Approved by Settlement Id If the settlement is Approved")
	// Updating the Refund Status to Approved by Settlement Id If the settlement is
	// Approved
	@PostMapping("/updateApprovedSettlementStatus/{settlementId}") // need to check this API for settlement approval
	public ResponseEntity<Object> updateApprovedSettlementStatus(
			@PathVariable("settlementId") int settlementId,
			@RequestParam("status") BookingDetails.RefundStatus status,
			@RequestParam("comments") String comments) {
		Map<String, Object> response = new HashMap<>();

		try {
			System.out.println("settlementId1: " + settlementId);
			System.out.println("status" + status);
			System.out.println("comments" + comments);

			// Update settlement status
			Settlement updateSettlement = settlementService.updateSettlementStatus(settlementId, status, comments);
			if (updateSettlement == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			System.out.println("updateSettlement12" + updateSettlement);
			// ak
			// Set approvedFlag and approvedFlagDate
			updateSettlement.setApprovedFlag(1);
			updateSettlement.setApprovedFlagDate(new Date());
			settlementRepository.save(updateSettlement);
			// ak

			// Log the action
			String adminname = (String) session.getAttribute("usertype");
			Integer loginid = (Integer) session.getAttribute("id");
			ApplicationLogs logs = new ApplicationLogs();
			logs.setComments("Action by " + adminname);
			logs.setLoginId(loginid);
			logs.setBookingId(updateSettlement.getBookingDetails().getBookingId());
			logs.setLogType("Booking Status: " + status.toString());

			try {
				InetAddress inetAddress = InetAddress.getLocalHost();
				String hostname = InetAddress.getLocalHost().getHostName();
				logs.setIpaddress(inetAddress.getHostAddress());
				logs.setSystemName(hostname);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

			applicationLogsRepository.save(logs);

			// Process refund request
			String refundResponse = bookingServiceImpl
					.processRefundRequest(updateSettlement.getBookingDetails().getBookingId());
			// ak
			// Prepare response map

			response.put("settlement", updateSettlement);
			response.put("approvedFlag", updateSettlement.getApprovedFlag());
			response.put("approvedFlagDate", updateSettlement.getApprovedFlagDate());

			if (refundResponse.contains("successful")) {
				// Refund success, return updated settlement and status
				// return new ResponseEntity<>(updateSettlement, HttpStatus.OK);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {

				// Refund failure, update refund status to FAILURE
				bookingService.updateRefundStatus(updateSettlement.getBookingDetails().getBookingId(),
						BookingDetails.RefundStatus.FAILURE);
				Map<String, Object> responses = new HashMap<>();
				responses.put("approvedFlag", 1);
				responses.put("message", "Refund processing failed: " + refundResponse);
				responses.put("status", "FAILURE");

				return new ResponseEntity<>(responses, HttpStatus.INTERNAL_SERVER_ERROR);
				// return new ResponseEntity<>("Refund processing failed: " + refundResponse,
				// HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			response.put("approvedFlag", 1);
			response.put("message", "Exception while approving settlement: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

			// return new ResponseEntity<>("An error occurred while processing the
			// settlement for Settlement ID: " + settlementId,
			// HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Settlement APPROVED REST API", description = "REST API for Updating the Refund Status to Approved by Settlement Id If the settlement is Approved")
	// Updating the Refund Status to Rejected by Settlement Id If the settlement is
	// Rejected
	@PostMapping("/updateRejectSettlementStatus/{settlementId}")
	public ResponseEntity<Settlement> updateRejectSettlementStatus(
			@PathVariable("settlementId") int settlementId,
			@RequestParam("status") BookingDetails.RefundStatus status,
			@RequestParam("comments") String comments) {
		Settlement updateSettlement = settlementService.updateRejectSettlementStatus(settlementId, status, comments);
		String adminname = (String) session.getAttribute("usertype");
		Integer loginid = (Integer) session.getAttribute("id");
		ApplicationLogs logs = new ApplicationLogs();
		logs.setComments("Action by " + adminname);
		logs.setLoginId(loginid);
		logs.setBookingId(updateSettlement.getBookingDetails().getBookingId());
		logs.setLogType("Booking Status:-" + status.toString());
		InetAddress inetAddress;
		try {
			inetAddress = InetAddress.getLocalHost();
			String hostname = InetAddress.getLocalHost().getHostName();
			logs.setIpaddress(inetAddress.getHostAddress());
			logs.setSystemName(hostname);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		applicationLogsRepository.save(logs);
		if (updateSettlement != null) {
			return new ResponseEntity<>(updateSettlement, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "USER STATUS APPROVED REST API", description = "REST API for Updating the user Status Approved")
	// Updating the user Status Approved
	@PostMapping("/updateApprovedStatus/{userId}")
	public ResponseEntity<Users> updateApprovedStatus(
			@PathVariable("userId") int userId,
			@RequestParam("status") Users.userStatus status,
			@RequestParam("comments") String comments) {

		Users updatedUser = userService.updateUserStatus(userId, status, comments);
		if (updatedUser != null) {
			return new ResponseEntity<>(updatedUser, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "USER STATUS REJECTED REST API", description = "REST API for Updating the user Status Rejected")
	// Updating the user Status Rejected
	@PostMapping("/updateRejectStatus/{userId}")
	public ResponseEntity<Users> updateRejectStatus(
			@PathVariable("userId") int userId,
			@RequestParam("status") Users.userStatus status,
			@RequestParam("comments") String comments) {

		Users updatedUser = userService.updateUserStatus(userId, status, comments);

		if (updatedUser != null) {
			return new ResponseEntity<>(updatedUser, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "BOOKING STATUS APPROVED REST API", description = "REST API for Updating the Booking Status Approved by Booking Id")
	// Updating the Booking Status Approved by Booking Id
	@PostMapping("/updateBookingApproved/{bookingId}")
	public ResponseEntity<BookingDetails> updateBookingApproved(
			@PathVariable("bookingId") int bookingId,
			@RequestParam("status") BookingDetails.BookingStatus status,
			@RequestParam("comments") String comments) {

		BookingDetails updatedBooking = bookingService.updateBookingStatus(bookingId, status, comments);
		String adminname = (String) session.getAttribute("usertype");
		Integer loginid = (Integer) session.getAttribute("id");
		ApplicationLogs logs = new ApplicationLogs();
		logs.setComments("Action by " + adminname);
		logs.setLoginId(loginid);
		logs.setBookingId(bookingId);
		logs.setLogType("Booking Status:-" + status.toString());
		InetAddress inetAddress;
		try {
			inetAddress = InetAddress.getLocalHost();
			String hostname = InetAddress.getLocalHost().getHostName();
			logs.setIpaddress(inetAddress.getHostAddress());
			logs.setSystemName(hostname);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		applicationLogsRepository.save(logs);

		if (updatedBooking != null) {
			return new ResponseEntity<>(updatedBooking, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	// ak

	@PostMapping("/toggleStatus")
	public ResponseEntity<String> toggleUserStatus(
			@RequestParam Integer userId,
			@RequestParam boolean active) {
		// System.out.println("userID: " + userId + ", active or deactivate: " +
		// active);
		try {
			userService.toggleUserStatus(userId, active);
			return ResponseEntity.ok("User status updated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user status");
		}
	}

	@Operation(summary = "BOOKING STATUS REJECTED REST API", description = "REST API for Updating the Booking Status Rejected by Booking Id")
	// Updating the Booking Status Rejected by Booking Id
	@PostMapping("/updateBookingReject/{bookingId}")
	public ResponseEntity<?> updateBookingReject(
			@PathVariable("bookingId") int bookingId,
			@RequestParam("status") BookingDetails.BookingStatus status,
			@RequestParam("comments") String comments,
			@RequestParam("reasontype") int reasontype) {

		if (reasontype == 1) {
			Optional<BookingDetails> book = bookingDetailsRepository.findById(bookingId);
			BookingDetails result = book.get();
			result.setBookingStatus(BookingStatus.REJECTED);
			result.setRefundStatus(RefundStatus.NA);
			List<BookingCalendar> bookingCalendarList = bookingCalendarRepository
					.getCancellingdata1(String.valueOf(result.getBookingId()));

			for (BookingCalendar calendar : bookingCalendarList) {
				boolean isUpdated = false;

				if (String.valueOf(result.getBookingId()).equals(calendar.getBookingId_1())) {
					calendar.setBookingId_1(null);
					isUpdated = true;
				}
				if (String.valueOf(result.getBookingId()).equals(calendar.getBookingId_2())) {
					calendar.setBookingId_2(null);
					isUpdated = true;
				}
				if (calendar.isMorningSlot()) {
					calendar.setMorningSlot(false);
					isUpdated = true;
				}
				if (calendar.isEveningSlot()) {
					calendar.setEveningSlot(false);
					isUpdated = true;
				}
				if (isUpdated) {
					calendar.setIs_delete(true);
					bookingCalendarRepository.save(calendar);
				}
			}
			bookingDetailsRepository.save(result);
			String adminname = (String) session.getAttribute("usertype");
			Integer loginid = (Integer) session.getAttribute("id");
			ApplicationLogs logs = new ApplicationLogs();
			logs.setComments("Action by " + adminname);
			logs.setLoginId(loginid);
			logs.setBookingId(bookingId);
			logs.setLogType("Booking Status:-" + status.toString());
			InetAddress inetAddress;
			try {
				inetAddress = InetAddress.getLocalHost();
				String hostname = InetAddress.getLocalHost().getHostName();
				logs.setIpaddress(inetAddress.getHostAddress());
				logs.setSystemName(hostname);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			return ResponseEntity.ok(result);
		} else if (reasontype == 2) {
			BookingDetails rejectBooking = bookingService.updateCancelledBookingStatus(bookingId, status, comments);
			Optional<BookingDetails> book = bookingDetailsRepository.findById(bookingId);
			BookingDetails bookingDetails = book.get();
			String rejectionsms = "Your booking ID " + bookingDetails.getBookingId() + " on "
					+ bookingDetails.getStartDate() + " has been rejected by GCC due to insufficient details.";
			String rejectedurlString = "https://tmegov.onex-aura.com/api/sms?key=pfTEYN6H&to="
					+ bookingDetails.getUsers().getMobileNo() + "&from=GCCCRP&body=" + rejectionsms
					+ "&entityid=1401572690000011081&templateid=1407172346485956648";
			// String
			// rejectedwhatsupmsg="https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to="+bookingDetails.getUsers().getMobileNo()+"&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Your+booking+with+ID+"+bookingId+"+on+"+bookingDetails.getStartDate()+"+has+been+rejected+by+GCC+due+to+insufficient+details.+%0AThanks+for+using+GCC+Community+Hall+Booking+System.";
			String rejectedwhatsupmsg = "https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to="
					+ bookingDetails.getUsers().getMobileNo()
					+ "&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Your+booking+with+ID+" + bookingId
					+ "+on+" + bookingDetails.getStartDate() + "+has+been+rejected+by+GCC+due+to+insufficient+details.";
			whatsappMsgServiceImpl.sendMsg(rejectedwhatsupmsg);
			whatsappMsgServiceImpl.sendSMS(rejectedurlString);
			String adminname = (String) session.getAttribute("usertype");
			Integer loginid = (Integer) session.getAttribute("id");
			ApplicationLogs logs = new ApplicationLogs();
			logs.setComments("Action by " + adminname);
			logs.setLoginId(loginid);
			logs.setBookingId(bookingId);
			logs.setLogType("Booking Status:-" + status.toString());
			InetAddress inetAddress;
			try {
				inetAddress = InetAddress.getLocalHost();
				String hostname = InetAddress.getLocalHost().getHostName();
				logs.setIpaddress(inetAddress.getHostAddress());
				logs.setSystemName(hostname);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			applicationLogsRepository.save(logs);
			return ResponseEntity.ok(rejectBooking);
		} else {
			return ResponseEntity.badRequest().body("Invalid reasontype");
		}
	}

	@Operation(summary = "Retrieving BOOKING STATUS APPROVED REST API", description = "REST API for retrieving the Booking Status Approved")
	// retrieving the Booking Status Approved
	@GetMapping("/user-Approved")
	public ResponseEntity<List<Users>> getUserApprovedList() {
		List<Users> userApproved = usersRepository.getUserApproved();
		return new ResponseEntity<>(userApproved, HttpStatus.OK);
	}

	@Operation(summary = "Retrieving BOOKING STATUS REJECTED REST API", description = "REST API for retrieving the Booking Status Rejected")
	// retrieving the Booking Status Rejected
	@GetMapping("/user-Reject")
	public ResponseEntity<List<Users>> getUserRejectedList() {
		List<Users> userReject = usersRepository.getUserReject();
		return new ResponseEntity<>(userReject, HttpStatus.OK);
	}

	@Operation(summary = "Counter Refund Approval by Officer REST API", description = "REST API to approve the Counter Refund by Officer")
	// RefundApproval by Officer
	@PostMapping("/approveRefund/{id}")
	public ResponseEntity<String> approveRefund(@PathVariable int id) {
		Optional<CounterRefundList> refundListOptional = counterRefundListRepository.findById(id);

		if (refundListOptional.isPresent()) {
			CounterRefundList refundList = refundListOptional.get();
			refundList.setInitialisationStatus(CounterRefundList.InitialisationStatus.APPROVED);
			counterRefundListRepository.save(refundList);
			return new ResponseEntity<>("Refund with counterId " + id + " has been approved", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Refund with counterId " + id + " not found", HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Counter Refund Reject by Officer REST API", description = "REST API to Reject the Counter Refund by Officer")
	// RefundReject by Officer
	@PostMapping("/rejectRefund/{id}")
	public ResponseEntity<String> rejectRefund(@PathVariable int id) {
		Optional<CounterRefundList> rejectRefund = counterRefundListRepository.findById(id);

		if (rejectRefund.isPresent()) {
			CounterRefundList refundList = rejectRefund.get();
			refundList.setInitialisationStatus(CounterRefundList.InitialisationStatus.REJECTED);
			counterRefundListRepository.save(refundList);
			return new ResponseEntity<>("Refund with counterId " + id + " has been approved", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Refund with counterId " + id + " not found", HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Reason By reasonName REST API", description = "REST API to get the Reason Details with the reason")
	// retrieving the Reason Details with the reason
	@GetMapping("/getReasonByName")
	public ResponseEntity<?> getReasonByName(@RequestParam String reasonName) {
		return ResponseEntity.ok(reasonMasterService.getReasonByName(reasonName));
	}

	@PostMapping("/uploadChallonDetails/{settlementId}")
	public ResponseEntity<Settlement> updateApprovedSettlementStatus(
			@PathVariable("settlementId") int settlementId,
			@RequestParam(name = "ddNumber", required = false) Long ddNumber,
			@RequestParam(name = "ddDate", required = false) String ddDate,
			@RequestParam(name = "ddamount", required = false) Long ddamount,
			@RequestParam(name = "challonnumber", required = false) Long challonnumber,
			@RequestParam(name = "challonamount", required = false) Long challonamount,
			@RequestParam(name = "challonimage") MultipartFile challonimage) {

		// Add null checks and default values if necessary
		Settlement settlement = settlementService.uploadChallonDetails(
				settlementId,
				ddNumber != null ? ddNumber : 0L,
				ddDate,
				ddamount != null ? ddamount : 0L,
				challonimage,
				challonnumber != null ? challonnumber : 0L,
				challonamount != null ? challonamount : 0L);

		if (settlement != null) {
			return new ResponseEntity<>(settlement, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/approveCancelledBookingRefund/{bookingId}")
	public ResponseEntity<String> updateCancelledRefundStatus(
			@PathVariable("bookingId") int bookingId,
			@RequestParam("status") BookingDetails.RefundStatus status) {
		try {
			// Update refund status preliminarily
			// //BookingDetails updatedBooking =
			// bookingService.updateRefundStatus(bookingId, status);
			// if (updatedBooking == null) {
			// return new ResponseEntity<>("Booking not found for ID: " + bookingId,
			// HttpStatus.NOT_FOUND);
			// }

			// Log the action
			String adminName = (String) session.getAttribute("usertype");
			Integer loginId = (Integer) session.getAttribute("id");
			ApplicationLogs logs = new ApplicationLogs();
			logs.setComments("Action by " + adminName);
			logs.setLoginId(loginId);
			logs.setBookingId(bookingId);
			logs.setLogType("Settlement Approve: " + status.toString());

			try {
				InetAddress inetAddress = InetAddress.getLocalHost();
				String hostname = InetAddress.getLocalHost().getHostName();
				logs.setIpaddress(inetAddress.getHostAddress());
				logs.setSystemName(hostname);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

			applicationLogsRepository.save(logs);

			// Process refund and adjust refund status accordingly
			String refundResponse = bookingServiceImpl.processRefundRequest(bookingId);

			if (refundResponse.contains("successful")) {
				// Refund success: leave refund status as updated
				return new ResponseEntity<>(refundResponse, HttpStatus.OK);
			} else {
				// Refund failure: update refund status to FAILURE
				bookingService.updateRefundStatus(bookingId, BookingDetails.RefundStatus.FAILURE);
				return new ResponseEntity<>(refundResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("An error occurred while processing the refund for Booking ID: " + bookingId,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/bookingstatistics")
	public ResponseEntity<Map<String, Map<String, Integer>>> getBookingStatistics(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
			HttpSession session,
			@RequestParam(required = false) String division,
			@RequestParam(required = false, defaultValue = "1000") Integer hallid,
			@RequestParam(required = false) String userType) {

		String zone = (String) session.getAttribute("zone");
		Map<String, Map<String, Integer>> statistics = bookingService.getOfficerDashboard(fromDate, toDate, zone,
				division, hallid, userType);
		return ResponseEntity.ok(statistics);

	}

	@GetMapping("/getHallsDetailsByZone")
	public ResponseEntity<Map<String, Object>> getHallsDetailsByZone(@RequestParam String zone) {

		Map<String, Object> hallzones = hallDetailService.getHallsDetailsForOfficer(zone);
		return ResponseEntity.ok(hallzones);
	}

	@GetMapping("/byhallid")
	public ResponseEntity<List<Map<String, Object>>> getBookingDetailsByHallId(@RequestParam int hallId) {
		List<Map<String, Object>> bookingDetails = bookingService.getHallDetailsForOfficer(hallId);
		return ResponseEntity.ok(bookingDetails);
	}

	// gccapps
	@GetMapping("/getApprovedBookingDetails")
	public List<BookingDetails> getApprovedBookingDetails() {
		return bookingService.getApprovedBookingDetails();
	}

	@GetMapping("/getRejectedBookingDetails")
	public List<BookingDetails> getRejectedBookingDetails() {
		return bookingService.getRejectedBookingDetails();
	}

	@GetMapping("/getwaitingforNOCapproval")
	public List<BookingDetails> getWaitingforNOCapproval() {
		return bookingService.getWaitingforNOCapproval();
	}

	@GetMapping("/getAllPaymentList")
	public List<PaymentsDTO> getAllPaymentList() {
		return paymentsService.getAllPaymentDetails();
	}
}
