package com.gcc.communityHall.paymentService.refund.DTO;

public class TransactionRefundResponse {

	private String merchantCode;
    private String merchantTransactionIdentifier;
    private String merchantTransactionRequestType;
    private String responseType;
    private String transactionState;
    private PaymentMethod paymentMethod;
    private ErrorDetails error;

   // @JsonIgnoreProperties("merchantAdditionalDetails")
    private Object merchantAdditionalDetails;


	public String getMerchantCode() {
		return merchantCode;
	}
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	public String getMerchantTransactionIdentifier() {
		return merchantTransactionIdentifier;
	}
	public void setMerchantTransactionIdentifier(String merchantTransactionIdentifier) {
		this.merchantTransactionIdentifier = merchantTransactionIdentifier;
	}
	public String getMerchantTransactionRequestType() {
		return merchantTransactionRequestType;
	}
	public void setMerchantTransactionRequestType(String merchantTransactionRequestType) {
		this.merchantTransactionRequestType = merchantTransactionRequestType;
	}
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	public String getTransactionState() {
		return transactionState;
	}
	public void setTransactionState(String transactionState) {
		this.transactionState = transactionState;
	}
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public ErrorDetails getError() {
		return error;
	}
	public void setError(ErrorDetails error) {
		this.error = error;
	}



	 public Object getMerchantAdditionalDetails() {
		return merchantAdditionalDetails;
	}
	public void setMerchantAdditionalDetails(Object merchantAdditionalDetails) {
		this.merchantAdditionalDetails = merchantAdditionalDetails;
	}
	@Override
	    public String toString() {
	        return "MyClass{" +
	                "merchantCode='" + merchantCode + '\'' +
	                ", merchantTransactionIdentifier='" + merchantTransactionIdentifier + '\'' +
	                ", merchantTransactionRequestType='" + merchantTransactionRequestType + '\'' +
	                ", responseType='" + responseType + '\'' +
	                ", transactionState='" + transactionState + '\'' +
	                ", paymentMethod=" + paymentMethod +
	                ", error=" + error +
	                '}';
	    }


}
