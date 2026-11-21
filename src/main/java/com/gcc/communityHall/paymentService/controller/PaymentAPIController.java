package com.gcc.communityHall.paymentService.controller;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Optional;

import com.gcc.communityHall.constants.AppConstants;
import com.gcc.communityHall.service.Impl.PaymentsServiceImpl;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.Users;
import com.gcc.communityHall.paymentService.config.PaymentConfig;
import com.gcc.communityHall.paymentService.entity.TransactionResponse;
import com.gcc.communityHall.paymentService.service.PaymentGatewayService;
import com.gcc.communityHall.paymentService.service.TransactionNumberGenerator;
import com.gcc.communityHall.repository.BookingDetailsRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;




@Tag(
        name = "CRUD REST APIs for Payment in CommunityHall",
        description = "CRUD REST APIs in CommunityHall to CREATE, UPDATE, FETCH AND DELETE Booking details"
)
@RestController
@RequestMapping("/victoriahallbooking_uat/payment/api")
@AllArgsConstructor
@Validated
public class PaymentAPIController {

	@Autowired
	PaymentGatewayService paymentService;

	@Autowired
	private PaymentsServiceImpl paymentsServiceImpl;

	@Autowired
	private PaymentConfig paymentConfig;

	@Autowired
	private BookingDetailsRepository bookRepo;

	@Autowired
	private TransactionNumberGenerator transactionNumber;


//	@Operation(
//            summary = "Refund the Total Amount REST API",
//            description = "REST API to refunding the Total Amount if the admin will cancel and block the hall"
//    )
//	 @GetMapping(value="/refund")
//	    public ResponseEntity<?> refundAmount(Model model,@RequestParam int bookingId) throws Exception {
//	    	//System.out.println("Refund Api call.......working on it");
//	    	TransactionResponse transactionRes = this.paymentService.getTransactionResponseByBookingIdAndStatusCode(bookingId,this.paymentConfig.getTransactionSuccessCode());
//	    	RefundResponse recordRefundResponse = new RefundResponse();
//
//	    	Merchant merchant = new Merchant();
//	    	merchant.setIdentifier(this.paymentConfig.getMerchantId());
//
//	        Transaction transaction = new Transaction();
//	        transaction.setAmount(transactionRes.getAmount());//Amount given here
//	        transaction.setIdentifier("");
//	        String[] datestime = transactionRes.getDate().split(" ");
//	        transaction.setDateTime(datestime[0]); //At what date transaction happens
//	        transaction.setRequestType("R");
//	        transaction.setToken("");
//	        transaction.setReference(transactionRes.getTpslTxnId()); //tpsl refer number
//
//	        TransactionData transactionData = new TransactionData();
//	        transactionData.setMerchant(merchant);
//	        transactionData.setTransaction(transaction);
//
//	        // Serialize TransactionData object to JSON string
//	        Gson gson = new Gson();
//	        String json = gson.toJson(transactionData);
//	        //System.out.println(json);
//
//	        String encryptHxRefund = this.paymentService.encryptHxRefund(json);
//			RestTemplate resttemplate = new RestTemplate();
//			String Url ="https://www.tpsl-india.in/PaymentGateway/merchant2.pg/"+this.paymentConfig.getMerchantId();
//			ResponseEntity<String> postForEntity = resttemplate.postForEntity(Url, encryptHxRefund, String.class);
//
//			String decryptHxTRefund = this.paymentService.decryptHxTRefund(postForEntity.getBody());
//			recordRefundResponse.setResponse(decryptHxTRefund);
//			//recordRefundResponse.setUserId("2");//make it dynamic
//			recordRefundResponse.setBookingId(bookingId);
//			try {
//			        ObjectMapper objectMapper = new ObjectMapper();
//
//				 TransactionRefundResponse myObject = objectMapper.readValue(decryptHxTRefund, TransactionRefundResponse.class);
//		         //System.out.println(myObject.toString());
//
//		     if(myObject.getPaymentMethod().getPaymentTransaction().getStatusCode().equals(this.paymentConfig.getRefundSuccessCode())) {
//		        	 //System.out.println("Refund Process complete........"+bookingId);
//		        	 recordRefundResponse.setAmount(myObject.getPaymentMethod().getPaymentTransaction().getAmount());
//		        	 recordRefundResponse.setBalaceAmount(myObject.getPaymentMethod().getPaymentTransaction().getBalanceAmount());
//		         }
//		         recordRefundResponse.setStatusCode(myObject.getPaymentMethod().getPaymentTransaction().getStatusCode());
//		         recordRefundResponse.setTxansactionId(myObject.getMerchantTransactionIdentifier());
//
//		       this.paymentService.recordRefund(recordRefundResponse);
//		         return ResponseEntity.ok(myObject);
//
//		        } catch (Exception e) {
//		            e.printStackTrace();
//		        }
//			 	return ResponseEntity.ok(decryptHxTRefund);
//
//	    }

