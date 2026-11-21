package com.gcc.communityHall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PensionerDetails {
	@JsonProperty("Employee_code")
	private String employee_code;

	@JsonProperty("Pensioner_Code")
	private String pensioner_code;

	@JsonProperty("Name")
	private String name;

	@JsonProperty("Old_Pensioner_Code")
	private String old_pensioner_code;

	@JsonProperty("Designation")
	private String designation;

	@JsonProperty("Department")
	private String department;

	@JsonProperty("Functionary")
	private String functionary;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("AADHAR_NO")
	private String aadhar_no;

	@JsonProperty("MOBILE_NO")
	private String mobile_no;

	public String getEmployee_code() {
		return employee_code;
	}

	public void setEmployee_code(String employee_code) {
		this.employee_code = employee_code;
	}

	public String getPensioner_code() {
		return pensioner_code;
	}

	public void setPensioner_code(String pensioner_code) {
		this.pensioner_code = pensioner_code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOld_pensioner_code() {
		return old_pensioner_code;
	}

	public void setOld_pensioner_code(String old_pensioner_code) {
		this.old_pensioner_code = old_pensioner_code;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getFunctionary() {
		return functionary;
	}

	public void setFunctionary(String functionary) {
		this.functionary = functionary;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAadhar_no() {
		return aadhar_no;
	}

	public void setAadhar_no(String aadhar_no) {
		this.aadhar_no = aadhar_no;
	}

	public String getMobile_no() {
		return mobile_no;
	}

	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}



}