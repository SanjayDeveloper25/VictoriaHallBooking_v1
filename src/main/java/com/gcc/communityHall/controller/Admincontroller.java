package com.gcc.communityHall.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcc.communityHall.others.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import com.gcc.communityHall.component.JwtUtil;
import com.gcc.communityHall.entity.ApplicationLogs;
import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.BookingDetails.RefundStatus;
import com.gcc.communityHall.entity.DiscountRange;
import com.gcc.communityHall.entity.GccBlockingDate;
import com.gcc.communityHall.entity.HallDetails;
import com.gcc.communityHall.entity.Notification;
import com.gcc.communityHall.entity.Settlement;
import com.gcc.communityHall.entity.Users;
import com.gcc.communityHall.others.UserSession;
import com.gcc.communityHall.paymentService.entity.TransactionResponse;
import com.gcc.communityHall.paymentService.repository.TransactionResponseRepository;
import com.gcc.communityHall.repository.ApplicationLogsRepository;
import com.gcc.communityHall.repository.BookingDetailsRepository;
import com.gcc.communityHall.repository.DiscountRangeRepository;
import com.gcc.communityHall.repository.GccBlockingDateRepository;
import com.gcc.communityHall.repository.HallDetailsRepository;
import com.gcc.communityHall.repository.NotificationRepository;
import com.gcc.communityHall.repository.SettlementRepository;
import com.gcc.communityHall.repository.UsersRepository;
import com.gcc.communityHall.service.BookingService;
import com.gcc.communityHall.service.HallDetailService;
import com.gcc.communityHall.service.ReasonMasterService;
import com.gcc.communityHall.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/victoriahallbooking_uat/admin")
public class Admincontroller {

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
    private HallDetailsRepository hallDetailsRepo;

    @Autowired
    private DiscountRangeRepository discountRangeRepo;

    @Autowired
    private GccBlockingDateRepository blockingDateRepository;

    @Autowired
    private BookingDetailsRepository bookingRepository;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ApplicationLogsRepository applicationLogsRepository;

    @Autowired
    private TransactionResponseRepository transactionResponseRepository;

    @GetMapping("/dashboard")
    public String viewDashboard(
            @RequestParam(required = false, defaultValue = "all zones") String zone,
            Model model, HttpSession session) {

        String token = (String) session.getAttribute("jwtToken");
        // System.out.println("tokensssssssss"+token);

        // Validate JWT token
        if (token != null && jwtUtil.validateToken(token)) {
            //System.out.println("tokennnnnnnnnnnnnnns"+token);
            // Token is valid, proceed with dashboard logic
            String adminName = (String) session.getAttribute("usertype");
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
            model.addAttribute("adminname", adminName);
            model.addAttribute("zone", zone);

            return "admin/dashboard";
        } else {
            // Token is invalid or not present, redirect to logout
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }


    @GetMapping("/Reports")
    public String viewReports(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            return "admin/reports";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/getReport")
    public String getReports(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            return "admin/reports";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }


    @GetMapping("/HallDetails")
    public String viewHallDetails(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            return "admin/halldetailslist";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }

    }

    @GetMapping("/halldetailslist")
    public String viewhalldetailsList(Model model, HttpSession session) {
        List<HallDetails> list = hallDetailsRepo.findAll();
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("adminname", adminname);
            model.addAttribute("halllist", list);
            return "admin/halldetailslist";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }

    }

