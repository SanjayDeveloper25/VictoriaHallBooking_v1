package com.gcc.communityHall.service.Impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gcc.communityHall.configuration.UuidBookingUtil;
import com.gcc.communityHall.entity.ApplicationLogs;
import com.gcc.communityHall.entity.BookingCalendar;
import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.BookingDetails.BookingStatus;
import com.gcc.communityHall.entity.BookingDetails.RefundStatus;
import com.gcc.communityHall.entity.BookingDocuments;
import com.gcc.communityHall.entity.BookingHistory;
import com.gcc.communityHall.entity.Damage;
import com.gcc.communityHall.entity.DiscountRange;
import com.gcc.communityHall.entity.EbDetails;
import com.gcc.communityHall.entity.GccBlockingDate;
import com.gcc.communityHall.entity.GccBlockingHistory;
import com.gcc.communityHall.entity.GccblockingDocuments;
import com.gcc.communityHall.entity.HallDetails;
import com.gcc.communityHall.entity.HallDetailsConcession;
import com.gcc.communityHall.entity.HallRequestDetails;
import com.gcc.communityHall.entity.ReasonDocumentMaster;
import com.gcc.communityHall.entity.ReasonMaster;
import com.gcc.communityHall.entity.Settlement;
import com.gcc.communityHall.entity.SettlementHistory;
import com.gcc.communityHall.entity.Users;
import com.gcc.communityHall.exception.SlotOccupiedException;
import com.gcc.communityHall.others.UserSession;
import com.gcc.communityHall.paymentService.repository.TransRefundResRepository;
import com.gcc.communityHall.paymentService.repository.TransactionResponseRepository;
import com.gcc.communityHall.paymentService.service.PaymentGatewayService;
import com.gcc.communityHall.repository.ApplicationLogsRepository;
import com.gcc.communityHall.repository.BookingCalendarRepository;
import com.gcc.communityHall.repository.BookingDetailsRepository;
import com.gcc.communityHall.repository.BookingDocumentsRepository;
import com.gcc.communityHall.repository.BookingHistoryRepository;
import com.gcc.communityHall.repository.DamageRepository;
import com.gcc.communityHall.repository.EbDetailsRepository;
import com.gcc.communityHall.repository.GccBlockingDateRepository;
import com.gcc.communityHall.repository.GccBlockingHistoryRepository;
import com.gcc.communityHall.repository.GccblockingDocumentsRepo;
import com.gcc.communityHall.repository.HallDetailsConcessionRepo;
import com.gcc.communityHall.repository.HallDetailsRepository;
import com.gcc.communityHall.repository.ReasonMasterRepository;
import com.gcc.communityHall.repository.SettlementHistoryRepository;
import com.gcc.communityHall.repository.SettlementRepository;
import com.gcc.communityHall.repository.UsersRepository;
import com.gcc.communityHall.repository.HallRequestDetailsRepository;
import com.gcc.communityHall.repository.PaymentsRepository;
import com.gcc.communityHall.service.ApplicationLogService;
import com.gcc.communityHall.service.BookingService;
import com.gcc.communityHall.service.DiscountRangeService;
import com.gcc.communityHall.service.HallDetailService;
import com.gcc.communityHall.service.ReasonMasterService;
import com.gcc.communityHall.service.UserService;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingDetailsRepository bookingDetailsRepository;
    @Autowired
    private BookingDocumentsRepository bookingDocumentsRepository;

    @Autowired
    private ReasonMasterService reasonMasterService;
    @Autowired
    private HallDetailService hallDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private UserSession userSession;

    @Autowired
    private GccBlockingDateRepository gccBlockingDateRepository;

    @Autowired
    private BookingCalendarRepository bookingCalendarRepository;

    @Autowired
    private BookingHistoryRepository bookingHistoryRepository;

    @Autowired
    private EbDetailsRepository ebDetailsRepository;

    @Autowired
    private DamageRepository damageRepository;

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    private ApplicationLogService applicationLogService;

    @Autowired
    private PaymentGatewayService paymentGatewayService;

    @Autowired
    private GccBlockingDateRepository blockingDateRepository;

    @Autowired
    private DiscountRangeService discountRangeService;

    @Autowired
    private HallDetailsRepository hallDetailsRepository;

    @Autowired
    private GccBlockingHistoryRepository blockingHistoryRepository;

    @Autowired
    private WhatsappMsgServiceImpl whatsappMsgServiceImpl;

    @Autowired
    private GccblockingDocumentsRepo gccblockingDocumentsRepo;

    @Autowired
    private HttpSession session;
    @Autowired
    private SettlementHistoryRepository settlementHistoryRepository;

    @Autowired
    private HallDetailsConcessionRepo hallDetailsConcessionRepo;

    @Autowired
    private ReasonMasterRepository reasonMasterRepository;

    @Autowired
    private ApplicationLogsRepository applicationLogsRepository;

    @Autowired
    private TransactionResponseRepository transactionResponseRepository;

    @Autowired
    private TransRefundResRepository transRefundResRepository;

    @Autowired
    private HallRequestDetailsRepository hallRequestDetailsRepository;

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Value("${file.upload.directory}")
    private String uploadDirectory;

    @Value("${file.fullpath}")
    private String serverPath;
    private final PlatformTransactionManager transactionManager;

    public BookingServiceImpl(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public BookingDetails getBookingsById(int BookingId) {
        Optional<BookingDetails> OPBookingDetails = bookingDetailsRepository.findById(BookingId);

        if (OPBookingDetails.isPresent()) {
            return OPBookingDetails.get();
        }
        return null;
    }

    // ak
    @Override
    public BookingDetails getBookingByRefId(String refId) {
        return bookingDetailsRepository.findByRefId(refId);
    }

    // ak
    @Transactional
    @Override
    public void migrateOldBookingsToRefId() {

        List<BookingDetails> oldBookings = bookingDetailsRepository.findAllOldBookingsWithoutRefId();

        for (BookingDetails booking : oldBookings) {

            int bookingId = booking.getBookingId();

            // Generate ref_id
            String generatedRefId = UuidBookingUtil.generateRefIdForBooking(bookingId);

            // Set into entity
            booking.setBookingRefId(generatedRefId);

            // Save
            bookingDetailsRepository.save(booking);

            System.out.println("Migrated booking ID " + bookingId +
                    " → ref_id = " + generatedRefId);
        }
    }

    // ak
    @Transactional
    public String getBookingRefIdByBookingId(int bookingId) {

        String refId = bookingDetailsRepository.getRefIdByBookingId(bookingId);

        // If missing → create new
        if (refId == null || refId.trim().isEmpty()) {

            // Generate refId using coworkspace logic
            refId = UuidBookingUtil.generateRefIdForBooking(bookingId);

            // Update using JPA repository
            bookingDetailsRepository.updateRefId(refId, bookingId);
        }

        return refId;
    }

    @Override
    public BookingDetails updateBookingDetails(BookingDetails bd) {
        return bookingDetailsRepository.save(bd);
    }

    @Override
    public BookingDetails updateBookingStatus(int bookingId, BookingDetails.BookingStatus status, String comments) {
        Optional<BookingDetails> booking = bookingDetailsRepository.findById(bookingId);
        String apikey = "5c995535-6244-11f0-98fc-02c8a5e042bd";
        String from = "919445061913";

        String hallName, userName, endDate;

        String[] numbers = { "9123565217", "9789091845", "9894935693" }; // for testing purpose
//        String[] numbers = { "9123565217", "9789091845", "9894935693",
//                "9444173345", "9994697909", "9176617754", "9445190998", "9381013498",
//                "9789977111" };
        if (booking.isPresent()) {
            BookingDetails book = booking.get();
            /*
             * BookingHistory bookingHistory = new BookingHistory();
             * bookingHistory.setBookingDetails(book);
             * bookingHistory.setStartDate(book.getStartDate());
             * bookingHistory.setEndDate(book.getEndDate());
             * bookingHistory.setBookingStatus(book.getBookingStatus());
             * bookingHistory.setComments(book.getComments());
             * bookingHistory.setCdate(new Date());
             * bookingHistory.setRefundStatus(book.getRefundStatus());
             * System.out.println("bookingHistory.."+bookingHistory);
             * bookingHistoryRepository.save(bookingHistory);
             */

            book.setBookingStatus(status);
            book.setComments(comments);
            BookingDetails bookings = bookingDetailsRepository.save(book);

            if ("APPROVED".equals(status)) {
                for (String mobNo : numbers) {

                    hallName = bookings.getHallDetails().getHallName();
                    userName = bookings.getUsers().getName();
                    // Format endDate from yyyy-MM-dd to dd-MM-yyyy
                    String rawEndDate = bookings.getEndDate();
                    try {
                        LocalDate date = LocalDate.parse(rawEndDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        endDate = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    } catch (Exception e) {
                        endDate = rawEndDate; // Fallback to original if parsing fails
                    }

                    String placeholders = "";
                    try {
                        placeholders = URLEncoder.encode(userName + "|~|" + hallName + "|~|" + endDate, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    String whatsappMsg = "https://sendapiv1.pinbot.ai/pinwa/sendMessage?apikey=" + apikey + "&from="
                            + from
                            + "&to=91" + mobNo + ""
                            + "&type=template&templateid=2815237&placeholders=" + placeholders + "";

                    System.out.println(whatsappMsg + ":" + mobNo);

                    whatsappMsgServiceImpl.sendMsg(whatsappMsg);
                }
            }

            return bookings;
        }
        return null;
    }

    @Override
    public String confirmTheDateAndSlot(String fromDate, String toDate, int days, int fromStartingSlot,
            int fromEndingSlot, int toStartingSlot, int toEndingSlot, int bookingId, int userId) {
        return null;
    }

    @Override
    @Transactional
    public String confirmBooking(int bookingId) {
        String exMsg = "Slot occupied";
        BookingDetails bookingDetails = null;
        try {
            Optional<BookingDetails> bookingDet = bookingDetailsRepository.findById(bookingId);

            if (!bookingDet.isPresent()) {
                // Handle invalid booking I D
                return "Invalid booking ID";
            }
            bookingDetails = bookingDet.get();
            // Validate booking details
            int hallId = bookingDetails.getHallDetails().getHallId();
            String hallName = bookingDetails.getHallDetails().getHallName();
            String userName = bookingDetails.getUsers().getName();
            LocalDate startDate = LocalDate.parse(bookingDetails.getStartDate());
            LocalDate endDate = LocalDate.parse(bookingDetails.getEndDate());

            // Iterate through each date of the booking
            int i = 0;
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                BookingCalendar bookingCalendar = bookingCalendarRepository.findByBdateAndHallId(date, hallId);

                if (bookingCalendar != null) {
                    // System.out.println(bookingCalendar.toString());
                    // System.out.println("date:-"+date );
                    // System.out.println("bookingDetails.getEndingSlot() :-
                    // "+bookingDetails.getEndingSlot());
                    // System.out.println("bookingCalendar.isMorningSlot():-"+bookingCalendar.isMorningSlot());
                    // System.out.println(date.equals(endDate) && bookingDetails.getEndingSlot() ==
                    // 1 && bookingCalendar.isMorningSlot());
                    // System.out.println();
                    // Check if the slot is already occupied
                    if (bookingCalendar.getIs_blocked()) {
                        throw new RuntimeException("GCC Blocked Date");
                    } else if ((date.equals(startDate) && bookingDetails.getStartingSlot() == 1
                            && bookingCalendar.isMorningSlot()) ||
                            (date.equals(startDate) && bookingDetails.getStartingSlot() == 2
                                    && bookingCalendar.isEveningSlot())
                            ||
                            (date.equals(endDate) && bookingDetails.getEndingSlot() == 1
                                    && bookingCalendar.isMorningSlot())
                            ||
                            (date.equals(endDate) && bookingDetails.getEndingSlot() == 2
                                    && bookingCalendar.isEveningSlot())) {

                        // System.out.println("booking slot occupied:- 218");
                        throw new RuntimeException(exMsg);
                    } else if (bookingCalendar.isMorningSlot() && bookingCalendar.isEveningSlot()) {
                        // System.out.println("booking slot occupied:- 221");
                        throw new RuntimeException(exMsg);

                    } else if (!date.equals(endDate) && i != 0 && i != Math.round(bookingDetails.getNoOfDays() - 1)
                            && (bookingCalendar.isMorningSlot() || bookingCalendar.isEveningSlot())) {
                        // System.out.println("booking slot occupied:- 227
                        // :-"+Math.round(bookingDetails.getNoOfDays()-1) );
                        throw new RuntimeException(exMsg);
                    } else if (date.equals(endDate) && bookingDetails.getEndingSlot() == 2
                            && bookingCalendar.isMorningSlot()
                            && i != 0 && i == Math.round(bookingDetails.getNoOfDays() - 1)) {
                        // System.out.println("booking slot occupied:- 240
                        // :-"+Math.round(bookingDetails.getNoOfDays()-1) );
                        throw new RuntimeException(exMsg);
                    }

                } else {
                    String uniqueId = date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + hallId;
                    System.out.println("uniqueId....." + uniqueId);

                    bookingCalendar = new BookingCalendar();
                    bookingCalendar.setBdate(date);
                    bookingCalendar.setHallId(hallId);
                    bookingCalendar.setIs_booked(0);
                    bookingCalendar.setIs_active(true);
                    bookingCalendar.setUnique_id(uniqueId);
                }

                // Mark the appropriate slot as booked based on starting and ending slots
                if (date.equals(startDate)) {
                    // System.out.println(i + "start :-"+bookingCalendar.toString());

                    if (bookingDetails.getStartingSlot() == 1) {
                        bookingCalendar.setMorningSlot(true);
                        bookingCalendar.setBookingId_1(String.valueOf(bookingId));
                        bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                        if (!date.equals(endDate)) {
                            bookingCalendar.setEveningSlot(true);
                            bookingCalendar.setBookingId_2(String.valueOf(bookingId));
                            bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                        } else if (startDate.equals(endDate) && bookingDetails.getStartingSlot() == 1
                                && bookingDetails.getEndingSlot() == 2) {
                            bookingCalendar.setEveningSlot(true);
                            bookingCalendar.setBookingId_2(String.valueOf(bookingId));
                            bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                        }

                    } else {
                        bookingCalendar.setEveningSlot(true);
                        bookingCalendar.setBookingId_2(String.valueOf(bookingId));
                        bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                    }
                } else if (date.equals(endDate)) {
                    if (bookingDetails.getEndingSlot() == 1) {
                        bookingCalendar.setMorningSlot(true);
                        bookingCalendar.setBookingId_1(String.valueOf(bookingId));
                        bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                    } else {
                        bookingCalendar.setEveningSlot(true);
                        bookingCalendar.setBookingId_2(String.valueOf(bookingId));
                        bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                        if (bookingDetails.getEndingSlot() == 2) {
                            bookingCalendar.setMorningSlot(true);
                            bookingCalendar.setBookingId_1(String.valueOf(bookingId));
                            bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                        }
                    }
                } else {
                    // For dates between start and end date, mark both slots as booked
                    bookingCalendar.setMorningSlot(true);
                    bookingCalendar.setEveningSlot(true);
                    bookingCalendar.setBookingId_1(String.valueOf(bookingId));
                    bookingCalendar.setBookingId_2(String.valueOf(bookingId));
                    bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 2);
                }
                // System.out.println(i + "end:-"+bookingCalendar.toString());
                bookingCalendarRepository.save(bookingCalendar);
                i++;
            }

            String statusMessage = processBookingStatusAndSendMessages(bookingDetails, hallName, userName, endDate);

            return statusMessage;

            /*
             * String reason = bookingDetails.getReason();
             * ReasonMaster reasondetails = reasonMasterRepository.findByReason(reason);
             * String reasoncode = reasondetails.getReasoncode();
             * if (reasoncode == null) {
             * reasoncode = ""; // or any default value
             * }
             * if (reasoncode.equals("CGE") || reasoncode.equals("CAO") ||
             * reasoncode.equals("SGE")) {
             * updateBookingStatusById(bookingDetails.getBookingId(),
             * BookingDetails.BookingStatus.WAITING_FOR_NOC, "Waiting For NOC Documents");
             * String bookingconfirmationmsg = "Your booking ID " +
             * bookingDetails.getBookingId() + " on " + bookingDetails.getBookingDate() +
             * " has been confirmed successfully. Thanks for using GCC Community Hall Booking System."
             * ;
             * String urlString = "https://tmegov.onex-aura.com/api/sms?key=pfTEYN6H&to=" +
             * bookingDetails.getUsers().getMobileNo() + "&from=GCCCRP&body=" +
             * bookingconfirmationmsg +
             * "&entityid=1401572690000011081&templateid=1407172346459428987";
             * String whatsupmsg =
             * "https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to="
             * + bookingDetails.getUsers().getMobileNo() +
             * "&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Your+booking+with+ID+"
             * + bookingDetails.getBookingId() + "+on+" + bookingDetails.getBookingDate() +
             * "+has+been+successfully+confirmed.+%0AThanks+for+using+GCC+Community+Hall+Booking+System.";
             * whatsappMsgServiceImpl.sendMsg(whatsupmsg);
             * whatsappMsgServiceImpl.sendSMS(urlString);
             * return "Waiting For NOC Documents";
             * } else {
             * updateBookingStatusById(bookingDetails.getBookingId(),
             * BookingDetails.BookingStatus.APPROVED, "Booking Confirmed Successfully");
             * String bookingconfirmationmsg = "Your booking ID " +
             * bookingDetails.getBookingId() + " on " + bookingDetails.getStartDate() +
             * " has been confirmed successfully. Thanks for using GCC Community Hall Booking System."
             * ;
             * String urlString = "https://tmegov.onex-aura.com/api/sms?key=pfTEYN6H&to=" +
             * bookingDetails.getUsers().getMobileNo() + "&from=GCCCRP&body=" +
             * bookingconfirmationmsg +
             * "&entityid=1401572690000011081&templateid=1407172346459428987";
             * String whatsupmsg =
             * "https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to="
             * + bookingDetails.getUsers().getMobileNo() +
             * "&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Your+booking+with+ID+"
             * + bookingDetails.getBookingId() + "+on+" + bookingDetails.getStartDate() +
             * "+has+been+successfully+confirmed.+%0AThanks+for+using+GCC+Community+Hall+Booking+System.";
             * whatsappMsgServiceImpl.sendMsg(whatsupmsg);
             * whatsappMsgServiceImpl.sendSMS(urlString);
             * return "Booking confirmed successfully";
             * }
             */
        } catch (Exception e) {
            e.printStackTrace();
            bookingFailure(bookingDetails, 2);
            // System.out.println(bookingDetails.getBookingStatus()+","+bookingDetails.getRefundStatus()+","+bookingDetails.getComments());
            // return "redirect:/communityhall/user/acknowledgement?bookingId=" +
            // bookingDetails.getBookingId();
            return "redirect:/victoriahallbooking_uat/user/acknowledgement?ref_id=" + bookingDetails.getBookingRefId();
        }

    }

    @Transactional
    public String processBookingStatusAndSendMessages(BookingDetails bookingDetails, String hallName, String userName,
            LocalDate endDate) {
        try {

            String apikey = "5c995535-6244-11f0-98fc-02c8a5e042bd";
            String from = "919445061913";

            String reason = bookingDetails.getReason();
            ReasonMaster reasonDetails = reasonMasterRepository.findByReason(reason);
            String reasonCode = (reasonDetails != null && reasonDetails.getReasoncode() != null)
                    ? reasonDetails.getReasoncode()
                    : "";

             String[] numbers = { "9123565217", "9789091845", "9894935693" }; // for testing purpose
//            String[] numbers = { "9123565217", "9789091845", "9894935693",
//                    "9444173345", "9994697909", "9176617754", "9445190998", "9381013498",
//                    "9789977111" };

            String bookingconfirmationmsg, urlString, whatsupmsg, bookingmsg;

            if (reasonCode.equals("CGE") || reasonCode.equals("CAO") || reasonCode.equals("SGE")) {
                updateBookingStatusById(bookingDetails.getBookingId(), BookingDetails.BookingStatus.WAITING_FOR_NOC,
                        "Waiting For NOC Documents");

                bookingconfirmationmsg = "Your booking ID " + bookingDetails.getBookingId() + " on "
                        + bookingDetails.getBookingDate()
                        + " has been confirmed successfully. Thanks for using GCC Community Hall Booking System.";

                urlString = "https://tmegov.onex-aura.com/api/sms?key=pfTEYN6H&to="
                        + bookingDetails.getUsers().getMobileNo() +
                        "&from=GCCCRP&body=" + bookingconfirmationmsg
                        + "&entityid=1401572690000011081&templateid=1407172346459428987";

                whatsupmsg = "https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to="
                        +
                        bookingDetails.getUsers().getMobileNo() +
                        "&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Your+booking+with+ID+" +
                        bookingDetails.getBookingId() + "+on+" + bookingDetails.getBookingDate() +
                        "+has+been+successfully+confirmed.+%0AThanks+for+using+GCC+Community+Hall+Booking+System.";

                for (String mobNo : numbers) {

                    String encodedHN = URLEncoder.encode(hallName, StandardCharsets.UTF_8);

                    // String endDateStr = endDate+"-%20Test%20message%20please%20ignore";

                    String msg = "A%20new%20" + encodedHN + "%20booking%20has%20been%20successfully%20made%20by%20"
                            + userName + "%20for%20" + endDate + ".";

                    String encodedFooter = URLEncoder.encode("- GCC IT Cell", StandardCharsets.UTF_8);
                    bookingmsg = "https://mediaapi.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ" +
                            "&send_to=" + mobNo + "&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=" + msg
                            + "&isTemplate=true&footer=" + encodedFooter + "";

                    System.out.println(bookingmsg + ":" + mobNo);

                    String placeholders = URLEncoder.encode(hallName + "|~|" + userName + "|~|" + endDate, "UTF-8");

                    String whatsappMsg = "https://sendapiv1.pinbot.ai/pinwa/sendMessage?apikey=" + apikey + "&from="
                            + from + "&to=91" + mobNo + ""
                            + "&type=template&templateid=2815236&placeholders=" + placeholders + "";

                    System.out.println(whatsappMsg + ":" + mobNo);

                    whatsappMsgServiceImpl.sendMsg(whatsappMsg);
                }
                // whatsappMsgServiceImpl.sendSMS(urlString);
                return "Waiting For NOC Documents";

            } else {
                updateBookingStatusById(bookingDetails.getBookingId(), BookingDetails.BookingStatus.APPROVED,
                        "Booking Confirmed Successfully");

                bookingconfirmationmsg = "Your booking ID " + bookingDetails.getBookingId() + " on "
                        + bookingDetails.getStartDate()
                        + " has been confirmed successfully. Thanks for using GCC Community Hall Booking System.";

                urlString = "https://tmegov.onex-aura.com/api/sms?key=pfTEYN6H&to="
                        + bookingDetails.getUsers().getMobileNo() +
                        "&from=GCCCRP&body=" + bookingconfirmationmsg
                        + "&entityid=1401572690000011081&templateid=1407172346459428987";

                whatsupmsg = "https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to="
                        +
                        bookingDetails.getUsers().getMobileNo() +
                        "&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Your+booking+with+ID+" +
                        bookingDetails.getBookingId() + "+on+" + bookingDetails.getStartDate() +
                        "+has+been+successfully+confirmed.+%0AThanks+for+using+GCC+Community+Hall+Booking+System.";

                // whatsappMsgServiceImpl.sendMsg(whatsupmsg);
                // whatsappMsgServiceImpl.sendSMS(urlString);
                return "Booking confirmed successfully";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @Override
    @Transactional
    public String confirmStateBooking(int bookingId) {
        String exMsg = "Slot occupied";
        BookingDetails bookingDetails = null;
        try {
            Optional<BookingDetails> bookingDet = bookingDetailsRepository.findById(bookingId);

            if (!bookingDet.isPresent()) {
                // Handle invalid booking ID
                return "Invalid booking ID";
            }
            bookingDetails = bookingDet.get();
            // Validate booking details
            int hallId = bookingDetails.getHallDetails().getHallId();
            LocalDate startDate = LocalDate.parse(bookingDetails.getStartDate());
            LocalDate endDate = LocalDate.parse(bookingDetails.getEndDate());

            // Iterate through each date of the booking
            int i = 0;
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                BookingCalendar bookingCalendar = bookingCalendarRepository.findByBdateAndHallId(date, hallId);

                if (bookingCalendar != null) {
                    // System.out.println(bookingCalendar.toString());
                    // System.out.println("date:-"+date );
                    // System.out.println("bookingDetails.getEndingSlot() :-
                    // "+bookingDetails.getEndingSlot());
                    // System.out.println("bookingCalendar.isMorningSlot():-"+bookingCalendar.isMorningSlot());
                    // System.out.println(date.equals(endDate) && bookingDetails.getEndingSlot() ==
                    // 1 && bookingCalendar.isMorningSlot());
                    // System.out.println();
                    // Check if the slot is already occupied
                    if ((date.equals(startDate) && bookingDetails.getStartingSlot() == 1
                            && bookingCalendar.isMorningSlot()) ||
                            (date.equals(startDate) && bookingDetails.getStartingSlot() == 2
                                    && bookingCalendar.isEveningSlot())
                            ||
                            (date.equals(endDate) && bookingDetails.getEndingSlot() == 1
                                    && bookingCalendar.isMorningSlot())
                            ||
                            (date.equals(endDate) && bookingDetails.getEndingSlot() == 2
                                    && bookingCalendar.isEveningSlot())) {
                        // System.out.println("booking slot occupied:- 218");
                        throw new RuntimeException(exMsg);
                    } else if (bookingCalendar.isMorningSlot() && bookingCalendar.isEveningSlot()) {
                        // System.out.println("booking slot occupied:- 221");
                        throw new RuntimeException(exMsg);

                    } else if (!date.equals(endDate) && i != 0 && i != Math.round(bookingDetails.getNoOfDays() - 1)
                            && (bookingCalendar.isMorningSlot() || bookingCalendar.isEveningSlot())) {
                        // System.out.println("booking slot occupied:- 227
                        // :-"+Math.round(bookingDetails.getNoOfDays()-1) );
                        throw new RuntimeException(exMsg);
                    } else if (date.equals(endDate) && bookingDetails.getEndingSlot() == 2
                            && bookingCalendar.isMorningSlot()
                            && i != 0 && i == Math.round(bookingDetails.getNoOfDays() - 1)) {
                        // System.out.println("booking slot occupied:- 240
                        // :-"+Math.round(bookingDetails.getNoOfDays()-1) );
                        throw new RuntimeException(exMsg);
                    }

                } else {
                    bookingCalendar = new BookingCalendar();
                    bookingCalendar.setBdate(date);
                    bookingCalendar.setHallId(hallId);
                    bookingCalendar.setIs_booked(0);
                    bookingCalendar.setIs_active(true);
                }

                // Mark the appropriate slot as booked based on starting and ending slots
                if (date.equals(startDate)) {
                    // System.out.println(i + "start :-"+bookingCalendar.toString());

                    if (bookingDetails.getStartingSlot() == 1) {
                        bookingCalendar.setMorningSlot(true);
                        bookingCalendar.setBookingId_1(String.valueOf(bookingId));
                        bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                        if (!date.equals(endDate)) {
                            bookingCalendar.setEveningSlot(true);
                            bookingCalendar.setBookingId_2(String.valueOf(bookingId));
                            bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                        } else if (startDate.equals(endDate) && bookingDetails.getStartingSlot() == 1
                                && bookingDetails.getEndingSlot() == 2) {
                            bookingCalendar.setEveningSlot(true);
                            bookingCalendar.setBookingId_2(String.valueOf(bookingId));
                            bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                        }

                    } else {
                        bookingCalendar.setEveningSlot(true);
                        bookingCalendar.setBookingId_2(String.valueOf(bookingId));
                        bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                    }
                } else if (date.equals(endDate)) {
                    if (bookingDetails.getEndingSlot() == 1) {
                        bookingCalendar.setMorningSlot(true);
                        bookingCalendar.setBookingId_1(String.valueOf(bookingId));
                        bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                    } else {
                        bookingCalendar.setEveningSlot(true);
                        bookingCalendar.setBookingId_2(String.valueOf(bookingId));
                        bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                        if (bookingDetails.getEndingSlot() == 2) {
                            bookingCalendar.setMorningSlot(true);
                            bookingCalendar.setBookingId_1(String.valueOf(bookingId));
                            bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                        }
                    }
                } else {
                    // For dates between start and end date, mark both slots as booked
                    bookingCalendar.setMorningSlot(true);
                    bookingCalendar.setEveningSlot(true);
                    bookingCalendar.setBookingId_1(String.valueOf(bookingId));
                    bookingCalendar.setBookingId_2(String.valueOf(bookingId));
                    bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 2);
                }

                // System.out.println(i + "end:-"+bookingCalendar.toString());
                bookingCalendarRepository.save(bookingCalendar);
                i++;
            }
            updateBookingStatusById(bookingDetails.getBookingId(), BookingDetails.BookingStatus.WAITING_FOR_NOC,
                    "Waiting for NOC Documents");
            String bookingconfirmationmsg = "Your booking ID " + bookingDetails.getBookingId() + " on "
                    + bookingDetails.getBookingDate()
                    + " has been confirmed successfully. Thanks for using GCC Community Hall Booking System.";
            String urlString = "https://tmegov.onex-aura.com/api/sms?key=pfTEYN6H&to="
                    + bookingDetails.getUsers().getMobileNo() + "&from=GCCCRP&body=" + bookingconfirmationmsg
                    + "&entityid=1401572690000011081&templateid=1407172346459428987";
            String whatsupmsg = "https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to="
                    + bookingDetails.getUsers().getMobileNo()
                    + "&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Your+booking+with+ID+"
                    + bookingDetails.getBookingId() + "+on+" + bookingDetails.getBookingDate()
                    + "+has+been+successfully+confirmed.+%0AThanks+for+using+GCC+Community+Hall+Booking+System.";
            whatsappMsgServiceImpl.sendMsg(whatsupmsg);
            whatsappMsgServiceImpl.sendSMS(urlString);
            // return "redirect:/communityhall/user/acknowledgement?bookingId=" +
            // bookingDetails.getBookingId();
            return "redirect:/victoriahallbooking_uat/user/acknowledgement?ref_id=" + bookingDetails.getBookingRefId();

        } catch (Exception e) {
            bookingFailure(bookingDetails, 2);
            // System.out.println(bookingDetails.getBookingStatus()+","+bookingDetails.getRefundStatus()+","+bookingDetails.getComments());
            // return "redirect:/communityhall/user/acknowledgement?bookingId=" +
            // bookingDetails.getBookingId();
            return "redirect:/victoriahallbooking_uat/user/acknowledgement?ref_id=" + bookingDetails.getBookingRefId();
        }

    }

    /*
     * @Transactional(propagation = Propagation.REQUIRES_NEW)
     * public String bookingFailure(BookingDetails bookingDetails) {
     * // System.out.println("Booking Failure:-"+bookingDetails.getBookingId());
     * bookingDetails.setBookingStatus(BookingStatus.REFUNDED);
     * bookingDetails.
     * setComments("Slot occupied by other user,Amount paid will be refunded.");
     * bookingDetailsRepository.save(bookingDetails);
     * if (!(bookingDetails.getRefundStatus() == RefundStatus.COMPLETED) &&
     * !(bookingDetails.getRefundStatus() == RefundStatus.CLOSED)) {
     * // BookingDetails bd =updateBookingStatusById(bookingDetails.getBookingId(),
     * BookingDetails.BookingStatus.REFUNDED,
     * "slot occupied Refund will happen shortly");
     * applicationLogService.saveLog(bookingDetails.getBookingId(),
     * ApplicationLogs.logsOfMsg.REFUNDED.toString(), "Refund will happen shortly");
     * try {
     * String refundedMsg =
     * paymentGatewayService.refundTheAmount(bookingDetails.getBookingId(),
     * bookingDetails.getUsers().getUserId());
     * //System.out.println("refundedMsg=="+refundedMsg);
     * if (refundedMsg.equals("Refunded")) {
     * //System.out.println("Refund completed........");
     * bookingDetails.setRefundStatus(BookingDetails.RefundStatus.COMPLETED);
     * bookingDetailsRepository.save(bookingDetails);
     * //System.out.println("refundStatus===="+bookingDetails.getRefundStatus());
     * long mobile = bookingDetails.getUsers().getMobileNo();
     * int bookingId = bookingDetails.getBookingId();
     * double refundAmt = bookingDetails.getAmountReceived();
     * // Thread thread = new Thread(() -> {
     * String settlementrefundsms = "Your refund amount of Rs. " + refundAmt +
     * " for Booking ID " + bookingId +
     * " has been initiated. The amount will be credited to your account within 3 to 15 days. Thanks for using the GCC Community Hall Booking System."
     * ;
     * String settlementrefundurlString =
     * "https://tmegov.onex-aura.com/api/sms?key=pfTEYN6H&to=" + mobile +
     * "&from=GCCCRP&body=" + settlementrefundsms +
     * "&entityid=1401572690000011081&templateid=1407172346804951524";
     * String settlementrefundwhatsupmsg =
     * "https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to="
     * + mobile +
     * "&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Your+refund+of+Rs.+"
     * + refundAmt + "+for+Booking+ID+" + bookingId +
     * "+has+been+initiated.+%0AThe+amount+will+be+credited+to+your+account+within+3+to+15+days.";
     * whatsappMsgServiceImpl.sendMsg(settlementrefundwhatsupmsg);
     * whatsappMsgServiceImpl.sendSMS(settlementrefundurlString);
     * // });
     * // thread.start();
     * return "Success";
     * }
     * } catch (Exception e) {
     * applicationLogService.saveLog(bookingDetails.getBookingId(),
     * ApplicationLogs.logsOfMsg.ERROR.toString(), "Refund Error");
     * return "error";
     * }
     * }
     * return "error";
     * }
     */

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String bookingFailure(BookingDetails bookingDetails, int flag) {
        // System.out.println("Booking Failure:-"+bookingDetails.getBookingId());
        bookingDetails.setBookingStatus(BookingStatus.REFUNDED);
        if (flag == 1) {
            bookingDetails.setComments("Booking Not Confirmed");
        } else {
            bookingDetails.setComments("Slot occupied by other user,Amount paid will be refunded.");
        }
        bookingDetailsRepository.save(bookingDetails);
        if (!(bookingDetails.getRefundStatus() == RefundStatus.COMPLETED)
                && !(bookingDetails.getRefundStatus() == RefundStatus.CLOSED)) {
            // BookingDetails bd =updateBookingStatusById(bookingDetails.getBookingId(),
            // BookingDetails.BookingStatus.REFUNDED, "slot occupied Refund will happen
            // shortly");
            applicationLogService.saveLog(bookingDetails.getBookingId(), ApplicationLogs.logsOfMsg.REFUNDED.toString(),
                    "Refund will happen shortly");
            try {
                String refundedMsg = paymentGatewayService.refundTheAmount(bookingDetails.getBookingId(),
                        bookingDetails.getUsers().getUserId());
                // System.out.println("refundedMsg=="+refundedMsg);
                if (refundedMsg.equals("Refunded")) {
                    // System.out.println("Refund completed........");
                    bookingDetails.setRefundStatus(BookingDetails.RefundStatus.COMPLETED);
                    bookingDetailsRepository.save(bookingDetails);
                    // System.out.println("refundStatus===="+bookingDetails.getRefundStatus());
                    long mobile = bookingDetails.getUsers().getMobileNo();
                    int bookingId = bookingDetails.getBookingId();
                    double refundAmt = bookingDetails.getAmountReceived();
                    // Thread thread = new Thread(() -> {
                    String settlementrefundsms = "Your refund amount of Rs. " + refundAmt + " for Booking ID "
                            + bookingId
                            + " has been initiated. The amount will be credited to your account within 3 to 15 days. Thanks for using the GCC Community Hall Booking System.";
                    String settlementrefundurlString = "https://tmegov.onex-aura.com/api/sms?key=pfTEYN6H&to=" + mobile
                            + "&from=GCCCRP&body=" + settlementrefundsms
                            + "&entityid=1401572690000011081&templateid=1407172346804951524";
                    String settlementrefundwhatsupmsg = "https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to="
                            + mobile + "&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Your+refund+of+Rs.+"
                            + refundAmt + "+for+Booking+ID+" + bookingId
                            + "+has+been+initiated.+%0AThe+amount+will+be+credited+to+your+account+within+3+to+15+days.";
                    // whatsappMsgServiceImpl.sendMsg(settlementrefundwhatsupmsg);
                    // whatsappMsgServiceImpl.sendSMS(settlementrefundurlString);
                    // });
                    // thread.start();
                    return "Success";
                }
            } catch (Exception e) {
                applicationLogService.saveLog(bookingDetails.getBookingId(), ApplicationLogs.logsOfMsg.ERROR.toString(),
                        "Refund Error");
                return "error";
            }
        }
        return "error";
    }

    @Override
    @Transactional
    public Map<String, Object> saveBookingDetails(Map<String, Object> formData) {
        Map<String, Object> hs = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        BookingDetails bookingDetails = null;
        List<BookingDocuments> uploadedDocs = new ArrayList<>();

        Integer userId = userSession.getUserId();
        String counterId = userSession.getCounter();

        System.out.println("counterId.." + counterId);

        ReasonMaster reasonMaster = reasonMasterService
                .getReasonById(Integer.parseInt(formData.get("reasonId").toString()));
        HallDetails hallDetails = hallDetailService.getHallById(Integer.parseInt(formData.get("hallId").toString()));
        Users user = userService.getUserById(userId);
        String bookingId = formData.get("bookingId").toString();

        if (bookingId != null && bookingId.equals("0")) {
            bookingDetails = new BookingDetails();
        } else {
            BookingDetails book = getBookingsById(Integer.parseInt(formData.get("bookingId").toString()));
            if (book != null) {
                bookingDetails = book;
                bookingDocumentsRepository.deleteByBookingId(book.getBookingId());
            } else {
                bookingDetails = new BookingDetails();
            }
        }

        if (user != null) {
            bookingDetails.setUsers(user);
            if (counterId != null) {
                bookingDetails.setCounterId(counterId);
            }
            if (hallDetails != null) {
                bookingDetails.setHallDetails(hallDetails);

                String startDate = formData.get("startDate").toString();

                LocalDate startD = LocalDate.parse(startDate, formatter);
                LocalDate today = LocalDate.now();
                LocalDate sixMonthsLater = today.plusMonths(6);

                if (startD.isBefore(today)) {
                    return Collections.singletonMap("status", "pastdate");
                } else if (startD.isAfter(sixMonthsLater)) {
                    return Collections.singletonMap("status", "futuredate");
                }
                String endDate = formData.get("endDate").toString();
                String relation = formData.get("relation").toString();
                double days = Double.parseDouble(formData.get("days").toString());

                int daysrange = Integer.parseInt(formData.get("daysrange").toString());
                Optional<DiscountRange> discountOpt = discountRangeService.getDiscountByUserType(user.getUsertype());
                DiscountRange discountrange = discountOpt.get();
                float discount = discountrange.getDiscountRange();
                int startingSlot = Integer.parseInt(formData.get("startingSlot").toString());
                int endingSlot = Integer.parseInt(formData.get("endingSlot").toString());
                double hallAmount = calcHallAmount(hallDetails, daysrange, startingSlot, endingSlot);
                hs.put("actualhallamount", hallAmount);
                double discountAmount = 0;
                String usertype = user.getUsertype().toString();
                String reason = reasonMaster.getReason();

                String startingSlot_txt = formData.get("startingSlot").toString();
                String endingSlot_txt = formData.get("endingSlot").toString();

                // double depositeamount = hallDetails.getDepositAmount() * daysrange;
                double depositeamount = hallDetails.getDepositAmount();
                System.out.println("depositeamount...." + depositeamount);
                double halfDepositAmount = hallDetails.getHalfDepositAmount();
                System.out.println("halfDepositAmount...." + halfDepositAmount);
                System.err.println("days.." + days);
                System.err.println("daysrange.." + daysrange);
                double Totaldepositeamount = calculateDepositAmount(daysrange, startingSlot_txt, endingSlot_txt,
                        depositeamount, halfDepositAmount);
                System.out.println("Totaldepositeamount.." + Totaldepositeamount);
                depositeamount = Totaldepositeamount;
                // Calculate discount based on user type
                if (usertype.equals("COUNCILLOR") || usertype.equals("PENSIONER") || usertype.equals("GCC_EMPLOYEE")
                        || usertype.equals("PUBLIC")) {
                    discountAmount = (hallAmount * discount) / 100;
                    hallAmount = hallAmount - discountAmount;
                    double gstAmount = Math.round(hallAmount * hallDetails.getGst()) / 100.0;
                    double totalAmount = hallAmount + gstAmount + depositeamount;

                    // Truncate the total amount to two decimal places
                    double truncatedTotalAmount = Math.round(totalAmount * 100.0) / 100.0;

                    // Set the calculated amounts to bookingDetails
                    bookingDetails.setDepositAmount(depositeamount);
                    bookingDetails.setHallAmount(hallAmount);
                    bookingDetails.setGst(gstAmount);
                    bookingDetails.setAmountReceived(truncatedTotalAmount);

                    bookingDetails.setAmountReceived(truncatedTotalAmount);

                    hs.put("totalAmount", truncatedTotalAmount);
                }
                // Calculate final hall amount after applying the discount

                hs.put("discount", discountrange.getDiscountRange());
                // Handle discount or hall amount adjustments based on the event reason
                String reasoncode = reasonMaster.getReasoncode();
                if (reasoncode == null) {
                    reasoncode = ""; // or any default value
                }
                hs.put("reasoncode", reasoncode);
                HallDetailsConcession governmentevents = hallDetailsConcessionRepo
                        .findbyhallId(hallDetails.getHallId());
                if (reasoncode.equals("CGE")) {
                    discountAmount = (governmentevents.getFulldayhallamount() * hallDetails.getSGEdiscount()) / 100;
                    // hallAmount = governmentevents.getFulldayhallamount();
                    hallAmount = hallAmount - discountAmount;
                    hs.put("discount", hallDetails.getSGEdiscount()); // No discount for State Govt Event
                    double cleaningCharges = governmentevents.getFullDayCleaningcharges();
                    double PASystem = governmentevents.getFulldayPAsystem();
                    double EB = governmentevents.getFulldayEBcharges();
                    double additionalcharges = cleaningCharges + PASystem + EB;
                    double gstAmount = Math.round((additionalcharges + hallAmount) * hallDetails.getGst()) / 100.0;
                    double depositAmount = hallDetails.getDepositAmount() * daysrange;
                    double totalAmount = hallAmount + additionalcharges + gstAmount + depositAmount;
                    double truncatedTotalAmount = Math.round(totalAmount * 100.0) / 100.0;
                    hs.put("actualhallamount", hallAmount);
                    bookingDetails.setDepositAmount(depositeamount);
                    bookingDetails.setHallAmount(hallAmount);
                    bookingDetails.setGst(gstAmount);
                    bookingDetails.setAmountReceived(truncatedTotalAmount);
                    hs.put("additionalcharges", additionalcharges);
                    hs.put("totalAmount", truncatedTotalAmount);
                } else if (reasoncode.equals("SGE")) {
                    discountAmount = (hallDetails.getFullDayRent() * hallDetails.getCGEdiscount()) / 100;
                    // hallAmount = hallDetails.getFullDayRent();
                    hallAmount = governmentevents.getFulldayhallamount();
                    hs.put("discount", hallDetails.getCGEdiscount()); // No discount for State Govt Event
                    double cleaningCharges = governmentevents.getFullDayCleaningcharges();
                    double PASystem = governmentevents.getFulldayPAsystem();
                    double EB = governmentevents.getFulldayEBcharges();
                    double additionalcharges = cleaningCharges + PASystem + EB;
                    double gstAmount = Math.round((additionalcharges + hallAmount) * hallDetails.getGst()) / 100.0;
                    double rentAmount = discountAmount;
                    double netAmount = rentAmount + additionalcharges;
                    depositeamount = governmentevents.getFullDayDeposit() * daysrange;
                    // double totalAmount = netAmount + gstAmount + depositAmount;
                    double totalAmount = hallAmount + additionalcharges + gstAmount + depositeamount;
                    double truncatedTotalAmount = Math.round(totalAmount * 100.0) / 100.0;
                    hs.put("actualhallamount", hallAmount);
                    bookingDetails.setDepositAmount(depositeamount);
                    // bookingDetails.setHallAmount(additionalcharges);
                    bookingDetails.setHallAmount(hallAmount);
                    bookingDetails.setGst(gstAmount);
                    bookingDetails.setAmountReceived(truncatedTotalAmount);

                    hs.put("rentAmount", rentAmount);
                    hs.put("gstAmount", gstAmount);
                    hs.put("netAmount", netAmount);

                    hs.put("additionalcharges", additionalcharges);
                    hs.put("totalAmount", truncatedTotalAmount);
                }

                bookingDetails.setNoOfDays(1);
                bookingDetails.setStartingSlot(startingSlot);
                bookingDetails.setEndingSlot(endingSlot);
                bookingDetails.setStartDate(startDate);
                bookingDetails.setEndDate(endDate);
                bookingDetails.setBookingDate(LocalDate.now());
                bookingDetails.setReason(reasonMaster.getReason());
                bookingDetails.setRefundStatus(BookingDetails.RefundStatus.WAITING);
                bookingDetails.setComments("Booking Slot not Confirmed.");
                bookingDetails.setBookingtype(BookingDetails.BookingType.ONLINE);
                bookingDetails.setRelationship(relation);

                int i = 0;
                String startDateStr = bookingDetails.getStartDate();
                String endDateStr = bookingDetails.getEndDate();
                LocalDate start = LocalDate.parse(startDateStr, formatter);
                LocalDate end = LocalDate.parse(endDateStr, formatter);
                int hallId = bookingDetails.getHallDetails().getHallId();

                for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                    BookingCalendar bookingCalendar = bookingCalendarRepository.findByBdateAndHallId(date, hallId);

                    if (bookingCalendar != null) {
                        if ((date.equals(start) && bookingDetails.getStartingSlot() == 1
                                && bookingCalendar.isMorningSlot()) ||
                                (date.equals(start) && bookingDetails.getStartingSlot() == 2
                                        && bookingCalendar.isEveningSlot())
                                ||
                                (date.equals(end) && bookingDetails.getEndingSlot() == 1
                                        && bookingCalendar.isMorningSlot())
                                ||
                                (date.equals(end) && bookingDetails.getEndingSlot() == 2
                                        && bookingCalendar.isEveningSlot())) {
                            return Collections.singletonMap("status", "slotOccupied");
                        } else if (bookingCalendar.isMorningSlot() && bookingCalendar.isEveningSlot()) {
                            return Collections.singletonMap("status", "slotOccupied");
                        } else if (!date.equals(end) && i != 0 && i != Math.round(bookingDetails.getNoOfDays() - 1)
                                && (bookingCalendar.isMorningSlot() || bookingCalendar.isEveningSlot())) {
                            return Collections.singletonMap("status", "slotOccupied");
                        } else if (date.equals(end) && bookingDetails.getEndingSlot() == 2
                                && bookingCalendar.isMorningSlot()
                                && i != 0 && i == Math.round(bookingDetails.getNoOfDays() - 1)) {
                            return Collections.singletonMap("status", "slotOccupied");
                        }
                    }
                    i++;
                }

                // Save the booking details after the loop
                bookingDetails = bookingDetailsRepository.save(bookingDetails);

                // confirmation of amount
                hs.put("bookingId", bookingDetails.getBookingId());
                hs.put("hallName", bookingDetails.getHallDetails().getHallName());
                hs.put("NoOfDay", bookingDetails.getNoOfDays());
                hs.put("startDate", bookingDetails.getStartDate());
                hs.put("endDate", bookingDetails.getEndDate());
                hs.put("startingSlot", bookingDetails.getStartingSlot());
                hs.put("endingSlot", bookingDetails.getEndingSlot());
                hs.put("hallAmount", bookingDetails.getHallAmount());
                hs.put("gst", bookingDetails.getGst());
                hs.put("depositAmount", depositeamount);
                hs.put("username", bookingDetails.getUsers().getName());
                hs.put("userId", bookingDetails.getUsers().getUserId());
                hs.put("mobileNo", bookingDetails.getUsers().getMobileNo());
                hs.put("userType", bookingDetails.getUsers().getUsertype());
                hs.put("reason", bookingDetails.getReason());
            }

            if (reasonMaster != null) {
                List<ReasonDocumentMaster> reasonDocMaster = reasonMaster.getReasonDocumentMasterList();

                for (ReasonDocumentMaster rm : reasonDocMaster) {
                    BookingDocuments bd = new BookingDocuments();
                    bd.setBookingDetails(bookingDetails);
                    boolean hasContent = false;

                    if (rm.isAddTextField()) {
                        Object textFieldValue = formData.get(rm.getFieldName());
                        if (textFieldValue != null && !textFieldValue.toString().isEmpty()) {
                            bd.setName(textFieldValue.toString());
                            bd.setBooking_label(rm.getFieldName());
                            hasContent = true;
                        }
                    }

                    if (rm.isAddDocumentField()) {
                        MultipartFile file = (MultipartFile) formData.get(rm.getDocFieldName());
                        if (file != null && !file.isEmpty()) {
                            bd.setBookingDocUrl(serverPath + "images/" + reasonMaster.getReason() + "/"
                                    + bookingDetails.getBookingId() + "_" + file.getOriginalFilename());
                            String filePath = uploadDirectory + "/" + reasonMaster.getReason() + "/"
                                    + bookingDetails.getBookingId() + "_" + file.getOriginalFilename();
                            bd.setDocumentName(file.getOriginalFilename());
                            bd.setDocumentType(file.getContentType());

                            // Ensure directory exists
                            File dir = new File(uploadDirectory + "/" + reasonMaster.getReason());
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }

                            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                                byte[] bytes = file.getBytes();
                                fos.write(bytes);
                                hasContent = true;
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    if (hasContent) {
                        bookingDocumentsRepository.save(bd);
                        uploadedDocs.add(bd);
                    }
                }
                hs.put("uploadedDocs", uploadedDocs);
            }
        }

        if (bookingDetails != null) {
            String refId = getBookingRefIdByBookingId(bookingDetails.getBookingId());
            hs.put("bookingRefId", refId);
        }

        hs.put("uploadedDocs", uploadedDocs);
        return hs;
    }

    private double calcHallAmount(HallDetails hallDetails, int daysrange, int startPeriod, int endPeriod) {
        double hallAmount = 0;
        if (daysrange == 1) {
            if (startPeriod == 1 && endPeriod == 2) {
                hallAmount = hallDetails.getFullDayRent();
            } else if (startPeriod == 1 && endPeriod == 1) {
                hallAmount = hallDetails.getHalfDayRent();
            } else if (startPeriod == 2 && endPeriod == 2) {
                hallAmount = hallDetails.getHalfDayRent();
            } else if (startPeriod == 2 && endPeriod == 1) {
                hallAmount = hallDetails.getFullDayRent();
            }
        } else {
            // For multiple days
            if (startPeriod == 1 && endPeriod == 2) {
                hallAmount = hallDetails.getFullDayRent() * daysrange;
            } else if (startPeriod == 1 && endPeriod == 1) {
                hallAmount = hallDetails.getFullDayRent() * (daysrange - 1) + hallDetails.getHalfDayRent();
            } else if (startPeriod == 2 && endPeriod == 2) {
                hallAmount = hallDetails.getFullDayRent() * (daysrange - 1) + hallDetails.getHalfDayRent();
            } else if (startPeriod == 2 && endPeriod == 1) {
                hallAmount = hallDetails.getFullDayRent() * (daysrange - 2) + hallDetails.getHalfDayRent() * 2;
            }
        }

        return hallAmount;
    }

    public double calculateDepositAmount(double numDays, String startPeriod, String endPeriod, double depositAmount,
            double HalfDepositAmount) {
        double damount = 0;
        // FOR FULL DAY
        // If there is only one day
        System.err.println("in calculate numDays..." + numDays);
        System.err.println("in calculate depositAmount..." + depositAmount);
        System.err.println("in calculate HalfDepositAmount..." + HalfDepositAmount);
        if (numDays == 1) {
            if ("1".equals(startPeriod) && "2".equals(endPeriod)) {
                damount = depositAmount;
            } else if ("1".equals(startPeriod) && "1".equals(endPeriod)) {
                damount = HalfDepositAmount;
            } else if ("2".equals(startPeriod) && "2".equals(endPeriod)) {
                damount = HalfDepositAmount;
            } else {
                damount = numDays;
            }
        } else {
            // For multiple days
            if ("1".equals(startPeriod) && "2".equals(endPeriod)) {
                damount = depositAmount * numDays;
            } else if ("1".equals(startPeriod) && "1".equals(endPeriod)) {
                damount = depositAmount * (numDays - 1) + HalfDepositAmount;
            } else if ("2".equals(startPeriod) && "2".equals(endPeriod)) {
                damount = depositAmount * (numDays - 1) + HalfDepositAmount;
            } else if ("2".equals(startPeriod) && "1".equals(endPeriod)) {
                damount = depositAmount * (numDays - 2) + depositAmount;
            } else {
                damount = numDays;
            }
        }
        System.err.println("in calculate damount..." + damount);
        return damount;
    }

    public String uploadNOCFile(MultipartFile file, int bookingId) {
        // Retrieve booking details, throw an exception if not found
        BookingDetails bookingDetails = bookingDetailsRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID: " + bookingId));
        String reason = "NOC";
        String fileName = bookingId + "_" + file.getOriginalFilename();
        String fileUrl = serverPath + "images/" + reason + "/" + fileName;
        String filePath = uploadDirectory + File.separator + reason + File.separator + fileName;

        // Ensure the directory exists
        File directory = new File(uploadDirectory + File.separator + reason);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }
        BookingHistory bookinghistory = new BookingHistory();
        bookinghistory.setBookingStatus(bookingDetails.getBookingStatus());
        bookinghistory.setBookingDetails(bookingDetails);
        bookinghistory.setStartDate(bookingDetails.getStartDate());
        bookinghistory.setEndDate(bookingDetails.getEndDate());
        bookinghistory.setComments(bookingDetails.getComments());
        bookinghistory.setCdate(new Date());
        bookinghistory.setRefundStatus(bookingDetails.getRefundStatus());
        bookingHistoryRepository.save(bookinghistory);

        BookingDocuments bd = new BookingDocuments();
        bd.setBookingDocUrl(fileUrl);
        bd.setDocumentName(file.getOriginalFilename());
        bd.setDocumentType(file.getContentType());
        bd.setBookingDetails(bookingDetails); // Set the associated booking details
        bookingDetails.setBookingStatus(BookingStatus.WAITING_FOR_NOC_APPROVAL);
        bookingDetails.setComments("Waiting for Admin Approval");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);
        bookingDetails.setUpdatedDate(formattedDate);

        // Save the file to the server
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            byte[] bytes = file.getBytes();
            fos.write(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Error saving file", e);
        }

        // Save the document details in the repository
        bookingDetailsRepository.save(bookingDetails);
        bookingDocumentsRepository.save(bd);

        return "File uploaded and saved successfully";
    }

    @Transactional
    public List<Integer> saveGCCBlockingDate(GccBlockingDate gcc) {
        // Save the GccBlockingDate first
        GccBlockingDate savedGcc = gccBlockingDateRepository.save(gcc);

        // Extract hall IDs and initialize list for booking IDs
        String[] hallIds = savedGcc.getHallId().split(",");
        List<Integer> bookingIds = new ArrayList<>();

        for (String hallId : hallIds) {
            HallDetails hallDetails = hallDetailsRepository.findById(Integer.parseInt(hallId))
                    .orElseThrow(() -> new RuntimeException("HallDetails not found for id: " + hallId));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // Get GCC User
            Users user = userRepository.findByUsername("gccuser");
            if (user == null) {
                throw new RuntimeException("User not found for username: gccuser");
            }

            // Create and save BookingDetails
            BookingDetails bookingDetails = new BookingDetails();
            bookingDetails.setBookingStatus(BookingDetails.BookingStatus.GCC_BLOCKED);
            bookingDetails.setReason(gcc.getBlockingReason());
            bookingDetails.setNoOfDays(0);
            bookingDetails.setAmountReceived(0);
            bookingDetails.setDepositAmount(0);
            bookingDetails.setRefundStatus(BookingDetails.RefundStatus.NA);
            bookingDetails.setBookingDate(LocalDate.now());
            bookingDetails.setStartDate(
                    LocalDate.ofInstant(savedGcc.getStartDate().toInstant(), ZoneId.systemDefault()).format(formatter));
            bookingDetails.setEndDate(
                    LocalDate.ofInstant(savedGcc.getEndDate().toInstant(), ZoneId.systemDefault()).format(formatter));
            bookingDetails.setHallAmount(0);
            bookingDetails.setHallDetails(hallDetails);
            bookingDetails.setUsers(user);

            bookingDetails = bookingDetailsRepository.save(bookingDetails);
            bookingIds.add(bookingDetails.getBookingId());

            System.out.println("BookingDetails saved with ID: " + bookingDetails.getBookingId());
        }

        // Save the list of booking IDs in the GccBlockingDate entity
        String bookingIdsStr = bookingIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        savedGcc.setBookingIds(bookingIdsStr);
        gccBlockingDateRepository.save(savedGcc);

        // Return the list of booking IDs
        return bookingIds;
    }

    public List<String> getCancelling(String fromDate, String toDate, String hallID) {
        List<String> data = bookingCalendarRepository.getCancelling(fromDate, toDate, hallID);
        return data;
    }

    public BookingCalendar getbyDates(LocalDate aa, String hallID) {
        BookingCalendar data = bookingCalendarRepository.getbyDates(aa, hallID);
        return data;
    }

    public void updateBookingStatus(BookingCalendar book) {
        bookingCalendarRepository.save(book);

    }

    public List<BookingCalendar> getCancellingdata1(String bookID) {
        List<BookingCalendar> data = bookingCalendarRepository.getCancellingdata1(bookID);
        return data;
    }

    // caretaker

    @Override
    public List<BookingDetails> getPresentsEventsByUserId(Integer hallId) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.findPresentsEventsByUserId(hallId);
    }

    public List<BookingDetails> getSettlementList(Integer hallId) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getSettlementList(hallId);
    }

    @Override
    public List<BookingDetails> getPastsEventsByUserId(Integer hallId) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.findPastEventsByHallId(hallId);
    }

    @Override
    public List<BookingDetails> getFuturesEventsByUserId(Integer hallId) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.findFutureEventsByHallId(hallId);
    }

    @Override
    public List<BookingDetails> getFilteredBookings(LocalDate fromDate, LocalDate toDate, List<Long> hallIds) {
        return bookingDetailsRepository.findByFromDateBetweenAndToDateBetweenAndHallIdIn(fromDate, toDate, hallIds);
    }

    @Override
    public List<GccBlockingDate> getBlockedEvents(LocalDate fromDate, LocalDate toDate, List<Long> hallIds) {
        return blockingDateRepository.findByFromDateAndToDateBetweenAndHallIdIn(fromDate, toDate, hallIds);
    }

    @Override
    public BookingDetails updateBookingStatustoGCCBlocked(BookingDetails bookingDetails) {
        BookingDetails save = bookingDetailsRepository.save(bookingDetails);
        return save;
    }
    // carearetakerend

    @Override
    public BookingDetails updateBookingStatusById(int bookingId, BookingDetails.BookingStatus status, String comments) {
        Optional<BookingDetails> bookingDetails = bookingDetailsRepository.findById(bookingId);
        if (bookingDetails.isPresent()) {
            BookingDetails bookDet = bookingDetails.get();
            // Update Booking Status
            bookDet.setBookingStatus(status);
            bookDet.setComments(comments);
            BookingDetails updatedBookingDetails = bookingDetailsRepository.save(bookDet);
            return updatedBookingDetails;
        }
        return null;
    }

    @Override
    public BookingDetails updateCancelledBookingStatus(int bookingId, BookingDetails.BookingStatus status,
            String comments) {
        Optional<BookingDetails> bookingDetails = bookingDetailsRepository.findById(bookingId);
        if (bookingDetails.isPresent()) {
            BookingDetails bookDet = bookingDetails.get();
            // System.out.println("Current booking status: " + bookDet.getBookingStatus());

            // Create Booking History Entry
            BookingHistory bookHistory = new BookingHistory();
            bookHistory.setBookingDetails(bookDet);
            bookHistory.setBookingStatus(bookDet.getBookingStatus());
            bookHistory.setStartDate(bookDet.getStartDate());
            bookHistory.setEndDate(bookDet.getEndDate());
            bookHistory.setComments(bookDet.getComments());
            bookHistory.setCdate(new Date());
            bookHistory.setRefundStatus(bookDet.getRefundStatus());
            bookingHistoryRepository.save(bookHistory);
            // Update Booking Status
            bookDet.setBookingStatus(status);
            bookDet.setComments(comments);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = LocalDateTime.now().format(formatter);
            bookDet.setUpdatedDate(formattedDate);
            BookingDetails updatedBookingDetails = bookingDetailsRepository.save(bookDet);
            List<BookingCalendar> bookingCalendarList = bookingCalendarRepository
                    .getCancellingdata1(String.valueOf(bookDet.getBookingId()));

            for (BookingCalendar calendar : bookingCalendarList) {

                if (String.valueOf(bookDet.getBookingId()).equals(calendar.getBookingId_1())) {
                    calendar.setBookingId_1(null);
                    calendar.setMorningSlot(false);
                }
                if (String.valueOf(bookDet.getBookingId()).equals(calendar.getBookingId_2())) {
                    calendar.setBookingId_2(null);
                    calendar.setEveningSlot(false);
                }
                bookingCalendarRepository.save(calendar);

            }
            return updatedBookingDetails;
        }
        // System.out.println("Booking details not found for ID: " + bookingId);
        return null;
    }

    @Override
    public EbDetails updateEbDetails(int bookingId, double startUnit, double endUnit, char status) {
        Optional<BookingDetails> bookingDetails = bookingDetailsRepository.findById(bookingId);
        if (bookingDetails.isPresent()) {
            BookingDetails bd = bookingDetails.get();
            EbDetails ed = bd.getEbDetails();
            if (ed == null) {
                ed = new EbDetails();
                ed.setBookingDetails(bd);
            }
            if (status == 'S') {
                ed.setCdate(new Date());
                ed.setStartingReading(startUnit);
                return ebDetailsRepository.save(ed);
            } else if (status == 'E') {
                double startReading = ed.getStartingReading();
                if (endUnit != 0) {
                    ed.setEndingReading(endUnit);
                    double usedUnit = Math.floor((endUnit - startReading) * 100) / 100.0;
                    ed.setUsedUnit(Float.parseFloat(usedUnit + ""));
                    float costPerUnit = bd.getHallDetails().getEbCostPerUnit();
                    double ebAmount = Math.floor((usedUnit * costPerUnit) * 100) / 100.0;
                    ed.setEbAmount(ebAmount);
                    ed.setUpdatedDate(new Date());
                    return ebDetailsRepository.save(ed);
                }
            }
        }
        return null;
    }

    @Override
    public String cancelBooking(int bookingId, BookingDetails.BookingStatus currentStatus, String comments) {
        Optional<BookingDetails> bd = bookingDetailsRepository.findById(bookingId);
        if (bd.isPresent()) {
            BookingDetails bookingDetails = bd.get();
            // System.out.println("currentStatus:-"+currentStatus);
            if (bookingDetails.getBookingStatus().equals(BookingDetails.BookingStatus.APPROVED)) {
                // System.err.println("test"+currentStatus);
                if (currentStatus.equals(BookingDetails.BookingStatus.CANCELED)) {
                    // System.err.println("test11"+currentStatus);
                    try {
                        // System.err.println("test13331==="+currentStatus);
                        applicationLogService.saveLog(bookingDetails.getBookingId(),
                                ApplicationLogs.logsOfMsg.USER_CANCELLED_BOOKING.toString(), comments);
                        String refundTheDepositAmount = paymentGatewayService.refundTheDepositAmount(
                                bookingDetails.getBookingId(), bookingDetails.getUsers().getUserId(),
                                bookingDetails.getDepositAmount());
                        // System.out.println("refundTheDepositAmount==="+refundTheDepositAmount);
                        if (refundTheDepositAmount.equals("Refunded")) {
                            updateBookingStatusById(bookingDetails.getBookingId(),
                                    BookingDetails.BookingStatus.CANCELED, "user cancelled Date:-" + new Date());

                            List<BookingCalendar> bookingCalendarList = bookingCalendarRepository
                                    .getCancellingdata1(String.valueOf(bookingDetails.getBookingId()));

                            for (BookingCalendar calendar : bookingCalendarList) {
                                boolean isUpdated = false;

                                if (String.valueOf(bookingDetails.getBookingId()).equals(calendar.getBookingId_1())) {
                                    calendar.setBookingId_1(null);
                                    isUpdated = true;
                                }
                                if (String.valueOf(bookingDetails.getBookingId()).equals(calendar.getBookingId_2())) {
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
                            return "success";
                        }
                        return "error";

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentStatus.equals(BookingDetails.BookingStatus.REJECTED)) {
                    try {
                        applicationLogService.saveLog(bookingDetails.getBookingId(),
                                ApplicationLogs.logsOfMsg.GCC_REJECTED.toString(), comments);
                        String refundtheAmount = paymentGatewayService.refundTheAmount(bookingDetails.getBookingId(),
                                bookingDetails.getUsers().getUserId());
                        // System.out.println("refundtheAmount==="+refundtheAmount);
                        if (refundtheAmount.equals("Refunded")) {
                            updateBookingStatusById(bookingDetails.getBookingId(),
                                    BookingDetails.BookingStatus.REJECTED, comments);

                            List<BookingCalendar> bookingCalendarList = bookingCalendarRepository
                                    .getCancellingdata1(String.valueOf(bookingDetails.getBookingId()));

                            for (BookingCalendar calendar : bookingCalendarList) {
                                boolean isUpdated = false;

                                if (String.valueOf(bookingDetails.getBookingId()).equals(calendar.getBookingId_1())) {
                                    calendar.setBookingId_1(null);
                                    isUpdated = true;
                                }
                                if (String.valueOf(bookingDetails.getBookingId()).equals(calendar.getBookingId_2())) {
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
                            return "success";
                        }
                        return "error";

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentStatus.equals(BookingDetails.BookingStatus.GCC_BLOCKED)) {
                    try {
                        applicationLogService.saveLog(bookingDetails.getBookingId(),
                                ApplicationLogs.logsOfMsg.GCC_BLOCKED_DATE.toString(), comments);
                        paymentGatewayService.refundTheAmount(bookingDetails.getBookingId(),
                                bookingDetails.getUsers().getUserId());
                        updateBookingStatusById(bookingDetails.getBookingId(), BookingDetails.BookingStatus.GCC_BLOCKED,
                                "total amount will refund shortly");
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }

        }
        return null;
    }

    /*
     * @Override
     * public BookingDetails getBookingDetailsWithSettlement(int settlementId) {
     * Optional<BookingDetails> SettleBookingDetails =
     * Optional.of(bookingDetailsRepository.findBySettlementIdWithBookingDetails(
     * settlementId));
     * if (SettleBookingDetails.isPresent()) {
     * System.out.println("settlement list: " + SettleBookingDetails.get());
     * return SettleBookingDetails.get();
     * 
     * }
     * return null;
     * }
     */
    // ak
    /*
     * @Override
     * public BookingDetails getBookingDetailsWithSettlement(int settlementId) {
     * // 🔹 Your existing logic (unchanged)
     * Optional<BookingDetails> SettleBookingDetails =
     * Optional.of(bookingDetailsRepository.findBySettlementIdWithBookingDetails(
     * settlementId));
     * 
     * if (SettleBookingDetails.isPresent()) {
     * BookingDetails bookingDetails = SettleBookingDetails.get();
     * System.out.println("settlement list: " + bookingDetails);
     * 
     * // 🔹 Keep the same return behavior, but do reset before returning
     * if (bookingDetails.getSettlement() == null) {
     * Settlement settlement = bookingDetails.getSettlement();
     * 
     * // ✅ Auto-reset logic (only if approvedFlag=1 and approvedFlagDate != null)
     * if (settlement.getApprovedFlag() == 1 && settlement.getApprovedFlagDate() !=
     * null) {
     * long millisDiff = System.currentTimeMillis() -
     * settlement.getApprovedFlagDate().getTime();
     * long daysDiff = millisDiff / (1000 * 60 * 60 * 24);
     * 
     * System.out.println("Approved Flag Date: " +
     * settlement.getApprovedFlagDate());
     * System.out.println("Current Java Date: " + new Date());
     * System.out.println("Days Difference: " + daysDiff);
     * 
     * if (daysDiff >= 4) {
     * settlement.setApprovedFlag(0);
     * settlement.setApprovedFlagDate(null);
     * settlementRepository.save(settlement);
     * System.out.println("✅ Approved flag reset after 4 days for Settlement ID: " +
     * settlement.getSettlementId());
     * }
     * }
     * }
     * 
     * // ✅ Return same object (your original logic)
     * return bookingDetails;
     * }
     * 
     * return null;
     * }
     */
    @Override
    public List<Object[]> getSettlementInitiatedReport(String fromDate, String toDate) {
        return bookingDetailsRepository.getSettlementInitiatedReport(fromDate, toDate);
    }

    // ak
    @Override
    public BookingDetails getBookingDetailsWithSettlement(int settlementId) {
        // 🔹 Your existing logic (unchanged)
        Optional<BookingDetails> SettleBookingDetails = Optional
                .ofNullable(bookingDetailsRepository.findBySettlementIdWithBookingDetails(settlementId));
        // ak
        /*
         * Optional<Settlement> settlementOptional =
         * settlementRepository.findById(settlementId);
         * String adminname = (String) session.getAttribute("usertype");
         * if (settlementOptional.isPresent()) {
         * Settlement settlement = settlementOptional.get();
         * 
         * SettlementHistory history = new SettlementHistory();
         * history.setSettlementId(settlement.getSettlementId());
         * history.setComments(settlement.getComments());
         * history.setCdate(settlement.getCdate());
         * history.setDamageAmount(settlement.getDamageAmount());
         * history.setEbAmount(settlement.getEbAmount());
         * history.setDepositAmount(settlement.getDepositAmount());
         * history.setRefundedAmount(settlement.getRefundedAmount());
         * history.setUpdatedDate(new Date());
         * history.setUpdatedId(adminname);
         * history.setChallon(settlement.getChallon());
         * history.setDddate(settlement.getDddate());
         * history.setDdnumber(settlement.getDdnumber());
         * history.setBookingDetails(settlement.getBookingDetails());
         * history.setCollectionPending(settlement.getCollectionPending());
         * history.setDdamount(settlement.getDdamount());
         * history.setChallonnumber(settlement.getChallonnumber());
         * history.setCollectionStatus(settlement.getCollectionStatus());
         * history.setChallonamount(settlement.getChallonamount());
         * history.setApprovedFlag(settlement.getApprovedFlag());
         * history.setApprovedFlagDate(settlement.getApprovedFlagDate());
         * history.setShDate(new Date());
         * 
         * settlementHistoryRepository.save(history);
         * System.out.
         * println("✅ Record inserted into SettlementHistory for Settlement ID: " +
         * settlementId);
         * }
         */

        if (SettleBookingDetails.isPresent()) {
            BookingDetails bookingDetails = SettleBookingDetails.get();
            System.out.println("settlement list: " + bookingDetails);

            // 🔹 Only do reset if settlement exists
            if (bookingDetails.getSettlement() != null) { // ✅ FIXED HERE
                Settlement settlement = bookingDetails.getSettlement();

                // ✅ Auto-reset logic (only if approvedFlag=1 and approvedFlagDate != null)
                if (settlement.getApprovedFlag() == 1 && settlement.getApprovedFlagDate() != null) {
                    long millisDiff = System.currentTimeMillis() - settlement.getApprovedFlagDate().getTime();
                    long daysDiff = millisDiff / (1000 * 60 * 60 * 24);

                    System.out.println("Approved Flag Date123: " + settlement.getApprovedFlagDate());
                    System.out.println("Current Java Date: " + new Date());
                    System.out.println("Days Difference: " + daysDiff);

                    if (daysDiff > 4) {
                        // settlement histoty insert
                        Optional<Settlement> settlementOptional = settlementRepository.findById(settlementId);
                        String adminname = (String) session.getAttribute("usertype");
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
                            System.out.println(
                                    "✅ Record inserted into SettlementHistory for Settlement ID: " + settlementId);
                        }

                        settlement.setApprovedFlag(0);
                        settlement.setApprovedFlagDate(null);
                        settlementRepository.save(settlement);
                        System.out.println("✅ Approved flag reset after 3 days for Settlement ID: "
                                + settlement.getSettlementId());
                    }
                }
            } else {
                System.out.println("⚠️ No settlement found for booking ID: " + bookingDetails.getBookingId());
            }

            // ✅ Return same object (your original logic)
            return bookingDetails;
        }

        return null;
    }

    @Override
    public BookingDetails saveebdetails(int bookingId, double startingReading,
            MultipartFile startingReadingimage) {
        BookingDetails bd = getBookingsById(bookingId);
        if (bd != null) {
            if (bd.getEbDetails() == null) {
                EbDetails eb = new EbDetails();
                eb.setCdate(new Date());
                eb.setStartingReading(startingReading);
                if (startingReadingimage != null) {
                    eb.setEbImageUrl(
                            (serverPath + "images/eb/" + bookingId + "_" + startingReadingimage.getOriginalFilename()));
                    String filePath = uploadDirectory + "/eb/" + bookingId + "_"
                            + startingReadingimage.getOriginalFilename();
                    try (FileOutputStream fos = new FileOutputStream(filePath)) {
                        byte[] bytes = startingReadingimage.getBytes();
                        fos.write(bytes);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                eb.setBookingDetails(bd);
                bd.setEbDetails(eb);
                return bd;
            }
        }
        return null;
    }

    @Override
    public String processEbDetailsAndDamage(int bookingId, int ebId, double startingReading, double endingReading,
            float usedUnit, double ebAmount, String[] assetNames, String[] comments, int[] quantities,
            Double[] assetAmounts, MultipartFile[] assetImages) {
        Optional<BookingDetails> findById = bookingDetailsRepository.findById(bookingId);
        if (findById.isPresent()) {
            BookingDetails book = findById.get();
            book.setRefundStatus(RefundStatus.WAITING);
            EbDetails updateEbDetails = updateEbDetails(bookingId, startingReading, endingReading, 'E');
            double damageAmount = 0;
            String adminname = (String) session.getAttribute("usertype");
            if (assetNames != null && assetNames.length > 0) {
                for (int i = 0; i < assetNames.length; i++) {
                    Damage damage = new Damage();
                    damage.setAssetName(assetNames[i]);
                    damage.setComments(comments[i]);
                    damage.setQuantity(quantities[i]);
                    damage.setAssetAmount(assetAmounts[i]);
                    damage.setUpdatedBy(adminname);
                    damage.setHallDetails(book.getHallDetails());
                    damageAmount = damageAmount + assetAmounts[i];
                    if (assetImages != null && !assetImages[i].isEmpty()) {
                        damage.setDamageImageUrl(
                                serverPath + "images/damage/" + bookingId + "_" + assetImages[i].getOriginalFilename());
                        String filePath = uploadDirectory + "/damage/" + bookingId + "_"
                                + assetImages[i].getOriginalFilename();
                        try (FileOutputStream fos = new FileOutputStream(filePath)) {
                            byte[] bytes = assetImages[i].getBytes();
                            fos.write(bytes);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    damage.setBookingDetails(book);
                    damageRepository.save(damage);

                }

            }
            Settlement settlement;
            if (book.getSettlement() != null) {
                settlement = book.getSettlement(); // Update existing settlement
            } else {
                settlement = new Settlement(); // Create new settlement
            }
            settlement.setCdate(LocalDate.now());
            settlement.setBookingDetails(book);
            settlement.setDepositAmount(book.getDepositAmount());
            settlement.setEbAmount(updateEbDetails.getEbAmount());
            settlement.setDamageAmount(damageAmount);
            settlement.setUpdatedId(adminname);
            double refundamount = book.getDepositAmount() - (updateEbDetails.getEbAmount() + damageAmount);
            if (refundamount < 0) {
                settlement.setRefundedAmount(0);
                settlement.setCollectionPending(Math.abs(refundamount));
                settlement.setCollectionStatus("waiting");
            } else {
                settlement.setRefundedAmount(refundamount);
                settlement.setCollectionPending(0);
            }
            settlementRepository.save(settlement);
            return "Success";
        }

        return "Error";
    }

    // sreeja
    @Override
    public List<BookingCalendar> getPastData(int hall_id) {
        List<BookingCalendar> data = bookingCalendarRepository.getPastData(hall_id);
        return data;
    }

    @Override
    public int checkUserBookinglimitation(int userId) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.checkUserBookinglimitation(userId);
    }

    @Override
    public List<BookingDetails> getAllBookingsByUserId(Integer userId) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getAllBookingsByUserId(userId);
    }

    public BookingDetails getByBookingId(String bookID) {

        return bookingDetailsRepository.getByBookingId(bookID);
    }

    public void updateBookingDetailsStatus(BookingDetails bookingDetails) {
        bookingDetailsRepository.save(bookingDetails);

    }

    @Override
    public List<Object[]> getBookingUserDetailsReport(String fromDate, String toDate) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getBookingUserDetailsReport(fromDate, toDate);
    }

    @Override
    public List<Object[]> getBookingHallDetailsReport(String fromDate, String toDate) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getBookingHallDetailsReport(fromDate, toDate);
    }

    public List<Map<String, Object>> getAllBookingEventsByUserId(Integer userId) {
        return bookingDetailsRepository.getAllBookingEventsByUserId(userId);
    }

    // counter
    public List<Map<String, Object>> getOfflineBookings() {
        return bookingDetailsRepository.findAllByBookingType();
    }

    @Override
    public List<BookingCalendar> getBookedslots(LocalDate fromDate, LocalDate endDate, List<Long> hallIdList) {
        return bookingCalendarRepository.findByHallIdAndDateRange(fromDate, endDate, hallIdList);
    }

    // public List<String> processRefundRequests(List<String> bookingIds) {
    // List<String> bookIds = new ArrayList<>();
    //
    // for (String bookingIdStr : bookingIds) {
    // try {
    // int bookingId = Integer.parseInt(bookingIdStr);
    // Optional<BookingDetails> bookings =
    // bookingDetailsRepository.findById(bookingId);
    // if (bookings.isPresent()) {
    // BookingDetails bookingDetails = bookings.get();
    // boolean refundSuccess = SettlementAmountRefund(bookingDetails);
    // if (!refundSuccess) {
    // bookIds.add(bookingIdStr); // Add ID to error list if refund fails
    // }
    // } else {
    // bookIds.add(bookingIdStr); // Add ID to error list if booking not found
    // }
    // } catch (NumberFormatException e) {
    // bookIds.add(bookingIdStr); // Add ID to error list if invalid format
    // } catch (Exception e) {
    // e.printStackTrace();
    // bookIds.add(bookingIdStr); // Add ID to error list for any other exception
    // }
    // }
    //
    // return bookIds;
    // }
    // private final ConcurrentHashMap<Integer, Boolean> processingMap = new
    // ConcurrentHashMap<>();
    //
    // @Synchronized
    public String processRefundRequest(int bookingId) {
        try {
            Optional<BookingDetails> bookingOptional = bookingDetailsRepository.findById(bookingId);
            if (bookingOptional.isPresent()) {
                BookingDetails bookingDetails = bookingOptional.get();
                boolean refundSuccess = SettlementAmountRefund(bookingDetails);
                if (refundSuccess) {
                    return "Refund successful for Booking ID: " + bookingId;
                } else {
                    return "Refund failed for Booking ID: " + bookingId;
                }
            } else {
                return "Booking not found for Booking ID: " + bookingId;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred while processing Booking ID: " + bookingId + ": " + e.getMessage();
        }
    }

    public boolean SettlementAmountRefund(BookingDetails bookingDetails) throws Exception {
        BookingStatus bookingStatus = bookingDetails.getBookingStatus();
        RefundStatus refunStatus = bookingDetails.getRefundStatus();
        String adminname = (String) session.getAttribute("usertype");
        if (bookingStatus == BookingStatus.APPROVED && !(refunStatus == RefundStatus.COMPLETED)
                && !(refunStatus == RefundStatus.CLOSED)) {
            applicationLogService.saveLog(bookingDetails.getBookingId(),
                    ApplicationLogs.logsOfMsg.SETTLEMENT_COMPLETED.toString(), "Refund will happen shortly");
            String refundThesettlementAmount = paymentGatewayService.refundTheDepositAmount(
                    bookingDetails.getBookingId(), bookingDetails.getUsers().getUserId(),
                    bookingDetails.getSettlement().getRefundedAmount());
            // System.out.println("refundThesettlementAmount === " +
            // refundThesettlementAmount);
            if (refundThesettlementAmount.equals("Refunded")) {
                Optional<BookingDetails> book = bookingDetailsRepository.findById(bookingDetails.getBookingId());
                BookingDetails bookings = book.get();
                // bookinghistoty update
                BookingHistory bookingHistory = new BookingHistory();
                bookingHistory.setStartDate(bookingDetails.getStartDate());
                bookingHistory.setEndDate(bookingDetails.getEndDate());
                bookingHistory.setComments(bookingDetails.getComments());
                bookingHistory.setBookingDetails(bookingDetails);
                bookingHistory.setBookingStatus(bookingDetails.getBookingStatus());
                bookingHistory.setUpdatedBy(adminname);
                bookingHistory.setCdate(new Date());
                bookingHistory.setRefundStatus(bookingDetails.getRefundStatus());
                bookingDetails.setComments("Completed the Refund");
                bookingHistoryRepository.save(bookingHistory);
                bookings.setBookingStatus(bookingStatus.COMPLETED);
                bookings.setRefundStatus(RefundStatus.COMPLETED);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDate = LocalDateTime.now().format(formatter);
                bookings.setUpdatedDate(formattedDate);
                bookings.setApprovedBy(adminname);
                bookingDetailsRepository.save(bookings);
                String settlementrefundsms = "Your refund amount of Rs. " + bookings.getSettlement().getRefundedAmount()
                        + " for Booking ID " + bookings.getBookingId()
                        + " has been initiated. The amount will be credited to your account within 3 to 15 days. Thanks for using the GCC Community Hall Booking System.";
                String settlementrefundurlString = "https://tmegov.onex-aura.com/api/sms?key=pfTEYN6H&to="
                        + bookings.getUsers().getMobileNo() + "&from=GCCCRP&body=" + settlementrefundsms
                        + "&entityid=1401572690000011081&templateid=1407172346804951524";
                String settlementrefundwhatsupmsg = "https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to="
                        + bookingDetails.getUsers().getMobileNo()
                        + "&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Your+refund+of+Rs.+"
                        + bookingDetails.getSettlement().getRefundedAmount() + "+for+Booking+ID+"
                        + bookingDetails.getBookingId()
                        + "+has+been+initiated.+%0AThe+amount+will+be+credited+to+your+account+within+3+to+15+days.";
                whatsappMsgServiceImpl.sendMsg(settlementrefundwhatsupmsg);
                whatsappMsgServiceImpl.sendSMS(settlementrefundurlString);
                return true;
            } else {
                return false;
            }
        } else if (bookingStatus == BookingStatus.REJECTED || bookingStatus == BookingStatus.GCC_BLOCKED
                && !(refunStatus == RefundStatus.COMPLETED) && !(refunStatus == RefundStatus.CLOSED)) {
            applicationLogService.saveLog(bookingDetails.getBookingId(),
                    ApplicationLogs.logsOfMsg.GCC_BLOCKED_DATE.toString(), "Refund will happen shortly");
            String refundFullAmount = paymentGatewayService.refundTheAmount(bookingDetails.getBookingId(),
                    bookingDetails.getUsers().getUserId());
            // System.out.println("refundTheDepositAmount === " + refundFullAmount);
            if (refundFullAmount.equals("Refunded")) {
                Optional<BookingDetails> book = bookingDetailsRepository.findById(bookingDetails.getBookingId());
                BookingDetails rejectedbookings = book.get();
                rejectedbookings.setRefundStatus(RefundStatus.COMPLETED);
                bookingDetailsRepository.save(rejectedbookings);
                String rejectedsms = "Your refund amount of Rs. " + rejectedbookings.getAmountReceived()
                        + " for Booking ID " + rejectedbookings.getBookingId()
                        + " has been initiated. The amount will be credited to your account within 3 to 15 days. Thanks for using the GCC Community Hall Booking System.";
                String rejectedrefundurlString = "https://tmegov.onex-aura.com/api/sms?key=pfTEYN6H&to="
                        + rejectedbookings.getUsers().getMobileNo() + "&from=GCCCRP&body=" + rejectedsms
                        + "&entityid=1401572690000011081&templateid=1407172346804951524";
                String settlementrefundwhatsupmsg = "https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to="
                        + bookingDetails.getUsers().getMobileNo()
                        + "&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Your+refund+of+Rs.+"
                        + bookingDetails.getAmountReceived() + "+for+Booking+ID+" + bookingDetails.getBookingId()
                        + "+has+been+initiated.+%0AThe+amount+will+be+credited+to+your+account+within+3+to+15+days.";
                whatsappMsgServiceImpl.sendMsg(settlementrefundwhatsupmsg);
                whatsappMsgServiceImpl.sendSMS(rejectedrefundurlString);
                return true;
            } else {
                return false;
            }
        } else if (bookingStatus == BookingStatus.CANCELED && !(refunStatus == RefundStatus.COMPLETED)
                && !(refunStatus == RefundStatus.CLOSED)) {
            // System.out.println("Booking status is Cancelled. Booking ID: " +
            // bookingDetails.getBookingId());
            applicationLogService.saveLog(bookingDetails.getBookingId(),
                    ApplicationLogs.logsOfMsg.USER_CANCELLED_BOOKING.toString(), "Refund will happen shortly");
            String refundTheDepositAmount = paymentGatewayService.refundTheDepositAmount(bookingDetails.getBookingId(),
                    bookingDetails.getUsers().getUserId(), bookingDetails.getDepositAmount());
            // System.out.println("refundTheDepositAmount === " + refundTheDepositAmount);
            if (refundTheDepositAmount.equals("Refunded")) {
                Optional<BookingDetails> book = bookingDetailsRepository.findById(bookingDetails.getBookingId());
                BookingDetails cancelledbookings = book.get();
                cancelledbookings.setRefundStatus(RefundStatus.COMPLETED);
                bookingDetailsRepository.save(cancelledbookings);
                String cancelledsms = "Your refund amount of Rs. " + cancelledbookings.getDepositAmount()
                        + " for Booking ID " + cancelledbookings.getBookingId()
                        + " has been initiated. The amount will be credited to your account within 3 to 15 days. Thanks for using the GCC Community Hall Booking System.";
                String cancelledrefundurlString = "https://tmegov.onex-aura.com/api/sms?key=pfTEYN6H&to="
                        + cancelledbookings.getUsers().getMobileNo() + "&from=GCCCRP&body=" + cancelledsms
                        + "&entityid=1401572690000011081&templateid=1407172346804951524";
                String settlementrefundwhatsupmsg = "https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to="
                        + bookingDetails.getUsers().getMobileNo()
                        + "&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Your+refund+of+Rs.+"
                        + bookingDetails.getDepositAmount() + "+for+Booking+ID+" + bookingDetails.getBookingId()
                        + "+has+been+initiated.+%0AThe+amount+will+be+credited+to+your+account+within+3+to+15+days.";
                whatsappMsgServiceImpl.sendMsg(settlementrefundwhatsupmsg);
                whatsappMsgServiceImpl.sendSMS(cancelledrefundurlString);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public BookingDetails updateRefundStatus(int bookingId, RefundStatus status) {
        Optional<BookingDetails> booking = bookingDetailsRepository.findById(bookingId);
        if (booking.isPresent()) {
            String adminname = (String) session.getAttribute("usertype");
            BookingDetails book = booking.get();
            book.setRefundStatus(status);
            BookingHistory bookingHistory = new BookingHistory();
            bookingHistory.setStartDate(book.getStartDate());
            bookingHistory.setEndDate(book.getEndDate());
            bookingHistory.setComments(book.getComments());
            bookingHistory.setBookingDetails(book);
            bookingHistory.setBookingStatus(book.getBookingStatus());
            bookingHistory.setUpdatedBy(adminname);
            bookingHistory.setCdate(new Date());
            bookingHistory.setRefundStatus(book.getRefundStatus());
            bookingHistoryRepository.save(bookingHistory);
            book.setComments("Refund status Updated to:-" + status);

            BookingDetails bookings = bookingDetailsRepository.save(book);
            return bookings;
        }
        return null;
    }

    // officer reports
    @Override
    public List<BookingDetails> getAllBookingDetailsByZone(String fromDate, String toDate, String zone) {
        return bookingDetailsRepository.getAllBookingDetailsByZone(fromDate, toDate, zone);
    }

    @Override
    public List<BookingDetails> getAllBookingDetailsByZoneWithStatus(String fromDate, String toDate, String zone,
            String bookingStatus) {
        return bookingDetailsRepository.getAllBookingDetailsByZoneWithStatus(fromDate, toDate, zone, bookingStatus);
    }

    @Override
    public List<Object[]> getAllPaymentsDetailsByZone(String fromDate, String toDate, String zone) {
        return bookingDetailsRepository.getAllPaymentsDetailsByZone(fromDate, toDate, zone);
    }

    @Override
    public List<Object[]> getBookingHallDetailsReportByZone(String fromDate, String toDate, String zone) {
        return bookingDetailsRepository.getBookingHallDetailsReportByZone(fromDate, toDate, zone);
    }

    // R
    @Transactional
    public Optional<String> updateBookingDatesAndCalendar(int bookingId, String startDat, String endDat) {
        Optional<BookingDetails> optionalBookingDetails = bookingDetailsRepository.findById(bookingId);
        // TransactionStatus status = transactionManager.getTransaction(new
        // DefaultTransactionDefinition());
        if (optionalBookingDetails.isPresent()) {
            BookingDetails bookingDetails = optionalBookingDetails.get();
            String starting = bookingDetails.getStartDate();
            int hallId = bookingDetails.getHallDetails().getHallId();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(startDat, formatter);
            LocalDate endDate = LocalDate.parse(endDat, formatter);

            // Retrieve existing calendar entries where bookingId matches booking_id1 or
            // booking_id2
            List<BookingCalendar> existingCalendars = bookingCalendarRepository
                    .getCancellingdata1(String.valueOf(bookingId));

            // Empty the existing date in calendar
            for (BookingCalendar calendar : existingCalendars) {
                if (calendar.getBookingId_1() != null && calendar.getBookingId_1().equals(String.valueOf(bookingId))) {
                    calendar.setMorningSlot(false);
                    calendar.setBookingId_1(null);
                }
                if (calendar.getBookingId_2() != null && calendar.getBookingId_2().equals(String.valueOf(bookingId))) {
                    calendar.setEveningSlot(false);
                    calendar.setBookingId_2(null);
                }
                bookingCalendarRepository.save(calendar);
            }

            // Check if the dates exist and update or create new entries accordingly
            String slotOccExpMsg = "Dates are already occupied. Please select another date.";
            int i = 0;
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                BookingCalendar bookingCalendar = bookingCalendarRepository.findByBdateAndHallId(date, hallId);
                if (bookingCalendar != null) {
                    // Check if the slot is already occupied
                    if ((date.equals(startDate) && bookingDetails.getStartingSlot() == 1
                            && bookingCalendar.isMorningSlot()) ||
                            (date.equals(startDate) && bookingDetails.getStartingSlot() == 2
                                    && bookingCalendar.isEveningSlot())
                            ||
                            (date.equals(endDate) && bookingDetails.getEndingSlot() == 1
                                    && bookingCalendar.isMorningSlot())
                            ||
                            (date.equals(endDate) && bookingDetails.getEndingSlot() == 2
                                    && bookingCalendar.isEveningSlot())) {

                        // return "Slot occupied";
                        throw new SlotOccupiedException(slotOccExpMsg);

                    } else if (bookingCalendar.isMorningSlot() && bookingCalendar.isEveningSlot()) {
                        // return "Slot occupied";
                        throw new SlotOccupiedException(slotOccExpMsg);

                    } else if (!date.equals(endDate) && i != 0 && i != Math.round(bookingDetails.getNoOfDays() - 1)
                            && (bookingCalendar.isMorningSlot() || bookingCalendar.isEveningSlot())) {
                        // System.out.println("booking slot occupied:- 227
                        // :-"+Math.round(bookingDetails.getNoOfDays()-1) );
                        throw new SlotOccupiedException(slotOccExpMsg);
                    } else if (date.equals(endDate) && bookingDetails.getEndingSlot() == 2
                            && bookingCalendar.isMorningSlot()
                            && i != 0 && i == Math.round(bookingDetails.getNoOfDays() - 1)) {
                        // System.out.println("booking slot occupied:- 240
                        // :-"+Math.round(bookingDetails.getNoOfDays()-1) );
                        throw new SlotOccupiedException(slotOccExpMsg);
                    }

                } else {
                    String uniqueId = date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + hallId;
                    bookingCalendar = new BookingCalendar();
                    bookingCalendar.setBdate(date);
                    bookingCalendar.setHallId(hallId);
                    bookingCalendar.setIs_booked(0);
                    bookingCalendar.setIs_active(true);
                    bookingCalendar.setUnique_id(uniqueId);
                }

                // Mark the appropriate slot as booked based on starting and ending slots
                if (date.equals(startDate)) {
                    if (bookingDetails.getStartingSlot() == 1) {
                        bookingCalendar.setMorningSlot(true);
                        bookingCalendar.setBookingId_1(String.valueOf(bookingId));
                        bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                        if (!date.equals(endDate)) {
                            bookingCalendar.setEveningSlot(true);
                            bookingCalendar.setBookingId_2(String.valueOf(bookingId));
                            bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                        } else if (startDate.equals(endDate) && bookingDetails.getStartingSlot() == 1
                                && bookingDetails.getEndingSlot() == 2) {
                            bookingCalendar.setEveningSlot(true);
                            bookingCalendar.setBookingId_2(String.valueOf(bookingId));
                            bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                        }

                    } else {
                        bookingCalendar.setEveningSlot(true);
                        bookingCalendar.setBookingId_2(String.valueOf(bookingId));
                        bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                    }
                } else if (date.equals(endDate)) {
                    if (bookingDetails.getEndingSlot() == 1) {
                        bookingCalendar.setMorningSlot(true);
                        bookingCalendar.setBookingId_1(String.valueOf(bookingId));
                        bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                    } else {
                        bookingCalendar.setEveningSlot(true);
                        bookingCalendar.setBookingId_2(String.valueOf(bookingId));
                        bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                        if (bookingDetails.getEndingSlot() == 2) {
                            bookingCalendar.setMorningSlot(true);
                            bookingCalendar.setBookingId_1(String.valueOf(bookingId));
                            bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                        }
                    }
                } else {
                    // For dates between start and end date, mark both slots as booked
                    bookingCalendar.setMorningSlot(true);
                    bookingCalendar.setEveningSlot(true);
                    bookingCalendar.setBookingId_1(String.valueOf(bookingId));
                    bookingCalendar.setBookingId_2(String.valueOf(bookingId));
                    bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 2);
                }

                bookingCalendarRepository.save(bookingCalendar);
                i++;
            }
            // transactionManager.rollback(status);
            updateResheduledBookingDetails(bookingId, startDat, endDat);

            String rescheduledmsg = "Your booking ID " + bookingId + " on " + starting
                    + " has been successfully rescheduled to " + startDate
                    + ". Thanks for using the GCC Community Hall Booking System.";
            String urlString = "https://tmegov.onex-aura.com/api/sms?key=pfTEYN6H&to="
                    + bookingDetails.getUsers().getMobileNo() + "&from=GCCCRP&body=" + rescheduledmsg
                    + "&entityid=1401572690000011081&templateid=1407172346565044999";
            String rescheduleMsg = "https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to="
                    + bookingDetails.getUsers().getMobileNo()
                    + "&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Your+booking+with+ID+" + bookingId
                    + "+on+" + starting + "+has+been+successfully+rescheduled+to+" + startDate
                    + "+.+%0AThank+you+for+using+the+GCC+Community+Hall+Booking+System.";
            whatsappMsgServiceImpl.sendMsg(rescheduleMsg);
            whatsappMsgServiceImpl.sendSMS(urlString);
            return Optional.of("Booking dates updated successfully.");
        }
        return Optional.of("Booking not found.");
    }

    private void updateResheduledBookingDetails(int bookingId, String startDate, String endDate) {
        Optional<BookingDetails> book = bookingDetailsRepository.findById(bookingId);
        BookingDetails bookingDetails = book.get();
        bookingDetails.setScheduleCount(bookingDetails.getScheduleCount() + 1);
        if (bookingDetails.getReason().equals("Commercial")) {
            bookingDetails.setBookingStatus(BookingStatus.WAITING_FOR_NOC);
        }
        // Updating Bookinghistory startDate and End Date
        BookingHistory bookingHistory = new BookingHistory();
        bookingHistory.setStartDate(bookingDetails.getStartDate());
        bookingHistory.setEndDate(bookingDetails.getEndDate());
        bookingHistory.setComments(bookingDetails.getComments());
        bookingHistory.setBookingDetails(bookingDetails);
        bookingHistory.setBookingStatus(BookingStatus.APPROVED);
        bookingHistory.setUpdatedBy(userSession.getDispayName());
        bookingHistory.setCdate(new Date());
        bookingDetails.setStartDate(startDate);
        bookingDetails.setEndDate(endDate);
        bookingHistory.setRefundStatus(bookingDetails.getRefundStatus());
        bookingDetails.setComments("slots Resheduled the Dates");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);
        bookingDetails.setUpdatedDate(formattedDate);
        bookingDetailsRepository.save(bookingDetails);
        bookingHistoryRepository.save(bookingHistory);
    }

    @Override
    public List<Object[]> getBookingHallDetailsReportByZone1(String fromDate, String toDate, String zone,
            Integer hallId) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getBookingHallDetailsReportByZone1(fromDate, toDate, zone, hallId);
    }

    public List<Object> getGccBlockingEvents(String hallId) {
        HallDetails hallDetails = hallDetailsRepository.findById(Integer.parseInt(hallId))
                .orElseThrow(() -> new RuntimeException("Hall not found"));

        // Fetch BookingDetails with GCC_BLOCKED status
        List<BookingDetails> bookingDetails = bookingDetailsRepository.findByHallDetailsAndBookingStatus(hallDetails,
                BookingDetails.BookingStatus.GCC_BLOCKED);

        // Fetch GccBlockingDate with BLOCKED status
        List<GccBlockingDate> gccBlockingDates = gccBlockingDateRepository.findAllByHallIdAndBlockedStatus(hallId,
                GccBlockingDate.BlockedStatus.BLOCKED);

        // Combine results into a list of Object (can be more specific if needed)
        List<Object> results = new ArrayList<>();
        results.addAll(bookingDetails);
        results.addAll(gccBlockingDates);

        return results;
    }

    @Override
    public int getBlockedHallCount(String blockingReason, Date startDate, Date endDate) {
        return gccBlockingDateRepository.countBlockedHallsInDateRange(blockingReason, startDate, endDate);
    }

    public boolean isBlockingRecordExists(String fromDate, String toDate, List<Long> hallIdsList) {
        LocalDate fromdate = LocalDate.parse(fromDate);
        LocalDate todate = LocalDate.parse(toDate);
        List<GccBlockingDate> existingRecords = gccBlockingDateRepository
                .findByFromDateAndToDateBetweenAndHallIdIn(fromdate, todate, hallIdsList);
        return !existingRecords.isEmpty();
    }

    @Override
    public void cancelAllBookings(String fromDate, String toDate, List<Long> hallIdsList,
            String reasonforblocking, List<String> selectedBookings, String updatedBy, String[] descriptions,
            MultipartFile[] documents) {
        if (selectedBookings != null && !selectedBookings.isEmpty()) {
            for (String bookingIdStr : selectedBookings) {
                Integer bookingId = Integer.parseInt(bookingIdStr);
                Optional<BookingDetails> optionalBooking = bookingDetailsRepository.findById(bookingId);

                if (optionalBooking.isPresent()) {
                    BookingDetails booking = optionalBooking.get();
                    BookingStatus currentStatus = booking.getBookingStatus();

                    if (currentStatus == BookingStatus.APPROVED || currentStatus == BookingStatus.WAITING_FOR_NOC ||
                            currentStatus == BookingStatus.WAITING_FOR_NOC_APPROVAL) {
                        booking.setBookingStatus(BookingStatus.GCC_BLOCKED);
                        List<BookingCalendar> calendardata = getCancellingdata1(bookingId.toString());
                        for (BookingCalendar bb : calendardata) {
                            if (bookingId != 0 && bb.getBookingId_1() != null &&
                                    bookingId.equals(Integer.valueOf(bb.getBookingId_1()))) {
                                bb.setBookingId_1(null);
                                bb.setMorningSlot(false);
                            }
                            if (bookingId != 0 && bb.getBookingId_2() != null &&
                                    bookingId.equals(Integer.valueOf(bb.getBookingId_2()))) {
                                bb.setBookingId_2(null);
                                bb.setEveningSlot(false);
                            }
                            // bb.setIs_blocked(true);
                            updateBookingStatus(bb);
                        }

                        // GCC blocking entry
                        GccBlockingDate gcc = new GccBlockingDate();
                        gcc.setStartDate(convertStringToDate(fromDate));
                        gcc.setEndDate(convertStringToDate(toDate));
                        gcc.setHallId(String.join(",",
                                hallIdsList.stream().map(String::valueOf).collect(Collectors.toList())));
                        gcc.setUpdatedBy(updatedBy);
                        gcc.setCdate(new Date());
                        gcc.setBlockingReason(reasonforblocking);
                        if (documents != null && documents.length > 0 && descriptions != null
                                && descriptions.length > 0) {
                            int i = 0;
                            for (MultipartFile document : documents) {
                                String filePath = uploadDirectory + "/" + gcc.getBlockingId() + "_" + i + "_"
                                        + document.getOriginalFilename();
                                String documentUrl = serverPath + "gccblockingdocs/" + gcc.getBlockingId() + "_" + i
                                        + "_" + document.getOriginalFilename();

                                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                                    // Save document to the file system
                                    byte[] bytes = document.getBytes();
                                    fos.write(bytes);

                                    // Save document details along with description in the database
                                    GccblockingDocuments documentDetails = new GccblockingDocuments(); // Assuming you
                                                                                                       // have a
                                                                                                       // DocumentDetails
                                                                                                       // entity
                                    documentDetails.setBlockingDate(gcc); // Assuming hallDetails is associated with the
                                                                          // document
                                    documentDetails.setBlockingDocument(documentUrl); // Save the URL of the uploaded
                                                                                      // document
                                    documentDetails.setDescription(descriptions[i]); // Save corresponding description
                                    gccblockingDocumentsRepo.save(documentDetails); // Save the document and description
                                                                                    // in the database
                                } catch (IOException e) {
                                    throw new RuntimeException("Failed to save document", e);
                                }
                                i++;
                            }
                        }

                        // GCC blocking History
                        GccBlockingHistory gccblockinghistory = new GccBlockingHistory();
                        gccblockinghistory.setStartDate(convertStringToDate(fromDate));
                        gccblockinghistory.setEndDate(convertStringToDate(toDate));
                        gccblockinghistory.setHallId(String.join(",",
                                hallIdsList.stream().map(String::valueOf).collect(Collectors.toList())));
                        gccblockinghistory.setUpdatedBy(updatedBy);
                        gccblockinghistory.setCdate(new Date());
                        gccblockinghistory.setBlockingReason(reasonforblocking);
                        List<Integer> bookingIds = saveGCCBlockingDate(gcc);
                        gccblockinghistory.setBookingIds(bookingIds.toString());
                        blockingHistoryRepository.save(gccblockinghistory);
                        // GCC calendar
                        for (Long hallId : hallIdsList) {
                            LocalDate fDate = LocalDate.parse(fromDate);
                            LocalDate tDate = LocalDate.parse(toDate);

                            List<LocalDate> dates = getDateRange(fDate, tDate);

                            for (LocalDate aa : dates) {
                                String uniqueId = aa.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + hallId;
                                BookingCalendar data = getbyDates(aa, hallId.toString());
                                int userbookingbookingId = bookingIds.get(hallIdsList.indexOf(hallId));
                                if (data != null) {
                                    System.out.println("uniqueId...1" + uniqueId);
                                    data.setBookingId_1(String.valueOf(userbookingbookingId));
                                    data.setBookingId_2(String.valueOf(userbookingbookingId));
                                    data.setMorningSlot(true);
                                    data.setEveningSlot(true);
                                    data.setIs_blocked(true);
                                    data.setUnique_id(uniqueId);
                                    updateBookingStatus(data);
                                } else {
                                    System.out.println("uniqueId...2" + uniqueId);
                                    BookingCalendar book = new BookingCalendar();
                                    book.setBdate(aa);
                                    book.setBookingId_1(String.valueOf(userbookingbookingId));
                                    book.setBookingId_2(String.valueOf(userbookingbookingId));
                                    book.setMorningSlot(true);
                                    book.setEveningSlot(true);
                                    book.setHallId(hallId.intValue());
                                    book.setIs_active(true);
                                    book.setIs_booked(2);
                                    book.setIs_delete(false);
                                    book.setIs_blocked(true);
                                    book.setUnique_id(uniqueId);
                                    updateBookingStatus(book);
                                }
                            }
                        }

                        updateBookingStatusById(booking.getBookingId(), booking.getBookingStatus().GCC_BLOCKED,
                                reasonforblocking);
                        // whatsup msg and SMS
                        String gccblockedsms = "Your booking ID " + bookingId + " on " + booking.getStartDate()
                                + " has been blocked by GCC due to " + reasonforblocking
                                + ". The entire amount paid will be refunded to your account with in 3 to 15 days.";
                        String gccblockedurlString = "https://tmegov.onex-aura.com/api/sms?key=pfTEYN6H&to="
                                + booking.getUsers().getMobileNo() + "&from=GCCCRP&body=" + gccblockedsms
                                + "&entityid=1401572690000011081&templateid=1407172346521513227";
                        // String
                        // gccblockedwhatsupmsg="https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to="+booking.getUsers().getMobileNo()+"&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Your+booking+with+ID+"+booking.getBookingId()+"+on+"+booking.getBookingDate()+"+has+been+blocked+by+GCC+due+to+"+reasonforblocking+".+%0AThanks+for+using+GCC+Community+Hall+Booking+System.";
                        String gccblockedwhatsupmsg = "https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to="
                                + booking.getUsers().getMobileNo()
                                + "&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Your+booking+with+ID+"
                                + booking.getBookingId() + "+on+" + booking.getStartDate()
                                + "+has+been+blocked+by+the+GCC+due+to+Goverment+event++.+%0AThe+entire+amount+paid+will+be+refunded+to+your+account+within+3+to+15+days.";
                        whatsappMsgServiceImpl.sendMsg(gccblockedwhatsupmsg);
                        whatsappMsgServiceImpl.sendSMS(gccblockedurlString);
                        bookingDetailsRepository.save(booking);
                    }
                }
            }
        } else {
            // Create and save a new GCC blocking date record
            GccBlockingDate gcc = new GccBlockingDate();
            gcc.setStartDate(convertStringToDate(fromDate));
            gcc.setEndDate(convertStringToDate(toDate));
            gcc.setHallId(String.join(",", hallIdsList.stream().map(String::valueOf).collect(Collectors.toList())));
            gcc.setUpdatedBy(updatedBy);
            gcc.setCdate(new Date());
            gcc.setBlockingReason(reasonforblocking);
            GccBlockingHistory gccblockinghistory = new GccBlockingHistory();
            gccblockinghistory.setStartDate(convertStringToDate(fromDate));
            gccblockinghistory.setEndDate(convertStringToDate(toDate));
            gccblockinghistory.setHallId(
                    String.join(",", hallIdsList.stream().map(String::valueOf).collect(Collectors.toList())));
            gccblockinghistory.setUpdatedBy(updatedBy);
            gccblockinghistory.setCdate(new Date());
            gccblockinghistory.setBlockingReason(reasonforblocking);
            List<Integer> bookingIds = saveGCCBlockingDate(gcc);
            String bookingIdsString = bookingIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            gccblockinghistory.setBookingIds(bookingIdsString);
            blockingHistoryRepository.save(gccblockinghistory);
            if (documents != null && documents.length > 0) {
                if (descriptions != null && descriptions.length == documents.length) {
                    int i = 0;
                    for (MultipartFile document : documents) {
                        String filePath = uploadDirectory + "/" + gcc.getBlockingId() + "_" + i + "_"
                                + document.getOriginalFilename();
                        String documentUrl = serverPath + "gccblockingdocs/" + gcc.getBlockingId() + "_" + i + "_"
                                + document.getOriginalFilename();

                        try (FileOutputStream fos = new FileOutputStream(filePath)) {
                            byte[] bytes = document.getBytes();
                            fos.write(bytes);

                            GccblockingDocuments documentDetails = new GccblockingDocuments();
                            documentDetails.setBlockingDate(gcc);
                            documentDetails.setBlockingDocument(documentUrl);
                            documentDetails.setDescription(descriptions[i]);
                            gccblockingDocumentsRepo.save(documentDetails);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to save document", e);
                        }
                        i++;
                    }
                } else {
                    // Handle case where descriptions array length does not match documents length
                }
            }
            // Iterate through hall IDs and corresponding dates to update BookingCalendar
            for (Long hallId : hallIdsList) {
                LocalDate fDate = LocalDate.parse(fromDate);
                LocalDate tDate = LocalDate.parse(toDate);
                List<LocalDate> dates = getDateRange(fDate, tDate);

                for (LocalDate date : dates) {
                    BookingCalendar data = getbyDates(date, hallId.toString());
                    String uniqueId = date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + hallId;
                    int userbookingbookingId = bookingIds.get(hallIdsList.indexOf(hallId));
                    if (data != null) {
                        System.out.println("uniqueId...11" + uniqueId);
                        data.setBookingId_1(String.valueOf(userbookingbookingId));
                        data.setBookingId_2(String.valueOf(userbookingbookingId));
                        data.setMorningSlot(true);
                        data.setEveningSlot(true);
                        data.setIs_blocked(true);
                        data.setUnique_id(uniqueId);
                        updateBookingStatus(data);
                    } else {
                        System.out.println("uniqueId...21" + uniqueId);
                        BookingCalendar book = new BookingCalendar();
                        book.setBdate(date);
                        book.setBookingId_1(String.valueOf(userbookingbookingId));
                        book.setBookingId_2(String.valueOf(userbookingbookingId));
                        book.setMorningSlot(true);
                        book.setEveningSlot(true);
                        book.setHallId(hallId.intValue());
                        book.setIs_active(true);
                        book.setIs_booked(2);
                        book.setIs_delete(false);
                        book.setIs_blocked(true);
                        book.setUnique_id(uniqueId);
                        updateBookingStatus(book);
                    }
                }
            }
        }
    }

    // Helper methods for date conversion and range calculation
    public Date convertStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<LocalDate> getDateRange(LocalDate fDate, LocalDate tDate) {
        List<LocalDate> dateRange = new ArrayList<>();
        LocalDate currentDate = fDate;
        while (!currentDate.isAfter(tDate)) {
            dateRange.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
        return dateRange;
    }

    @Override
    public List<BookingDetails> findBookingDetailsByBookingId(Long bookingId) {
        return bookingDetailsRepository.findBookingDetailsByBookingId(bookingId);
    }

    // admin dashboard
    @Override
    public Map<String, Map<String, Integer>> getBookingStatistics(LocalDate fromDate, LocalDate toDate, String zone,
            String division, Integer hallid, String userType) {
        List<Map<String, Object>> rawStatistics = bookingDetailsRepository.getBookingStatistics(fromDate, toDate, zone,
                division, hallid, userType);
        return transformData(rawStatistics);
    }

    private Map<String, Map<String, Integer>> transformData(List<Map<String, Object>> rawStatistics) {
        Map<String, Map<String, Integer>> transformedData = new HashMap<>();

        for (Map<String, Object> entry : rawStatistics) {
            if (entry.isEmpty())
                continue; // Skip empty entries

            String category = (String) entry.get("category");
            String key = entry.get("key") != null ? (String) entry.get("key") : "total";
            Integer totalCount = entry.get("totalCount") != null ? ((Number) entry.get("totalCount")).intValue() : 0;

            transformedData.putIfAbsent(category, new HashMap<>());
            transformedData.get(category).put(key, totalCount);

            if (category.equals("main")) {
                Integer mainTotalAmount = entry.get("main_total_amount") != null
                        ? ((Number) entry.get("main_total_amount")).intValue()
                        : 0;
                Integer mainRefundedAmount = entry.get("main_refunded_amount") != null
                        ? ((Number) entry.get("main_refunded_amount")).intValue()
                        : 0;
                Integer mainNetAmount = entry.get("main_net_amount") != null
                        ? ((Number) entry.get("main_net_amount")).intValue()
                        : 0;
                Integer mainGstAmount = entry.get("main_gst_amount") != null
                        ? ((Number) entry.get("main_gst_amount")).intValue()
                        : 0;
                transformedData.get(category).put("main_total_amount", mainTotalAmount);
                transformedData.get(category).put("main_net_amount", mainNetAmount);
                transformedData.get(category).put("main_refunded_amount", mainRefundedAmount);
                transformedData.get(category).put("main_gst_amount", mainGstAmount);
            }
            if (category.equals("past_future")) {
                Integer pastbooking = entry.get("PAST_Events") != null ? ((Number) entry.get("PAST_Events")).intValue()
                        : 0;
                Integer futurebooking = entry.get("FUTURE_Events") != null
                        ? ((Number) entry.get("FUTURE_Events")).intValue()
                        : 0;
                transformedData.get(category).put("PAST_Events", pastbooking);
                transformedData.get(category).put("FUTURE_Events", futurebooking);
            }
        }

        return transformedData;
    }

    public List<Map<String, Object>> getBookingDetailsByHallId(int hallId) {
        return bookingDetailsRepository.getBookingDetailsByHallId(hallId);
    }
    // officer Dashboard

    @Override
    public Map<String, Map<String, Integer>> getOfficerDashboard(LocalDate fromDate, LocalDate toDate, String zone,
            String division, Integer hallid, String userType) {
        List<Map<String, Object>> rawStatistics = bookingDetailsRepository.getOfficerDashboard(fromDate, toDate, zone,
                division, hallid, userType);
        return transformedData(rawStatistics);
    }

    private Map<String, Map<String, Integer>> transformedData(List<Map<String, Object>> rawStatistics) {
        Map<String, Map<String, Integer>> transformedData = new HashMap<>();

        for (Map<String, Object> entry : rawStatistics) {
            if (entry.isEmpty())
                continue; // Skip empty entries

            String category = (String) entry.get("category");
            String key = entry.get("key") != null ? (String) entry.get("key") : "total";
            Integer totalCount = entry.get("totalCount") != null ? ((Number) entry.get("totalCount")).intValue() : 0;

            transformedData.putIfAbsent(category, new HashMap<>());
            transformedData.get(category).put(key, totalCount);

            if (category.equals("main")) {
                Integer mainTotalAmount = entry.get("main_total_amount") != null
                        ? ((Number) entry.get("main_total_amount")).intValue()
                        : 0;
                Integer mainNetAmount = entry.get("main_net_amount") != null
                        ? ((Number) entry.get("main_net_amount")).intValue()
                        : 0;
                Integer mainRefundedAmount = entry.get("main_refunded_amount") != null
                        ? ((Number) entry.get("main_refunded_amount")).intValue()
                        : 0;
                transformedData.get(category).put("main_total_amount", mainTotalAmount);
                transformedData.get(category).put("main_net_amount", mainNetAmount);
                transformedData.get(category).put("main_refunded_amount", mainRefundedAmount);
            }
            if (category.equals("past_future")) {
                Integer pastbooking = entry.get("PAST_Events") != null ? ((Number) entry.get("PAST_Events")).intValue()
                        : 0;
                Integer futurebooking = entry.get("FUTURE_Events") != null
                        ? ((Number) entry.get("FUTURE_Events")).intValue()
                        : 0;
                transformedData.get(category).put("PAST_Events", pastbooking);
                transformedData.get(category).put("FUTURE_Events", futurebooking);
            }
        }

        return transformedData;
    }

    @Override
    public List<Map<String, Object>> getHallDetailsForOfficer(Integer hallIid) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getHallDetailsForOfficer(hallIid);
    }

    // @Override
    // public List<Object[]> getAllPaymentsDetails(String fromDate, String toDate,
    // String paymentStatus) {
    // // TODO Auto-generated method stub
    // return bookingDetailsRepository.getAllPaymentsDetails(fromDate,
    // toDate,paymentStatus);
    // }
    //
    // admin reports
    @Override
    public List<Object[]> getBookingDetailsByBookingDate(String fromDate, String toDate, String zone, String division,
            Integer hallid) {
        // TODO Auto-generated method stub

        return bookingDetailsRepository.getBookingDetailsByBookingDate(fromDate, toDate, zone, division, hallid);
    }

    @Override
    public List<Object[]> getBookingDetailsByEventDate(String fromDate, String toDate, String zone, String division,
            Integer hallid) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getBookingDetailsByEventDate(fromDate, toDate, zone, division, hallid);
    }

    @Override
    public List<Object[]> getBookingDetailsByBookingDateWithStatus(String fromDate, String toDate,
            String bookingStatus, String zone, String division, Integer hallid) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getBookingDetailsByBookingDateWithStatus(fromDate, toDate, bookingStatus, zone,
                division, hallid);
    }

    @Override
    public List<Object[]> getBookingDetailsByEventDateWithStatus(String fromDate, String toDate,
            String bookingStatus, String zone, String division, Integer hallid) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getBookingDetailsByEventDateWithStatus(fromDate, toDate, bookingStatus, zone,
                division, hallid);
    }

    @Override
    public List<Object[]> getAllPaymentsDetails(String fromDate, String toDate, String paymentStatus, String zone,
            String division, Integer hallid) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getAllPaymentsDetails(fromDate, toDate, paymentStatus, zone, division, hallid);
    }

    @Override
    public List<Object[]> getAllExtraPaymentsDetails(String fromDate, String toDate, String paymentStatus, String zone,
            String division, Integer hallid) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getAllExtraPaymentsDetails(fromDate, toDate, paymentStatus, zone, division,
                hallid);
    }

    @Override
    public List<Object[]> getBookingHallDetailsReportByBookingDate(String fromDate, String toDate, String zone,
            String division, Integer hallid) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getBookingHallDetailsReportByBookingDate(fromDate, toDate, zone, division,
                hallid);
    }

    @Override
    public List<Object[]> getBookingHallDetailsReportByEventDate(String fromDate, String toDate, String zone,
            String division, Integer hallid) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getBookingHallDetailsReportByEventDate(fromDate, toDate, zone, division,
                hallid);
    }

    @Override
    public List<BookingDetails> getAllBookingsettlementDetailsByBookingDate(String fromDate, String toDate) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getAllBookingsettlementDetailsByBookingDate(fromDate, toDate);
    }

    @Override
    public List<BookingDetails> getAllBookingsettlementDetailsByBookingDateWithStatus(String fromDate,
            String toDate, String refundStatus) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getAllBookingsettlementDetailsByBookingDateWithStatus(fromDate, toDate,
                refundStatus);
    }

    @Override
    public List<BookingDetails> getAllBookingsettlementDetailsByEventDate(String fromDate, String toDate) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getAllBookingsettlementDetailsByEventDate(fromDate, toDate);
    }

    @Override
    public List<BookingDetails> getAllBookingsettlementDetailsByEventDateWithStatus(String fromDate,
            String toDate, String refundStatus) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getAllBookingsettlementDetailsByEventDateWithStatus(fromDate, toDate,
                refundStatus);
    }

    @Override
    public List<BookingDetails> getAllBookingDetailsBookingDateByZone(String fromDate, String toDate,
            String zone) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getAllBookingDetailsBookingDateByZone(fromDate, toDate, zone);
    }

    @Override
    public List<BookingDetails> getAllBookingDetailsEventDateByZone(String fromDate, String toDate,
            String zone) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getAllBookingDetailsEventDateByZone(fromDate, toDate, zone);
    }

    @Override
    public List<BookingDetails> getAllBookingDetailsBookingDateByZoneWithStatus(String fromDate, String toDate,
            String zone, String bookingStatus) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getAllBookingDetailsBookingDateByZoneWithStatus(fromDate, toDate, zone,
                bookingStatus);
    }

    @Override
    public List<BookingDetails> getAllBookingDetailsEventDateByZoneWithStatus(String fromDate, String toDate,
            String zone, String bookingStatus) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getAllBookingDetailsEventDateByZoneWithStatus(fromDate, toDate, zone,
                bookingStatus);
    }

    @Override
    public List<BookingDetails> getAllBookingsettlementDetailsByZoneByBookingDate(String fromDate,
            String toDate, String zone) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getAllBookingsettlementDetailsByZoneByBookingDate(fromDate, toDate, zone);
    }

    @Override
    public List<BookingDetails> getAllBookingsettlementDetailsByZoneByEventDate(String fromDate, String toDate,
            String zone) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getAllBookingsettlementDetailsByZoneByEventDate(fromDate, toDate, zone);
    }

    @Override
    public List<BookingDetails> getAllBookingsettlementDetailsByZoneByBookingDateWithStatus(String fromDate,
            String toDate, String zone, String refundStatus) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getAllBookingsettlementDetailsByZoneByBookingDateWithStatus(fromDate, toDate,
                zone, refundStatus);

    }

    @Override
    public List<BookingDetails> getAllBookingsettlementDetailsByZoneByEventDateWithStatus(String fromDate,
            String toDate, String zone, String refundStatus) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getAllBookingsettlementDetailsByZoneByEventDateWithStatus(fromDate, toDate,
                zone, refundStatus);
    }

    @Override
    public List<Object[]> getBookingHallDetailsReportByZoneByBookingDate(String fromDate, String toDate,
            String zone) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getBookingHallDetailsReportByZoneByBookingDate(fromDate, toDate, zone);
    }

    @Override
    public List<Object[]> getBookingHallDetailsReportByZoneByEventDate(String fromDate, String toDate,
            String zone) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getBookingHallDetailsReportByZoneByEventDate(fromDate, toDate, zone);
    }

    @Override
    public List<Object[]> getBookingHallDetailsReportByZoneByBookingDateHallname(String fromDate, String toDate,
            String zone, Integer hallId) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getBookingHallDetailsReportByZoneByBookingDateHallname(fromDate, toDate, zone,
                hallId);
    }

    @Override
    public List<Object[]> getBookingHallDetailsReportByZoneByEventDateHallname(String fromDate, String toDate,
            String zone, Integer hallId) {
        // TODO Auto-generated method stub
        return bookingDetailsRepository.getBookingHallDetailsReportByZoneByEventDateHallname(fromDate, toDate, zone,
                hallId);
    }

    // gccapps
    @Override
    public List<BookingDetails> getApprovedBookingDetails() {
        return bookingDetailsRepository.findByBookingStatus(BookingDetails.BookingStatus.APPROVED);
    }

    @Override
    public List<BookingDetails> getRejectedBookingDetails() {
        return bookingDetailsRepository.findByBookingStatus(BookingDetails.BookingStatus.REJECTED);
    }

    @Override
    public List<BookingDetails> getWaitingforNOCapproval() {
        return bookingDetailsRepository.findByBookingStatus(BookingDetails.BookingStatus.WAITING_FOR_NOC_APPROVAL);
    }

    @Override
    public List<Object[]> getAllPaymentsDetails(String fromDate, String toDate) {
        // TODO Auto-generated method stub
        // return bookingDetailsRepository.getAllPaymentsDetails(fromDate, toDate);
        return null;
    }

    @Override
    public List<BookingDetails> getPastsEvents() {
        return bookingDetailsRepository.findPastsEvents();
    }

    @Override
    public List<BookingDetails> getPresentsEvents() {
        return bookingDetailsRepository.findPresentsEvents();
    }

    @Override
    public List<BookingDetails> getFuturesEvents() {
        return bookingDetailsRepository.findFuturesEvents();
    }

    @Override
    public List<BookingDetails> getSettlementApprovedList() {
        return bookingDetailsRepository.getSettlementApprovedList();
    }

    @Override
    public List<Map<String, Object>> getHallDetails(Integer hall_id) {
        // TODO Auto-generated method stub
        // return bookingDetailsRepository.getHallDetails(hall_id);
        return null;
    }

    public List<Object[]> getUserDetailsByBookingDate(String fromDate, String toDate) {
        return userRepository.findUserDetailsByBookingDate(fromDate, toDate);
    }

    // developer
    @Override
    public List<Object[]> getTransactionresponse(String fromDate, String toDate, Long bookingId, Long hallId) {
        return transactionResponseRepository.getTransactionResponseByBookingId(fromDate, toDate, bookingId, hallId);
    }

    @Override
    public List<Object[]> getRefundResponse(String fromDate, String toDate, Long bookingId) {
        return transRefundResRepository.getRefundResponseByBookingId(fromDate, toDate, bookingId);
    }

    @Override
    public List<Object[]> getBookingCalendarDetails(String fromDate, String toDate, Long bookingId, String hallName) {
        return bookingCalendarRepository.findBookingCalendarDetails(fromDate, toDate, bookingId, hallName);
    }

    @Override
    public List<BookingDetails> getBookingsByDateAndHall(String bookingDate, Long hallId) {
        LocalDate date = LocalDate.parse(bookingDate); // Ensure format matches yyyy-MM-dd
        return bookingDetailsRepository.findByBookingDateAndHallId(date, hallId);
    }

    @Override
    @Transactional
    public BookingDetails updateRefundStatusScheduler(int bookingId, RefundStatus status) {
        Optional<BookingDetails> booking = bookingDetailsRepository.findById(bookingId);
        if (booking.isPresent()) {
            String adminname = "Refund Scheduler";
            BookingDetails book = booking.get();
            book.setRefundStatus(status);
            book.setComments("Refund status Updated to:-" + status);
            BookingDetails bookings = bookingDetailsRepository.save(book);

            BookingHistory bookingHistory = new BookingHistory();
            bookingHistory.setStartDate(book.getStartDate());
            bookingHistory.setEndDate(book.getEndDate());
            bookingHistory.setComments(book.getComments());
            bookingHistory.setBookingDetails(book);
            bookingHistory.setBookingStatus(book.getBookingStatus());
            bookingHistory.setUpdatedBy(adminname);
            bookingHistory.setCdate(new Date());
            bookingHistory.setRefundStatus(book.getRefundStatus());
            bookingHistoryRepository.save(bookingHistory);

            return bookings;
        }
        return null;
    }

    @Override
    public List<Object[]> getCounterWiseBookingCount(String fromDate, String toDate) {
        return bookingDetailsRepository.getCounterWiseBookingCount(fromDate, toDate);
    }

    /*
     * @Override
     * public List<Object[]> getHallWiseBookingDetails(String fromDate, String
     * toDate) {
     * return bookingDetailsRepository.getHallWiseBookingDetails(fromDate, toDate);
     * }
     * 
     * @Override
     * public List<Object[]> getBookingDetailsForAllHalls(String fromDate, String
     * toDate){
     * return
     * bookingDetailsRepository.getBookingDetailsForAllHalls(fromDate,toDate);
     * }
     */

    @Override
    public List<Object[]> getHallWiseBookingDetails(String fromDate, String toDate, String counterId) {
        return bookingDetailsRepository.getHallWiseBookingDetails(fromDate, toDate, counterId);
    }

    @Override
    public List<Object[]> getBookingDetailsForAllHalls(String fromDate, String toDate, String counterId,
            Integer hallid) {
        return bookingDetailsRepository.getBookingDetailsForAllHalls(fromDate, toDate, counterId, hallid);
    }

    @Override
    public List<Object[]> getCounterWiseBookingDetails(String fromDate, String toDate) {
        return bookingDetailsRepository.getCounterWiseBookingDetails(fromDate, toDate);
    }

    @Override
    public List<Object> getZoneWiseBookingList(String fromDate, String toDate) {
        return bookingDetailsRepository.getZoneWiseBookingList(fromDate, toDate);
    }

    @Override
    public List<Object> getAllHallWiseBookingList(String fromDate, String toDate) {
        return bookingDetailsRepository.getHallWiseBookingList(fromDate, toDate);
    }

    @Override
    public List<Object> getAllCounterWiseBookingList(String fromDate, String toDate) {
        return bookingDetailsRepository.getCounterWiseBookingList(fromDate, toDate);
    }

    @Override
    public String saveHallRequestDetails(String fromDate, String toDate, int startingSlot, int endingSlot,
            String relationship, String reason, int hallId, int userId,
            String orgType, String eventDesc, String offAddress, MultipartFile bonafide) {

        String exMsg = "Slot occupied";

        HallDetails hallDetails = hallDetailService.getHallById(hallId);
        Users user = userService.getUserById(userId);

        HallRequestDetails hallRequestDetails = new HallRequestDetails();
        hallRequestDetails.setStartDate(fromDate);
        hallRequestDetails.setEndDate(toDate);
        hallRequestDetails.setStartingSlot(startingSlot);
        hallRequestDetails.setEndingSlot(endingSlot);
        hallRequestDetails.setReason(reason);
        hallRequestDetails.setHallId(hallId);
        hallRequestDetails.setUserId(userId);
        hallRequestDetails.setOrgType(orgType);
        hallRequestDetails.setEventDescription(eventDesc);
        hallRequestDetails.setOfficeDetails(offAddress);
        if (bonafide != null && !bonafide.isEmpty()) {
            hallRequestDetails.setBonafidePath(serverPath + "images/" + bonafide.getOriginalFilename());
            String filePath = uploadDirectory + "/" + bonafide.getOriginalFilename();
            // File saving logic is missing here in original method, but NCP is fixed.
        } else {
            hallRequestDetails.setBonafidePath(null);
        }
        hallRequestDetailsRepository.save(hallRequestDetails);

        BookingDetails bookingDetails = new BookingDetails();
        bookingDetails.setBookingDate(LocalDate.now());
        bookingDetails.setBookingStatus(BookingStatus.PENDING);
        bookingDetails.setBookingtype(BookingDetails.BookingType.ONLINE);
        bookingDetails.setEndDate(toDate);
        bookingDetails.setStartDate(fromDate);
        bookingDetails.setStartingSlot(startingSlot);
        bookingDetails.setEndingSlot(endingSlot);
        bookingDetails.setReason(reason);
        bookingDetails.setNoOfDays(1);
        bookingDetails.setRefundStatus(RefundStatus.WAITING);
        bookingDetails.setScheduleCount(0);
        bookingDetails.setRelationship(relationship);
        bookingDetails.setComments("Booking Slot not Confirmed.");
        bookingDetails.setUsers(user);
        bookingDetails.setHallDetails(hallDetails);
        BookingDetails SavedBookingDetails = bookingDetailsRepository.save(bookingDetails);
        int bookingId = SavedBookingDetails.getBookingId();

        // BookingCalendar bookingCalendar = new BookingCalendar();
        // bookingCalendar.setBookingDetails(bookingDetails);
        // bookingCalendar.setBookingDate(LocalDate.parse(fromDate));
        // bookingCalendar.setStartingSlot(startingSlot);
        // bookingCalendar.setEndingSlot(endingSlot);
        // bookingCalendar.setHallDetails(hallDetails);
        // bookingCalendar.setUsers(user);
        // bookingCalendarRepository.save(bookingCalendar);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(fromDate, formatter);
        LocalDate endDate = LocalDate.parse(toDate, formatter);

        int i = 0;

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            BookingCalendar bookingCalendar = bookingCalendarRepository.findByBdateAndHallId(date, hallId);

            if (bookingCalendar != null) {
                if (bookingCalendar.getIs_blocked()) {
                    throw new RuntimeException("GCC Blocked Date");
                } else if ((date.equals(startDate) && bookingDetails.getStartingSlot() == 1
                        && bookingCalendar.isMorningSlot()) ||
                        (date.equals(startDate) && bookingDetails.getStartingSlot() == 2
                                && bookingCalendar.isEveningSlot())
                        ||
                        (date.equals(endDate) && bookingDetails.getEndingSlot() == 1
                                && bookingCalendar.isMorningSlot())
                        ||
                        (date.equals(endDate) && bookingDetails.getEndingSlot() == 2
                                && bookingCalendar.isEveningSlot())) {

                    // System.out.println("booking slot occupied:- 218");
                    throw new RuntimeException(exMsg);
                } else if (bookingCalendar.isMorningSlot() && bookingCalendar.isEveningSlot()) {
                    // System.out.println("booking slot occupied:- 221");
                    throw new RuntimeException(exMsg);

                } else if (!date.equals(endDate) && i != 0 && i != Math.round(bookingDetails.getNoOfDays() - 1)
                        && (bookingCalendar.isMorningSlot() || bookingCalendar.isEveningSlot())) {
                    // System.out.println("booking slot occupied:- 227
                    // :-"+Math.round(bookingDetails.getNoOfDays()-1) );
                    throw new RuntimeException(exMsg);
                } else if (date.equals(endDate) && bookingDetails.getEndingSlot() == 2
                        && bookingCalendar.isMorningSlot()
                        && i != 0 && i == Math.round(bookingDetails.getNoOfDays() - 1)) {
                    // System.out.println("booking slot occupied:- 240
                    // :-"+Math.round(bookingDetails.getNoOfDays()-1) );
                    throw new RuntimeException(exMsg);
                }

            } else {
                String uniqueId = date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + hallId;
                System.out.println("uniqueId....." + uniqueId);

                bookingCalendar = new BookingCalendar();
                bookingCalendar.setBdate(date);
                bookingCalendar.setHallId(hallId);
                bookingCalendar.setIs_booked(0);
                bookingCalendar.setIs_active(true);
                bookingCalendar.setUnique_id(uniqueId);
            }

            // Mark the appropriate slot as booked based on starting and ending slots
            if (date.equals(startDate)) {
                // System.out.println(i + "start :-"+bookingCalendar.toString());

                if (bookingDetails.getStartingSlot() == 1) {
                    bookingCalendar.setMorningSlot(true);
                    bookingCalendar.setBookingId_1(String.valueOf(bookingId));
                    bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                    if (!date.equals(endDate)) {
                        bookingCalendar.setEveningSlot(true);
                        bookingCalendar.setBookingId_2(String.valueOf(bookingId));
                        bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                    } else if (startDate.equals(endDate) && bookingDetails.getStartingSlot() == 1
                            && bookingDetails.getEndingSlot() == 2) {
                        bookingCalendar.setEveningSlot(true);
                        bookingCalendar.setBookingId_2(String.valueOf(bookingId));
                        bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                    }

                } else {
                    bookingCalendar.setEveningSlot(true);
                    bookingCalendar.setBookingId_2(String.valueOf(bookingId));
                    bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                }
            } else if (date.equals(endDate)) {
                if (bookingDetails.getEndingSlot() == 1) {
                    bookingCalendar.setMorningSlot(true);
                    bookingCalendar.setBookingId_1(String.valueOf(bookingId));
                    bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                } else {
                    bookingCalendar.setEveningSlot(true);
                    bookingCalendar.setBookingId_2(String.valueOf(bookingId));
                    bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                    if (bookingDetails.getEndingSlot() == 2) {
                        bookingCalendar.setMorningSlot(true);
                        bookingCalendar.setBookingId_1(String.valueOf(bookingId));
                        bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 1);
                    }
                }
            } else {
                // For dates between start and end date, mark both slots as booked
                bookingCalendar.setMorningSlot(true);
                bookingCalendar.setEveningSlot(true);
                bookingCalendar.setBookingId_1(String.valueOf(bookingId));
                bookingCalendar.setBookingId_2(String.valueOf(bookingId));
                bookingCalendar.setIs_booked(bookingCalendar.getIs_booked() + 2);
            }
            // System.out.println(i + "end:-"+bookingCalendar.toString());
            bookingCalendarRepository.save(bookingCalendar);
            i++;
        }

        bookingDetailsRepository.save(bookingDetails);

        return "success";

    }

    @Override
    public List<HallRequestDetails> getPendingGovtRequests() {
        return hallRequestDetailsRepository.findPendingRequests();
    }

    @Override
    public List<HallRequestDetails> getApprovedGovtRequests() {
        return hallRequestDetailsRepository.findApprovedRequests();
    }

    @Override
    public List<HallRequestDetails> getRejectedGovtRequests() {
        return hallRequestDetailsRepository.findRejectedRequests();
    }

    @Override
    @Transactional
    public void updateGovtRequest(int reqId, String action, double discountPercentage, boolean nocRequired) {
        HallRequestDetails request = hallRequestDetailsRepository.findById(reqId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        BookingDetails booking = bookingDetailsRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if ("ACCEPT".equalsIgnoreCase(action)) {
            if (nocRequired) {
                request.setBookingStatus("WAITING_FOR_NOC");
                booking.setBookingStatus(BookingStatus.WAITING_FOR_NOC);
            } else {
                request.setBookingStatus("APPROVED");
                booking.setBookingStatus(BookingStatus.APPROVED);
            }

            // Apply discount
            if (discountPercentage > 0) {
                float discountFactor = (float) (1 - (discountPercentage / 100));
                booking.setHallAmount(booking.getHallAmount() * discountFactor);
                booking.setDepositAmount(booking.getDepositAmount() * discountFactor);
                booking.setGst(booking.getGst() * discountFactor);
            }

        } else if ("REJECT".equalsIgnoreCase(action)) {
            request.setBookingStatus("REJECTED");
            booking.setBookingStatus(BookingStatus.REJECTED);
        }

        hallRequestDetailsRepository.save(request);
        bookingDetailsRepository.save(booking);
    }

    @Override
    public List<Map<String, Object>> getvphcollectionpending(int bookingId) {

        return bookingDetailsRepository.findApprovedBookingWithHallRequest(bookingId);
    }

    public boolean isPaymentAlreadyDoneOrInitiated(int bookingId) {

        boolean existsInPayments = paymentsRepository.existsByBookingDetails_BookingId(bookingId);

        boolean existsInTransaction = transactionResponseRepository.existsByBookingId(bookingId);

        return existsInPayments || existsInTransaction;
    }

}
