package com.gcc.communityHall.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.gcc.communityHall.service.Impl.PaymentsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gcc.communityHall.entity.BookingCalendar;
import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.HallDetails;
import com.gcc.communityHall.entity.Notification;
import com.gcc.communityHall.entity.Users;
import com.gcc.communityHall.entity.Users.userType;
import com.gcc.communityHall.others.UserSession;
import com.gcc.communityHall.repository.HallDetailsRepository;
import com.gcc.communityHall.repository.NotificationRepository;
import com.gcc.communityHall.service.BookingService;
import com.gcc.communityHall.service.CalendarService;
import com.gcc.communityHall.service.HallDetailService;
import com.gcc.communityHall.service.UserService;
import com.gcc.communityHall.service.Impl.WhatsappMsgServiceImpl;

@Controller
@RequestMapping(value = { "/victoriahallbooking_uat/user", "/victoriahallbooking_uat", "", "/" })
public class UserController {

    @Autowired
    UserSession userSession;

    @Autowired
    private BookingService bookingService;
    @Autowired
    private HallDetailsRepository hallDetailsRepository;
    @Autowired
    private HallDetailService hallDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    CalendarService calendarService;

    @Autowired
    private WhatsappMsgServiceImpl whatsappMsgServiceImpl;

    @Autowired
    private HallDetailService hallDetailService;

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private PaymentsServiceImpl paymentsServiceImpl;

    @GetMapping(value = { "", "/", "/dashboard", "/index" })
    public String viewDashboard(Model model) {
        List<HallDetails> halls = hallDetailsRepository.findActiveHalls();
        List<HallDetails> hallnames = hallDetailsService.getHallNames();
        for (HallDetails hall : halls) {
            if (hall.getHallImagesList() != null && !hall.getHallImagesList().isEmpty()) {
                model.addAttribute("hasImages", true);
                break;
            }
        }
        List<Notification> newnotification = notificationRepository.findActivenotifications();
        // Create a string to concatenate all notifications descriptions
        StringBuilder notificationText = new StringBuilder();

        for (Notification notification : newnotification) {
            notificationText.append(notification.getNotification_description());
        }
        // System.out.println("user controller \nCounter Id:
        // "+userSession.getCounter()+"\nCounter Name: "+userSession.getCounterName());
        // Pass the concatenated string to the model
        model.addAttribute("newnotification", notificationText.toString());
        model.addAttribute("userName", userSession.getDispayName());
        model.addAttribute("halls", halls);
        model.addAttribute("hallnames", hallnames);
        model.addAttribute("serverDate", LocalDate.now().toString()); // for server date
        model.addAttribute("counter", userSession.getCounter());
        model.addAttribute("counterName", userSession.getCounterName());

        return "user/dashboard";
    }

