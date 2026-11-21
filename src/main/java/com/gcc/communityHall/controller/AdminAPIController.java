package com.gcc.communityHall.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import com.gcc.communityHall.component.JwtUtil;
import com.gcc.communityHall.dto.BookingResponseDTO;
import com.gcc.communityHall.dto.ErrorResponseDto;
import com.gcc.communityHall.entity.ApplicationLogs;
import com.gcc.communityHall.entity.BookingCalendar;
import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.BookingDetails.BookingStatus;
import com.gcc.communityHall.entity.BookingDetails.RefundStatus;
import com.gcc.communityHall.entity.DiscountRange;
import com.gcc.communityHall.entity.GccBlockingDate;
import com.gcc.communityHall.entity.GccBlockingHistory;
import com.gcc.communityHall.entity.HallDetails;
import com.gcc.communityHall.entity.HallImages;
import com.gcc.communityHall.entity.Notification;
import com.gcc.communityHall.entity.ReasonDocumentMaster;
import com.gcc.communityHall.entity.ReasonMaster;
import com.gcc.communityHall.entity.Settlement;
import com.gcc.communityHall.entity.Users;
import com.gcc.communityHall.others.AppConfig;
import com.gcc.communityHall.others.UserSession;
import com.gcc.communityHall.paymentService.service.PaymentGatewayServiceImpl;
import com.gcc.communityHall.repository.ApplicationLogsRepository;
import com.gcc.communityHall.repository.BookingCalendarRepository;
import com.gcc.communityHall.repository.BookingDetailsRepository;
import com.gcc.communityHall.repository.DiscountRangeRepository;
import com.gcc.communityHall.repository.GccBlockingDateRepository;
import com.gcc.communityHall.repository.GccBlockingHistoryRepository;
import com.gcc.communityHall.repository.HallDetailsRepository;
import com.gcc.communityHall.repository.HallImagesRepository;
import com.gcc.communityHall.repository.NotificationRepository;
import com.gcc.communityHall.repository.ReasonMasterRepository;
import com.gcc.communityHall.repository.SettlementRepository;
import com.gcc.communityHall.repository.UsersRepository;
import com.gcc.communityHall.service.BookingService;
import com.gcc.communityHall.service.HallDetailService;
import com.gcc.communityHall.service.ReasonMasterService;
import com.gcc.communityHall.service.SettlementService;
import com.gcc.communityHall.service.UserService;
import com.gcc.communityHall.service.Impl.BookingServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

//@RestController
//@RequestMapping("/admin/api")
@Tag(name = "CRUD REST APIs for Admin in CommunityHall", description = "CRUD REST APIs in CommunityHall to CREATE, UPDATE, FETCH AND DELETE Booking details")
@RestController
@RequestMapping("/victoriahallbooking_uat/admin/api")
@AllArgsConstructor
@Validated
public class AdminAPIController {

    @Autowired
    private HallDetailsRepository hallDetailsRepository;

    @Autowired
    private HallDetailService hallDetailService;
    @Autowired
    private BookingDetailsRepository bookingDetailsRepository;

    @Autowired
    private SettlementService settlementService;

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private BookingDetailsRepository bookingDetailsRepo;

    @Autowired
    private DiscountRangeRepository discountRangeRepo;

    @Autowired
    private GccBlockingDateRepository blockingDateRepository;

    @Autowired
    private ReasonMasterService reasonMasterService;

    @Autowired
    private HallDetailService hallService;
    @Autowired
    private UserService userService;

    @Autowired
    private BookingCalendarRepository bookingCalendarRepository;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private HallImagesRepository hallImagesRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private PaymentGatewayServiceImpl paymentGatewayServiceImpl;

    @Autowired
    private NotificationRepository notificationRepo;

    @Autowired
    private HttpSession session;

    @Autowired
    private ApplicationLogsRepository applicationLogsRepository;

