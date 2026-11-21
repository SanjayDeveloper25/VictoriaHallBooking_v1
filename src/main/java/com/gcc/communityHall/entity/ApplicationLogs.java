package com.gcc.communityHall.entity;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcc.communityHall.others.UserSession;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class ApplicationLogs {

    @Autowired
    @Transient
    private UserSession userSession;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    private String logType;
    private int bookingId;
    private int loginId =0;
    private String comments;
    private String SystemName;
    private String Ipaddress;

    @Column(name = "cdate",nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date cdate = new Date();



    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getLoginId() {
        if( userSession.getUserId()!=0){
            return  userSession.getUserId();
        }
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }


    public enum logsOfMsg{
        SENDING_SMS,USER_CANCELLED_BOOKING,USER_INVALID,PAYMENT_STARTED,PAYMENT_COMPLETED,PAYMENT,FAILURE,
        GCC_REJECTED,GCC_BLOCKED_DATE,SETTLEMENT_COMPLETED,REFUNDED,ERROR
    }

    @Transient
    private ApplicationLogs.logsOfMsg logsOfMsg;

    public ApplicationLogs.logsOfMsg getLogsOfMsg() {
        return logsOfMsg;
    }

    public void setLogsOfMsg(ApplicationLogs.logsOfMsg logsOfMsg) {
        this.logsOfMsg = logsOfMsg;
    }

	public UserSession getUserSession() {
		return userSession;
	}

	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}

	public String getSystemName() {
		return SystemName;
	}

	public void setSystemName(String systemName) {
		SystemName = systemName;
	}

	public String getIpaddress() {
		return Ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		Ipaddress = ipaddress;
	}
    
}
