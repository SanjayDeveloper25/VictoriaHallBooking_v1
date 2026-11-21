package com.gcc.communityHall.paymentService.refund.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorDetails {

	@JsonProperty("code")
    private String errorCode;

    @JsonProperty("desc")
    private String errorDescription;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}



}
