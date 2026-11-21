package com.gcc.communityHall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FamilyPensionerDetails {

	@JsonProperty("Family_Pensioner_Code")
    private String family_Pensioner_Code;

    @JsonProperty("Family_Pensioner_Name")
    private String family_Pensioner_Name;

    @JsonProperty("Pensioner_Code")
    private String pensioner_Code;

	@JsonProperty("Old_Fam_Pensioner_Number")
    private String old_Fam_Pensioner_Number;

    @JsonProperty("Employee_Code")
    private String employee_Code;

    @JsonProperty("Employee_Name")
    private String employee_Name;

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

	public String getFamily_Pensioner_Code() {
		return family_Pensioner_Code;
	}

	public void setFamily_Pensioner_Code(String family_Pensioner_Code) {
		this.family_Pensioner_Code = family_Pensioner_Code;
	}

	public String getFamily_Pensioner_Name() {
		return family_Pensioner_Name;
	}

	public void setFamily_Pensioner_Name(String family_Pensioner_Name) {
		this.family_Pensioner_Name = family_Pensioner_Name;
	}

	public String getPensioner_Code() {
		return pensioner_Code;
	}

	public void setPensioner_Code(String pensioner_Code) {
		this.pensioner_Code = pensioner_Code;
	}

	public String getOld_Fam_Pensioner_Number() {
		return old_Fam_Pensioner_Number;
	}

	public void setOld_Fam_Pensioner_Number(String old_Fam_Pensioner_Number) {
		this.old_Fam_Pensioner_Number = old_Fam_Pensioner_Number;
	}

	public String getEmployee_Code() {
		return employee_Code;
	}

	public void setEmployee_Code(String employee_Code) {
		this.employee_Code = employee_Code;
	}

	public String getEmployee_Name() {
		return employee_Name;
	}

	public void setEmployee_Name(String employee_Name) {
		this.employee_Name = employee_Name;
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