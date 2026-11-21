package com.gcc.communityHall.paymentService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:paymentConfig.properties")
public class PaymentConfig {

	@Value("${payment.worldline.merchantId}")
    private String merchantId;

	@Value("${payment.worldline.encryptionKey}")
    private String encryptionKey;

	@Value("${payment.worldline.encryptionIV}")
    private String encryptionIV;

	@Value("${payment.worldline.schemeCode}")
    private String schemeCode;

	@Value("${payment.worldline.merchantName}")
    private String merchantName;

	@Value("${payment.worldline.transactionSuccessCode}")
    private String transactionSuccessCode;

	@Value("${payment.worldline.refundSuccessCode}")
    private String refundSuccessCode;


	@Value("${razorpay.key-id}")
	private String razorpayKeyId;

	@Value("${razorpay.key-secret}")
	private String razorpayKeySecret;

	public String getRazorpayKeyId() {
		return razorpayKeyId;
	}

	public String getRazorpayKeySecret() {
		return razorpayKeySecret;
	}




	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getEncryptionKey() {
		return encryptionKey;
	}
	public byte[] getEncryptionKeyByte() {
		return encryptionKey.getBytes();
	}

	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}

	public String getEncryptionIV() {
		return encryptionIV;
	}

	public byte[] getEncryptionIVByte() {
		return encryptionIV.getBytes();
	}

	public void setEncryptionIV(String encryptionIV) {
		this.encryptionIV = encryptionIV;
	}

	public String getSchemeCode() {
		return schemeCode;
	}

	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getTransactionSuccessCode() {
		return transactionSuccessCode;
	}

	public void setTransactionSuccessCode(String transactionSuccessCode) {
		this.transactionSuccessCode = transactionSuccessCode;
	}

	public String getRefundSuccessCode() {
		return refundSuccessCode;
	}

	public void setRefundSuccessCode(String refundSuccessCode) {
		this.refundSuccessCode = refundSuccessCode;
	}





}
