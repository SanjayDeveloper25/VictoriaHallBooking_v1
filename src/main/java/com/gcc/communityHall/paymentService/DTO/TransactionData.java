package com.gcc.communityHall.paymentService.DTO;

public class TransactionData {

	 	private Merchant merchant;
	    private Transaction transaction;

		public Merchant getMerchant() {
			return merchant;
		}
		public void setMerchant(Merchant merchant) {
			this.merchant = merchant;
		}
		public Transaction getTransaction() {
			return transaction;
		}
		public void setTransaction(Transaction transaction) {
			this.transaction = transaction;
		}



}