    // Checkavilability Api
    @GetMapping("/checkAvail")
    public String checkAvail(@RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate,
            @RequestParam("starting") String starting,
            @RequestParam("ending") String ending,
            @RequestParam(name = "hallId", required = false) String hallId,
            @RequestParam(name = "zone", required = false) String zone,
            Model model) {

        List<Notification> newnotification = notificationRepository.findActivenotifications();

        // Create a string to concatenate all notifications descriptions
        StringBuilder notificationText = new StringBuilder();

        for (Notification notification : newnotification) {
            notificationText.append(notification.getNotification_description());
        }

        // Pass the concatenated string to the model
        model.addAttribute("newnotification", notificationText.toString());

        List<HallDetails> hallnames = hallDetailService.getHallNames();
        List<HallDetails> hallsByZone;
        int hallIdd = 0;

        // Parse hallId if provided
        if (hallId != null && !hallId.isEmpty()) {
            try {
                hallIdd = Integer.parseInt(hallId);
            } catch (NumberFormatException e) {
                System.err.println("Invalid hallId: " + hallId);
            }
        }

        // Fetch halls by zone if zone is provided
        if (zone != null && !zone.isEmpty()) {
            hallsByZone = hallDetailsRepository.findByZone(zone);
        } else {
            hallsByZone = hallDetailsRepository.findActiveHalls();
        }

        // Fetch calendar data based on provided parameters
        List<BookingCalendar> data = calendarService.getCalendarByDateAndHallIdAndSlot(fromDate, toDate, starting,
                ending, hallIdd);
        HashSet<Integer> removeHallId = new HashSet<>();

        // Collect hall IDs to be removed
        for (BookingCalendar calendar : data) {
            boolean isSameDay = fromDate.equals(toDate) && fromDate.equals(calendar.getBdate().toString());
            boolean isMorningStart = starting.equals("1");
            boolean isMorningEnd = ending.equals("1");
            boolean isEveningStart = starting.equals("2");
            boolean isEveningEnd = ending.equals("2");

            if (isSameDay) {
                // Same day booking cases
                if (isMorningStart && isMorningEnd
                        && (calendar.getBdate().toString().equals(fromDate) && calendar.isMorningSlot())) {
                    // System.out.println("Same day morning-morning match found: hallId=" +
                    // calendar.getHallId());
                    removeHallId.add(calendar.getHallId());
                } else if (isEveningStart && isEveningEnd
                        && (calendar.getBdate().toString().equals(fromDate) && calendar.isEveningSlot())) {
                    // System.out.println("Same day evening-evening match found: hallId=" +
                    // calendar.getHallId());
                    removeHallId.add(calendar.getHallId());
                } else if (isMorningStart && isEveningEnd &&
                        ((calendar.getBdate().toString().equals(fromDate) && calendar.isMorningSlot()) ||
                                (calendar.getBdate().toString().equals(toDate) && calendar.isEveningSlot()))) {
                    // System.out.println("Same day morning-evening match found: hallId=" +
                    // calendar.getHallId());
                    removeHallId.add(calendar.getHallId());
                }
            } else {
                // Multi-day booking cases
                if ((isMorningStart && isMorningEnd) &&
                        ((calendar.getBdate().toString().equals(fromDate) && calendar.isMorningSlot()) ||
                                (calendar.getBdate().toString().equals(toDate) && calendar.isMorningSlot()))) {
                    // System.out.println("Multi-day morning-morning match found: hallId=" +
                    // calendar.getHallId());
                    removeHallId.add(calendar.getHallId());
                } else if ((isEveningStart && isEveningEnd) &&
                        ((calendar.getBdate().toString().equals(fromDate) && calendar.isEveningSlot()) ||
                                (calendar.getBdate().toString().equals(toDate) && calendar.isEveningSlot()))) {
                    // System.out.println("Multi-day evening-evening match found: hallId=" +
                    // calendar.getHallId());
                    removeHallId.add(calendar.getHallId());
                } else if (isMorningStart && isEveningEnd &&
                        ((calendar.getBdate().toString().equals(fromDate) && calendar.isMorningSlot()) ||
                                (calendar.getBdate().toString().equals(toDate) && calendar.isEveningSlot()))) {
                    // System.out.println("Multi-day morning-evening match found: hallId=" +
                    // calendar.getHallId());
                    removeHallId.add(calendar.getHallId());
                } else if (isEveningStart && isMorningEnd &&
                        ((calendar.getBdate().toString().equals(fromDate) && calendar.isEveningSlot()) ||
                                (calendar.getBdate().toString().equals(toDate) && calendar.isMorningSlot()))) {
                    // System.out.println("Multi-day evening-morning match found: hallId=" +
                    // calendar.getHallId());
                    removeHallId.add(calendar.getHallId());
                }
            }
        }

        // Remove unavailable halls from the list
        hallsByZone.removeIf(hall -> removeHallId.contains(hall.getHallId()));
        List<HallDetails> removeHallById = new ArrayList<>();
        if (hallIdd != 0) {
            HallDetails hallDetails = hallDetailService.getHallById(hallIdd);
            // Ensure the hall is available before adding it to the list
            if (!removeHallId.contains(hallIdd)) {
                removeHallById.add(hallDetails);
            }
        } else {
            removeHallById = hallDetailService.removeHallById(removeHallId);
        }

        // Populate the model attributes
        model.addAttribute("userName", userSession.getDispayName());
        model.addAttribute("hallnames", hallnames);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("startingSlot", starting.equals("1") ? "Morning" : "Evening");
        model.addAttribute("endingSlot", ending.equals("1") ? "Morning" : "Evening");
        model.addAttribute("halls", (hallId != null && !hallId.isEmpty()) ? removeHallById : hallsByZone);
        model.addAttribute("counter", userSession.getCounter());
        model.addAttribute("counterName", userSession.getCounterName());

        return "user/dashboard";
    }

