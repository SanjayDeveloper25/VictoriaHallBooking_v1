package com.gcc.communityHall.paymentService.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "refund_info")
public class RefundClientInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String srno;
    private String refTxnId;
    private String refTxnAmt;
    private String getRefTxnDate;
    private  String Status;
    private String clntRfndId;
    private String transactionId;
    private  String arnNo;
    private int bookingId;
    private int userId;
    @Column(name = "cdate",nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date cdate = new Date();

    public String getClntRfndId() {
        return clntRfndId;
    }

    public void setClntRfndId(String clntRfndId) {
        this.clntRfndId = clntRfndId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getRefTxnId() {
        return refTxnId;
    }

    public void setRefTxnId(String refTxnId) {
        this.refTxnId = refTxnId;
    }

    public String getRefTxnAmt() {
        return refTxnAmt;
    }

    public void setRefTxnAmt(String refTxnAmt) {
        this.refTxnAmt = refTxnAmt;
    }

    public String getGetRefTxnDate() {
        return getRefTxnDate;
    }

    public void setGetRefTxnDate(String getRefTxnDate) {
        this.getRefTxnDate = getRefTxnDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }



    public String getArnNo() {
        return arnNo;
    }

    public void setArnNo(String arnNo) {
        this.arnNo = arnNo;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    @Override
    public String toString() {
        return "RefundClientInfo{" +
                "id=" + id +
                ", srno='" + srno + '\'' +
                ", refTxnId='" + refTxnId + '\'' +
                ", refTxnAmt='" + refTxnAmt + '\'' +
                ", getRefTxnDate='" + getRefTxnDate + '\'' +
                ", Status='" + Status + '\'' +
                ", clntRfndId='" + clntRfndId + '\'' +
                ", arnNo='" + arnNo + '\'' +
                ", bookingId=" + bookingId +
                ", userId=" + userId +
                ", cdate=" + cdate +
                '}';
    }
}
