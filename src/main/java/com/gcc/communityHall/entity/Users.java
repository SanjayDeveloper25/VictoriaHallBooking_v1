package com.gcc.communityHall.entity;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    public enum userStatus {
        PENDING, APPROVED, REJECTED
    }

    public enum userType {
    	PUBLIC,PENSIONER, GCC_EMPLOYEE, COUNCILLOR,FAMILYPENSIONER
    }

    @Column(name="user_status", columnDefinition = "VARCHAR(20) DEFAULT 'APPROVED'")
    @Enumerated(EnumType.STRING)
    private Users.userStatus userstatus = userStatus.APPROVED ;

    @Enumerated(EnumType.STRING)
    @Column(name="usertype", columnDefinition = "VARCHAR(20) DEFAULT 'PUBLIC'")
    private Users.userType usertype = userType.PUBLIC ;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "mobile_no", nullable = false)
    private long mobileNo;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "doorno", nullable = false)
    private String doorNo;
    @Column(name = "street", nullable = false)
    private String street;
    @Column(name = "area", nullable = false)
    private String area;
    @Column(name = "location", nullable = false)
    private String location;
    @Column(name = "pin_code", nullable = false)
    private int pinCode;
    @Column(name = "profilephoto")
    private String profilephoto;
    @Column(name="updated_date")
    private String updatedDate;
    @Column(name="updatedby")
    private String updatedBy;
	


    private String governmentId;
    @Column(name = "aadhar_number", nullable = false)
    private long aadharnumber;
    private String department;
    @Column(nullable = false,columnDefinition = "BOOLEAN DEFAULT true")
    private boolean isActive =true;

    @Column(name = "cdate",nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date cdate = new Date();
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "users")
    @JsonIgnore
    List<BookingDetails> bookingDetailsList;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "users")
    List<UserHistory> userHistories;
    
    @Transient
    private MultipartFile userPhoto;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public userStatus getUserstatus() {
        return userstatus;
    }

    public void setUserstatus(userStatus userstatus) {
        this.userstatus = userstatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(long mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPinCode() {
        return pinCode;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }

    public userType getUsertype() {
        return usertype;
    }

    public void setUsertype(userType usertype) {
        this.usertype = usertype;
    }

    public String getGovernmentId() {
        return governmentId;
    }

    public void setGovernmentId(String governmentId) {
        this.governmentId = governmentId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    public List<BookingDetails> getBookingDetailsList() {
        return bookingDetailsList;
    }

    public void setBookingDetailsList(List<BookingDetails> bookingDetailsList) {
        this.bookingDetailsList = bookingDetailsList;
    }

    public List<UserHistory> getUserHistories() {
        return userHistories;
    }

    public void setUserHistories(List<UserHistory> userHistories) {
        this.userHistories = userHistories;
    }
    
    public String getProfilephoto() {
		return profilephoto;
	}

	public void setProfilephoto(String profilephoto) {
		this.profilephoto = profilephoto;
	}

	public MultipartFile getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(MultipartFile userPhoto) {
		this.userPhoto = userPhoto;
	}
	public long getAadharnumber() {
		return aadharnumber;
	}

	public void setAadharnumber(long aadharnumber) {
		this.aadharnumber = aadharnumber;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Override
	public String toString() {
	    return "Users [userId=" + userId + 
	           ", userstatus=" + userstatus + 
	           ", usertype=" + usertype + 
	           ", name=" + name + 
	           ", mobileNo=" + mobileNo + 
	           ", email=" + email + 
	           ", doorNo=" + doorNo + 
	           ", street=" + street + 
	           ", area=" + area + 
	           ", location=" + location + 
	           ", pinCode=" + pinCode + 
	           ", profilephoto=" + profilephoto + 
	           ", governmentId=" + governmentId + 
	           ", aadharnumber=" + aadharnumber + 
	           ", department=" + department + 
	           ", isActive=" + isActive + 
	           ", cdate=" + cdate + 
	           ", bookingDetailsList=" + bookingDetailsList + 
	           ", userHistories=" + userHistories + 
	           ", userPhoto=" + userPhoto + 
	           "]";
	}

}
