package com.gcc.communityHall.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class BookingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;

    public enum BookingStatus {
        PENDING, APPROVED, REJECTED, CANCELED, PAYMENT_FAILURE, GCC_BLOCKED, REFUNDED, COMPLETED, GCC_UNBLOCKED,
        WAITING_FOR_NOC, WAITING_FOR_NOC_APPROVAL
    }

    public enum RefundStatus {
        APPROVED, WAITING, INITIATED, REJECTED, COMPLETED, NA, FAILURE, CLOSED
    }

    public enum BookingType {
        ONLINE, OFFLINE
    }

    @Column(name = "booking_type")
    @Enumerated(EnumType.STRING)
    private BookingType bookingtype;

    @Column(name = "no_of_days")
    private double noOfDays;
    @Column(name = "start_date")
    private String startDate;
    @Column(name = "end_date")
    private String endDate;
    @Column(name = "starting_slot")
    private int startingSlot;
    @Column(name = "ending_slot")
    private int endingSlot;
    @Column(name = "booking_date")
    private LocalDate bookingDate;
    @Column(name = "booking_status")
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus = BookingStatus.PENDING;
    @Column(name = "deposit_amount")
    private double depositAmount;
    @Column(name = "hall_amount")
    private double hallAmount;
    @Column(name = "amount_received")
    private double amountReceived;
    private double gst;
    @Column(name = "approved_by")
    private String approvedBy;
    @Column(name = "refund_status")
    @Enumerated(EnumType.STRING)
    private RefundStatus refundStatus;
    @Column(name = "updated_date")
    private String updatedDate;
    private String reason;
    private int scheduleCount;
    @Column(name = "relationship")
    private String relationship;
    private String comments;

    // ak
    @Column(name = "booking_ref_id")
    private String bookingRefId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "counterdetails_counter_id")
    private CounterDetails counterDetails;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Users users;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private HallDetails hallDetails;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "bookingDetails")
    private List<BookingHistory> bookingHistories;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bookingDetails")
    List<BookingDocuments> bookingDocumentsList;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "bookingDetails")
    private Settlement settlement;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bookingDetails")
    private EbDetails ebDetails;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bookingDetails")
    private List<Damage> damageList;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bookingDetails")
    private Payments payments;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bookingDetails")
    private ExtraPayments extrapayments;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bookingDetails")
    private RefundTransaction refundTransaction;

    private String counterId;

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public double getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(double days) {
        this.noOfDays = days;
    }// ak

    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getStartingSlot() {
        return startingSlot;
    }

    public void setStartingSlot(int startingSlot) {
        this.startingSlot = startingSlot;
    }

    public int getEndingSlot() {
        return endingSlot;
    }

    public void setEndingSlot(int endingSlot) {
        this.endingSlot = endingSlot;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(double depositAmount) {
        this.depositAmount = depositAmount;
    }

    public double getHallAmount() {
        return hallAmount;
    }

    public void setHallAmount(double hallAmount) {
        this.hallAmount = hallAmount;
    }

    public double getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(double amountReceived) {
        this.amountReceived = amountReceived;
    }

    public double getGst() {
        return gst;
    }

    public void setGst(double gst) {
        this.gst = gst;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public RefundStatus getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(RefundStatus refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public HallDetails getHallDetails() {
        return hallDetails;
    }

    public void setHallDetails(HallDetails hallDetails) {
        this.hallDetails = hallDetails;
    }

    public List<BookingDocuments> getBookingDocumentsList() {
        return bookingDocumentsList;
    }

    public void setBookingDocumentsList(List<BookingDocuments> bookingDocumentsList) {
        this.bookingDocumentsList = bookingDocumentsList;
    }

    public Settlement getSettlement() {
        return settlement;
    }

    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
    }

    public EbDetails getEbDetails() {
        return ebDetails;
    }

    public void setEbDetails(EbDetails ebDetails) {
        this.ebDetails = ebDetails;
    }

    public List<Damage> getDamageList() {
        return damageList;
    }

    public void setDamageList(List<Damage> damageList) {
        this.damageList = damageList;
    }

    public Payments getPayments() {
        return payments;
    }

    public void setPayments(Payments payments) {
        this.payments = payments;
    }

    public RefundTransaction getRefundTransaction() {
        return refundTransaction;
    }

    public void setRefundTransaction(RefundTransaction refundTransaction) {
        this.refundTransaction = refundTransaction;
    }

    public CounterDetails getCounterDetails() {
        return counterDetails;
    }

    public void setCounterDetails(CounterDetails counterDetails) {
        this.counterDetails = counterDetails;
    }

    public BookingType getBookingtype() {
        return bookingtype;
    }

    public void setBookingtype(BookingType bookingtype) {
        this.bookingtype = bookingtype;
    }

    public List<BookingHistory> getBookingHistories() {
        return bookingHistories;
    }

    public void setBookingHistories(List<BookingHistory> bookingHistories) {
        this.bookingHistories = bookingHistories;
    }

    public int getScheduleCount() {
        return scheduleCount;
    }

    public void setScheduleCount(int scheduleCount) {
        this.scheduleCount = scheduleCount;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ExtraPayments getExtrapayments() {
        return extrapayments;
    }

    public void setExtrapayments(ExtraPayments extrapayments) {
        this.extrapayments = extrapayments;
    }

    public String getCounterId() {
        return counterId;
    }

    public void setCounterId(String counterId) {
        this.counterId = counterId;
    }

}