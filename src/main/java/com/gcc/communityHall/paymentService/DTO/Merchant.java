package com.gcc.communityHall.paymentService.DTO;

import org.springframework.stereotype.Component;

@Component
public class Merchant {


	 private String identifier;


		public String getIdentifier() {
	        return identifier;
	    }

	    public void setIdentifier(String identifier) {
	        this.identifier = identifier;
	    }

}