    @GetMapping("/settlement-approved")
    public String viewSettlementMaster(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            List<BookingDetails> approvedSettlements = bookingDetailsRepository.findAllWithApprovedRefundStatus();
            List<BookingDetails> settlements = new ArrayList<>();
            settlements.addAll(approvedSettlements);
            List<BookingDetails> approvedcancelledbookings = bookingDetailsRepository.findCancelledList();
            List<BookingDetails> approvedrejectedbookings = bookingDetailsRepository.findRejectedList();
            model.addAttribute("adminname", adminname);
            model.addAttribute("settlementlist", settlements);
            model.addAttribute("approvedcancelledbookings", approvedcancelledbookings);
            model.addAttribute("approvedrejectedbookings", approvedrejectedbookings);
            return "admin/settlement-approved";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/settlement-initiatedlist")
    public String viewSettlementInitiatedMaster(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            List<BookingDetails> settlement = bookingDetailsRepository.findByBookingDetailsRefundStatus();
            List<BookingDetails> Initiatedcancelledbookings = bookingDetailsRepository.findInitiatedCancelledList();
            List<BookingDetails> Initiatedrejectedbookings = bookingDetailsRepository.findInitiatedRejectedList();
            model.addAttribute("title", "settlement initiate");
            model.addAttribute("adminname", adminname);
            model.addAttribute("settlementlist", settlement);
            model.addAttribute("Initiatedcancelledbookings", Initiatedcancelledbookings);
            model.addAttribute("Initiatedrejectedbookings", Initiatedrejectedbookings);
            return "admin/settlement-initiate";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/settlement-list")
    public String settlementPendingList(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            model.addAttribute("adminname", adminname);
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("title", "settlement Pending");
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
            List<BookingDetails> settlement = bookingDetailsRepository.findAllSettlementPending();
            List<BookingDetails> cancelledbookings = bookingDetailsRepository.findAllSettlementCancelledList();
            List<BookingDetails> rejectedbookings = bookingDetailsRepository.findAllSettlementRejectedList();
            model.addAttribute("settlementlist", settlement);
            //AKI
            List<BookingDetails> settlementIntiated = bookingDetailsRepository.findSettlementInitiatedPending();
             model.addAttribute("settlementIntiated", settlementIntiated);
            model.addAttribute("cancelledbookings", cancelledbookings);
            model.addAttribute("rejectedbookings", rejectedbookings);
            System.out.println("Settlement List: " + settlement);
            return "admin/settlement-list";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/refundfailed-list")
    public String RefundFailedList(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            model.addAttribute("adminname", adminname);
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
            return "admin/refund-failedlist";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/settlement-completedlist")
    public String viewSettlementcompletedMaster(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            List<BookingDetails> completedlist = bookingDetailsRepository.findSettlementCompletedList();
            List<BookingDetails> cancelledcompletedlist = bookingDetailsRepository.findCancelledCompletedList();
            List<BookingDetails> rejectedcompletedlist = bookingDetailsRepository.findRejectedSettlementCompletedList();
            model.addAttribute("title", "settlement initiate");
            model.addAttribute("adminname", adminname);
            model.addAttribute("completedlist", completedlist);
            model.addAttribute("cancelledcompletedlist", cancelledcompletedlist);
            model.addAttribute("rejectedcompletedlist", rejectedcompletedlist);
            return "admin/settlement-completed";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/ReasonMaster")
    public String viewreasonMaster(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("adminname", adminname);
            return "admin/reasonmaster";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/userdeactive")
    public String viewuserDetails(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("adminname", adminname);
            return "admin/userdetails";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/Gccblocking")
    public String viewGccBlocking(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("adminname", adminname);
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

            return "admin/gccblocking";
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

    @PostMapping("/saveHallDetails")
    public String saveHall(
            @RequestParam String hallName,
            @RequestParam float depositAmount,
            @RequestParam float halfdepositAmt,
            @RequestParam float halfDayAmt,
            @RequestParam float fullDayAmt,
            @RequestParam float gst,
            @RequestParam float hallArea,
            @RequestParam String ebNumber,
            @RequestParam float ebCostPerUnit,
            @RequestParam int parkingCapacity,
            @RequestParam int seatingCapacity,
            @RequestParam String doorNo,
            @RequestParam String street,
            @RequestParam String area,
            @RequestParam String zone,
            @RequestParam String division,
            @RequestParam(required = false) String landMark,
            @RequestParam String city,
            @RequestParam int pinCode,
            @RequestParam(required = false) String latitude,
            @RequestParam(required = false) String longitude,
            @RequestParam String hallInchargeName,
            @RequestParam long hallInchargeNo,
            @RequestParam("hallImages") MultipartFile[] hallImages, HttpSession session) {
        String updatedId = (String) session.getAttribute("employeeId");
        hallDetailService.saveHall(hallName, depositAmount, halfDayAmt, fullDayAmt, gst, hallArea, ebNumber, ebCostPerUnit, parkingCapacity, seatingCapacity, doorNo,
                street, area, zone, division, landMark, city, pinCode, latitude, longitude, hallInchargeName, hallInchargeNo, updatedId, hallImages, halfdepositAmt);
        String adminname = (String) session.getAttribute("usertype");
        Integer loginid = (Integer) session.getAttribute("id");
        ApplicationLogs logs = new ApplicationLogs();
        logs.setComments("Action by " + adminname);
        logs.setLoginId(loginid);
        logs.setLogType("Adding New Hall:-" + hallName);
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
        // Redirect to a success page or return a ModelAndView as per your application flow
        return "redirect:/victoriahallbooking_uat/admin/halldetailslist";
    }

    @GetMapping("/approval-history")
    public String viewApproval(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("adminname", adminname);
            model.addAttribute("adminname", adminname);
            model.addAttribute("title", "approvalcontrol");
            List<Users> Pending = usersRepository.getPendingList();
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
            model.addAttribute("pendinglist", Pending);
            return "admin/approval-history";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/user-approved")
    public String viewUserApproved(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("adminname", adminname);
            model.addAttribute("title", "reasondetails");
            List<Users> approved = usersRepository.getUserApproved();
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
            model.addAttribute("approvedlist", approved);
            return "admin/user-approved";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/user-reject")
    public String viewUserReject(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("adminname", adminname);
            model.addAttribute("title", "reasondetails");
            List<Users> reject = usersRepository.getUserReject();
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
            model.addAttribute("rejectedlist", reject);
            return "admin/user-reject";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/booking-list")
    public String viewBookingList(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("adminname", adminname);
            List<BookingDetails> bookingList = bookingDetailsRepository.getAllApprovedList();
            model.addAttribute("bookingList", bookingList);
            return "admin/booking-list"; // Assuming "officer/rejected-booking-list" is the name of your HTML template
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/rejected-list")
    public String viewRejectedBookings(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("adminname", adminname);
            List<BookingDetails> rejectedBookings = bookingDetailsRepository.getAllRejectingList();
            model.addAttribute("rejectedBookings", rejectedBookings);

            return "admin/rejected-list";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/bookingwaitinglist")
    public String viewWaitingBookings(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("adminname", adminname);
            List<BookingDetails> waitingBookings = bookingDetailsRepository.getAllwaitingList();
            model.addAttribute("waitingBookings", waitingBookings);

            return "admin/bookingwaiting";
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
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
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

            if ("10".equals(request)) {

                List<Object> zoneWiseBookingList = bookingService.getZoneWiseBookingList(fromDate, toDate);
                model.addAttribute("zoneWiseBookingList", zoneWiseBookingList);

                // Fetch all hall data (not dependent on zone dropdown yet)
                List<Object> hallWiseBookingList = bookingService.getAllHallWiseBookingList(fromDate, toDate);
                String hallWiseBookingJson = new ObjectMapper().writeValueAsString(hallWiseBookingList);
                model.addAttribute("hallWiseBookingList", hallWiseBookingJson);

                // Fetch all counter data
                List<Object> counterWiseBookingList = bookingService.getAllCounterWiseBookingList(fromDate, toDate);
                String counterWiseBookingJson = new ObjectMapper().writeValueAsString(counterWiseBookingList);
                model.addAttribute("counterWiseBookingList", counterWiseBookingJson);
            }
            //ak
            if ("11".equals(request)) {
    // Fetch Settlement Initiated Report details
    List<Object[]> settlementInitiatedList = bookingService.getSettlementInitiatedReport(fromDate, toDate);
    System.out.println("settlementInitatedList"+ settlementInitiatedList);

    // Add the list to the model
    model.addAttribute("settlementInitiatedList", settlementInitiatedList);
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
            model.addAttribute("adminname", adminname);
            System.out.println("request" + request);
            

            return "admin/reports";
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

    @GetMapping("/DiscountRange")
    public String viewDiscountRange(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            List<DiscountRange> discountRanges = discountRangeRepo.findAll();

            model.addAttribute("discountRanges", discountRanges);
            model.addAttribute("adminname", adminname);
            return "admin/discountrange";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/ongoingevents")
    public String viewRecentevents(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            List<BookingDetails> presenteventslist = bookingDetailsRepository.findPresentBookingList();
            List<BookingDetails> pasteventslist = bookingDetailsRepository.findPastEventsList();
            List<BookingDetails> futureeventslist = bookingDetailsRepository.findFutureEventsList();
            model.addAttribute("title", "settlement initiate");
            model.addAttribute("adminname", adminname);
            model.addAttribute("presenteventslist", presenteventslist);
            model.addAttribute("pasteventslist", pasteventslist);
            model.addAttribute("futureeventslist", futureeventslist);
            return "admin/ongoingevents";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/manualrefund")
    public String viewManualRefund(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("adminname", adminname);
            return "admin/manualrefund";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/developer")
    public String viewDeveloper(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("adminname", adminname);
            return "admin/developer";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    //    @GetMapping("/getDeveloperdetails")
//    public String getDetails(Model model,HttpSession session)
//    {
//    	String adminname = (String) session.getAttribute("usertype"); 
//    	 if(adminname != null) {
//    		 long mobileno = (long) session.getAttribute("mobileno");
//   	     	model.addAttribute("mobileno", mobileno);
//    	return "admin/developer";
//    }else {
//    	session.invalidate(); // Invalidate the session to clear all session attributes
//        return "redirect:/victoriahallbooking_uat/login";
//    }
//    }
    @PostMapping("/getDeveloperdetails")
    public String generatedetails(Model model,
                                  @RequestParam(name = "FromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fromDate,
                                  @RequestParam(name = "ToDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String toDate,
                                  @RequestParam(name = "request") String request,
                                  @RequestParam(name = "bookingId", required = false) Long bookingId,
                                  @RequestParam(name = "hallId", required = false) Long hallId,
                                  @RequestParam(name = "hallName", required = false) String hallName, HttpSession session) {


        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            List<Object[]> TransactionResponse = new ArrayList<>();
            if (request.equals("1")) {
                if (bookingId == null && hallId == null) {
                    TransactionResponse = bookingService.getTransactionresponse(fromDate, toDate, null, null);
                } else if (bookingId != null && hallId == null) {
                    TransactionResponse = bookingService.getTransactionresponse(fromDate, toDate, bookingId, null);
                } else if (bookingId == null && hallId != null) {
                    TransactionResponse = bookingService.getTransactionresponse(fromDate, toDate, null, hallId);
                } else {
                    TransactionResponse = bookingService.getTransactionresponse(fromDate, toDate, bookingId, hallId);
                }
            }
            List<Object[]> RefundResponse = new ArrayList<>();
            if (request.equals("2")) {
                if (bookingId == null) {
                    RefundResponse = bookingService.getRefundResponse(fromDate, toDate, null);
                } else {
                    RefundResponse = bookingService.getRefundResponse(fromDate, toDate, bookingId);
                }
            }
            List<Object[]> BookingCalendar = new ArrayList<>();
            if (request.equals("3")) {
                System.out.println("outside");
//                if (hallName.isEmpty()) {
//                    System.out.println("check1");
//                    // Fetch all details for the given date range
//                    BookingCalendar = bookingService.getBookingCalendarDetails(fromDate, toDate,hallName);
//                } else {
//                    System.out.println("check2");
//                    // Fetch filtered details based on hallName
//                    BookingCalendar = bookingService.getBookingCalendarDetails(fromDate, toDate, hallName);
//                }
                BookingCalendar = bookingService.getBookingCalendarDetails(fromDate, toDate, bookingId, hallName);
            }

            model.addAttribute("fromdate", fromDate);
            model.addAttribute("todate", toDate);
            model.addAttribute("request", request);
            model.addAttribute("TransactionResponse", TransactionResponse);
            model.addAttribute("RefundResponse", RefundResponse);
            model.addAttribute("BookingCalendar", BookingCalendar);
            model.addAttribute("bookingId", bookingId);
            model.addAttribute("hallId", hallId);
            model.addAttribute("hallName", hallName);
            model.addAttribute("adminname", adminname);

            return "admin/developer";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/notification")
    public String viewNotificationPage(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            List<Notification> newnotifications = notificationRepository.findActivenotifications();
            model.addAttribute("newnotifications", newnotifications);
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("adminname", adminname);
            return "admin/notification";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/raisesettlement")
    public String viewcaretakerpage(Model model, HttpSession session, @RequestParam("bookingId") int bookingId) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("adminname", adminname);
            BookingDetails booking = bookingService.getBookingsById(bookingId);
            model.addAttribute("booking", booking);
            return "admin/caretakerpage";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/settlementlist")
    public String viewSettlementList(Model model, HttpSession session) {
        String userType = (String) session.getAttribute("usertype");
        if (userType != null) {
            List<BookingDetails> settlemetList = bookingRepository.getAllSettlementList();
            model.addAttribute("settlemetList", settlemetList);
            return "admin/settlementlist";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/adminpage")
    public String viewcaretaker(Model model, HttpSession session, @RequestParam("bookingId") int bookingId) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("adminname", adminname);
            model.addAttribute("title", "Admin");
            model.addAttribute("baseURL", appConfig.getBaseUrl());
            BookingDetails booking = bookingService.getBookingsById(bookingId);
            model.addAttribute("booking", booking);
            return "admin/adminpage";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/settlement-reject")
    public String settlementRejectedList(Model model, HttpSession session) {
        String userType = (String) session.getAttribute("usertype");
        if (userType != null) {
            List<BookingDetails> settlement = bookingDetailsRepository.findAllSettlementRejected();
            model.addAttribute("title", "Settlement Rejected");
            model.addAttribute("rejectSettlementlist", settlement);
            return "admin/settlement-reject";
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

    @GetMapping("/viewUploadNoc")
    public String viewUploadNoc(Model model, HttpSession session) {
        String adminname = (String) session.getAttribute("usertype");
        if (adminname != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("adminname", adminname);

            // Fetch waiting bookings
            List<BookingDetails> waitingBookings = bookingDetailsRepository.getAllwaitingNOC();
            System.out.println("Waiting Bookings: " + waitingBookings.size());
            model.addAttribute("waitingBookings", waitingBookings);

            return "admin/viewUploadNoc";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

}

