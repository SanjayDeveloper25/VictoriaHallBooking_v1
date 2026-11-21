package com.gcc.communityHall.paymentService.refund.DTO;

public class PaymentMethod {

    private String token;
    private String instrumentAliasName;
    private String instrumentToken;
    private String bankSelectionCode;
    private PaymentTransaction paymentTransaction;
    private Object authentication;
    private ErrorDetails error;
    private Object desItc;
    private Object walletDetails;
    private Object upi;


    private Object paymentMode;
    private Object aCS;
    private Object oTP;

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getInstrumentAliasName() {
		return instrumentAliasName;
	}
	public void setInstrumentAliasName(String instrumentAliasName) {
		this.instrumentAliasName = instrumentAliasName;
	}
	public String getInstrumentToken() {
		return instrumentToken;
	}
	public void setInstrumentToken(String instrumentToken) {
		this.instrumentToken = instrumentToken;
	}
	public String getBankSelectionCode() {
		return bankSelectionCode;
	}
	public void setBankSelectionCode(String bankSelectionCode) {
		this.bankSelectionCode = bankSelectionCode;
	}
	public PaymentTransaction getPaymentTransaction() {
		return paymentTransaction;
	}
	public void setPaymentTransaction(PaymentTransaction paymentTransaction) {
		this.paymentTransaction = paymentTransaction;
	}
	public Object getAuthentication() {
		return authentication;
	}
	public void setAuthentication(Object authentication) {
		this.authentication = authentication;
	}
	public ErrorDetails getError() {
		return error;
	}
	public void setError(ErrorDetails error) {
		this.error = error;
	}
	public Object getDesItc() {
		return desItc;
	}
	public void setDesItc(Object desItc) {
		this.desItc = desItc;
	}
	public Object getWalletDetails() {
		return walletDetails;
	}
	public void setWalletDetails(Object walletDetails) {
		this.walletDetails = walletDetails;
	}
	public Object getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(Object paymentMode) {
		this.paymentMode = paymentMode;
	}
	public Object getaCS() {
		return aCS;
	}
	public void setaCS(Object aCS) {
		this.aCS = aCS;
	}
	public Object getoTP() {
		return oTP;
	}
	public void setoTP(Object oTP) {
		this.oTP = oTP;
	}
	public Object getUpi() {
		return upi;
	}
	public void setUpi(Object upi) {
		this.upi = upi;
	}







}
