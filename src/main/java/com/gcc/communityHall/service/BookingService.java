package com.gcc.communityHall.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gcc.communityHall.entity.BookingCalendar;
import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.BookingDetails.BookingStatus;
import com.gcc.communityHall.entity.BookingDetails.RefundStatus;
import com.gcc.communityHall.entity.EbDetails;
import com.gcc.communityHall.entity.GccBlockingDate;
import com.gcc.communityHall.entity.HallRequestDetails;

public interface BookingService {
  BookingDetails getBookingsById(int BookingId);

  // ak
  BookingDetails getBookingByRefId(String refId);

  // ak
  void migrateOldBookingsToRefId();

  BookingDetails updateBookingDetails(BookingDetails bd);

  public String confirmTheDateAndSlot(String fromDate, String toDate, int days, int fromStartingSlot,
      int fromEndingSlot, int toStartingSlot, int toEndingSlot, int bookingId, int userId);

  public Map<String, Object> saveBookingDetails(Map<String, Object> formData);

  public BookingDetails updateBookingStatustoGCCBlocked(BookingDetails bookingDetails);

  public BookingDetails updateBookingStatus(int bookingId, BookingDetails.BookingStatus status, String comments);

  public BookingDetails updateBookingStatusById(int bookingId, BookingDetails.BookingStatus status, String comments);

  public EbDetails updateEbDetails(int bookingId, double startUnit, double endUnit, char status);

  // typeOfCancel depends { }
  String cancelBooking(int bookingId, BookingDetails.BookingStatus currentStatus, String comments);

  public String confirmBooking(int bookingId);

  public BookingDetails getBookingDetailsWithSettlement(int settlementId);

  // sreeja
  List<BookingDetails> getPresentsEventsByUserId(Integer hallId);

  List<BookingDetails> getPastsEventsByUserId(Integer hallId);

  List<BookingDetails> getFuturesEventsByUserId(Integer hallId);

  List<BookingDetails> getSettlementList(Integer hallId);

  public List<BookingDetails> getFilteredBookings(LocalDate fromDate, LocalDate toDate, List<Long> hallIds);

  public List<BookingCalendar> getPastData(int hall_id);

  public String processEbDetailsAndDamage(
      int bookingId, int ebId,
      double startingReading, double endingReading,
      float usedUnit, double ebAmount,
      String assetNames[], String comments[], int quantities[], Double assetAmounts[], MultipartFile assetImages[]);

  public int checkUserBookinglimitation(int userId);

  List<BookingDetails> getAllBookingsByUserId(Integer userId);

  List<Map<String, Object>> getAllBookingEventsByUserId(Integer userId);

  // List<BookingDetails> getAllBookingDetailsWithStatus(String fromDate, String
  // toDate, String bookingStatus);

  // List<Object[]> getAllPaymentsDetails( String fromDate, String toDate);

  List<Object[]> getBookingUserDetailsReport(String fromDate, String toDate);

  List<Object[]> getBookingHallDetailsReport(String fromDate, String toDate);

  List<GccBlockingDate> getBlockedEvents(LocalDate fromDate, LocalDate toDate, List<Long> hallIds);

  List<Map<String, Object>> getOfflineBookings();

  List<BookingCalendar> getBookedslots(LocalDate fromDate, LocalDate endDate, List<Long> hallIdList);

  BookingDetails updateCancelledBookingStatus(int bookingId, BookingStatus status, String comments);

  BookingDetails updateRefundStatus(int bookingId, RefundStatus status);

  // officer service
  List<BookingDetails> getAllBookingDetailsByZone(String fromDate, String toDate, String zone);

  List<BookingDetails> getAllBookingDetailsByZoneWithStatus(String fromDate, String toDate, String zone,
      String bookingStatus);

  List<Object[]> getAllPaymentsDetailsByZone(String fromDate, String toDate, String zone);

  List<Object[]> getBookingHallDetailsReportByZone(String fromDate, String toDate, String zone);

  Optional<String> updateBookingDatesAndCalendar(int bookingId, String startDate, String endDate);

  List<Object[]> getBookingHallDetailsReportByZone1(String fromDate, String toDate, String zone, Integer hallId);

  List<Object> getGccBlockingEvents(String hallId);

  int getBlockedHallCount(String blockingReason, Date startDate, Date endDate);

  @Transactional
  void cancelAllBookings(String fromDate, String toDate, List<Long> hallIdsList,
      String reasonforblocking, List<String> selectedBookings, String updatedBy, String[] descriptions,
      MultipartFile[] documents);

  List<BookingDetails> findBookingDetailsByBookingId(Long bookingId);

  // admin dashboard
  Map<String, Map<String, Integer>> getBookingStatistics(LocalDate fromDate, LocalDate toDate, String zone,
      String division, Integer hallid, String userType);

  public List<Map<String, Object>> getBookingDetailsByHallId(int hallId);

  // officer dashboard
  Map<String, Map<String, Integer>> getOfficerDashboard(LocalDate fromDate, LocalDate toDate, String zone,
      String division, Integer hallid, String userType);

  List<Map<String, Object>> getHallDetailsForOfficer(Integer hallIid);
  // List<Object[]> getAllPaymentsDetails(String fromDate, String toDate, String
  // paymentStatus);
  // admin reports

  List<Object[]> getBookingDetailsByBookingDate(String fromDate, String toDate, String zone, String division,
      Integer hallid);

  List<Object[]> getBookingDetailsByEventDate(String fromDate, String toDate, String zone, String division,
      Integer hallid);