	/*@Operation(
            summary = "payment-request REST API",
            description = "REST API payment-request"
    )
	    @PostMapping(value = {"/payment-request"})
	    public HashMap<String,Object> startPayment(int userId,int bookingId) throws NoSuchAlgorithmException {

	    	//System.out.println("I entered payment Service......"+bookingId+ "User Id :- "+userId);
	        HashMap<String, Object> hs = new HashMap<>();
	    	Optional<BookingDetails> findById = bookRepo.findById(bookingId);

	    	if(findById.isPresent()) {
	    		//System.out.println("Success in finding booking Id");
	    		BookingDetails booking = findById.get();
	    		Users user = booking.getUsers();
	    		int findUserId=user.getUserId();
	    	//System.out.println("Merchant ID:-"+this.paymentConfig.getMerchantId());

		    String txnId="VPH"+transactionNumber.generateTransactionNumber()+String.format("%05d", findUserId); //create transaction Id unique from our side
		    String totalAmt = String.valueOf(booking.getAmountReceived()); //Amount of Community Hall from our side
	    	 TransactionResponse response = new TransactionResponse();
	    	 response.setTxansactionId(txnId);
	    	 response.setHallId(booking.getHallDetails().getHallId());
	    	 response.setUserId(String.valueOf(findUserId));//make it dynamic
	    	 response.setStatusCode("temp");
	    	 response.setBookingId(bookingId);
	         this.paymentService.recordResponse(response);


	        String merchantId = this.paymentConfig.getMerchantId(); //This is Test Merchant ID from worldLine
	        String consumerId=String.format("%05d", findUserId); // Replace consumer ID in c964634 from our side
	        String consumerMobileNo=String.valueOf(user.getMobileNo());   //Replace consumer Mobile no in 9879879870 from our side
	        String consumerEmailId=user.getEmail(); //replace consumer email Id

	        String encryptedtoken = this.paymentService.getToken(merchantId+"|", txnId+"|", totalAmt+"|", "|", consumerId+"|", consumerMobileNo+"|", consumerEmailId+"|", "|", "|", "|", "|", "|", "|", "|", "|", "|", this.paymentConfig.getEncryptionKey());

	        hs.put("txansactionId",txnId);
	        hs.put("token",encryptedtoken);
	        hs.put("amount",totalAmt);
	        hs.put("merchantId",merchantId);
	        hs.put("consumerMobileNo",consumerMobileNo);
	        hs.put("consumerEmailId",consumerEmailId);
	        hs.put("consumerId",consumerId);
	        hs.put("error", false);


	    	}
	    	else {
	    		hs.put("error", true);
	    	}

	        return hs;
	    }*/


