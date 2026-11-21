package com.gcc.communityHall.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.BookingDetails.RefundStatus;
import com.gcc.communityHall.entity.CounterRefundList;
import com.gcc.communityHall.entity.HallDetails;
import com.gcc.communityHall.entity.Payments;
import com.gcc.communityHall.entity.Settlement;
import com.gcc.communityHall.entity.Users;
import com.gcc.communityHall.others.AppConfig;
import com.gcc.communityHall.repository.BookingDetailsRepository;
import com.gcc.communityHall.repository.CounterRefundListRepository;
import com.gcc.communityHall.repository.HallDetailsRepository;
import com.gcc.communityHall.repository.PaymentsRepository;
import com.gcc.communityHall.repository.SettlementRepository;
import com.gcc.communityHall.repository.UsersRepository;
import com.gcc.communityHall.service.BookingService;
import com.gcc.communityHall.service.HallDetailService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/victoriahallbooking_uat/officer")
public class OfficerController {
    @Autowired
    private AppConfig appConfig;

    @Autowired
    private BookingDetailsRepository bookingDetailsRepository;

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private SettlementRepository settlementRepository;
    @Autowired
    private CounterRefundListRepository counterRefundListRepository;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private HallDetailsRepository hallDetailsRepository;
    @Autowired
    private HallDetailService hallDetailsService;


    @GetMapping("/login")
    public String viewLoginPage(Model model) {
        String msg = "Login to Your Account";
        model.addAttribute("errortxt", msg);
        return "officiallogin";
    }