  List<Object[]> getBookingDetailsByBookingDateWithStatus(String fromDate, String toDate, String bookingStatus,
      String zone, String division, Integer hallid);

  List<Object[]> getBookingDetailsByEventDateWithStatus(String fromDate, String toDate, String bookingStatus,
      String zone, String division, Integer hallid);

  List<Object[]> getAllPaymentsDetails(String fromDate, String toDate, String paymentStatus, String zone,
      String division, Integer hallid);

  List<Object[]> getBookingHallDetailsReportByBookingDate(String fromDate, String toDate, String zone, String division,
      Integer hallid);

  List<Object[]> getBookingHallDetailsReportByEventDate(String fromDate, String toDate, String zone, String division,
      Integer hallid);

  List<BookingDetails> getAllBookingsettlementDetailsByBookingDate(String fromDate, String toDate);

  List<BookingDetails> getAllBookingsettlementDetailsByBookingDateWithStatus(String fromDate, String toDate,
      String refundStatus);

  List<BookingDetails> getAllBookingsettlementDetailsByEventDate(String fromDate, String toDate);

  List<BookingDetails> getAllBookingsettlementDetailsByEventDateWithStatus(String fromDate, String toDate,
      String refundStatus);

  List<BookingDetails> getAllBookingDetailsBookingDateByZone(String fromDate, String toDate, String zone);

  List<BookingDetails> getAllBookingDetailsEventDateByZone(String fromDate, String toDate, String zone);

  List<BookingDetails> getAllBookingDetailsBookingDateByZoneWithStatus(String fromDate, String toDate, String zone,
      String bookingStatus);

  List<BookingDetails> getAllBookingDetailsEventDateByZoneWithStatus(String fromDate, String toDate, String zone,
      String bookingStatus);

  List<BookingDetails> getAllBookingsettlementDetailsByZoneByBookingDate(String fromDate, String toDate, String zone);

  List<BookingDetails> getAllBookingsettlementDetailsByZoneByEventDate(String fromDate, String toDate, String zone);

  List<BookingDetails> getAllBookingsettlementDetailsByZoneByBookingDateWithStatus(String fromDate, String toDate,
      String zone, String refundStatus);

  List<BookingDetails> getAllBookingsettlementDetailsByZoneByEventDateWithStatus(String fromDate, String toDate,
      String zone, String refundStatus);

  List<Object[]> getBookingHallDetailsReportByZoneByBookingDate(String fromDate, String toDate, String zone);

  List<Object[]> getBookingHallDetailsReportByZoneByEventDate(String fromDate, String toDate, String zone);

  List<Object[]> getBookingHallDetailsReportByZoneByBookingDateHallname(String fromDate, String toDate, String zone,
      Integer hallId);

  List<Object[]> getBookingHallDetailsReportByZoneByEventDateHallname(String fromDate, String toDate, String zone,
      Integer hallId);

  BookingDetails saveebdetails(int bookingId, double startingReading, MultipartFile startingReadingimage);

  String confirmStateBooking(int bookingId);

  String uploadNOCFile(MultipartFile file, int bookingId);

  // gccapps
  List<BookingDetails> getApprovedBookingDetails();

  List<BookingDetails> getRejectedBookingDetails();

  List<Object[]> getAllPaymentsDetails(String fromDate, String toDate);

  List<BookingDetails> getPastsEvents();

  List<BookingDetails> getPresentsEvents();

  List<BookingDetails> getFuturesEvents();

  List<BookingDetails> getSettlementApprovedList();

  List<Map<String, Object>> getHallDetails(Integer hall_id);

  List<BookingDetails> getWaitingforNOCapproval();

  List<Object[]> getAllExtraPaymentsDetails(String fromDate, String toDate, String paymentStatus, String zone,
      String division, Integer hallid);

  // developer
  List<Object[]> getTransactionresponse(String fromDate, String toDate, Long bookingId, Long hallId);

  List<Object[]> getRefundResponse(String fromDate, String toDate, Long bookingId);

  List<Object[]> getBookingCalendarDetails(String fromDate, String toDate, Long bookingId, String hallName);

  List<BookingDetails> getBookingsByDateAndHall(String bookingDate, Long hallId);

  BookingDetails updateRefundStatusScheduler(int bookingId, RefundStatus status);

  List<Object[]> getCounterWiseBookingCount(String fromDate, String toDate);

  List<Object[]> getHallWiseBookingDetails(String fromDate, String toDate, String CounterId);

  List<Object[]> getBookingDetailsForAllHalls(String fromDate, String toDate, String CounterId, Integer hallid);

  List<Object[]> getCounterWiseBookingDetails(String fromDate, String toDate);

  List<Object> getZoneWiseBookingList(String fromDate, String toDate);

  List<Object> getAllHallWiseBookingList(String fromDate, String toDate);

  List<Object> getAllCounterWiseBookingList(String fromDate, String toDate);

  // ak
  List<Object[]> getSettlementInitiatedReport(String fromDate, String toDate);

  public String saveHallRequestDetails(String fromDate, String toDate, int startingSlot,
      int endingSlot, String relationship, String reason, int hallId, int userId,
      String orgType, String eventDesc, String offAddress, MultipartFile bonafide);

  List<HallRequestDetails> getPendingGovtRequests();

  List<HallRequestDetails> getApprovedGovtRequests();

  List<HallRequestDetails> getRejectedGovtRequests();

  void updateGovtRequest(int reqId, String action, double discountPercentage, boolean nocRequired);

  List<Map<String, Object>> getvphcollectionpending(int bookingId);

  boolean isPaymentAlreadyDoneOrInitiated(int bookingId);

}
