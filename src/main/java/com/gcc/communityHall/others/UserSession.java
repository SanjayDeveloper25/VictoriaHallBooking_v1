package com.gcc.communityHall.others;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSession {
    private Integer userId =0 ;
    private String userType;
    private String dispayName;
    private Long mobileno;

    // added for counter login
    private String counter;
    private String counterName;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDispayName() {
        return dispayName;
    }

    public void setDispayName(String dispayName) {
        this.dispayName = dispayName;
    }

	public Long getMobileno() {
		return mobileno;
	}

	public void setMobileno(Long mobileno) {
		this.mobileno = mobileno;
	}

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getCounterName() {
        return counterName;
    }

    public void setCounterName(String counterName) {
        this.counterName = counterName;
    }

}
