package com.gcc.communityHall.paymentService.service;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.databind.JsonNode;
import com.gcc.communityHall.service.Impl.PaymentsServiceImpl;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.BookingDetails.BookingStatus;
import com.gcc.communityHall.entity.BookingDetails.RefundStatus;
import com.gcc.communityHall.entity.RefundTransaction;
import com.gcc.communityHall.paymentService.DTO.Merchant;
import com.gcc.communityHall.paymentService.DTO.Transaction;
import com.gcc.communityHall.paymentService.DTO.TransactionData;
import com.gcc.communityHall.paymentService.config.PaymentConfig;
import com.gcc.communityHall.paymentService.entity.RefundResponse;
import com.gcc.communityHall.paymentService.entity.TransactionResponse;
import com.gcc.communityHall.paymentService.refund.DTO.TransactionRefundResponse;
import com.gcc.communityHall.paymentService.repository.TransRefundResRepository;
import com.gcc.communityHall.paymentService.repository.TransactionResponseRepository;
import com.gcc.communityHall.repository.BookingDetailsRepository;
import com.gcc.communityHall.repository.RefundTransactionRepository;
import com.gcc.communityHall.service.Impl.WhatsappMsgServiceImpl;
import com.google.gson.Gson;


@Service
public class PaymentGatewayServiceImpl implements PaymentGatewayService {

	@Autowired
	private PaymentConfig paymentConfig;

	@Autowired
	private TransactionResponseRepository transactionResRepo;

	@Autowired
	private TransRefundResRepository refundResRepo;
	
	@Autowired
	private BookingDetailsRepository bookingDetailsRepository;
	
	@Autowired
	private WhatsappMsgServiceImpl whatsappMsgServiceImpl;
	
	@Autowired
	private RefundTransactionRepository refundTransactionRepository;
    @Autowired
    private PaymentsServiceImpl paymentsServiceImpl;

	@Override
	public String generateToken(String token) throws NoSuchAlgorithmException {
		//System.out.println("Token:-" + token);
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(token.getBytes());

		byte[] bytes = md.digest();

		BigInteger bigInt = new BigInteger(1, bytes);
		String encryptedtoken = bigInt.toString(16);

		while (encryptedtoken.length() < 128) {
			encryptedtoken = "0" + encryptedtoken;
		}
		//System.out.println("Encryption :- " + encryptedtoken);
		return encryptedtoken;
	}

//The process of encryption for refund API
	@Override
	public String encryptHxRefund(String text) throws Exception {
		 Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		    SecretKeySpec keySpec = new SecretKeySpec(this.paymentConfig.getEncryptionKeyByte(), "AES");
		    IvParameterSpec ivSpec = new IvParameterSpec(this.paymentConfig.getEncryptionIVByte());
		    cipher.init(1, keySpec, ivSpec);
		    byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
		    String hxencstr = ToHexStrRefund(results);
		    return hxencstr;
	}

