package com.gcc.communityHall.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeDetailsResponse {
	@JsonProperty("EmployeeDetailsResult")
    private List<EmployeeDetails> EmployeeDetailsResult;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("ResultStatus")
    private boolean resultStatus;

	public List<EmployeeDetails> getEmployeeDetailsResult() {
		return EmployeeDetailsResult;
	}

	public void setEmployeeDetailsResult(List<EmployeeDetails> employeeDetailsResult) {
		EmployeeDetailsResult = employeeDetailsResult;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(boolean resultStatus) {
		this.resultStatus = resultStatus;
	}
    
    

}