    @PostMapping("/newregistrationform")
    public String showRegistrationForm(@ModelAttribute("newform") Users newform, Model model) {
        Users user = userService.user(newform);
        userSession.setMobileno(user.getMobileNo());
        userSession.setDispayName(user.getName());
        userSession.setUserId(user.getUserId());
        userSession.setUserType(user.getUsertype().toString());
        System.out.println("usetrtype==" + user.getUsertype().toString());
        // System.out.println("employee id"+user.getGovernmentId());
        // System.out.println("employee id"+user.getDepartment());
        if (user.getUsertype() == userType.PUBLIC) {
            String registersms = "Welcome to GCC, Your User Registration for booking Community Centre is successful.";
            String registerurlString = "https://tmegov.onex-aura.com/api/sms?key=pfTEYN6H&to=" + user.getMobileNo()
                    + "&from=GCCCRP&body=" + registersms
                    + "&entityid=1401572690000011081&templateid=1407172346621223019";
            String registerwhatsupmsg = "https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to="
                    + user.getMobileNo()
                    + "&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Welcome+to+GCC%2C+%0A%0AYour+User+Registration+for+booking+Community+Centre+is+successful.";
            // whatsappMsgServiceImpl.sendMsg(registerwhatsupmsg);
            // whatsappMsgServiceImpl.sendSMS(registerurlString);
        } else {
            String registerpendingsms = "Welcome to GCC, Your User Registration request for booking Community Centre is submitted successfully.";
            String registerpendingurlString = "https://tmegov.onex-aura.com/api/sms?key=pfTEYN6H&to="
                    + user.getMobileNo() + "&from=GCCCRP&body=" + registerpendingsms
                    + "&entityid=1401572690000011081&templateid=1407172346629919381";
            String registerpendingwhatsupmsg = "https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to="
                    + user.getMobileNo()
                    + "&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Welcome+to+GCC%2C+%0A%0AYour+User+Registration+request+for+booking+Community+Centre+submitted+successfully.";
            // whatsappMsgServiceImpl.sendMsg(registerpendingwhatsupmsg);
            // whatsappMsgServiceImpl.sendSMS(registerpendingurlString);
        }
        return "redirect:/victoriahallbooking_uat/dashboard";
    }

    @GetMapping("/hallbookingform")
    public String viewform(Model model) {
        String username = userSession.getDispayName();
        // System.out.println("server Date22....."+LocalDate.now().toString());
        model.addAttribute("serverDate", LocalDate.now().toString()); // for server date
        model.addAttribute("userName", username);
        model.addAttribute("counter", userSession.getCounter());
        model.addAttribute("counterName", userSession.getCounterName());
        return "user/hallbookingform";
    }

    // @GetMapping("/acknowledgement")
    // public String viewakowledgement(Model model, int bookingId) {
    // BookingDetails bookingsDetails = bookingService.getBookingsById(bookingId);
    // model.addAttribute("bookingsDetails", bookingsDetails); // Add single
    // BookingDetails object
    // model.addAttribute("userName", userSession.getDispayName());
    // return "user/acknowledgement";
    // }

    // ak
    @GetMapping("/acknowledgement")
    public String viewAcknowledgement(@RequestParam("ref_id") String refId, Model model) {

        // Fetch booking using booking_ref_id
        BookingDetails bookingsDetails = bookingService.getBookingByRefId(refId);

        int bookingId =  bookingsDetails.getBookingId() ;

        Map<String, Object> transactionDetails =
                paymentsServiceImpl.getTransactionPaymentDetailsByBookingId(bookingId);

        if (bookingsDetails == null) {
            model.addAttribute("errorMsg", "Invalid booking reference number.");
            return "user/error";
        }

        model.addAttribute("bookingsDetails", bookingsDetails);
        model.addAttribute("userName", userSession.getDispayName());

        model.addAttribute(
                    "orderId",
                transactionDetails.getOrDefault("order_id", "NA")
        );

        model.addAttribute(
                "PaymentId",
                transactionDetails.getOrDefault("payment_id", "NA")
        );

        return "user/acknowledgement";
    }