	@Operation(
			summary = "payment-request REST API",
			description = "REST API payment-request"
	)
	@PostMapping(value = {"/payment-request"})
	public HashMap<String,Object> startPayment(int userId,int bookingId) throws NoSuchAlgorithmException, RazorpayException {

		//System.out.println("I entered payment Service......"+bookingId+ "User Id :- "+userId);
		HashMap<String, Object> res = new HashMap<>();
		Optional<BookingDetails> findById = bookRepo.findById(bookingId);

		if(findById.isPresent()) {
			//System.out.println("Success in finding booking Id");
			BookingDetails booking = findById.get();
			Users user = booking.getUsers();
			int findUserId=user.getUserId();

			if (findUserId != userId) {
				res.put("status", "error");
				res.put("error", true);
				res.put("message", "Invalid user for this booking");
				return res;
			}

			String txnId="VPH"+transactionNumber.generateTransactionNumber()+String.format("%05d", findUserId); //create transaction Id unique from our side
			String totalAmt = String.valueOf(booking.getAmountReceived());

			String consumerMobileNo=String.valueOf(user.getMobileNo());
			String consumerEmailId=user.getEmail();

			TransactionResponse response = new TransactionResponse();
			response.setTxansactionId(txnId);
			response.setHallId(booking.getHallDetails().getHallId());
			response.setUserId(String.valueOf(findUserId));
			response.setStatusCode("temp");
			response.setBookingId(bookingId);
			response.setAmount(totalAmt);
			this.paymentService.recordResponse(response);


//			String merchantId = this.paymentConfig.getMerchantId(); //This is Test Merchant ID from worldLine
			//String consumerId=String.format("%05d", findUserId);


			var client = new RazorpayClient(AppConstants.key_id, AppConstants.key_secret);

			JSONObject options = new JSONObject();
			BigDecimal amountInRupees =
					new BigDecimal(String.valueOf(booking.getAmountReceived()));

			int amountInPaise = amountInRupees
					.multiply(new BigDecimal("100"))
					.setScale(0, RoundingMode.HALF_UP)
					.intValueExact();

			options.put("amount", amountInPaise);
			options.put("currency", "INR");

			// options.put("payment_capture", 1);
			//options.put("id",bookingId);

			Order od = client.orders.create(options);
			String razorpayOrderId = od.get("id");

			int orderlogId = paymentsServiceImpl.saveorderLog(razorpayOrderId,od.toString(), String.valueOf(bookingId));

			if (orderlogId <= 0) {
				throw new RuntimeException("Failed to save order log");
			}
			int updatedRows = paymentsServiceImpl.updateOrderId(
									razorpayOrderId, bookingId);

			if (updatedRows <= 0) {
				throw new RuntimeException(
						"Failed to update order ID in transaction response");
			}

			String key = AppConstants.key_id;

			res.put("status", "created");
			res.put("error", false);

			res.put("key", key);
			res.put("id", od.get("id"));
			res.put("amount", od.get("amount"));
			res.put("currency", od.get("currency"));

			res.put("customerName", "GCC");

			res.put("customerEmail",
					user.getEmail() != null
							?consumerEmailId
							: "gccservices@gmail.com");

			res.put("customerPhone",
					user.getMobileNo() != 0
							? consumerMobileNo
							: "9876543210");

			res.put("description",
					"Payment for Victoria Public Hall Booking");


			res.put("callback_url", "https://lavender-lunchroom-mastiff.ngrok-free.dev/victoriahallbooking_uat/payment/response");
//			res.put("callback_url", "https://gccservices.in/victoriahallbooking_uat/payment/response");
			// res.put("callback_url", "https://gccservices.in/victoriahallbooking_uat/bookingapi/Confirm");

			res.put("refid",bookingId);
			System.out.println(res);
			res.put("txansactionId",txnId);

			res.put("consumerMobileNo",consumerMobileNo);
			res.put("consumerEmailId",consumerEmailId);
			res.put("error", false);


		}
		else {
			res.put("error", true);
		}

		return res;
	}
	
	@Operation(
            summary = "Additional Damage payment-request REST API",
            description = "REST API Additional Damage payment-request"
    )
	    @PostMapping(value = {"/additionaldamage-payment-request"})
	    public HashMap<String,Object> startextraPayment(int userId,int bookingId) throws NoSuchAlgorithmException {

	    	//System.out.println("I entered payment Service......"+bookingId+ "User Id :- "+userId);
	        HashMap<String, Object> hs = new HashMap<>();
	    	Optional<BookingDetails> findById = bookRepo.findById(bookingId);

	    	if(findById.isPresent()) {
	    		//System.out.println("Success in finding booking Id");
	    		BookingDetails booking = findById.get();
	    		Users user = booking.getUsers();
	    		int findUserId=user.getUserId();
	    	//System.out.println("Merchant ID:-"+this.paymentConfig.getMerchantId());

		    String txnId="VPHED"+transactionNumber.generateTransactionNumber()+String.format("%05d", findUserId); //create transaction Id unique from our side
		    String totalAmt = String.valueOf(booking.getSettlement().getCollectionPending()); //Amount of Community Hall from our side
	    	 TransactionResponse response = new TransactionResponse();
	    	 response.setTxansactionId(txnId);
	    	 response.setHallId(booking.getHallDetails().getHallId());
	    	 response.setUserId(String.valueOf(findUserId));//make it dynamic
	    	 response.setStatusCode("temp");
	    	 response.setBookingId(bookingId);
	         this.paymentService.recordResponse(response);


	        String merchantId = this.paymentConfig.getMerchantId(); //This is Test Merchant ID from worldLine
	        String consumerId=String.format("%05d", findUserId); // Replace consumer ID in c964634 from our side
	        String consumerMobileNo=String.valueOf(user.getMobileNo());   //Replace consumer Mobile no in 9879879870 from our side
	        String consumerEmailId=user.getEmail(); //replace consumer email Id

	        String encryptedtoken = this.paymentService.getToken(merchantId+"|", txnId+"|", totalAmt+"|", "|", consumerId+"|", consumerMobileNo+"|", consumerEmailId+"|", "|", "|", "|", "|", "|", "|", "|", "|", "|", this.paymentConfig.getEncryptionKey());
	        System.out.println(totalAmt);
	        hs.put("txansactionId",txnId);
	        hs.put("token",encryptedtoken);
	        hs.put("amount",totalAmt);
	        hs.put("merchantId",merchantId);
	        hs.put("consumerMobileNo",consumerMobileNo);
	        hs.put("consumerEmailId",consumerEmailId);
	        hs.put("consumerId",consumerId);
	        hs.put("error", false);
	    	}
	    	else {
	    		hs.put("error", true);
	    	}

	        return hs;
	    }

}