    @GetMapping("/officer-dashboard")
    public String viewDashboard(Model model, HttpSession session) {
        String officername = (String) session.getAttribute("usertype");
        if (officername != null) {
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("officername", officername);
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);

            model.addAttribute("zone", zone);

            return "officer/officer-dashboard";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/booking-pending")
    public String viewhall(Model model, HttpSession session) {
        String officername = (String) session.getAttribute("usertype");
        if (officername != null) {
            model.addAttribute("officername", officername);
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("title", "pending bookings");
            model.addAttribute("baseURL", appConfig.getBaseUrl());
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
            List<BookingDetails> pendingbooking = bookingDetailsRepository.getPendingOrPaymentFailureBookings(zone);
            model.addAttribute("pendingbooking", pendingbooking);
            return "officer/booking-pending";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/payment-list")
    public String viewPayment(Model model, HttpSession session) {
        String officername = (String) session.getAttribute("usertype");
        if (officername != null) {
            model.addAttribute("officername", officername);
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("title", "paymentlist");
            model.addAttribute("baseURL", appConfig.getBaseUrl());
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
            // System.out.println(zone);
            List<Payments> pay = paymentsRepository.getPaymentListByzone(zone);
            model.addAttribute("paymentlist", pay);
            return "officer/payment-list";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/bookingwaitinglist")
    public String viewWaitingBookings(Model model, HttpSession session) {
        String officername = (String) session.getAttribute("usertype");
        if (officername != null) {
            model.addAttribute("officername", officername);
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("title", "bookinglist");
            model.addAttribute("baseURL", appConfig.getBaseUrl());
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
            List<BookingDetails> bookingList = bookingDetailsRepository.getWaitinglist(zone);
            model.addAttribute("bookingList", bookingList);
            return "officer/booking-list"; // Assuming "officer/rejected-booking-list" is the name of your HTML template
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/booking-list")
    public String viewBookingList(Model model, HttpSession session) {
        String officername = (String) session.getAttribute("usertype");
        if (officername != null) {
            model.addAttribute("officername", officername);
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("title", "bookinglist");
            model.addAttribute("baseURL", appConfig.getBaseUrl());
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
            List<BookingDetails> bookingList = bookingDetailsRepository.getApprovedlist(zone);
            model.addAttribute("bookingList", bookingList);
            return "officer/booking-list"; // Assuming "officer/rejected-booking-list" is the name of your HTML template
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/rejected-list")
    public String viewRejectedBookings(Model model, HttpSession session) {
        String officername = (String) session.getAttribute("usertype");
        if (officername != null) {
            model.addAttribute("officername", officername);
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("title", "rejectedlist");
            model.addAttribute("baseURL", appConfig.getBaseUrl());
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
            List<BookingDetails> rejectedBookings = bookingDetailsRepository.getrejectedlist(zone);
            model.addAttribute("rejectedBookings", rejectedBookings);

            return "officer/rejected-list";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }


    @GetMapping("/settlement-list")
    public String settlementPendingList(Model model, HttpSession session) {
        String officername = (String) session.getAttribute("usertype");
        if (officername != null) {
            model.addAttribute("officername", officername);
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("title", "settlement Pending");
            model.addAttribute("baseURL", appConfig.getBaseUrl());
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
            System.out.println("Zone: " + zone);
            List<BookingDetails> settlement = bookingDetailsRepository.findSettlementPendingByZone(zone);
            //ak
             List<BookingDetails> settlementIntiated = bookingDetailsRepository.findSettlementInitiatedPendingByZone(zone);
            List<BookingDetails> cancelledbookings = bookingDetailsRepository.findCancelledListByZone(zone);
            List<BookingDetails> rejectedbookings = bookingDetailsRepository.findRejectedListByZone(zone);
            model.addAttribute("settlementlist", settlement);
            //AK
            model.addAttribute("settlementIntiated", settlementIntiated);
            model.addAttribute("cancelledbookings", cancelledbookings);
            model.addAttribute("rejectedbookings", rejectedbookings);
            System.out.println("settlementIntiated"+settlementIntiated);
            return "officer/settlement-list";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/refundfailed-list")
    public String RefundFailedList(Model model, HttpSession session) {
        String officername = (String) session.getAttribute("usertype");
        if (officername != null) {
            model.addAttribute("officername", officername);
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("title", "settlementapproved");
            model.addAttribute("baseURL", appConfig.getBaseUrl());
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
            List<BookingDetails> settlementrefundfailed = bookingDetailsRepository.findSettlementRefundFailedByZone(zone);
            List<BookingDetails> refundfailedcancelledbookings = bookingDetailsRepository.findRefundFaileCancelledListByZone(zone);
            List<BookingDetails> refundfailedrejectedbookings = bookingDetailsRepository.findRefundFaileRejectedListByZone(zone);
            model.addAttribute("settlementrefundfailed", settlementrefundfailed);
            model.addAttribute("refundfailedcancelledbookings", refundfailedcancelledbookings);
            model.addAttribute("refundfailedrejectedbookings", refundfailedrejectedbookings);
            return "officer/refund-failedlist";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/settlement-approved")
    public String settlementApprovedList(Model model, HttpSession session) {
        String officername = (String) session.getAttribute("usertype");
        if (officername != null) {
            model.addAttribute("officername", officername);
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("title", "settlementapproved");
            model.addAttribute("baseURL", appConfig.getBaseUrl());
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
            List<BookingDetails> settlement = bookingDetailsRepository.findSettlementApprovedByZone(zone);
            List<BookingDetails> approvedcancelledbookings = bookingDetailsRepository.findapprovedCancelledListByZone(zone);
            List<BookingDetails> approvedrejectedbookings = bookingDetailsRepository.findapprovedRejectedListByZone(zone);
            model.addAttribute("approvedSettlementlist", settlement);
            model.addAttribute("approvedcancelledbookings", approvedcancelledbookings);
            model.addAttribute("approvedrejectedbookings", approvedrejectedbookings);
            return "officer/settlement-approved";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/settlement-reject")
    public String settlementRejectedList(Model model, HttpSession session) {
        String officername = (String) session.getAttribute("usertype");
        if (officername != null) {
            model.addAttribute("officername", officername);
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("title", "settlement rejected");
            model.addAttribute("baseURL", appConfig.getBaseUrl());
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
            List<BookingDetails> settlement = bookingDetailsRepository.findSettlementRejectedByZone(zone);
            model.addAttribute("rejectSettlementlist", settlement);
            return "officer/settlement-reject";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbookingookingooking/login";
        }
    }

    @GetMapping("/settlement-completedlist")
    public String viewSettlementcompletedMaster(Model model, HttpSession session) {
        String officername = (String) session.getAttribute("usertype");
        if (officername != null) {
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
            List<BookingDetails> completedlist = bookingDetailsRepository.findSettlementCompletedListbyzone(zone);
            List<BookingDetails> cancelledcompletedlist = bookingDetailsRepository.findCancelledCompletedListbyzone(zone);
            List<BookingDetails> rejectedcompletedlist = bookingDetailsRepository.findRejectedSettlementCompletedListbyzone(zone);
            model.addAttribute("title", "settlement initiate");
            model.addAttribute("officername", officername);
            model.addAttribute("completedlist", completedlist);
            model.addAttribute("cancelledcompletedlist", cancelledcompletedlist);
            model.addAttribute("rejectedcompletedlist", rejectedcompletedlist);
            return "officer/settlement-completed";
        } else {
            session.invalidate(); // Invalidate the session to clear all session attributes
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/reports")
    public String viewReports(Model model, HttpSession session) {
        String officername = (String) session.getAttribute("usertype");
        if (officername != null) {
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("officername", officername);
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("zone", zone);
            List<HallDetails> hallnames = hallDetailsService.getHallNames();
            model.addAttribute("hallnames", hallnames);
            return "officer/report";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @GetMapping("/getReport")
    public String viewgetReports(Model model, HttpSession session) {
        String officername = (String) session.getAttribute("usertype");
        if (officername != null) {
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("officername", officername);
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("zone", zone);
            List<HallDetails> hallnames = hallDetailsService.getHallNames();
            model.addAttribute("hallnames", hallnames);
            return "officer/report";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }

    @PostMapping("/getReport")
    public String generateReport(Model model, HttpSession session,
                                 @RequestParam(name = "FromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fromDate,
                                 @RequestParam(name = "ToDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String toDate,
                                 @RequestParam(name = "request") String request,
                                 @RequestParam(name = "bookingStatus", required = false) String bookingStatus,
                                 @RequestParam(name = "filterby", required = false) String filterby,
                                 @RequestParam(name = "refundStatus", required = false) String refundStatus,
                                 @RequestParam(name = "hallname", required = false, defaultValue = "0") Integer hallId) {
        String officername = (String) session.getAttribute("usertype");
        if (officername != null) {
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
            model.addAttribute("officername", officername);
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            List<HallDetails> hallnames = hallDetailsService.getHallNamesbyzone(zone);
            model.addAttribute("hallnames", hallnames);
            List<BookingDetails> bookingList = handleBookingRequest(request, fromDate, toDate, bookingStatus, zone, filterby);
            List<BookingDetails> bookingSettlementList = handleBookingSettlementRequest(request, fromDate, toDate, refundStatus, zone, filterby);
            List<Object[]> paymentList = request.equals("3") ? bookingService.getAllPaymentsDetailsByZone(fromDate, toDate, zone) : new ArrayList<>();
            List<Object[]> hallbookingList = handleHallBookingRequest(request, fromDate, toDate, zone, hallId, filterby);
            model.addAttribute("bookings", bookingList);
            model.addAttribute("bookingSettlementList", bookingSettlementList);
            model.addAttribute("bookingStatus", bookingStatus);
            model.addAttribute("refundStatus", refundStatus);
            model.addAttribute("fromdate", fromDate);
            model.addAttribute("todate", toDate);
            model.addAttribute("request", request);
            model.addAttribute("filterby", filterby);
            model.addAttribute("paymentLists", paymentList);
            model.addAttribute("hallbookingLists", hallbookingList);

            return "officer/report";
        } else {
            session.invalidate();
            return "redirect:/victoriahallbooking_uat/login";
        }
    }


    private List<BookingDetails> handleBookingRequest(String request, String fromDate, String toDate, String bookingStatus, String zone, String filterby) {
        if (!request.equals("1")) {
            return new ArrayList<>();
        }
        if (bookingStatus.isEmpty() && filterby.isEmpty()) {
            return bookingService.getAllBookingDetailsBookingDateByZone(fromDate, toDate, zone);
        }
        if (filterby.isEmpty() && !bookingStatus.isEmpty()) {
            return bookingService.getAllBookingDetailsBookingDateByZoneWithStatus(fromDate, toDate, zone, bookingStatus);
        }
        if (!filterby.isEmpty()) {

            if ("1".equals(filterby)) {
                if (bookingStatus.isEmpty()) {
                    return bookingService.getAllBookingDetailsBookingDateByZone(fromDate, toDate, zone);
                } else {
                    return bookingService.getAllBookingDetailsBookingDateByZoneWithStatus(fromDate, toDate, zone, bookingStatus);

                }

            }
            if ("2".equals(filterby)) {
                if (bookingStatus.isEmpty()) {
                    return bookingService.getAllBookingDetailsEventDateByZone(fromDate, toDate, zone);
                } else {
                    return bookingService.getAllBookingDetailsEventDateByZoneWithStatus(fromDate, toDate, zone, bookingStatus);
                }

            }
        }

        return new ArrayList<>();
    }

    private List<BookingDetails> handleBookingSettlementRequest(String request, String fromDate, String toDate, String refundStatus, String zone, String filterby) {
        if (!request.equals("2")) {
            return new ArrayList<>();
        }
        if ((refundStatus == null || refundStatus.isEmpty()) && filterby.isEmpty()) {
            return bookingService.getAllBookingsettlementDetailsByZoneByBookingDate(fromDate, toDate, zone);
        }
        if ((refundStatus != null && !refundStatus.isEmpty()) && filterby.isEmpty()) {
            return bookingService.getAllBookingsettlementDetailsByZoneByBookingDateWithStatus(fromDate, toDate, zone, refundStatus);
        }
        if (!filterby.isEmpty()) {

            if ("1".equals(filterby)) {
                if (refundStatus.isEmpty() || refundStatus == null) {
                    return bookingService.getAllBookingsettlementDetailsByZoneByBookingDate(fromDate, toDate, zone);
                } else {
                    return bookingService.getAllBookingsettlementDetailsByZoneByBookingDateWithStatus(fromDate, toDate, zone, refundStatus);
                }

            }
            if ("2".equals(filterby)) {
                if (refundStatus.isEmpty() || refundStatus == null) {
                    return bookingService.getAllBookingsettlementDetailsByZoneByEventDate(fromDate, toDate, zone);
                } else {
                    return bookingService.getAllBookingsettlementDetailsByZoneByEventDateWithStatus(fromDate, toDate, zone, refundStatus);
                }

            }
        }


        return new ArrayList<>();

    }

    private List<Object[]> handleHallBookingRequest(String request, String fromDate, String toDate, String zone, Integer hallId, String filterby) {
        if (!request.equals("4")) {
            return new ArrayList<>();
        }
        if (filterby.isEmpty() && hallId == 0) {
            return bookingService.getBookingHallDetailsReportByZoneByBookingDate(fromDate, toDate, zone);
        }
        if (filterby.isEmpty() && hallId != 0) {
            return bookingService.getBookingHallDetailsReportByZoneByBookingDateHallname(fromDate, toDate, zone, hallId);
        }
        if (!filterby.isEmpty()) {
            if ("1".equals(filterby)) {
                if (hallId != 0) {
                    return bookingService.getBookingHallDetailsReportByZoneByBookingDateHallname(fromDate, toDate, zone, hallId);
                } else {
                    return bookingService.getBookingHallDetailsReportByZoneByBookingDate(fromDate, toDate, zone);
                }
            }
            if ("2".equals(filterby)) {
                if (hallId != 0) {
                    return bookingService.getBookingHallDetailsReportByZoneByEventDateHallname(fromDate, toDate, zone, hallId);
                } else {
                    return bookingService.getBookingHallDetailsReportByZoneByEventDate(fromDate, toDate, zone);
                }
            }
        }
        return new ArrayList<>();
    }


    @GetMapping("/counter-refund")
    public String counterRefundList(Model model, HttpSession session) {
        String officername = (String) session.getAttribute("usertype");
        if (officername != null) {
            model.addAttribute("officername", officername);
            long mobileno = (long) session.getAttribute("mobileno");
            model.addAttribute("mobileno", mobileno);
            model.addAttribute("title", "settlement rejected");
            model.addAttribute("baseURL", appConfig.getBaseUrl());
            String zone = (String) session.getAttribute("zone");
            model.addAttribute("zone", zone);
            List<CounterRefundList> counterRefund = counterRefundListRepository.findCounterRefundList(zone);
            model.addAttribute("counterRefund", counterRefund);
            return "officer/counter-refund";
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
}
