package com.gcc.communityHall.paymentService.DTO;

import org.springframework.stereotype.Component;

@Component
public class Transaction {

	  private String amount;
	    private String identifier;
	    private String dateTime;
	    private String requestType;
	    private String token;
	    private String reference;





		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
		public String getIdentifier() {
			return identifier;
		}
		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}
		public String getDateTime() {
			return dateTime;
		}
		public void setDateTime(String dateTime) {
			this.dateTime = dateTime;
		}
		public String getRequestType() {
			return requestType;
		}
		public void setRequestType(String requestType) {
			this.requestType = requestType;
		}
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public String getReference() {
			return reference;
		}
		public void setReference(String reference) {
			this.reference = reference;
		}


}
