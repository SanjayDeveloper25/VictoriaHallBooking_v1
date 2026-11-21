package com.gcc.communityHall.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PensionerDetailsResponse {

	 @JsonProperty("PensionerDetailsResult")
	    private List<PensionerDetails> employees;

	    @JsonProperty("Message")
	    private String message;

	    @JsonProperty("ResultStatus")
	    private boolean resultStatus;

		public List<PensionerDetails> getEmployees() {
			return employees;
		}

		public void setEmployees(List<PensionerDetails> employees) {
			this.employees = employees;
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