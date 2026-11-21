package com.gcc.communityHall.controller;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcc.communityHall.entity.GccBlockingDate;
import com.gcc.communityHall.others.AppConfig;
import com.gcc.communityHall.repository.GccBlockingDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.Users;
import com.gcc.communityHall.repository.BookingDetailsRepository;
import com.gcc.communityHall.repository.UsersRepository;
import com.gcc.communityHall.service.BookingService;
import com.gcc.communityHall.service.HallDetailService;
import com.gcc.communityHall.service.ReasonMasterService;
import com.gcc.communityHall.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/victoriahallbooking_uat/headquarters")
public class HeadquartersController {
    @Autowired
    HallDetailService hallDetailService;
    @Autowired
    ReasonMasterService reasonMasterService;
    @Autowired
    private BookingDetailsRepository bookingDetailsRepo;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;
    @Autowired
    private BookingDetailsRepository bookingDetailsRepository;

    @Autowired
    private GccBlockingDateRepository blockingDateRepository;

    @Autowired
    private AppConfig appConfig;

    @GetMapping("/dashboard")
    public String viewDashboard(
            @RequestParam(required = false, defaultValue = "all zones") String zone,
            Model model, HttpSession session) {

        String headquartersname = (String) session.getAttribute("usertype");
        if (headquartersname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            List<Map<String, Object>> getMonthCount = bookingDetailsRepository.getAllAdminmonthcount(zone);
            List<Map<String, Object>> count = bookingDetailsRepository.getAdmincount(zone);
            Map<String, Object> map = count.get(0);

            model.addAttribute("mounthcount", getMonthCount);
            model.addAttribute("APPROVED", map.get("approved_count"));
            model.addAttribute("REJECTED", map.get("rejected_count"));
            model.addAttribute("PENDING", map.get("pending_count"));
            model.addAttribute("CANCELED", map.get("CANCELED"));
            model.addAttribute("PAYMENT_FAILURE", map.get("PAYMENT_FAILURE"));
            model.addAttribute("GCC_BLOCKED", map.get("GCC_BLOCKED"));
            model.addAttribute("headquartersname", headquartersname);
            model.addAttribute("zone", zone);

            return "headquarters/dashboard";
        } else {
            // Token is invalid or not present, redirect to logout
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }


    @GetMapping("/Reports")
    public String viewReports(Model model, HttpSession session) {
        String headquartersname = (String) session.getAttribute("usertype");
        if (headquartersname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("headquartersname", headquartersname);
            return "headquarters/reports";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/getReport")
    public String getReports(Model model, HttpSession session) {
        String headquartersname = (String) session.getAttribute("usertype");
        if (headquartersname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("headquartersname", headquartersname);
            return "headquarters/reports";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/approval-history")
    public String viewApproval(Model model, HttpSession session) {
        String headquartersname = (String) session.getAttribute("usertype");
        if (headquartersname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("headquartersname", headquartersname);
            model.addAttribute("title", "approvalcontrol");
            List<Users> Pending = usersRepository.getPendingList();
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
            model.addAttribute("pendinglist", Pending);
            return "headquarters/approval-history";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/user-approved")
    public String viewUserApproved(Model model, HttpSession session) {
        String headquartersname = (String) session.getAttribute("usertype");
        if (headquartersname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("headquartersname", headquartersname);
            model.addAttribute("title", "reasondetails");
            List<Users> approved = usersRepository.getUserApproved();
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
            model.addAttribute("approvedlist", approved);
            return "headquarters/user-approved";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/user-reject")
    public String viewUserReject(Model model, HttpSession session) {
        String headquartersname = (String) session.getAttribute("usertype");
        if (headquartersname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("headquartersname", headquartersname);
            model.addAttribute("title", "reasondetails");
            List<Users> reject = usersRepository.getUserReject();
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
            model.addAttribute("rejectedlist", reject);
            return "headquarters/user-reject";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @PostMapping("/getReport")
    public String generateReport(Model model,
                                 @RequestParam(name = "FromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fromDate,
                                 @RequestParam(name = "ToDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String toDate,
                                 @RequestParam(name = "request") String request,
                                 @RequestParam(name = "userstatus", required = false) String userstatus,
                                 @RequestParam(name = "usertype", required = false) String usertype,
                                 @RequestParam(name = "bookingStatus", required = false) String bookingStatus,
                                 @RequestParam(name = "refundStatus", required = false) String refundStatus,
                                 @RequestParam(name = "paymentStatus", required = false) String paymentStatus,
                                 @RequestParam(name = "filterby", required = false) String filterby,
                                 @RequestParam(name = "zone1", required = false, defaultValue = "ALL") String zone,
                                 @RequestParam(name = "division1", required = false, defaultValue = "ALL") String division,
                                 @RequestParam(name = "hall1", required = false) Integer hallid,
                                 @RequestParam(name = "basedon", required = false) String basedon,
                                 @RequestParam(name = "bookingId", required = false) Long bookingId,
                                 @RequestParam(name = "reporttype", required = false) String reporttype,
                                 @RequestParam(name = "counterId", required = false) String  counterId, HttpSession session) throws JsonProcessingException {
        String headquartersname = (String) session.getAttribute("usertype");
        if (headquartersname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            List<Users> userList = handleUserRequest(request, fromDate, toDate, userstatus, usertype);
            List<Object[]> bookingList = handleBookingRequest(request, fromDate, toDate, bookingStatus, filterby, bookingId, zone, division, hallid);
            List<BookingDetails> bookingSettlementList = handleBookingSettlementRequest(request, fromDate, toDate, refundStatus, filterby);
            //List<Object[]> paymentList = request.equals("3") ? bookingService.getAllPaymentsDetails(fromDate, toDate,zone,division,hallid) : new ArrayList<>();
            List<Object[]> paymentList = new ArrayList<>();
            List<Object[]> extrapaymentList = new ArrayList<>();
            if (request.equals("3")) {
                if (paymentStatus == null || paymentStatus.isEmpty()) {
                    paymentList = bookingService.getAllPaymentsDetails(fromDate, toDate, null, zone, division, hallid);
                } else {
                    paymentList = bookingService.getAllPaymentsDetails(fromDate, toDate, paymentStatus, zone, division, hallid);
                }
            }
            if (request.equals("8")) {
                if (paymentStatus == null || paymentStatus.isEmpty()) {
                    extrapaymentList = bookingService.getAllExtraPaymentsDetails(fromDate, toDate, null, zone, division, hallid);
                } else {
                    extrapaymentList = bookingService.getAllExtraPaymentsDetails(fromDate, toDate, paymentStatus, zone, division, hallid);
                }
            }
            List<Object[]> userbookingList = request.equals("4") ? bookingService.getBookingUserDetailsReport(fromDate, toDate) : new ArrayList<>();
            //List<Object[]> hallbookingList = request.equals("5") ? bookingService.getBookingHallDetailsReport(fromDate, toDate) : new ArrayList<>();
            // In your controller method
            // Fetch booking count by zone
            List<Object[]> totalcount = new ArrayList<>();
            int totalBookings = 0;
            if ("7".equals(request)) {
                if ("zone".equals(basedon)) {
                    totalcount = bookingDetailsRepository.countBookingsByZone();
                    // Format zone data
                    totalcount = totalcount.stream()
                            .map(data -> new Object[]{"Zone-" + data[0], data[1]})
                            .collect(Collectors.toList());
                } else if ("division".equals(basedon)) {
                    totalcount = bookingDetailsRepository.countBookingsByDivisions();
                    // Format division data
                    totalcount = totalcount.stream()
                            .map(data -> new Object[]{"Division-" + data[0], data[1]})
                            .collect(Collectors.toList());
                }

                totalBookings = totalcount.stream()
                        .mapToInt(data -> ((Number) data[1]).intValue())
                        .sum();
            }
            model.addAttribute("basedon", basedon);
            model.addAttribute("totalcount", totalcount);
            model.addAttribute("totalBookings", totalBookings);
            List<Object[]> hallbookingList = handleBookingHallDetailsReportRequest(request, fromDate, toDate, filterby, zone, division, hallid);

            if ("9".equals(request)) {
                if("abstract".equals(reporttype)){
                    List<Object[]> counterBookingList = new ArrayList<>();
                    counterBookingList = bookingService.getCounterWiseBookingCount(fromDate, toDate);
                    model.addAttribute("counterBookingList", counterBookingList);
                    System.out.println("counterBookingList.." + counterBookingList);

                    List<Object[]> hallBookingByCounter = bookingService.getHallWiseBookingDetails(fromDate, toDate, counterId);
                    // Convert to JSON for JS usage
                    String hallBookingJson = new ObjectMapper().writeValueAsString(hallBookingByCounter);
                    model.addAttribute("hallBookingJson", hallBookingJson);


                    List<Object[]> hallBookingDetails = bookingService.getBookingDetailsForAllHalls(fromDate, toDate, counterId, hallid);
                    String hallBookingDetailsJson = new ObjectMapper().writeValueAsString(hallBookingDetails);
                    model.addAttribute("hallBookingDetailsJson", hallBookingDetailsJson);

                } else {
                    List<Object[]> counterBookingList = new ArrayList<>();
                    counterBookingList = bookingService.getCounterWiseBookingDetails(fromDate, toDate);
                    model.addAttribute("counterBookingList", counterBookingList);
                }
                model.addAttribute("reporttype", reporttype);
            }

            model.addAttribute("filterby", filterby);
            model.addAttribute("users", userList);
            model.addAttribute("bookings", bookingList);
            model.addAttribute("fromdate", fromDate);
            model.addAttribute("todate", toDate);
            model.addAttribute("userstatus", userstatus);
            model.addAttribute("bookingSettlementList", bookingSettlementList);
            model.addAttribute("usertype", usertype);
            model.addAttribute("bookingStatus", bookingStatus);
            model.addAttribute("refundStatus", refundStatus);
            model.addAttribute("paymentStatus", paymentStatus);
            model.addAttribute("request", request);
            model.addAttribute("paymentLists", paymentList);
            model.addAttribute("extrapaymentLists", extrapaymentList);
            model.addAttribute("userbookingLists", userbookingList);
            model.addAttribute("hallbookingLists", hallbookingList);
            model.addAttribute("bookingId", bookingId);
            model.addAttribute("zone1", zone);
            model.addAttribute("division1", division);
            model.addAttribute("hall1", hallid);
            model.addAttribute("headquartersname", headquartersname);

            return "headquarters/reports";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    private List<Object[]> handleBookingHallDetailsReportRequest(String request, String fromDate, String toDate, String filterby, String zone, String division, Integer hallid) {
        if (!request.equals("5")) {
            return new ArrayList<>();
        }
        if (filterby.isEmpty()) {
            return bookingService.getBookingHallDetailsReportByBookingDate(fromDate, toDate, zone, division, hallid);
        }
        if ("1".equals(filterby)) {

            return bookingService.getBookingHallDetailsReportByBookingDate(fromDate, toDate, zone, division, hallid);

        }
        if ("2".equals(filterby)) {

            return bookingService.getBookingHallDetailsReportByEventDate(fromDate, toDate, zone, division, hallid);

        }

        return new ArrayList<>();
    }

    private List<Users> handleUserRequest(String request, String fromDate, String toDate, String userstatus, String usertype) {
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


    private List<Object[]> handleBookingRequest(String request, String fromDate, String toDate, String bookingStatus, String filterby, Long bookingId, String zone, String division, Integer hallid) {
        if (!request.equals("2")) {
            return new ArrayList<>();
        }
        if (filterby.isEmpty() && bookingStatus.isEmpty() && bookingId == null) {
            return bookingService.getBookingDetailsByBookingDate(fromDate, toDate, zone, division, hallid);
        }
        if (bookingId != null && bookingStatus.isEmpty()) {
            return bookingDetailsRepo.getAllBookingDetailsWithBookingId(fromDate, toDate, bookingId);
        }
        if (bookingId != null && !bookingStatus.isEmpty()) {
            return bookingDetailsRepo.getAllBookingDetailsWithBookingIdwithbookingStatus(fromDate, toDate, bookingId, bookingStatus);
        }
        if (bookingId == null && filterby.isEmpty() && !bookingStatus.isEmpty()) {
            return bookingService.getBookingDetailsByBookingDateWithStatus(fromDate, toDate, bookingStatus, zone, division, hallid);
        }
        if (!filterby.isEmpty()) {

            if ("1".equals(filterby)) {
                if (bookingStatus.isEmpty()) {
                    return bookingService.getBookingDetailsByBookingDate(fromDate, toDate, zone, division, hallid);
                } else {
                    return bookingService.getBookingDetailsByBookingDateWithStatus(fromDate, toDate, bookingStatus, zone, division, hallid);
                }

            }
            if ("2".equals(filterby)) {
                if (bookingStatus.isEmpty()) {
                    return bookingService.getBookingDetailsByEventDate(fromDate, toDate, zone, division, hallid);
                } else {
                    return bookingService.getBookingDetailsByEventDateWithStatus(fromDate, toDate, bookingStatus, zone, division, hallid);
                }

            }
        }
        // Handle default or error case
        return new ArrayList<>();
    }


    private List<BookingDetails> handleBookingSettlementRequest(String request, String fromDate, String toDate, String refundStatus, String filterby) {
        if (!request.equals("6")) {
            return new ArrayList<>();
        }
        if (filterby.isEmpty() && refundStatus.isEmpty()) {
            return bookingService.getAllBookingsettlementDetailsByBookingDate(fromDate, toDate);
        }
        if (filterby.isEmpty() && !refundStatus.isEmpty()) {
            return bookingService.getAllBookingsettlementDetailsByBookingDateWithStatus(fromDate, toDate, refundStatus);
        }
        if (!filterby.isEmpty()) {

            if ("1".equals(filterby)) {
                if (refundStatus.isEmpty()) {
                    return bookingService.getAllBookingsettlementDetailsByBookingDate(fromDate, toDate);
                } else {
                    return bookingService.getAllBookingsettlementDetailsByBookingDateWithStatus(fromDate, toDate, refundStatus);
                }

            }
            if ("2".equals(filterby)) {
                if (refundStatus.isEmpty()) {
                    return bookingService.getAllBookingsettlementDetailsByEventDate(fromDate, toDate);
                } else {
                    return bookingService.getAllBookingsettlementDetailsByEventDateWithStatus(fromDate, toDate, refundStatus);
                }

            }
        }

        // Handle default or error case
        return new ArrayList<>();
    }

    @GetMapping("/ongoingevents")
    public String viewRecentevents(Model model, HttpSession session) {
        String headquartersname = (String) session.getAttribute("usertype");
        if (headquartersname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            List<BookingDetails> presenteventslist = bookingDetailsRepository.findPresentBookingList();
            List<BookingDetails> pasteventslist = bookingDetailsRepository.findPastEventsList();
            List<BookingDetails> futureeventslist = bookingDetailsRepository.findFutureEventsList();
            model.addAttribute("title", "settlement initiate");
            model.addAttribute("headquartersname", headquartersname);
            model.addAttribute("presenteventslist", presenteventslist);
            model.addAttribute("pasteventslist", pasteventslist);
            model.addAttribute("futureeventslist", futureeventslist);
            return "headquarters/ongoingevents";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/booking-list")
    public String viewBookingList(Model model, HttpSession session) {
        String headquartersname = (String) session.getAttribute("usertype");
        if (headquartersname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("headquartersname", headquartersname);
            List<BookingDetails> bookingList = bookingDetailsRepository.getAllApprovedList();
            model.addAttribute("bookingList", bookingList);
            return "headquarters/booking-list"; // Assuming "officer/rejected-booking-list" is the name of your HTML template
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/rejected-list")
    public String viewRejectedBookings(Model model, HttpSession session) {
        String headquartersname = (String) session.getAttribute("usertype");
        if (headquartersname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("headquartersname", headquartersname);
            List<BookingDetails> rejectedBookings = bookingDetailsRepository.getAllRejectingList();
            model.addAttribute("rejectedBookings", rejectedBookings);

            return "headquarters/rejected-list";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/bookingwaitinglist")
    public String viewWaitingBookings(Model model, HttpSession session) {
        String headquartersname = (String) session.getAttribute("usertype");
        if (headquartersname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("headquartersname", headquartersname);
            List<BookingDetails> waitingBookings = bookingDetailsRepository.getAllwaitingList();
            model.addAttribute("waitingBookings", waitingBookings);

            return "headquarters/bookingwaiting";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/settlement-approved")
    public String viewSettlementMaster(Model model, HttpSession session) {
        String headquartersname = (String) session.getAttribute("usertype");
        if (headquartersname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            List<BookingDetails> approvedSettlements = bookingDetailsRepository.findAllWithApprovedRefundStatus();
            List<BookingDetails> settlements = new ArrayList<>();
            settlements.addAll(approvedSettlements);
            List<BookingDetails> approvedcancelledbookings = bookingDetailsRepository.findCancelledList();
            List<BookingDetails> approvedrejectedbookings = bookingDetailsRepository.findRejectedList();
            model.addAttribute("headquartersname", headquartersname);
            model.addAttribute("settlementlist", settlements);
            model.addAttribute("approvedcancelledbookings", approvedcancelledbookings);
            model.addAttribute("approvedrejectedbookings", approvedrejectedbookings);
            return "headquarters/settlement-approved";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/settlement-initiatedlist")
    public String viewSettlementInitiatedMaster(Model model, HttpSession session) {
        String headquartersname = (String) session.getAttribute("usertype");
        if (headquartersname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            List<BookingDetails> settlement = bookingDetailsRepository.findByBookingDetailsRefundStatus();
            List<BookingDetails> Initiatedcancelledbookings = bookingDetailsRepository.findInitiatedCancelledList();
            List<BookingDetails> Initiatedrejectedbookings = bookingDetailsRepository.findInitiatedRejectedList();
            model.addAttribute("title", "settlement initiate");
            model.addAttribute("headquartersname", headquartersname);
            model.addAttribute("settlementlist", settlement);
            model.addAttribute("Initiatedcancelledbookings", Initiatedcancelledbookings);
            model.addAttribute("Initiatedrejectedbookings", Initiatedrejectedbookings);
            return "headquarters/settlement-initiate";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/settlement-list")
    public String settlementPendingList(Model model, HttpSession session) {
        String headquartersname = (String) session.getAttribute("usertype");
        if (headquartersname != null) {
            model.addAttribute("headquartersname", headquartersname);
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("title", "settlement Pending");
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
             //ak
             List<BookingDetails> settlementIntiated = bookingDetailsRepository.findSettlementInitiatedPending();
            List<BookingDetails> settlement = bookingDetailsRepository.findAllSettlementPending();
            List<BookingDetails> cancelledbookings = bookingDetailsRepository.findAllSettlementCancelledList();
            List<BookingDetails> rejectedbookings = bookingDetailsRepository.findAllSettlementRejectedList();
            model.addAttribute("settlementlist", settlement);
            //AK
             model.addAttribute("settlementIntiated", settlementIntiated);
            model.addAttribute("cancelledbookings", cancelledbookings);
            model.addAttribute("rejectedbookings", rejectedbookings);
            return "headquarters/settlement-list";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/refundfailed-list")
    public String RefundFailedList(Model model, HttpSession session) {
        String headquartersname = (String) session.getAttribute("usertype");
        if (headquartersname != null) {
            model.addAttribute("headquartersname", headquartersname);
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("title", "settlementapproved");
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
            List<BookingDetails> settlementrefundfailed = bookingDetailsRepository.findAllSettlementRefundFailed();
            List<BookingDetails> refundfailedcancelledbookings = bookingDetailsRepository.findAllRefundFaileCancelledList();
            List<BookingDetails> refundfailedrejectedbookings = bookingDetailsRepository.findAllRefundFaileRejectedList();
            model.addAttribute("settlementrefundfailed", settlementrefundfailed);
            model.addAttribute("refundfailedcancelledbookings", refundfailedcancelledbookings);
            model.addAttribute("refundfailedrejectedbookings", refundfailedrejectedbookings);
            return "headquarters/refund-failedlist";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/settlement-completedlist")
    public String viewSettlementcompletedMaster(Model model, HttpSession session) {
        String headquartersname = (String) session.getAttribute("usertype");
        if (headquartersname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            List<BookingDetails> completedlist = bookingDetailsRepository.findSettlementCompletedList();
            List<BookingDetails> cancelledcompletedlist = bookingDetailsRepository.findCancelledCompletedList();
            List<BookingDetails> rejectedcompletedlist = bookingDetailsRepository.findRejectedSettlementCompletedList();
            model.addAttribute("title", "settlement initiate");
            model.addAttribute("headquartersname", headquartersname);
            model.addAttribute("completedlist", completedlist);
            model.addAttribute("cancelledcompletedlist", cancelledcompletedlist);
            model.addAttribute("rejectedcompletedlist", rejectedcompletedlist);
            return "headquarters/settlement-completed";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/settlementlist")
    public String viewSettlementList(Model model, HttpSession session) {
        String headquartersname = (String) session.getAttribute("usertype");
        if (headquartersname != null) {
            List<BookingDetails> settlemetList = bookingDetailsRepository.getAllSettlementList();
            model.addAttribute("headquartersname", headquartersname);
            model.addAttribute("settlemetList", settlemetList);
            return "headquarters/settlementlist";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/headquarterspage")
    public String viewcaretaker(Model model, HttpSession session, @RequestParam("bookingId") int bookingId) {
        String caretakerName = (String) session.getAttribute("usertype");
        if (caretakerName != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("caretakerName", caretakerName);
            model.addAttribute("title", "caretaker");
            model.addAttribute("baseURL", appConfig.getBaseUrl());
            BookingDetails booking = bookingService.getBookingsById(bookingId);
            model.addAttribute("booking", booking);
            return "headquarters/headquarterspage";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/settlement-reject")
    public String settlementRejectedList(Model model, HttpSession session) {
        String headquartersname = (String) session.getAttribute("usertype");
        if (headquartersname != null) {
            List<BookingDetails> settlement = bookingDetailsRepository.findAllSettlementRejected();
            model.addAttribute("headquartersname", headquartersname);
            model.addAttribute("title", "Settlement Rejected");
            model.addAttribute("rejectSettlementlist", settlement);
            return "headquarters/settlement-reject";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/logout")
    public String viewLogout(Model model, HttpSession session) {
        session.invalidate();
        return "redirect:/victoriahallbooking_uat/login";
    }

    @GetMapping("/Gccblocking")
    public String viewGccBlocking(Model model, HttpSession session) {
        String headquartersname = (String) session.getAttribute("usertype");
        if (headquartersname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("headquartersname", headquartersname);
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

            // Add attributes to the model for the view
            model.addAttribute("gccblocked", uniqueGccBlockingDates);
            model.addAttribute("hallCountMap", hallCountMap);

            return "headquarters/gccblocking";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    // Utility method to ensure distinct by a key
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = new HashSet<>();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @GetMapping("/viewUploadNoc")
    public String viewUploadNoc(Model model, HttpSession session) {
        String headquartersname = (String) session.getAttribute("usertype");
        if (headquartersname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("headquartersname", headquartersname);

            // Fetch waiting bookings
            List<BookingDetails> waitingBookings = bookingDetailsRepository.getAllwaitingNOC();
            System.out.println("Waiting Bookings: " + waitingBookings.size());
            model.addAttribute("waitingBookings", waitingBookings);

            return "headquarters/viewUploadNoc";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

}

