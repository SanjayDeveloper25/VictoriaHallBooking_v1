package com.gcc.communityHall.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FamilyPensionerDetailsResponse {

	@JsonProperty("FamilyPensionerDetailsResult")
    private List<FamilyPensionerDetails> familyPensionerDetails;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("ResultStatus")
    private boolean resultStatus;

	public List<FamilyPensionerDetails> getFamilyPensionerDetails() {
		return familyPensionerDetails;
	}

	public void setFamilyPensionerDetails(List<FamilyPensionerDetails> familyPensionerDetails) {
		this.familyPensionerDetails = familyPensionerDetails;
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