    @Operation(summary = "Update HallDetails REST API", description = "REST API to update the Hall Details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status UPDATED", content = @Content(mediaType = "text/plain", schema = @Schema(implementation = HallDetails.class), examples = @ExampleObject(value = "Hall details updated successfully"))),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class), examples = @ExampleObject(name = "Internal Server Error Example", value = "{ \"path\": \"/updateHallDetails\", \"status\": 500, \"error\": \"Internal Server Error\", \"timestamp\": \"2024-06-22T05:47:04.347+00:00\" }")))
    })
    @PostMapping("/updateHallDetails")
    public ResponseEntity<String> updateHallInfo(
            @RequestParam("hallId") String hallId,
            @RequestParam("zone") String zone,
            @RequestParam("division") String division,
            @RequestParam("hallName") String hallName,
            @RequestParam("doorNo") String doorNo,
            @RequestParam("street") String street,
            @RequestParam("area") String area,
            @RequestParam("pinCode") int pinCode,
            @RequestParam("landMark") String landMark,
            @RequestParam("latitude") String latitude,
            @RequestParam("longitude") String longitude,
            @RequestParam("hallInchargeMobno") long hallInchargeMobno,
            @RequestParam("hallInchargeName") String hallInchargeName,
            @RequestParam("depositAmount") float depositAmount,
            @RequestParam("halfDepositAmount") float halfDepositAmount,
            @RequestParam("fullDayRent") float fullDayRent,
            @RequestParam("halfDayRent") float halfDayRent,
            @RequestParam("parkingCapacity") String parkingCapacity,
            @RequestParam("seatingCapacity") int seatingCapacity,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam("ebNumber") String ebNumber,
            @RequestParam("ebCostPerUnit") float ebCostPerUnit,
            @RequestParam("gst") float gst,
            @RequestParam("city") String city,
            @RequestParam("isActive") boolean isActive) {

        HallDetails hallDetails = new HallDetails();
        hallDetails.setHallId(Integer.parseInt(hallId));
        hallDetails.setZone(zone);
        hallDetails.setDivision(division);
        hallDetails.setHallName(hallName);
        hallDetails.setDoorNo(doorNo);
        hallDetails.setStreet(street);
        hallDetails.setArea(area);
        hallDetails.setPinCode(pinCode);
        hallDetails.setLandMark(landMark);
        hallDetails.setLatitude(latitude);
        hallDetails.setLongitude(longitude);
        hallDetails.setHallInchargeMobno(hallInchargeMobno);
        hallDetails.setHallInchargeName(hallInchargeName);
        hallDetails.setDepositAmount(depositAmount);
        hallDetails.setHalfDepositAmount(halfDepositAmount);
        hallDetails.setFullDayRent(fullDayRent);
        hallDetails.setHalfDayRent(halfDayRent);
        hallDetails.setParkingCapacity(parkingCapacity);
        hallDetails.setSeatingCapacity(seatingCapacity);
        hallDetails.setEbNumber(ebNumber);
        hallDetails.setEbCostPerUnit(ebCostPerUnit);
        hallDetails.setGst(gst);
        hallDetails.setCity(city);
        hallDetails.setActive(isActive);

        hallDetailService.updateHallDetail(hallDetails, images);
        String adminname = (String) session.getAttribute("usertype");
        Integer loginid = (Integer) session.getAttribute("id");
        ApplicationLogs logs = new ApplicationLogs();
        logs.setComments("Action by " + adminname);
        logs.setLoginId(loginid);
        logs.setLogType("Hall Details edit:-" + hallName);
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

        return ResponseEntity.ok("Success");
    }

    @Operation(summary = "Update Settlement Status REST API", description = "REST API to update the Settlement Status to INITIATED")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status UPDATED", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Settlement.class), examples = @ExampleObject(name = "Settlement Example", value = "{\"settlementId\": Id, \"Refundstatus\": \"INITIATED\", \"comments\": \"Settlement initiated successfully\"}"))),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class), examples = @ExampleObject(name = "Internal Server Error Example", value = "{ \"path\": \"/updateInitiatedSettlementStatus/{settlementId}\", \"status\": 500, \"error\": \"Internal Server Error\", \"timestamp\": \"2024-06-22T05:47:04.347+00:00\" }")))
    })
    @PostMapping("/updateInitiatedSettlementStatus/{settlementId}")
    public ResponseEntity<Settlement> updateInitiatedSettlementStatus(
            @PathVariable("settlementId") int settlementId,
            @RequestParam("status") BookingDetails.RefundStatus status,
            @RequestParam("comments") String comments) {
        Settlement updateInitialiseSettlement = settlementService.updateSettlementStatus(settlementId, status,
                comments);
        String adminname = (String) session.getAttribute("usertype");
        Integer loginid = (Integer) session.getAttribute("id");
        ApplicationLogs logs = new ApplicationLogs();
        logs.setComments("Action by " + adminname);
        logs.setLoginId(loginid);
        logs.setLogType("Settlement Initiation:-" + status);
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
        if (updateInitialiseSettlement != null) {
            return new ResponseEntity<>(updateInitialiseSettlement, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "HallDetails by HallId REST API", description = "REST API to Retrieving the Hall Details by hallId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HallDetails.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/getHallDetails/{hallId}")
    public ResponseEntity<HallDetails> getHallDetails(@PathVariable String hallId) {
        int id;
        try {
            id = Integer.parseInt(hallId);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        HallDetails hallDetails = hallDetailsRepository.findHallDetailsById(id);
        // System.out.println(hallDetails.isActive());
        if (hallDetails != null) {
            return ResponseEntity.ok(hallDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Saving the DiscountRange REST API", description = "REST API to Saving the Discount Range for usertypes")
    @PostMapping("/savediscountrange")
    public String saveDiscountrange(
            @RequestParam String userType,
            @RequestParam float discountRange, HttpSession session) {

        Optional<DiscountRange> existingDiscountRanges = discountRangeRepo.findByUsertype(userType);
        if (!existingDiscountRanges.isEmpty()) {
            return "waiting";
        }

        String adminname = (String) session.getAttribute("usertype");
        DiscountRange discount = new DiscountRange();
        discount.setUsertype(userType);
        discount.setDiscountRange(discountRange);
        discount.setcDate(LocalDate.now());
        discount.setUpdtedBy(adminname);
        discount.setIs_active(true);
        discount.setIs_delete(false);

        discountRangeRepo.save(discount);
        return "success";
    }

    @PostMapping("/savenotification")
    public String saveNotification(
            @RequestParam String notification, HttpSession session) {

        String adminname = (String) session.getAttribute("usertype");
        Notification newnotification = new Notification();
        newnotification.setNotification_description(notification);
        newnotification.setCdate(LocalDate.now());
        newnotification.setCreatedBY(adminname);
        newnotification.setActive(true);
        newnotification.setDelete(false);
        notificationRepo.save(newnotification);
        Integer loginid = (Integer) session.getAttribute("id");
        ApplicationLogs logs = new ApplicationLogs();
        logs.setComments("Action by " + adminname);
        logs.setLoginId(loginid);
        logs.setLogType("Added New Notification:-" + newnotification.getNotification_description());
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
        return "success";
    }

    @Operation(summary = " Update the DiscountRange REST API", description = "REST API to Updating the Discount Range for usertypes")
    @PutMapping("/updateById/{id}")
    public String updateDiscountRangeById(@PathVariable int id, @RequestParam float discountRange,
            HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        Optional<DiscountRange> existingDiscount = discountRangeRepo.findById(id);
        if (existingDiscount.isPresent()) {
            DiscountRange updatediscount = existingDiscount.get();
            updatediscount.setDiscountRange(discountRange);
            updatediscount.setUpdtedBy(adminname);
            updatediscount.setcDate(LocalDate.now());
            discountRangeRepo.save(updatediscount);
            Integer loginid = (Integer) session.getAttribute("id");
            ApplicationLogs logs = new ApplicationLogs();
            logs.setComments("Action by " + adminname);
            logs.setLoginId(loginid);
            logs.setLogType("Discount Details Update:-" + updatediscount.getUsertype());
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
            return "success";
        } else {
            return "error: Entry with ID " + id + " not found";
        }
    }

    @Operation(summary = " Delete the DiscountRange REST API", description = "REST API to Deleting the Discount for usertypes")
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deleteDiscountRangeById(@PathVariable int id) {
        if (discountRangeRepo.existsById(id)) {
            discountRangeRepo.deleteById(id);
            String adminname = (String) session.getAttribute("usertype");
            Integer loginid = (Integer) session.getAttribute("id");
            ApplicationLogs logs = new ApplicationLogs();
            logs.setComments("Action by " + adminname);
            logs.setLoginId(loginid);
            logs.setLogType("Discount Details Delete:-" + id);
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
            return ResponseEntity.ok("DiscountRange with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if the entry does not exist
        }
    }

    @PostMapping("/deletenotificationById/{id}")
    public ResponseEntity<String> deleteNotificationById(@PathVariable int id, HttpSession session) {
        Optional<Notification> notification = notificationRepo.findById(id);
        Notification newnotification = notification.get();
        newnotification.setActive(false);
        newnotification.setDelete(true);
        String adminname = (String) session.getAttribute("usertype");
        newnotification.setUpdatedBy(adminname);
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        newnotification.setUpdatedDate(date);
        notificationRepo.save(newnotification);
        Integer loginid = (Integer) session.getAttribute("id");
        ApplicationLogs logs = new ApplicationLogs();
        logs.setComments("Action by " + adminname);
        logs.setLoginId(loginid);
        logs.setLogType("Detele Notification:-" + newnotification.getNotification_description());
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
        return ResponseEntity.ok("Notification with ID " + id + " deleted successfully.");

    }

    @Operation(summary = "Retrieving the Discount Details REST API", description = "REST API to Retrieve the Discount Details for usertypes")
    // Retrieving the Discount Details
    @GetMapping("/getDiscountDetails")
    public Optional<DiscountRange> getDIscountDetails(@RequestParam("id") int id) {
        Optional<DiscountRange> discountdetails = discountRangeRepo.findById(id);
        return discountdetails;
    }

    @Operation(summary = "Save Reasons REST API", description = "REST API to save the reasons")
    // saving the Reason
    @PostMapping("/saveReason")
    public ResponseEntity<?> saveReason(@RequestBody ReasonMaster reasaon) {
        return ResponseEntity.ok(reasonMasterService.saveTheReason(reasaon));
    }

    @Operation(summary = "All Reasons REST API", description = "REST API to retrie the allreasons")
    // retrieving the allreasons
    @GetMapping("/getReasons")
    @ResponseBody
    public List<ReasonMaster> getAllReasons() {
        // return reasonMasterService.getAllReasons();
        return reasonMasterService.getAllReasonsWithStatus();
    }

    @Operation(summary = "Reason Documents by reasonId REST API", description = "REST API to Get the ReasonDocuments By reasonId")
    // Getting ReasonDocuments By reasonId
    @GetMapping("/getReasonDocumentDetails")
    public List<ReasonDocumentMaster> getReasonDocumentDetails(@RequestParam int reasonId) {
        return reasonMasterService.getReasonDocumentDetailsByReasonId(reasonId);
    }

    @Operation(summary = "Booking Details by Fromdate,toDate and HallId REST API", description = "REST API to get the Booking Details by Fromdate,toDate and HallId for GccBlokcing")
    // @GetMapping("/filter")
    // public ResponseEntity<List<BookingResponseDTO>> getFilteredBookings(
    // @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate
    // fromDate,
    // @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate
    // toDate,
    // @RequestParam("hallIds") List<Long> hallIds) {
    //
    // List<BookingDetails> bookings = bookingService.getFilteredBookings(fromDate,
    // toDate, hallIds);
    //
    // List<BookingResponseDTO> responses = new ArrayList<>();
    // for (BookingDetails bookingDetails : bookings) {
    // // Fetch GccBlockingDate records for each bookingId
    // List<GccBlockingDate> gccBlockedList =
    // blockingDateRepository.getByBookingId(bookingDetails.getBookingId());
    // Integer blockingId = gccBlockedList.isEmpty() ? null :
    // gccBlockedList.get(0).getBlockingId();
    // responses.add(new BookingResponseDTO(bookingDetails, blockingId));
    // }
    //
    // return ResponseEntity.ok(responses);
    // }
    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> getFilteredBookings(
            @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
            @RequestParam("hallIds") List<Long> hallIds) {

        List<BookingDetails> bookings = bookingService.getFilteredBookings(fromDate, toDate, hallIds);
        List<GccBlockingDate> gccBlockedList = blockingDateRepository
                .findByFromDateAndToDateBetweenAndHallIdIn(fromDate, toDate, hallIds);

        Map<String, Object> response = new HashMap<>();
        response.put("bookings", bookings);
        response.put("blockedEvents", gccBlockedList);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = " GCC Blocked Booking Details REST API", description = "REST API to get the GCC Blocked BookingDetails")
    // retrieving the GCC Blocked BookingDetails
    @GetMapping("/blockedevents")
    public ResponseEntity<List<GccBlockingDate>> getBlockedEvents(
            @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
            @RequestParam("hallIds") List<Long> hallIds) {
        // System.out.println("Blocked Events Request - fromDate: " + fromDate + ",
        // toDate: " + toDate + ", hallIds: " + hallIds );
        List<GccBlockingDate> blockedBookings = bookingService.getBlockedEvents(fromDate, toDate, hallIds);

        // List<BookingDetails> bookingDetails =
        // bookingService.getByBookingId(fromDate,toDate,hallIds,blockingId,bookingId);

        if (blockedBookings.isEmpty()) {
            // System.out.println("No blocked bookings found.");
            return ResponseEntity.ok(blockedBookings);
        }

        for (GccBlockingDate blockedBooking : blockedBookings) {
            // System.out.println("Blocked Event: " + blockedBooking);
        }

        return ResponseEntity.ok(blockedBookings);
    }

    @Operation(summary = "UNBLOCK the gcc Blocked Event REST API", description = "REST API to unblocking the Gcc blocked Events for blocking")
    @GetMapping("/cancelByid")
    public ResponseEntity<String> cancellationById(@RequestParam int blockingId,
            @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        // Fetch the blocking date entity
        GccBlockingDate blockingDate = blockingDateRepository.findById(blockingId).orElse(null);

        if (blockingDate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blocking ID not found.");
        }

        // Get the booking IDs from the blocking date entity
        String bookingIds = blockingDate.getBookingIds();
        String hallIds = blockingDate.getHallId();

        // Convert the booking IDs to a list of integers
        List<Integer> bookingIdList = Arrays.stream(bookingIds.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        // Convert the hall IDs to a list of longs if provided
        List<Long> hallIdList = hallIds != null ? Arrays.stream(hallIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList())
                : Collections.emptyList();

        // Fetch the booking calendar entries within the date range
        List<BookingCalendar> bookingCalendars = bookingService.getBookedslots(fromDate, endDate, hallIdList);

        if (bookingCalendars.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No booking slots found for the given date range and hall IDs.");
        }

        // Iterate over bookingCalendars and update them as needed
        for (BookingCalendar bookingslots : bookingCalendars) {
            bookingslots.setBookingId_1(null);
            bookingslots.setBookingId_2(null);
            bookingslots.setMorningSlot(false);
            bookingslots.setEveningSlot(false);
            bookingslots.setIs_blocked(false);
            bookingCalendarRepository.save(bookingslots);
        }

        blockingDate.setBlockedStatus(GccBlockingDate.BlockedStatus.UNBLOCKED);
        blockingDateRepository.save(blockingDate);

        // Process each bookingId and update the booking details
        for (int bookingId : bookingIdList) {
            Optional<BookingDetails> bookingDetails = bookingDetailsRepo.findById(bookingId);
            if (bookingDetails.isPresent()) {
                BookingDetails bookings = bookingDetails.get();
                bookings.setBookingStatus(BookingDetails.BookingStatus.GCC_UNBLOCKED);
                bookings.setRefundStatus(BookingDetails.RefundStatus.NA);
                bookingDetailsRepo.save(bookings);
            }
        }
        String adminname = (String) session.getAttribute("usertype");
        Integer loginid = (Integer) session.getAttribute("id");
        ApplicationLogs logs = new ApplicationLogs();
        logs.setComments("Action by " + adminname);
        logs.setLoginId(loginid);
        logs.setLogType("Gcc UnBlocking for the Id:-" + blockingDate.getBlockingId());
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
        return ResponseEntity.ok("Success");
    }
    // @GetMapping("/cancelByid")
    // public ResponseEntity<String> cancellationById(
    // @RequestParam int blockingId,
    // @RequestParam int bookingId,
    // @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate
    // fromDate,
    // @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate
    // endDate,
    // @RequestParam(value = "hallId", required = false) List<Long> hallId) {
    //
    // System.out.println("Received blockingId: " + blockingId);
    // System.out.println("Received bookingId: " + bookingId);
    // System.out.println("Received fromDate: " + fromDate);
    // System.out.println("Received hallId: " + hallId);
    //
    // // Retrieve blocking date
    // GccBlockingDate blockingDate =
    // blockingDateRepository.findById(blockingId).orElse(null);
    //
    // if (blockingDate == null) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blocking ID not
    // found.");
    // }
    //
    // if (hallId != null && bookingId > 0) {
    // // Specific bookingId and hallId selected
    //
    // // Retrieve and update booking calendar
    // List<BookingCalendar> bookingCalendar =
    // bookingService.getBookedslots(fromDate, endDate, hallId);
    // if (bookingCalendar.isEmpty()) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No booking slots
    // found for the given date and hall ID.");
    // }
    //
    // BookingCalendar bookingSlots = bookingCalendar.get(0);
    // bookingSlots.setBookingId_1(null);
    // bookingSlots.setBookingId_2(null);
    // bookingSlots.setMorningSlot(false);
    // bookingSlots.setEveningSlot(false);
    //
    // // Process hallIds and bookingIds
    // List<String> hallIdList = Arrays.asList(blockingDate.getHallId().split(","));
    // hallIdList.remove(hallId.toString());
    // String updatedHallIds = String.join(",", hallIdList);
    //
    // List<String> bookingIdList =
    // Arrays.asList(blockingDate.getBookingIds().split(","));
    // bookingIdList.remove(Integer.toString(bookingId));
    // String updatedBookingIds = String.join(",", bookingIdList);
    //
    // // Update blockingDate with new values
    // blockingDate.setHallId(updatedHallIds);
    // blockingDate.setBookingIds(updatedBookingIds);
    //
    // // Create an entry in GccBlockingHistory
    // GccBlockingHistory blockingHistory = new GccBlockingHistory();
    // blockingHistory.setBlockedStatus(GccBlockingHistory.BlockedStatus.UNBLOCKED);
    // blockingHistory.setBlockingReason(blockingHistory.getBlockingReason());
    // blockingHistory.setCdate(new Date());
    // blockingHistory.setStartDate(blockingHistory.getStartDate());
    // blockingHistory.setEndDate(blockingHistory.getEndDate());
    // blockingHistory.setUpdatedBy(blockingHistory.getUpdatedBy());
    // blockingHistory.setBookingIds(updatedBookingIds);
    // blockingHistory.setHallId(updatedHallIds);
    // blockingHistoryRepository.save(blockingHistory);
    //
    // // Update the blocking date status
    // blockingDate.setBlockedStatus(GccBlockingDate.BlockedStatus.UNBLOCKED);
    // blockingDateRepository.save(blockingDate);
    //
    // // Save booking calendar updates
    // bookingCalendarRepository.save(bookingSlots);
    //
    // } else {
    // // All halls and bookings selected
    // // Retrieve and clear all bookings for the date range
    // List<BookingCalendar> bookingCalendars =
    // bookingService.getBookedslots(fromDate,endDate,hallId);
    // for (BookingCalendar bookingCalendar : bookingCalendars) {
    // bookingCalendar.setBookingId_1(null);
    // bookingCalendar.setBookingId_2(null);
    // bookingCalendar.setMorningSlot(false);
    // bookingCalendar.setEveningSlot(false);
    // bookingCalendarRepository.save(bookingCalendar);
    // }
    //
    // // Update blocking date status to UNBLOCKED
    // blockingDate.setBlockedStatus(GccBlockingDate.BlockedStatus.UNBLOCKED);
    // blockingDateRepository.save(blockingDate);
    //
    // // Create an entry in GccBlockingHistory
    // GccBlockingHistory blockingHistory = new GccBlockingHistory();
    // blockingHistory.setBlockedStatus(GccBlockingHistory.BlockedStatus.UNBLOCKED);
    // blockingHistory.setBlockingReason(blockingHistory.getBlockingReason());
    // blockingHistory.setCdate(new Date());
    // blockingHistory.setStartDate(blockingHistory.getStartDate());
    // blockingHistory.setEndDate(blockingHistory.getEndDate());
    // blockingHistory.setUpdatedBy(blockingHistory.getUpdatedBy());
    // blockingHistoryRepository.save(blockingHistory);
    // }
    //
    // return ResponseEntity.ok("Success");
    // }

    @Operation(summary = "HAll IMAGES by HallId REST API", description = "REST API to get the hall Images by Hall Id")
    // retrieving Hall Images by Hallid
    @GetMapping("/getImageById")
    public List<HallImages> getImageDetails(@RequestParam int hallId) {
        return hallService.getHallImages(hallId);
    }

    @Operation(summary = "GCC Blocking REST API", description = "REST API to Block the Halls by Admin")
    // GccBlocking Api
    @PostMapping("/cancelAll")
    public ResponseEntity<String> cancelAllBookings(
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate,
            @RequestParam("hallID") String hallID,
            @RequestParam("reasonforblocking") String reasonforblocking,
            @RequestParam(value = "selectedBookings", required = false) List<String> selectedBookings,
            @RequestParam(value = "description", required = false) String[] descriptions, // Handle descriptions
            @RequestParam(value = "document", required = false) MultipartFile[] documents, // Handle documents
            HttpSession session) {

        // Your existing logic
        String updatedBy = (String) session.getAttribute("employeeId");

        // Split and convert hall IDs
        List<Long> hallIdsList = Arrays.stream(hallID.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        // Call the service with descriptions and documents
        bookingService.cancelAllBookings(fromDate, toDate, hallIdsList, reasonforblocking, selectedBookings, updatedBy,
                descriptions, documents);
        String adminname = (String) session.getAttribute("usertype");
        Integer loginid = (Integer) session.getAttribute("id");
        ApplicationLogs logs = new ApplicationLogs();
        logs.setComments("Action by " + adminname);
        logs.setLoginId(loginid);
        logs.setLogType("Gcc Blocking for the Reason:-" + reasonforblocking);
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
        return ResponseEntity.ok("Success");
    }

    // @PostMapping("/blockhall")
    // public ResponseEntity<String> blockHall(
    // @RequestParam("blockingId") Integer blockingId,
    // @RequestParam("hallID") String hallID, // Ensure this matches the parameter
    // name used in AJAX
    // @RequestParam(value = "selectedBookings", required = false) List<String>
    // selectedBookings,
    // HttpSession session) {
    //
    // String updatedBy = (String) session.getAttribute("employeeId");
    // Optional<GccBlockingDate> gccblocking =
    // blockingDateRepository.findById(blockingId);
    // GccBlockingDate gccblockingdata = gccblocking.get();
    //
    // // Convert startDate and endDate to String
    // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    // String startDateString = sdf.format(gccblockingdata.getStartDate());
    // String endDateString = sdf.format(gccblockingdata.getEndDate());
    //
    // // Split and convert hall IDs
    // List<Long> hallIdsList = Arrays.stream(hallID.split(","))
    // .map(Long::parseLong)
    // .collect(Collectors.toList());
    //
    // System.out.println("selected BookingIds: " + selectedBookings);
    //
    // // Pass the formatted date strings
    // bookingService.cancelAllBookings(startDateString, endDateString, hallIdsList,
    // gccblockingdata.getDepartment(), gccblockingdata.getBlockingReason(),
    // selectedBookings, updatedBy);
    //
    // return ResponseEntity.ok("Success");
    // }

    @GetMapping("/bookingdetailsByuserId")
    public List<BookingDetails> getuserDetails(@RequestParam Integer userId) {
        return bookingService.getAllBookingsByUserId(userId);
    }

    @GetMapping("/settlement-approved")
    public ResponseEntity<List<Settlement>> getApprovedSettlements(HttpSession session) {
        List<Settlement> approvedSettlements = settlementRepository
                .findByBookingDetailsRefundStatus(RefundStatus.APPROVED);
        return ResponseEntity.ok(approvedSettlements);
    }
    // @PostMapping("/complete-refund")
    // public ResponseEntity<String> completeRefund(@RequestBody List<String>
    // bookingIds, HttpSession session, HttpServletResponse response) throws
    // IOException {
    // String token = (String) session.getAttribute("jwtToken");
    // //System.out.println("tokensssssssss" + token);
    //
    // // Validate JWT token
    // if (token != null && jwtUtil.validateToken(token)) {
    // // System.out.println("tokennnnnnnnnnnnnnns" + token);
    // try {
    // // System.out.println("Received booking IDs for refund: " + bookingIds);
    //
    // List<String> bookids = bookingService.processRefundRequests(bookingIds);
    //
    // if (!bookids.isEmpty()) {
    // // Prepare and return error response if there are errors
    // StringBuilder errorResponse = new StringBuilder("Errors processing refund for
    // IDs: ");
    // for (String bookId : bookids) {
    // errorResponse.append(bookId).append(", ");
    // }
    // return
    // ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
    // }
    //
    // return ResponseEntity.ok("Refund completed successfully");
    // } catch (Exception e) {
    // e.printStackTrace();
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error
    // processing refund: " + e.getMessage());
    // }
    // } else {
    // // Token is invalid or not present, redirect to login
    // session.invalidate(); // Invalidate the session to clear all session
    // attributes
    // response.sendRedirect("/login"); // Redirect to the login page
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return HTTP
    // 401 Unauthorized
    // }
    // }

    // @GetMapping("/Gccblocking")
    // public ResponseEntity<List<Object>>
    // getGccBlockingEvents(@RequestParam("hallId") int hallId) {
    // System.out.println("Received hallId: " + hallId);
    //
    // List<Object> results = bookingService.getGccBlockingEvents(hallId);
    //
    // return ResponseEntity.ok(results);
    // }
    @GetMapping("/Gccblocking")
    public ResponseEntity<List<Object>> getGccBlockingEvents(@RequestParam("hallId") String hallId) {
        // System.out.println("Received hallId: " + hallId);

        List<Object> results = bookingService.getGccBlockingEvents(hallId);

        return ResponseEntity.ok(results);
    }

    @GetMapping("/getblockedbookingdetails")
    public ResponseEntity<List<Map<String, Object>>> viewBlockedHallsByHallIdAndReason(
            @RequestParam String hallIds,
            @RequestParam int blockingId) {

        // System.out.println("Received hallIds: " + hallIds);
        // System.out.println("Received blockingId: " + blockingId);

        List<Integer> hallIdList;
        try {
            hallIdList = Arrays.stream(hallIds.split("\\s*,\\s*"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            System.err.println("Invalid hall ID format: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }

        Optional<GccBlockingDate> gccOpt = blockingDateRepository.findById(blockingId);
        if (!gccOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        GccBlockingDate savedGcc = gccOpt.get();
        LocalDate startDate = LocalDate.ofInstant(savedGcc.getStartDate().toInstant(), ZoneId.systemDefault());
        LocalDate endDate = LocalDate.ofInstant(savedGcc.getEndDate().toInstant(), ZoneId.systemDefault());

        List<Map<String, Object>> result = new ArrayList<>();

        for (int hallId : hallIdList) {
            int bookingCount = bookingDetailsRepository.countByHallIdAndBookingDateBetween(hallId, startDate, endDate);
            // System.out.println("bookingCount==="+bookingCount);
            Optional<HallDetails> hallDetailsOpt = hallDetailsRepository.findById(hallId);

            if (hallDetailsOpt.isPresent()) {
                HallDetails hallDetails = hallDetailsOpt.get();
                List<Integer> bookingIds = bookingDetailsRepository.findBookingIdsByHallIdAndBookingDateBetween(hallId,
                        startDate, endDate);
                // System.out.println("Booking IDs for hallId " + hallId + ": " + bookingIds);
                // // Add this line

                Map<String, Object> hallInfo = new HashMap<>();
                hallInfo.put("hallName", hallDetails.getHallName());
                hallInfo.put("zone", hallDetails.getZone());
                hallInfo.put("division", hallDetails.getDivision());
                hallInfo.put("bookingCount", bookingCount);
                hallInfo.put("bookingIds", bookingIds);
                hallInfo.put("hallId", hallId);

                result.add(hallInfo);
            }
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/getbookingdetails")
    public ResponseEntity<List<BookingDetails>> getBookingDetails(@RequestParam("bookingId") Long bookingId) {
        // System.out.println("Received bookingId: " + bookingId);
        try {
            List<BookingDetails> details = bookingService.findBookingDetailsByBookingId(bookingId);
            // System.out.println("bookingdetails==="+details);
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/blockedHallsByHallIdAndReason")
    @ResponseBody
    public Map<String, Object> viewBlockedHallsByHallIdAndReason(
            @RequestParam String department,
            @RequestParam String blockingReason,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {

        List<GccBlockingDate> blockedHalls = blockingDateRepository.findByBlockingReasonAndDateRange(blockingReason,
                startDate, endDate);

        // Ensure distinct by hallId and other keys
        List<GccBlockingDate> uniqueGccBlockingDates = blockedHalls.stream()
                .filter(distinctByKey(
                        g -> Arrays.asList(g.getHallId(), g.getBlockingReason(), g.getStartDate(), g.getEndDate())))
                .collect(Collectors.toList());

        // Prepare response data
        Map<String, Object> response = new HashMap<>();
        if (!uniqueGccBlockingDates.isEmpty()) {
            GccBlockingDate firstBlockedHall = uniqueGccBlockingDates.get(0); // Adjust as necessary
            response.put("blockingReason", firstBlockedHall.getBlockingReason());
            response.put("cdate", firstBlockedHall.getCdate());
            response.put("startDate", firstBlockedHall.getStartDate());
            response.put("endDate", firstBlockedHall.getEndDate());

            List<Map<String, Object>> hallDetailsList = new ArrayList<>();
            for (GccBlockingDate blockedHall : uniqueGccBlockingDates) {
                // Split the hallId string into individual hall IDs
                String[] hallIds = blockedHall.getHallId().split(",");

                for (String hallIdStr : hallIds) {
                    try {
                        int hallId = Integer.parseInt(hallIdStr.trim());
                        HallDetails hall = hallService.getHallById(hallId);
                        if (hall != null) {
                            Map<String, Object> hallDetails = new HashMap<>();
                            hallDetails.put("hallName", hall.getHallName());
                            hallDetails.put("zone", hall.getZone());
                            hallDetails.put("division", hall.getDivision());
                            hallDetails.put("numOfBookings", calculateBookings(hall, blockedHall));
                            hallDetails.put("refundedAmount", calculateRefund(hall, blockedHall));
                            hallDetailsList.add(hallDetails);
                        }
                    } catch (NumberFormatException e) {
                        // Handle the case where hallId is not a valid integer
                        e.printStackTrace();
                    }
                }
            }
            response.put("halls", hallDetailsList);
        } else {
            response.put("blockingReason", "N/A");
            response.put("cdate", "N/A");
            response.put("startDate", "N/A");
            response.put("endDate", "N/A");
            response.put("halls", Collections.emptyList());
        }
        return response;
    }

    // Utility method to ensure distinct by a key
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = new HashSet<>();
        return t -> seen.add(keyExtractor.apply(t));
    }

    // Method to calculate number of bookings
    private int calculateBookings(HallDetails hall, GccBlockingDate blockedHall) {
        return blockingDateRepository.countBlockedBookings(hall.getHallId(), blockedHall.getStartDate(),
                blockedHall.getEndDate());
    }

    private double calculateRefund(HallDetails hall, GccBlockingDate blockedHall) {
        // Implement logic to calculate refund
        return 0.0;
    }

    // admin dashboard
    @GetMapping("/bookingstatistics")
    public ResponseEntity<Map<String, Map<String, Integer>>> getBookingStatistics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false, defaultValue = "ALL") String zone,
            @RequestParam(required = false, defaultValue = "ALL") String division,
            @RequestParam(required = false, defaultValue = "1000") Integer hallid,
            @RequestParam(required = false, defaultValue = "ALL") String userType) {

        try {
            Map<String, Map<String, Integer>> statistics = bookingService.getBookingStatistics(fromDate, toDate, zone,
                    division, hallid, userType);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            // Log the exception and return an appropriate error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyMap());
        }
    }

    @GetMapping("/getHallsDetailsByZone")
    public ResponseEntity<Map<String, Object>> getHallsDetailsByZone(@RequestParam String zone) {
        Map<String, Object> hallzones = hallDetailService.getHallsDetailsByZone(zone);
        // Debug log to verify the response before sending it to the client
        // System.out.println("Response from getHallsDetailsByZone: " + hallzones);
        return ResponseEntity.ok(hallzones);
    }

    @GetMapping("/byhallid")
    public ResponseEntity<List<Map<String, Object>>> getBookingDetailsByHallId(@RequestParam int hallId) {
        List<Map<String, Object>> bookingDetails = bookingService.getBookingDetailsByHallId(hallId);
        return ResponseEntity.ok(bookingDetails);
    }

    @PostMapping("/savenewHallImages")
    public ResponseEntity<?> updateHallImages(@RequestParam("hallId") Integer hallId,
            @RequestParam("newHallImages") MultipartFile[] newHallImages) {
        // Call the service method to update the hall images
        hallService.updatenewHallImages(hallId, newHallImages);
        return ResponseEntity.ok().build();
    }

    // @DeleteMapping("/deleteHallImage/{hallImageId}")
    // public ResponseEntity<?> deleteHallImage(@PathVariable Long hallImageId) {
    // // Call the service method to delete the hall image
    // hallService.deleteHallImage(hallImageId);
    // return ResponseEntity.ok().build();
    // }

    @PutMapping("/updateHallImage/{hallImageId}")
    public ResponseEntity<Map<String, String>> updateHallImage(
            @PathVariable Integer hallImageId,
            @RequestParam("hallImage") MultipartFile hallImage) throws IOException {

        try {
            String newImageUrl = hallService.updateHallImage(hallImageId, hallImage);
            Map<String, String> response = new HashMap<>();
            response.put("newImageUrl", newImageUrl);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to update image"));
        }
    }

    @DeleteMapping("/deleteHallImage/{hallImageId}")
    public ResponseEntity<String> deleteHallImage(@PathVariable Integer hallImageId) {
        try {
            // Find the image by ID and delete it
            hallImagesRepository.deleteById(hallImageId);
            return ResponseEntity.ok("Image deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting image.");
        }
    }

    @GetMapping("/bookingdetailsByzone")
    public ResponseEntity<List<BookingDetails>> getBookingDetailsByZone(@RequestParam String zone) {
        List<BookingDetails> bookingDetails = bookingDetailsRepository.findByZone(zone);
        return ResponseEntity.ok(bookingDetails);
    }

    @GetMapping("/divisionsByZone")
    public List<String> getDivisionsByZone(@RequestParam("zoneId") String zoneId) {
        // Use zoneId instead of zone
        return hallDetailsRepository.findDistinctDivisionsByZone(zoneId);
    }

    // Fetch hall names by division
    @GetMapping("/hallsByDivision")
    public ResponseEntity<List<String>> getHallsByDivision(@RequestParam String division) {
        List<String> halls = hallDetailsRepository.findHallsByDivision(division);
        if (halls.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(halls, HttpStatus.OK);
    }

    @PostMapping("/manualrefund")
    public ResponseEntity<?> manualRefund(
            @RequestParam int bookingId,
            @RequestParam String txansactionId,
            @RequestParam int userId,
            @RequestParam String code,
            @RequestParam String amount) throws Exception {

        // Check if the code matches the value from AppConfig
        if (code.equals(appConfig.getManualRefundCode())) {
            // Call the refund method from PaymentService
            String response = paymentGatewayServiceImpl.manualrefundTheAmount(bookingId, userId, txansactionId, amount);
            // Check the response from the payment service
            if ("Refunded".equals(response)) {
                return ResponseEntity.ok("Refund processed successfully");
            } else {
                // If there was an error in processing the refund
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing refund.");
            }
        } else {
            // Return error message if the code doesn't match
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Code is wrong");
        }
    }

    // gccapps
    @GetMapping("/viewsettlement-pending")
    public ResponseEntity<Map<String, Object>> viewSettlementPendingMaster(HttpSession session) {

        List<BookingDetails> settlement = bookingDetailsRepository.findAllWithPendingRefundStatus();
        List<BookingDetails> cancelledbookings = bookingDetailsRepository.findPendingCancelledList();
        List<BookingDetails> rejectedbookings = bookingDetailsRepository.findPendingRejectedList();

        Map<String, Object> response = new HashMap<>();
        response.put("settlementlist", settlement);
        response.put("cancelledbookings", cancelledbookings);
        response.put("rejectedbookings", rejectedbookings);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/viewsettlement-complete")
    public ResponseEntity<Map<String, Object>> viewSettlementcompleteMaster(HttpSession session) {

        List<BookingDetails> completedlist = bookingDetailsRepository.findAllwithCompletedRefundStatus();
        List<BookingDetails> Completedcancelledbookings = bookingDetailsRepository.findCompletedCancelledList();
        List<BookingDetails> Completedrejectedbookings = bookingDetailsRepository.findCompletedRejectedList();

        Map<String, Object> response = new HashMap<>();
        response.put("completedlist", completedlist);
        response.put("Completedcancelledbookings", Completedcancelledbookings);
        response.put("Completedrejectedbookings", Completedrejectedbookings);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/viewsettlement-approved")
    public ResponseEntity<Map<String, Object>> viewSettlementApprovedMaster(HttpSession session) {

        List<BookingDetails> approvedSettlements = bookingDetailsRepository.findAllWithApprovedRefundStatus();
        List<BookingDetails> settlements = new ArrayList<>(approvedSettlements);
        List<BookingDetails> approvedCancelledBookings = bookingDetailsRepository.findCancelledList();
        List<BookingDetails> approvedRejectedBookings = bookingDetailsRepository.findRejectedList();

        Map<String, Object> response = new HashMap<>();
        response.put("settlementList", settlements);
        response.put("approvedCancelledBookings", approvedCancelledBookings);
        response.put("approvedRejectedBookings", approvedRejectedBookings);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/viewsettlement-reject")
    public ResponseEntity<Map<String, Object>> viewSettlementRejectedMaster(HttpSession session) {

        List<BookingDetails> settlementReject = bookingDetailsRepository.findSettlementRejected();

        Map<String, Object> response = new HashMap<>();
        response.put("settlementlist", settlementReject);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/viewsettlement-initiated")
    public ResponseEntity<Map<String, Object>> viewSettlementInitiateMaster(HttpSession session) {

        List<BookingDetails> settlement = bookingDetailsRepository.findByBookingDetailsRefundStatus();
        List<BookingDetails> Initiatedcancelledbookings = bookingDetailsRepository.findInitiatedCancelledList();
        List<BookingDetails> Initiatedrejectedbookings = bookingDetailsRepository.findInitiatedRejectedList();

        Map<String, Object> response = new HashMap<>();
        response.put("settlementlist", settlement);
        response.put("Initiatedcancelledbookings", Initiatedcancelledbookings);
        response.put("Initiatedrejectedbookings", Initiatedrejectedbookings);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-pending")
    public ResponseEntity<List<Users>> viewApproval() {
        List<Users> pendingList = userRepository.getPendingList();
        return new ResponseEntity<>(pendingList, HttpStatus.OK);
    }

    @GetMapping("/user-approved")
    public ResponseEntity<List<Users>> viewUserApproved() {
        List<Users> approved = userRepository.getUserApproved();
        return new ResponseEntity<>(approved, HttpStatus.OK);
    }

    @GetMapping("/user-reject")
    public ResponseEntity<List<Users>> viewUserReject() {
        List<Users> rejected = userRepository.getUserReject();
        return new ResponseEntity<>(rejected, HttpStatus.OK);
    }

    @GetMapping("/halldetailslist")
    public ResponseEntity<List<HallDetails>> getHallDetailsList() {
        List<HallDetails> list = hallDetailsRepository.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/discountRange")
    public ResponseEntity<List<DiscountRange>> getAllDiscountRanges() {
        List<DiscountRange> discount = discountRangeRepo.findAll();
        return ResponseEntity.ok(discount);
    }

    @PostMapping("/getReport")
    public ResponseEntity<Map<String, Object>> generateReport(
            @RequestParam(name = "FromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fromDate,
            @RequestParam(name = "ToDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String toDate,
            @RequestParam(name = "request") String request,
            @RequestParam(name = "userstatus", required = false) String userstatus,
            @RequestParam(name = "usertype", required = false) String usertype,
            @RequestParam(name = "bookingStatus", required = false) String bookingStatus,
            @RequestParam(name = "refundStatus", required = false) String refundStatus,
            @RequestParam(name = "bookingId", required = false) Long bookingId) {

        Map<String, Object> response = new HashMap<>();

        List<Users> userList = handleUserRequest(request, fromDate, toDate, userstatus, usertype);
        List<Object[]> bookingList = handleBookingRequest(request, fromDate, toDate, bookingStatus, bookingId);
        List<BookingDetails> bookingSettlementList = handleBookingSettlementRequest(request, fromDate, toDate,
                refundStatus);
        List<Object[]> paymentList = request.equals("3") ? bookingService.getAllPaymentsDetails(fromDate, toDate)
                : new ArrayList<>();
        List<Object[]> userbookingList = request.equals("4")
                ? bookingService.getBookingUserDetailsReport(fromDate, toDate)
                : new ArrayList<>();
        List<Object[]> hallbookingList = request.equals("5")
                ? bookingService.getBookingHallDetailsReport(fromDate, toDate)
                : new ArrayList<>();

        response.put("users", userList);
        response.put("bookings", bookingList);
        response.put("fromdate", fromDate);
        response.put("todate", toDate);
        response.put("userstatus", userstatus);
        response.put("bookingSettlementList", bookingSettlementList);
        response.put("usertype", usertype);
        response.put("bookingStatus", bookingStatus);
        response.put("refundStatus", refundStatus);
        response.put("request", request);
        response.put("paymentLists", paymentList);
        response.put("userbookingLists", userbookingList);
        response.put("hallbookingLists", hallbookingList);
        response.put("bookingId", bookingId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private List<Users> handleUserRequest(String request, String fromDate, String toDate, String userstatus,
            String usertype) {
        if (!request.equals("1")) {
            return new ArrayList<>();
        }
        if ((userstatus != null && !userstatus.isEmpty()) && (usertype != null && !usertype.isEmpty())) {
            return userService.getAllUsersWithFilters(fromDate, toDate, userstatus, usertype);
        }
        if (userstatus.isEmpty() && (usertype != null && !usertype.isEmpty())) {
            return userService.getAllUsersWithTypeFilters(fromDate, toDate, usertype);
        }
        if (usertype.isEmpty() && (userstatus != null && !userstatus.isEmpty())) {
            return userService.getAllUsersWithStatusFilters(fromDate, toDate, userstatus);
        }
        return userService.getAllUsers(fromDate, toDate);
    }

    private List<Object[]> handleBookingRequest(String request, String fromDate, String toDate, String bookingStatus,
            Long bookingId) {
        if (!request.equals("2")) {
            return new ArrayList<>();
        }
        if (bookingStatus != null && !bookingStatus.isEmpty()) {
            return bookingDetailsRepo.getAllBookingDetailsWithStatus(fromDate, toDate, bookingStatus);
        } else if (bookingId != null) {
            return bookingDetailsRepo.getAllBookingDetailsWithBookingId(fromDate, toDate, bookingId);
        } else {
            return bookingDetailsRepo.getAllBookingDetails(fromDate, toDate);
        }
    }

    private List<BookingDetails> handleBookingSettlementRequest(String request, String fromDate, String toDate,
            String refundStatus) {
        if (!request.equals("6")) {
            return new ArrayList<>();
        }
        if (refundStatus != null && !refundStatus.isEmpty()) {
            return bookingDetailsRepository.getAllBookingsettlementDetailsWithStatus(fromDate, toDate, refundStatus);
        }
        return bookingDetailsRepository.getAllBookingsettlementDetails(fromDate, toDate);
    }

    @GetMapping("/getHallDetailsById")
    public ResponseEntity<List<Map<String, Object>>> getHallDetailsById(@RequestParam Integer hall_id) {
        List<Map<String, Object>> halldetails = bookingService.getHallDetailsForOfficer(hall_id);
        return ResponseEntity.ok(halldetails);
    }

    @GetMapping("/listgccblocking")
    public ResponseEntity<Map<String, Object>> getGccBlocking() {

        // Fetch all blocked GCC dates
        List<GccBlockingDate> gccBlocked = blockingDateRepository.findAllBlocked();

        // Ensure distinct by hallId, blockingReason, startDate, and endDate
        List<GccBlockingDate> uniqueGccBlockingDates = gccBlocked.stream()
                .filter(distinctByKey(g -> Arrays.asList(
                        g.getHallId(),
                        g.getBlockingReason(),
                        g.getStartDate(),
                        g.getEndDate())))
                .collect(Collectors.toList());

        // Map to store the count of blocked halls for each GccBlockingDate
        Map<Integer, Integer> hallCountMap = new HashMap<>();
        for (GccBlockingDate blockedDate : uniqueGccBlockingDates) {
            // Parse and count unique hall IDs
            String hallIds = blockedDate.getHallId();
            if (hallIds != null && !hallIds.isEmpty()) {
                Set<String> uniqueHallIds = new HashSet<>(Arrays.asList(hallIds.split(",")));
                int hallCount = uniqueHallIds.size();
                hallCountMap.put(blockedDate.getBlockingId(), hallCount);
            } else {
                hallCountMap.put(blockedDate.getBlockingId(), 0); // No halls blocked
            }
        }

        // Prepare the response data
        Map<String, Object> response = new HashMap<>();
        response.put("gccblocked", uniqueGccBlockingDates);
        response.put("hallCountMap", hallCountMap);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/details")
    public ResponseEntity<List<Object[]>> getUserDetails(
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate) {
        List<Object[]> userDetails = userRepository.findUserDetailsByBookingDate(fromDate, toDate);
        return ResponseEntity.ok(userDetails);
    }

    @GetMapping("/getPaymentswithRefunds")
    public List<Object[]> getPaymentsWithRefunds(@RequestParam String startDate, @RequestParam String endDate) {
        return userRepository.findPaymentsWithRefunds(startDate, endDate);
    }

    @GetMapping("/getpaymentswithoutRefunds")
    public List<Object[]> getPaymentsWithoutRefunds(@RequestParam String startDate, @RequestParam String endDate) {
        return userRepository.findPaymentsWithoutRefunds(startDate, endDate);
    }

    @GetMapping("/getRefundDetails")
    public List<Object[]> getPaymentsWithRefundsOnly(@RequestParam String startDate, @RequestParam String endDate) {
        return userRepository.findPaymentsWithRefundsOnly(startDate, endDate);
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody Map<String, String> messagePayload) {
        String userMessage = messagePayload.get("message");

        // Process the user message and prepare a response
        String response = processMessage(userMessage);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("response", response);

        return ResponseEntity.ok(responseBody);
    }

    private String processMessage(String userMessage) {
        // Simple logic to return a response, could be more advanced (AI, NLP, etc.)
        if (userMessage.toLowerCase().contains("hello")) {
            return "Hello! How can I assist you today?";
        } else {
            return "Thanks for your message! We will get back to you shortly.";
        }
    }

    @PostMapping("/uploadnoc")
    public ResponseEntity<String> uploadnoc(@RequestParam("nocFile") MultipartFile file,
            @RequestParam("bookingid") int bookingId) {
        try {
            System.out.println("Received file: " + file.getOriginalFilename());
            System.out.println("Booking ID: " + bookingId);

            bookingService.uploadNOCFile(file, bookingId);
            return ResponseEntity.ok("File uploaded and saved successfully");
        } catch (Exception e) {
            System.err.println("Error during file upload: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        }
    }
}