    // ak
    @PostMapping("/migrate-old-bookings")
    @ResponseBody
    public ResponseEntity<String> migrateOldBookings() {
        try {
            bookingService.migrateOldBookingsToRefId();
            return ResponseEntity.ok("Migration completed successfully. All old bookings now have ref_ids.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Migration failed: " + e.getMessage());
        }
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        // Clear all login session (Check API Controller /gcc-login)
        userSession.setUserId(null);
        userSession.setMobileno(null);
        userSession.setUserType(null);
        userSession.setDispayName(null);
        return "redirect:/victoriahallbooking_uat/user/dashboard";
    }

    /*
     * @GetMapping("/viewhistory")
     * public String viewBookingDetails(Model model, @RequestParam(name =
     * "bookingStatus", required = false) String bookingStatus) {
     * // Retrieve userId from session
     * Integer userId = userSession.getUserId();
     * model.addAttribute("userName", userSession.getDispayName());
     * 
     * //List<String> test = new ArrayList<>();
     * model.addAttribute("userName", userSession.getDispayName());
     * model.addAttribute("counter", userSession.getCounter());
     * model.addAttribute("counterName", userSession.getCounterName());
     * 
     * List<Map<String, Object>> bookingPast =
     * bookingService.getAllBookingEventsByUserId(userId);
     * 
     * model.addAttribute("userBookingspast", bookingPast);
     * model.addAttribute("serverDate", LocalDate.now().toString()); //for server
     * date
     * 
     * return "user/viewhistory";
     * }
     */
    // ak
    @GetMapping("/viewhistory")
    public String viewBookingDetails(Model model,
            @RequestParam(name = "bookingStatus", required = false) String bookingStatus) {

        Integer userId = userSession.getUserId();

        model.addAttribute("userName", userSession.getDispayName());
        model.addAttribute("counter", userSession.getCounter());
        model.addAttribute("counterName", userSession.getCounterName());

        // Fetch all past bookings
        List<Map<String, Object>> bookingPast = bookingService.getAllBookingEventsByUserId(userId);

        // Extract booking_ref_id per booking
        Map<Integer, String> refIdMap = new HashMap<>();

        if (bookingPast != null && !bookingPast.isEmpty()) {

            for (Map<String, Object> row : bookingPast) {

                Integer bookingId = (Integer) row.get("booking_id");
                String refId = (String) row.get("booking_ref_id"); // <-- coming from SQL

                if (bookingId != null && refId != null) {
                    refIdMap.put(bookingId, refId);
                }
            }
        }
        // System.out.println("Row keys: " + bookingPast.get(0).keySet());
        // System.out.println("RefId Map123: " + refIdMap);

        model.addAttribute("userBookingspast", bookingPast);
        model.addAttribute("bookingRefIds", refIdMap); // <-- SENT TO FRONTEND
        model.addAttribute("serverDate", LocalDate.now().toString());

        return "user/viewhistory";
    }

    @GetMapping("/communityhallList")
    public String viewhalllist(Model model) {
        model.addAttribute("userName", userSession.getDispayName());
        model.addAttribute("counter", userSession.getCounter());
        model.addAttribute("counterName", userSession.getCounterName());
        return "user/communityhallList";
    }

    @GetMapping("/hallAmountCalculation")
    public String viewCalculate(Model model) {
        model.addAttribute("userName", userSession.getDispayName());
        model.addAttribute("counter", userSession.getCounter());
        model.addAttribute("counterName", userSession.getCounterName());
        return "user/calculatehallamount";
    }

    @GetMapping("/collectionpending")
    public String viewCollectionpending(Model model, int bookingId) {
        BookingDetails bookingsDetails = bookingService.getBookingsById(bookingId);
        model.addAttribute("bookingsDetails", bookingsDetails); // Add single BookingDetails object
        model.addAttribute("userName", userSession.getDispayName());
        return "user/collectionpending";
    }

    @GetMapping("/collectionpendingacknowledgement")
    public String viewCollectionpendingAck(Model model, int bookingId) {
        BookingDetails bookingsDetails = bookingService.getBookingsById(bookingId);
        model.addAttribute("bookingsDetails", bookingsDetails); // Add single BookingDetails object
        model.addAttribute("userName", userSession.getDispayName());
        return "user/extraebdamageamountack";
    }

    @GetMapping("/viewhallform")
    public String viewhallform(Model model) {
        String username = userSession.getDispayName();
        model.addAttribute("userName", username);
        model.addAttribute("serverDate", LocalDate.now().toString()); // for server date
        return "user/viewhallform";

    }

}
