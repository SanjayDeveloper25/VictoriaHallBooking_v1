package com.gcc.communityHall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeDetails {
	
	@JsonProperty("name")
    private String name;

    @JsonProperty("Department")
    private String Department;

    @JsonProperty("Designation")
    private String Designation;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return Department;
	}

	public void setDepartment(String department) {
		Department = department;
	}

	public String getDesignation() {
		return Designation;
	}

	public void setDesignation(String designation) {
		Designation = designation;
	}

	

}