	@Override
	public String ToHexStrRefund(byte[] dataBuf) {
		String hxstr = null;
		Hex hx = new Hex();
		hxstr = new String(hx.encode(dataBuf));
		return hxstr;
	}

//The process of decryption for refund API
	@Override
	public String decryptHxTRefund(String hxstrr) throws Exception{
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");

		SecretKeySpec keySpec = new SecretKeySpec(this.paymentConfig.getEncryptionKeyByte(), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(this.paymentConfig.getEncryptionIVByte());
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

		byte[] decbytes = FromHexStr(hxstrr);

		byte[] results = cipher.doFinal(decbytes);
		return new String(results, "UTF-8").trim();
	}

	@Override
	public byte[] FromHexStr(String hxStr) {
		byte[] charArray = hxStr.getBytes();
		byte[] bufstr = null;
		Hex hxx = new Hex();

		try {
			bufstr = hxx.decode(charArray);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bufstr;
	}

	@Override
	public TransactionResponse recordResponse( TransactionResponse transactionResponse) {
		// TODO Auto-generated method stub
		//System.out.println("=====================================");
		//System.out.println(transactionResponse.toString());
		return transactionResRepo.save(transactionResponse);
	//transactionResRepo.updateTransactionResponse(transactionResponse.getId(), transactionResponse.getAmount(), transactionResponse.getBookingId(), transactionResponse.getDate(), transactionResponse.getHallId(), transactionResponse.getResponse(), transactionResponse.getStatusCode(), transactionResponse.getTpslTxnId(), transactionResponse.getTxansactionId(), transactionResponse.getUserId());
		//return transactionResponse;
	}

	@Override
	public RefundResponse recordRefund(RefundResponse refundReponse) {
		// TODO Auto-generated method stub
		return this.refundResRepo.save(refundReponse);
	}

	@Override
	public TransactionResponse getTransactionResponseByBookingId(int bookingId) {
		// TODO Auto-generated method stub
		Optional<TransactionResponse> findByBookingId = this.transactionResRepo.findByBookingId(bookingId);
		if(findByBookingId.isPresent()) {
			return findByBookingId.get();
		}
		else {
			return new TransactionResponse();
		}
	}
	@Override
	public List<TransactionResponse> getTransactionResponseByuserId(String userId) {
		// TODO Auto-generated method stub
		List<TransactionResponse> findByuserId = this.transactionResRepo.findByuserId(userId);
		return findByuserId;
	}

       @Override
	public TransactionResponse getTransactionResponseByTxansactionId(String txansactionId,String statusCode) {
		// TODO Auto-generated method stub
		Optional<TransactionResponse> findByTxansactionId=this.transactionResRepo.findByTxansactionIdAndStatusCode(txansactionId,statusCode);
		if(findByTxansactionId.isPresent()) {
			return findByTxansactionId.get();
		}
		else {
			return new TransactionResponse();
		}
	}

	@Override
	public TransactionResponse getTransactionResponseByBookingIdAndStatusCode(int bookingId, String statusCode) {
		Optional<TransactionResponse> findByTxansactionId=this.transactionResRepo .findByBookingIdAndStatusCode(bookingId,statusCode);
		if(findByTxansactionId.isPresent()) {
			return findByTxansactionId.get();
		}
		else {
			return new TransactionResponse();
		}
	}

	@Override
	public String getToken(String merchantId, String txnId, String totalAmt, String accountNo, String consumerId,
			String consumerMobileNo, String consumerEmailId, String debitStartDate, String debitEndDate,
			String maxAmount, String amountType, String frequency, String cardNumber, String expMonth, String expYear,
			String cvvCode, String salt) throws NoSuchAlgorithmException {
        String token=merchantId+txnId+totalAmt+accountNo+consumerId+consumerMobileNo+
                consumerEmailId+debitStartDate+debitEndDate+maxAmount+amountType+
                frequency+cardNumber+expMonth+expYear+cvvCode+salt;

		return generateToken(token);
	}

	@Override
	public String refundTheAmount(int bookingId,int userId) throws Exception {
		//System.out.println("Refund Api call.......working on it");
		TransactionResponse transactionRes = this.getTransactionResponseByBookingIdAndStatusCode(bookingId,this.paymentConfig.getTransactionSuccessCode());
		RefundResponse recordRefundResponse = new RefundResponse();

		Merchant merchant = new Merchant();
		merchant.setIdentifier(this.paymentConfig.getMerchantId());

		Transaction transaction = new Transaction();
		transaction.setAmount(transactionRes.getAmount());//Amount given here
		transaction.setIdentifier("");
		String[] datestime = transactionRes.getDate().split(" ");
		transaction.setDateTime(datestime[0]); //At what date transaction happens
		transaction.setRequestType("R");
		transaction.setToken("");
		transaction.setReference(transactionRes.getTpslTxnId()); //tpsl refer number

		TransactionData transactionData = new TransactionData();
		transactionData.setMerchant(merchant);
		transactionData.setTransaction(transaction);

		// Serialize TransactionData object to JSON string
		Gson gson = new Gson();
		String json = gson.toJson(transactionData);
		//System.out.println(json);

		String encryptHxRefund = this.encryptHxRefund(json);
		RestTemplate resttemplate = new RestTemplate();
		String Url ="https://www.tpsl-india.in/PaymentGateway/merchant2.pg/"+this.paymentConfig.getMerchantId();
		ResponseEntity<String> postForEntity = resttemplate.postForEntity(Url, encryptHxRefund, String.class);

		String decryptHxTRefund = this.decryptHxTRefund(postForEntity.getBody());
		recordRefundResponse.setResponse(decryptHxTRefund);
		recordRefundResponse.setUserId(String.valueOf(userId));//make it dynamic
		recordRefundResponse.setBookingId(bookingId);		
		recordRefundResponse.setDate(LocalDate.now());
		try {
			ObjectMapper objectMapper = new ObjectMapper();

			TransactionRefundResponse myObject = objectMapper.readValue(decryptHxTRefund, TransactionRefundResponse.class);
			//System.out.println(myObject.toString());

			recordRefundResponse.setStatusCode(myObject.getPaymentMethod().getPaymentTransaction().getStatusCode());
			recordRefundResponse.setTxansactionId(myObject.getMerchantTransactionIdentifier());
			if(myObject.getPaymentMethod().getPaymentTransaction().getStatusCode().equals(this.paymentConfig.getRefundSuccessCode())) {
				//System.out.println("Refund Process complete........"+bookingId);
				recordRefundResponse.setAmount(myObject.getPaymentMethod().getPaymentTransaction().getAmount());
				recordRefundResponse.setBalaceAmount(myObject.getPaymentMethod().getPaymentTransaction().getBalanceAmount());
				this.recordRefund(recordRefundResponse);
				RefundTransaction refundTransaction=new RefundTransaction();
				refundTransaction.setAmountRefunded(Double.parseDouble(recordRefundResponse.getAmount()));
				refundTransaction.setCdate(new Date());
				Optional<BookingDetails> bookingsDetails=bookingDetailsRepository.findById(bookingId);
				BookingDetails bookingDetails=bookingsDetails.get();
				refundTransaction.setBookingDetails(bookingDetails);
				refundTransaction.setRefundIdentifier(myObject.getPaymentMethod().getPaymentTransaction().getRefundIdentifier());
				refundTransaction.setRefundStatus(recordRefundResponse.getStatusCode());
				refundTransaction.setTransactionId(recordRefundResponse.getTxansactionId());
				refundTransactionRepository.save(refundTransaction);
				return "Refunded";
			}
			this.recordRefund(recordRefundResponse);
			return "Error";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Error";

	}




	@Override
	public String refundTheDepositAmount(int bookingId, int userId,double amount) throws Exception {
		//System.out.println("Refund Api call.......working on it");
		TransactionResponse transactionRes = this.getTransactionResponseByBookingIdAndStatusCode(bookingId,this.paymentConfig.getTransactionSuccessCode());
		RefundResponse recordRefundResponse = new RefundResponse();

		Merchant merchant = new Merchant();
		merchant.setIdentifier(this.paymentConfig.getMerchantId());

		Transaction transaction = new Transaction();
		transaction.setAmount(String.valueOf(amount));//Amount given here
		transaction.setIdentifier("");
		String[] datestimes = transactionRes.getDate().split(" ");
		transaction.setDateTime(datestimes[0]); //At what date transaction happens
		transaction.setRequestType("R");
		transaction.setToken("");
		transaction.setReference(transactionRes.getTpslTxnId()); //tpsl refer number

		TransactionData transactionData = new TransactionData();
		transactionData.setMerchant(merchant);
		transactionData.setTransaction(transaction);

		// Serialize TransactionData object to JSON string
		Gson gson = new Gson();
		String json = gson.toJson(transactionData);
		System.out.println(json);

		String encryptHxRefund = this.encryptHxRefund(json);
		RestTemplate resttemplate = new RestTemplate();
		String Url ="https://www.tpsl-india.in/PaymentGateway/merchant2.pg/"+this.paymentConfig.getMerchantId();
		ResponseEntity<String> postForEntity = resttemplate.postForEntity(Url, encryptHxRefund, String.class);

		String decryptHxTRefund = this.decryptHxTRefund(postForEntity.getBody());
		recordRefundResponse.setResponse(decryptHxTRefund);
		recordRefundResponse.setUserId(String.valueOf(userId));//make it dynamic
		recordRefundResponse.setBookingId(bookingId);
		recordRefundResponse.setDate(LocalDate.now());
		try {
			ObjectMapper objectMapper = new ObjectMapper();

			TransactionRefundResponse myObject = objectMapper.readValue(decryptHxTRefund, TransactionRefundResponse.class);
			System.out.println(myObject.toString());

			recordRefundResponse.setStatusCode(myObject.getPaymentMethod().getPaymentTransaction().getStatusCode());
			recordRefundResponse.setTxansactionId(myObject.getMerchantTransactionIdentifier());
			if(myObject.getPaymentMethod().getPaymentTransaction().getStatusCode().equals(this.paymentConfig.getRefundSuccessCode())) {
				//System.out.println("Refund Process complete........"+bookingId);
				recordRefundResponse.setAmount(myObject.getPaymentMethod().getPaymentTransaction().getAmount());
				recordRefundResponse.setBalaceAmount(myObject.getPaymentMethod().getPaymentTransaction().getBalanceAmount());
				this.recordRefund(recordRefundResponse);
				RefundTransaction refundTransaction=new RefundTransaction();
				refundTransaction.setAmountRefunded(Double.parseDouble(recordRefundResponse.getAmount()));
				refundTransaction.setCdate(new Date());
				Optional<BookingDetails> bookingsDetails=bookingDetailsRepository.findById(bookingId);
				BookingDetails bookingDetails=bookingsDetails.get();
				refundTransaction.setBookingDetails(bookingDetails);
				refundTransaction.setRefundStatus(recordRefundResponse.getStatusCode());
				refundTransaction.setTransactionId(recordRefundResponse.getTxansactionId());
				refundTransaction.setRefundIdentifier(myObject.getPaymentMethod().getPaymentTransaction().getRefundIdentifier());
				refundTransactionRepository.save(refundTransaction);
				return "Refunded";
			}
			this.recordRefund(recordRefundResponse);
			return "Error";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Error";
	}
	@Override
	public String manualrefundTheAmount(int bookingId,int userId, String txansactionId, String amount) throws Exception {
		//System.out.println("Refund Api call.......working on it");
		TransactionResponse transactionRes = this.getTransactionResponseByBookingIdAndTransactionIdStatusCode(bookingId,txansactionId,this.paymentConfig.getTransactionSuccessCode());
		RefundResponse recordRefundResponse = new RefundResponse();
		Optional<BookingDetails> bookings=bookingDetailsRepository.findById(bookingId);
		BookingDetails bookingdetails= null;
		if(bookings.isPresent()) {
			bookingdetails=bookings.get();
			if((bookingdetails.getBookingStatus() == BookingDetails.BookingStatus.REFUNDED) &&
					(bookingdetails.getRefundStatus() == RefundStatus.COMPLETED)) {
				return "Error";
			}
		}
		else {
			return "Error";
		}
		
		Merchant merchant = new Merchant();
		merchant.setIdentifier(this.paymentConfig.getMerchantId());

		Transaction transaction = new Transaction();
		transaction.setAmount(amount);//Amount given here
		transaction.setIdentifier("");
		String[] datestime = transactionRes.getDate().split(" ");
		transaction.setDateTime(datestime[0]); //At what date transaction happens
		transaction.setRequestType("R");
		transaction.setToken("");
		transaction.setReference(transactionRes.getTpslTxnId()); //tpsl refer number

		TransactionData transactionData = new TransactionData();
		transactionData.setMerchant(merchant);
		transactionData.setTransaction(transaction);

		// Serialize TransactionData object to JSON string
		Gson gson = new Gson();
		String json = gson.toJson(transactionData);
		//System.out.println(json);

		String encryptHxRefund = this.encryptHxRefund(json);
		RestTemplate resttemplate = new RestTemplate();
		String Url ="https://www.tpsl-india.in/PaymentGateway/merchant2.pg/"+this.paymentConfig.getMerchantId();
		ResponseEntity<String> postForEntity = resttemplate.postForEntity(Url, encryptHxRefund, String.class);

		String decryptHxTRefund = this.decryptHxTRefund(postForEntity.getBody());
		recordRefundResponse.setResponse(decryptHxTRefund);
		recordRefundResponse.setUserId(String.valueOf(userId));//make it dynamic
		recordRefundResponse.setBookingId(bookingId);		
		recordRefundResponse.setDate(LocalDate.now());
		try {
			ObjectMapper objectMapper = new ObjectMapper();

			TransactionRefundResponse myObject = objectMapper.readValue(decryptHxTRefund, TransactionRefundResponse.class);
			//System.out.println(myObject.toString());

			recordRefundResponse.setStatusCode(myObject.getPaymentMethod().getPaymentTransaction().getStatusCode());
			recordRefundResponse.setTxansactionId(myObject.getMerchantTransactionIdentifier());
			if(myObject.getPaymentMethod().getPaymentTransaction().getStatusCode().equals(this.paymentConfig.getRefundSuccessCode())) {
				//System.out.println("Refund Process complete........"+bookingId);
				long mobile = bookingdetails.getUsers().getMobileNo();
        		String settlementrefundsms="Your refund amount of Rs. "+amount+" for Booking ID "+bookingId+" has been initiated. The amount will be credited to your account within 3 to 15 days. Thanks for using the GCC Community Hall Booking System.";
        		String settlementrefundurlString ="https://tmegov.onex-aura.com/api/sms?key=pfTEYN6H&to="+mobile+"&from=GCCCRP&body="+settlementrefundsms+"&entityid=1401572690000011081&templateid=1407172346804951524";	
        		String settlementrefundwhatsupmsg="https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to="+mobile+"&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Your+refund+of+Rs.+"+amount+"+for+Booking+ID+"+bookingId+"+has+been+initiated.+%0AThe+amount+will+be+credited+to+your+account+within+3+to+15+days.";
	    		// whatsappMsgServiceImpl.sendMsg(settlementrefundwhatsupmsg);
	    		// whatsappMsgServiceImpl.sendSMS(settlementrefundurlString);
				recordRefundResponse.setAmount(myObject.getPaymentMethod().getPaymentTransaction().getAmount());
				recordRefundResponse.setBalaceAmount(myObject.getPaymentMethod().getPaymentTransaction().getBalanceAmount());
				this.recordRefund(recordRefundResponse);
				bookingdetails.setBookingStatus(BookingStatus.REFUNDED);
				bookingdetails.setRefundStatus(RefundStatus.COMPLETED);
				bookingDetailsRepository.save(bookingdetails);
				RefundTransaction refundTransaction=new RefundTransaction();
				refundTransaction.setAmountRefunded(Double.parseDouble(recordRefundResponse.getAmount()));
				refundTransaction.setCdate(new Date());
				refundTransaction.setBookingDetails(bookingdetails);
				refundTransaction.setRefundIdentifier(myObject.getPaymentMethod().getPaymentTransaction().getRefundIdentifier());
				refundTransaction.setRefundStatus(recordRefundResponse.getStatusCode());
				refundTransaction.setTransactionId(recordRefundResponse.getTxansactionId());
				refundTransactionRepository.save(refundTransaction);
				return "Refunded";
			}
			this.recordRefund(recordRefundResponse);
			return "Error";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Error";

	}

	@Override
	public TransactionResponse getTransactionResponseByBookingIdAndTransactionIdStatusCode(int bookingId,String txansactionId, String statusCode) {
		Optional<TransactionResponse> findByTxansactionId=this.transactionResRepo .findByBookingIdAndTxansactionIdAndStatusCode(bookingId,txansactionId,statusCode);
		if(findByTxansactionId.isPresent()) {
			return findByTxansactionId.get();
		}
		else {
			return new TransactionResponse();
		}
	}
}
