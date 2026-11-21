package com.gcc.communityHall.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class HallDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hallId;
    private String hallName;
    private String doorNo;
    private String street;
    private String area;
    private int pinCode;
    private String division;
    private String zone;
    @Column(name="land_mark")
    private String landMark;
    @Column(name="latitude")
    private String latitude;
    @Column(name="longitude")
    private String longitude;
    private float hallArea;
    @Column(name="seating_capacity")
    private int seatingCapacity;
    @Column(name="parking_capacity")
    private String parkingCapacity;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "hallDetails")
    private List<HallImages> hallImagesList;
    
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "hallDetails")
    private List<FloorDetails> floorDetailsList;
    
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "hallDetails")
    private List<HallDetailsConcession> hallDetailsConcession;
    
    @Column(name="hall_Incharge_no",nullable=false)
    private long hallInchargeMobno;

    @Column(name="hall_Incharge_name",nullable=false)
    private String hallInchargeName;

    @Column(name = "deposit_amount", nullable = false)
    private float depositAmount;

    @Column(name = "half_deposit_amount", nullable = false)
    private float halfDepositAmount;

    @Column(name="full_day_rent",nullable=false)
    private float fullDayRent;
    @Column(name="half_day_rent",nullable=false)
    private float halfDayRent;
    private float gst;
    @Column(nullable = false,columnDefinition = "BOOLEAN DEFAULT true")
    private boolean isActive =true;
    @Column(nullable = false,columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isDelete =false;
    @Column(name="c_date")
    private LocalDate createddate;
    @Column(name=" updated_id")
    private String updatedId;
    @Column(name="update_date")
    private Date updatedDate = new Date();
    private String ebNumber;
    private float ebCostPerUnit;
    private String city;
    @Column(nullable = false,columnDefinition = "BOOLEAN DEFAULT false")
    private boolean SGE=false;
    @Column(nullable = false,columnDefinition = "BOOLEAN DEFAULT false")
    private boolean CGE=false;
    private float SGEdiscount;
    private float CGEdiscount;

    private int minDay;
    private int maxDay;

    public int getHallId() {
        return hallId;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
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

    public int getPinCode() {
        return pinCode;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLankMark(String landMark) {
        this.landMark = landMark;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public float getHallArea() {
        return hallArea;
    }

    public void setHallArea(float hallArea) {
        this.hallArea = hallArea;
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public String getParkingCapacity() {
        return parkingCapacity;
    }

    public void setParkingCapacity(String parkingCapacity) {
        this.parkingCapacity = parkingCapacity;
    }

    public List<HallImages> getHallImagesList() {
        return hallImagesList;
    }

    public void setHallImagesList(List<HallImages> hallImagesList) {
        this.hallImagesList = hallImagesList;
    }

    public long getHallInchargeMobno() {
        return hallInchargeMobno;
    }

    public void setHallInchargeMobno(long hallInchargeMobno) {
        this.hallInchargeMobno = hallInchargeMobno;
    }

    public String getHallInchargeName() {
        return hallInchargeName;
    }

    public void setHallInchargeName(String hallInchargeName) {
        this.hallInchargeName = hallInchargeName;
    }

    public float getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(float depositAmount) {
        this.depositAmount = depositAmount;
    }

    public float getFullDayRent() {
        return fullDayRent;
    }

    public void setFullDayRent(float fullDayRent) {
        this.fullDayRent = fullDayRent;
    }

    public float getHalfDayRent() {
        return halfDayRent;
    }

    public void setHalfDayRent(float halfDayRent) {
        this.halfDayRent = halfDayRent;
    }

    public float getGst() {
        return gst;
    }

    public void setGst(float gst) {
        this.gst = gst;
    }

    public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public LocalDate getCreateddate() {
        return createddate;
    }

    public void setCreateddate(LocalDate createddate) {
        this.createddate = createddate;
    }

    public String getUpdatedId() {
        return updatedId;
    }

    public void setUpdatedId(String updatedId) {
        this.updatedId = updatedId;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getEbNumber() {
        return ebNumber;
    }

    public void setEbNumber(String ebNumber) {
        this.ebNumber = ebNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    public float getEbCostPerUnit() {
        return ebCostPerUnit;
    }

    public void setEbCostPerUnit(float ebCostPerUnit) {
        this.ebCostPerUnit = ebCostPerUnit;
    }

	public List<FloorDetails> getFloorDetailsList() {
		return floorDetailsList;
	}

	public void setFloorDetailsList(List<FloorDetails> floorDetailsList) {
		this.floorDetailsList = floorDetailsList;
	}

	public boolean isSGE() {
		return SGE;
	}

	public void setSGE(boolean sGE) {
		SGE = sGE;
	}

	public boolean isCGE() {
		return CGE;
	}

	public void setCGE(boolean cGE) {
		CGE = cGE;
	}

	public float getSGEdiscount() {
		return SGEdiscount;
	}

	public void setSGEdiscount(float sGEdiscount) {
		SGEdiscount = sGEdiscount;
	}

	public float getCGEdiscount() {
		return CGEdiscount;
	}

	public void setCGEdiscount(float cGEdiscount) {
		CGEdiscount = cGEdiscount;
	}

	public List<HallDetailsConcession> getHallDetailsConcession() {
		return hallDetailsConcession;
	}

	public void setHallDetailsConcession(List<HallDetailsConcession> hallDetailsConcession) {
		this.hallDetailsConcession = hallDetailsConcession;
	}

    public float getHalfDepositAmount() {
        return halfDepositAmount;
    }

    public void setHalfDepositAmount(float halfDepositAmount) {
        this.halfDepositAmount = halfDepositAmount;
    }

    public int getMinDay() {
        return minDay;
    }

    public void setMinDay(int minDay) {
        this.minDay = minDay;
    }

    public int getMaxDay() {
        return maxDay;
    }

    public void setMaxDay(int maxDay) {
        this.maxDay = maxDay;
    }
	
}
