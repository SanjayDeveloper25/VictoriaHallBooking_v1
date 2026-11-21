package com.gcc.communityHall.paymentService.DTO;

import java.util.HashMap;

public class RefundRequest {

	private HashMap<String, String> merchant;
	private HashMap<String,String> transaction;

	public HashMap<String, String> getMerchant() {
		return merchant;
	}
	public void setMerchant(HashMap<String, String> merchant) {
		this.merchant = merchant;
	}
	public HashMap<String, String> getTransaction() {
		return transaction;
	}
	public void setTransaction(HashMap<String, String> transaction) {
		this.transaction = transaction;
	}



}
