package com.gcc.communityHall.paymentService.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.gcc.communityHall.configuration.EncryptionUtil;
import com.gcc.communityHall.constants.AppConstants;
import com.gcc.communityHall.entity.*;
import com.gcc.communityHall.others.AppConfig;
//import com.gcc.communityHall.repository.ErpPostDataRepository;
import com.gcc.communityHall.service.Impl.PaymentsServiceImpl;
import com.gcc.communityHall.service.PaymentsService;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gcc.communityHall.entity.BookingDetails.BookingStatus;
import com.gcc.communityHall.entity.BookingDetails.RefundStatus;
import com.gcc.communityHall.paymentService.config.PaymentConfig;
import com.gcc.communityHall.paymentService.entity.TransactionResponse;
import com.gcc.communityHall.paymentService.repository.TransactionResponseRepository;
import com.gcc.communityHall.paymentService.service.PaymentGatewayService;
import com.gcc.communityHall.repository.BookingDetailsRepository;
import com.gcc.communityHall.repository.ExtraPaymentsRepository;
import com.gcc.communityHall.repository.PaymentsRepository;
import com.gcc.communityHall.service.Impl.BookingServiceImpl;

@Controller
@RequestMapping("/victoriahallbooking_uat/payment")
public class PaymentController {
    // ak
    @Autowired
    private EncryptionUtil encryptionUtil;

    @Autowired
    PaymentGatewayService paymentService;

    @Autowired
    private PaymentConfig paymentConfig;

    @Autowired
    private BookingServiceImpl bookser;

    @Autowired
    private TransactionResponseRepository transactionResRepo;

    @Autowired
    private PaymentsRepository paymentRepo;

    @Autowired
    private ExtraPaymentsRepository extraPaymentsRepository;

    @Autowired
    private BookingDetailsRepository bookingDetailsRepository;

    @Autowired
    private AppConfig appConfig;

    // @Autowired
    // private ErpPostDataRepository erpPostDataRepository;

    @Autowired
    private PaymentsService paymentsService;
    @Autowired
    private PaymentsServiceImpl paymentsServiceImpl;

    // ak
    /*@PostMapping(value = "/response")
    public String response(Model model, @RequestParam(required = false) String msg) throws NoSuchAlgorithmException {

        String salt = this.paymentConfig.getEncryptionKey();
        String confirmToken = "";
        String status = this.paymentConfig.getTransactionSuccessCode();

        String[] res = msg.split("\\|");

        System.out.println("Payment Response: " + res[0] + " | " + res[1] + " | " + res[2] + " | " + res[3] + " | " + res[4] + " | " + res[5]
                + " | " + res[6] + " | " + res[7] + " | " + res[8] + " | " + res[9]);
        String c = "";

        TransactionResponse response = this.paymentService.getTransactionResponseByTxansactionId(res[3], "temp");

        Payments payment = new Payments();
        payment.setTransactionId(response.getTxansactionId());
        double amount = 0;

        // ------------------ PAYMENT STRING PROCESSING ------------------
        for (int i = 0; i < res.length; i++) {

            if (i == res.length - 1) {
                c = c + salt;
                confirmToken = res[i];
            } else if (res[i].isEmpty()) {
                c = c + "|";
            } else if (res[i].equals("NA")) {
                c = c + "NA|";
            } else {
                c = c + res[i] + "|";
            }

            if (i == 5) {
                response.setTpslTxnId(res[i]);
                response.setAmount(res[6]);
                response.setTxansactionId(res[3]);
                response.setDate(res[8]);
            }
        }

        int bookingId = response.getBookingId();

        try {
            amount = Double.parseDouble(response.getAmount());
        } catch (Exception e) {
        }

        payment.setAmountReceived(amount);
        payment.setPaymentStatus("Not Completed");

        BookingDetails bookingsDetails = bookser.getBookingsById(bookingId); // JPA fetch
        payment.setBookingDetails(bookingsDetails);
        response.setResponse(msg);
        response.setStatusCode(res[0]);
        bookingsDetails.setPayments(payment);
        payment.setCdate(new Date());

        // save transaction response
        transactionResRepo.save(response);

        // =============================================================
        // CASE 1: FAILURE - AMOUNT MISMATCH
        // =============================================================
        if (bookingsDetails.getAmountReceived() != amount) {

            if (paymentService.generateToken(c).equals(confirmToken) && status.equals(res[0])) {
                payment.setPaymentStatus("Success");
                bookingsDetails.setPayments(payment);
                paymentRepo.save(payment);

                // booking failure logic
                bookser.bookingFailure(bookingsDetails, 1);
            }

            // Fetch OR Generate booking_ref_id
            String refId = bookser.getBookingRefIdByBookingId(bookingId);

            // encrypt bookingId (coworkspace logic)
            String encryptedId = encryptionUtil.encrypt(String.valueOf(bookingId));

            model.addAttribute("ref_id", refId);
            model.addAttribute("encryptedId", encryptedId);

            return "redirect:/victoriahallbooking_uat/user/acknowledgement?ref_id=" + refId;
        }

        // =============================================================
        // CASE 2: SUCCESS FLOW
        // =============================================================
        if (paymentService.generateToken(c).equals(confirmToken) && status.equals(res[0])) {

            payment.setPaymentStatus("Success");
            bookingsDetails.setPayments(payment);
            paymentRepo.save(payment);

            try {
                bookser.confirmBooking(bookingId);
            } catch (Exception e) {
                // ignore
            }
        }

        // 🔥 Fetch OR Generate booking_ref_id
        String refId = bookser.getBookingRefIdByBookingId(bookingId);

        // 🔐 Encrypt bookingId
        String encryptedId = encryptionUtil.encrypt(String.valueOf(bookingId));

        model.addAttribute("ref_id", refId);
        model.addAttribute("encryptedId", encryptedId);

        return "redirect:/victoriahallbooking_uat/user/acknowledgement?ref_id=" + refId;
    }*/


    @PostMapping("/response")
    public String updateOrder(
            Model model,
            @RequestParam("razorpay_payment_id") String paymentId,
            @RequestParam("razorpay_order_id") String orderId,
            @RequestParam("razorpay_signature") String signature) {

        try {
            // 1. Find internal transaction ID using Razorpay order ID
            String txnId = paymentsServiceImpl.getTransactionIdByOrderId(orderId);

            if (txnId == null || txnId.isBlank()) {
                throw new RuntimeException(
                        "Transaction ID not found for Razorpay order: " + orderId);
            }

            // 2. Fetch existing temporary transaction response
            TransactionResponse response =
                    paymentService.getTransactionResponseByTxansactionId(txnId, "temp");

            if (response == null) {
                throw new RuntimeException("Transaction response record not found");
            }


            Payments payment = new Payments();
            payment.setTransactionId(response.getTxansactionId());

            // 3. Verify Razorpay signature
            JSONObject signatureOptions = new JSONObject();
            signatureOptions.put("razorpay_order_id", orderId);
            signatureOptions.put("razorpay_payment_id", paymentId);
            signatureOptions.put("razorpay_signature", signature);

            boolean isValid = Utils.verifyPaymentSignature(
                    signatureOptions,
                    AppConstants.key_secret
            );

            if (!isValid) {
                throw new RuntimeException("Invalid Razorpay payment signature");
            }

            // 4. Fetch payment details from Razorpay
            RazorpayClient razorpayClient =
                    new RazorpayClient(
                            AppConstants.key_id,
                            AppConstants.key_secret
                    );


            double amount = 0;
            int bookingId = response.getBookingId();

            try {
                amount = Double.parseDouble(response.getAmount());
            } catch (Exception e) {
            }

            payment.setAmountReceived(amount);
            payment.setPaymentStatus("Not Completed");

            BookingDetails bookingDetails =
                    bookser.getBookingsById(bookingId);

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            String transactionDateTime =
                    LocalDateTime.now().format(formatter);

            // response.setTpslTxnId(paymentId);
            response.setAmount(String.valueOf(amount));
            payment.setBookingDetails(bookingDetails);
            response.setDate(transactionDateTime);
            response.setTxansactionId(txnId);
            response.setStatusCode(
                    paymentConfig.getTransactionSuccessCode()
            );

            payment.setCdate(new Date());

            transactionResRepo.save(response);

            payment.setPaymentStatus("Success");
            bookingDetails.setPayments(payment);
            paymentRepo.save(payment);

            int updatedRows =
                    paymentsServiceImpl.updateRazorpayPaymentId(txnId, paymentId);


            bookser.confirmBooking(bookingId);

            String refId =
                    bookser.getBookingRefIdByBookingId(bookingId);

            String encryptedId = encryptionUtil.encrypt(String.valueOf(bookingId));

            model.addAttribute("ref_id", refId);
            model.addAttribute("encryptedId", encryptedId);


            if (refId == null || refId.isBlank()) {
                throw new RuntimeException("Booking reference ID not found");
            }

            // 13. Redirect to acknowledgement page
            return "redirect:https://gccservices.in/victoriahallbooking_uat/user/acknowledgement?ref_id="
                    + URLEncoder.encode(refId, StandardCharsets.UTF_8);

//            return "redirect:http://localhost:8063/victoriahallbooking_uat/user/acknowledgement?ref_id="
//                    + URLEncoder.encode(refId, StandardCharsets.UTF_8);

        } catch (Exception e) {
            e.printStackTrace();

            // Redirect to your payment failure page
            return "redirect:/victoriahallbooking_uat/user/paymentFailed";
        }
    }



    /*@PostMapping("/response")
    public ResponseEntity<?> updateOrder(
            Model model,
            @RequestParam("razorpay_payment_id") String paymentId,
            @RequestParam("razorpay_order_id") String orderId,
            @RequestParam("razorpay_signature") String signature)
    {
        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", orderId);
            options.put("razorpay_payment_id", paymentId);
             options.put("razorpay_signature", signature);

            System.out.println("Razorpay signature verified successfully");

            RazorpayClient razorpayClient = new RazorpayClient(AppConstants.key_id, AppConstants.key_secret);

            List<Payment> payments = razorpayClient.orders.fetchPayments(orderId);
            System.out.println("Payment response line :100 :"+payments);

            for (Payment payment : payments) {

                System.out.println(
                        new JSONObject(payment.toString()).toString(4)
                );

            }


            // System.out.println("razorpayOrder: " + razorpayOrder);


            TransactionResponse response = this.paymentService.getTransactionResponseByTxansactionId(orderId, "temp");

            Payments payment = new Payments();
            payment.setTransactionId(response.getTxansactionId());
            double amount = 0;

            int bookingId = response.getBookingId();

            try {
                amount = Double.parseDouble(response.getAmount());
            } catch (Exception e) {
            }

            payment.setAmountReceived(amount);
            payment.setPaymentStatus("Not Completed");

            BookingDetails bookingsDetails = bookser.getBookingsById(bookingId);

            bookingsDetails.setPayments(payment);
            payment.setCdate(new Date());

            // save transaction response
            transactionResRepo.save(response);


            boolean isValid = Utils.verifyPaymentSignature(
                    options,
                    AppConstants.key_secret
            );


            if (!isValid) {
                System.out.println("Invalid Razorpay signature");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid payment signature");
            }else{
                payment.setPaymentStatus("Success");
                bookingsDetails.setPayments(payment);
                paymentRepo.save(payment);

                try {
                    bookser.confirmBooking(bookingId);
                } catch (Exception e) {
                    // ignore
                }

            }

            String refId = bookser.getBookingRefIdByBookingId(bookingId);

            // 🔐 Encrypt bookingId
            String encryptedId = encryptionUtil.encrypt(String.valueOf(bookingId));

            model.addAttribute("ref_id", refId);
            model.addAttribute("encryptedId", encryptedId);

            return "redirect:/victoriahallbooking_uat/user/acknowledgement?ref_id="     + refId;

            return ResponseEntity.internalServerError()
                    .body("Unable to save payment details");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Payment processing failed");
        }
    }*/


    /*
     * @PostMapping(value = "/response")
     * public String response(Model model, @RequestParam(required = false) String
     * msg) throws NoSuchAlgorithmException {
     * 
     * String salt = this.paymentConfig.getEncryptionKey();
     * 
     * String confirmToken = "";
     * String status = this.paymentConfig.getTransactionSuccessCode();
     * String s = new String(msg);
     * String c = new String();
     * String res[] = s.split("\\|");
     * 
     * TransactionResponse response =
     * this.paymentService.getTransactionResponseByTxansactionId(res[3], "temp");
     * Payments payment = new Payments();
     * payment.setTransactionId(response.getTxansactionId());
     * double amount = 0;
     * 
     * 
     * for (int i = 0; i < res.length; i++) {
     * if (i == res.length - 1) {
     * c = c + salt;
     * confirmToken = res[i];
     * } else if (res[i].isEmpty()) {
     * c = c + "|";
     * } else if (res[i].equals("NA")) {
     * c = c + "NA|";
     * } else {
     * c = c + res[i] + "|";
     * }
     * 
     * if (i == 5) {
     * response.setTpslTxnId(res[i]);
     * response.setAmount(res[6]);
     * response.setTxansactionId(res[3]);
     * //String[] tDate = res[8].split(" ");
     * response.setDate(res[8]);
     * }
     * }
     * int bookingId = response.getBookingId();
     * try {
     * amount = Double.parseDouble(response.getAmount());
     * } catch (Exception e) {
     * // TODO: handle exception
     * //System.out.println("payment Controller lineNo-67");
     * }
     * payment.setAmountReceived(amount);
     * payment.setPaymentStatus("Not Completed");
     * 
     * BookingDetails bookingsDetails = bookser.getBookingsById(bookingId);
     * payment.setBookingDetails(bookingsDetails);
     * response.setResponse(msg);
     * response.setStatusCode(res[0]);
     * bookingsDetails.setPayments(payment);
     * payment.setCdate(new Date());
     * 
     * System.out.println("bookingsDetails.getAmountReceived().." +
     * bookingsDetails.getAmountReceived());
     * System.out.println("amount: " + amount);
     * 
     * TransactionResponse save = transactionResRepo.save(response);
     * 
     * if (bookingsDetails.getAmountReceived() != amount) { // Reject booking if the
     * amount is incorrect
     * if(paymentService.generateToken(c).equals(confirmToken) &&
     * status.equals(res[0])){
     * payment.setPaymentStatus("Success");
     * bookingsDetails.setPayments(payment);
     * paymentRepo.save(payment);
     * bookser.bookingFailure(bookingsDetails, 1);
     * }
     * //ak
     * 
     * 
     * 
     * model.addAttribute("bookingsDetails", bookingsDetails);
     * return "redirect:/victoriahallbooking_uat/user/acknowledgement?bookingId=" +
     * bookingsDetails.getBookingId();
     * }
     * 
     * 
     * if (paymentService.generateToken(c).equals(confirmToken) &&
     * status.equals(res[0])) {
     * payment.setPaymentStatus("Success");
     * bookingsDetails.setPayments(payment);
     * try {
     * String confirmBooking = bookser.confirmBooking(response.getBookingId());
     * }catch (Exception e){
     * // return "redirect:/communityhall/user/acknowledgement?bookingId=" +
     * bookingsDetails.getBookingId();
     * 
     * }
     * // postDataToERP(payment, response); // post payment details to ERP
     * //
     * System.out.println("\n confirmBooking:- paymentController 111"+confirmBooking
     * +"\n");
     * // if(confirmBooking.equals("Booking confirmed successfully")) {
     * // //bookser.updateBookingStatusById(bookingsDetails.getBookingId(),
     * BookingDetails.BookingStatus.APPROVED, "confirm");
     * // // String bookingconfimationsms =
     * "Your booking with ID "+bookingsDetails.getBookingId()+" on"
     * +bookingsDetails.getBookingId()
     * +" has been successfully confirmed. Thanks for using GCC Community Hall Booking System."
     * ;
     * // }else if(confirmBooking.equals("Slot occupied")) {
     * //// System.out.println("Slot occupied line 115");
     * //// BookingDetails crrSts =
     * bookser.updateBookingStatusById(bookingsDetails.getBookingId(),
     * BookingDetails.BookingStatus.REFUNDED,
     * "slot occupied Refund will happen shortly");
     * //// System.out.println("BookingDetails:-"+crrSts.getBookingStatus());
     * // }
     * // else if(confirmBooking.equals("Error confirming booking")) {
     * // bookser.updateBookingStatusById(bookingsDetails.getBookingId(),
     * BookingDetails.BookingStatus.PENDING, "Error");
     * //
     * // }
     * 
     * }
     * 
     * // System.out.println(response.toString());
     * model.addAttribute("bookingsDetails", bookingsDetails); // Add single
     * BookingDetails object
     * //System.out.println(save.toString());
     * paymentRepo.save(payment);
     * return "redirect:/victoriahallbooking_uat/user/acknowledgement?bookingId=" +
     * bookingsDetails.getBookingId();
     * }
     */

    /*
     * public void postDataToERP(Payments payment, TransactionResponse res) {
     * RestTemplate restTemplate = new RestTemplate();
     * erpPostData erpData = new erpPostData();
     * 
     * SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
     * String txDate = outputFormat.format(payment.getCdate());
     * String assesNo = LocalDate.now().getYear() + "/COMM/" + res.getBookingId();
     * 
     * Map<String, Object> data = new TreeMap<>();
     *//* static mandatory input to be sent *//*
                                               * data.put("serviceId", "getOtherCollection");
                                               * data.put("COLLECTION_TYPE", "Community Hall");
                                               * data.put("TOTALTRANSACTIONCODE", "450310000");
                                               * // /end of static/
                                               * // /unique data to be send/
                                               * data.put("ASSESSMENTNO", assesNo);
                                               * data.put("UNIQUERECEIPTID", res.getBookingId());
                                               * data.put("TRANSACTIONNO", payment.getTransactionId());
                                               * data.put("TRANSACTIONREFNO", res.getTpslTxnId());
                                               * data.put("TRANSACTIONDATE", txDate); //payment date format dd/mm/yyyy
                                               * -> 19/02/2025
                                               * data.put("TOTALTRANSACTIONAMOUNT",
                                               * payment.getBookingDetails().getAmountReceived());
                                               * 
                                               * data.put("MSTRCODEONE", "061");
                                               * data.put("SPLITAMOUNTONE",
                                               * payment.getBookingDetails().getHallAmount());
                                               * data.put("MSTRCODETWO", "062");
                                               * data.put("SPLITAMOUNTTWO",
                                               * payment.getBookingDetails().getDepositAmount());
                                               * data.put("MSTRCODETHREE", "063");
                                               * data.put("SPLITAMOUNTTHREE", payment.getBookingDetails().getGst());
                                               * // /Amount Collected from party/
                                               * data.put("PARTYNAME",
                                               * payment.getBookingDetails().getUsers().getName());
                                               * data.put("PARTYADDRESS",
                                               * payment.getBookingDetails().getUsers().getStreet() + "," +
                                               * payment.getBookingDetails().getUsers().getArea() + "," +
                                               * payment.getBookingDetails().getUsers().getLocation() + "," +
                                               * payment.getBookingDetails().getUsers().getPinCode());
                                               * data.put("PARTYMOBILE",
                                               * payment.getBookingDetails().getUsers().getMobileNo());
                                               *//* end *//*
                                                           * System.out.println(data);
                                                           * System.out.println(appConfig.getCommErpUrl());
                                                           * // ResponseEntity<String> stringResponseEntity =
                                                           * restTemplate.postForEntity(appConfig.getErpUrl(), data,
                                                           * String.class);
                                                           * // System.out.println(stringResponseEntity.getBody());
                                                           * 
                                                           * erpData.setTx_id(payment.getTransactionId());
                                                           * erpData.setBooking_det_booking_id(res.getBookingId());
                                                           * erpData.setStatus(true);
                                                           * erpData.setCdate(LocalDate.now());
                                                           * erpData.setPay_status(1);
                                                           * erpPostDataRepository.save(erpData);
                                                           * 
                                                           * UriComponentsBuilder builder =
                                                           * UriComponentsBuilder.fromHttpUrl(appConfig.getCommErpUrl())
                                                           * ;
                                                           * for (Map.Entry<String, Object> entry : data.entrySet()) {
                                                           * builder.queryParam(entry.getKey(), entry.getValue());
                                                           * }
                                                           * 
                                                           * String urlMsg = builder.toUriString();
                                                           * System.out.println(urlMsg);
                                                           * 
                                                           * try {
                                                           * HttpURLConnection connection = null;
                                                           * 
                                                           * URL url = new URL(urlMsg);
                                                           * connection = (HttpURLConnection) url.openConnection();
                                                           * connection.setRequestMethod("GET");
                                                           * 
                                                           * int responseCode = connection.getResponseCode();
                                                           * // response=responseCode+"";
                                                           * System.out.println("Response Code: " + responseCode);
                                                           * 
                                                           * if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                                                           * BufferedReader in = new BufferedReader(new
                                                           * InputStreamReader(connection.getInputStream()));
                                                           * String inputLine;
                                                           * StringBuilder response = new StringBuilder();
                                                           * while ((inputLine = in.readLine()) != null) {
                                                           * response.append(inputLine);
                                                           * }
                                                           * in.close();
                                                           * System.out.println("Response: " + response.toString());
                                                           * 
                                                           * ObjectMapper objectMapper = new ObjectMapper();
                                                           * JsonNode jsonNode =
                                                           * objectMapper.readTree(response.toString());
                                                           * 
                                                           * String resultStatus =
                                                           * jsonNode.get("ResultStatus").asText();
                                                           * 
                                                           * if (!resultStatus.isEmpty() &&
                                                           * resultStatus.equals("false")) {
                                                           * erpData.setStatus(false);
                                                           * erpPostDataRepository.save(erpData);
                                                           * }
                                                           * 
                                                           * } else {
                                                           * System.out.println("GET request failed.");
                                                           * }
                                                           * 
                                                           * } catch (Exception e) {
                                                           * e.printStackTrace();
                                                           * }
                                                           * }
                                                           */

    @PostMapping(value = "/additionaldamage-response")
    public String additionaldamagePaymentresponse(Model model, @RequestParam(required = false) String msg)
            throws NoSuchAlgorithmException {

        String salt = this.paymentConfig.getEncryptionKey();

        String confirmToken = "";
        String status = this.paymentConfig.getTransactionSuccessCode();
        String s = new String(msg);
        String c = new String();
        String res[] = s.split("\\|");

        TransactionResponse response = this.paymentService.getTransactionResponseByTxansactionId(res[3], "temp");
        ExtraPayments payment = new ExtraPayments();
        payment.setTransactionId(response.getTxansactionId());
        double amount = 0;

        for (int i = 0; i < res.length; i++) {
            if (i == res.length - 1) {
                c = c + salt;
                confirmToken = res[i];
            } else if (res[i].isEmpty()) {
                c = c + "|";
            } else if (res[i].equals("NA")) {
                c = c + "NA|";
            } else {
                c = c + res[i] + "|";
            }

            if (i == 5) {
                response.setTpslTxnId(res[i]);
                response.setAmount(res[6]);
                response.setTxansactionId(res[3]);
                // String[] tDate = res[8].split(" ");
                response.setDate(res[8]);
            }
        }
        int bookingId = response.getBookingId();
        try {
            amount = Double.parseDouble(response.getAmount());
        } catch (Exception e) {
            // TODO: handle exception
            // System.out.println("payment Controller lineNo-67");
        }
        payment.setAmountReceived(amount);
        payment.setPaymentStatus("Not Completed");

        BookingDetails bookingsDetails = bookser.getBookingsById(bookingId);
        payment.setBookingDetails(bookingsDetails);
        response.setResponse(msg);
        response.setStatusCode(res[0]);
        bookingsDetails.setExtrapayments(payment);
        payment.setCdate(new Date());

        TransactionResponse save = transactionResRepo.save(response);

        if (paymentService.generateToken(c).equals(confirmToken) && status.equals(res[0])) {
            payment.setPaymentStatus("Success");
            bookingsDetails.setExtrapayments(payment);
            bookingsDetails.getSettlement().setCollectionStatus("Paid");
            bookingsDetails.setBookingStatus(BookingStatus.COMPLETED);
            bookingsDetails.setRefundStatus(RefundStatus.CLOSED);
            bookingsDetails.setComments("Collection Amount Paid Successfully");
            bookingDetailsRepository.save(bookingsDetails);
        }
        model.addAttribute("bookingsDetails", bookingsDetails);
        extraPaymentsRepository.save(payment);
        // postEPDataToERP(payment, response); //post extra payments details to ERP
        // return
        // "redirect:/communityhall/user/collectionpendingacknowledgement?bookingId=" +
        // bookingsDetails.getBookingId();
        return "redirect:/victoriahallbooking_uat/user/collectionpendingacknowledgement?bookingId="
                + bookingsDetails.getBookingId();
    }

    /*
     * public void postEPDataToERP(ExtraPayments payment, TransactionResponse res) {
     * RestTemplate restTemplate = new RestTemplate();
     * erpPostData erpData = new erpPostData();
     * 
     * SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
     * String txDate = outputFormat.format(payment.getCdate());
     * String assesNo = LocalDate.now().getYear() + "/COMM/ED/" +
     * res.getBookingId();
     * 
     * Map<String, Object> data = new TreeMap<>();
     *//* static mandatory input to be sent *//*
                                               * data.put("serviceId", "getOtherCollection");
                                               * data.put("COLLECTION_TYPE", "Community Hall");
                                               * data.put("TOTALTRANSACTIONCODE", "450310000");
                                               * // /end of static/
                                               * // /unique data to be send/
                                               * data.put("ASSESSMENTNO", assesNo);
                                               * data.put("UNIQUERECEIPTID", res.getBookingId());
                                               * data.put("TRANSACTIONNO", payment.getTransactionId());
                                               * data.put("TRANSACTIONREFNO", res.getTpslTxnId());
                                               * data.put("TRANSACTIONDATE", txDate); //payment date format dd/mm/yyyy
                                               * -> 19/02/2025
                                               * data.put("TOTALTRANSACTIONAMOUNT", payment.getAmountReceived());
                                               * 
                                               * data.put("MSTRCODEONE", "061");
                                               * data.put("SPLITAMOUNTONE", 0);
                                               * data.put("MSTRCODETWO", "062");
                                               * data.put("SPLITAMOUNTTWO", payment.getAmountReceived());
                                               * data.put("MSTRCODETHREE", "063");
                                               * data.put("SPLITAMOUNTTHREE", 0);
                                               * // /Amount Collected from party/
                                               * data.put("PARTYNAME",
                                               * payment.getBookingDetails().getUsers().getName());
                                               * data.put("PARTYADDRESS",
                                               * payment.getBookingDetails().getUsers().getStreet() + "," +
                                               * payment.getBookingDetails().getUsers().getArea() + "," +
                                               * payment.getBookingDetails().getUsers().getLocation() + "," +
                                               * payment.getBookingDetails().getUsers().getPinCode());
                                               * data.put("PARTYMOBILE",
                                               * payment.getBookingDetails().getUsers().getMobileNo());
                                               *//* end *//*
                                                           * System.out.println(data);
                                                           * System.out.println(appConfig.getCommErpUrl());
                                                           * // ResponseEntity<String> stringResponseEntity =
                                                           * restTemplate.postForEntity(appConfig.getErpUrl(), data,
                                                           * String.class);
                                                           * // System.out.println(stringResponseEntity.getBody());
                                                           * 
                                                           * erpData.setTx_id(payment.getTransactionId());
                                                           * erpData.setBooking_det_booking_id(res.getBookingId());
                                                           * erpData.setStatus(true);
                                                           * erpData.setCdate(LocalDate.now());
                                                           * erpData.setPay_status(2);
                                                           * erpPostDataRepository.save(erpData);
                                                           * 
                                                           * UriComponentsBuilder builder =
                                                           * UriComponentsBuilder.fromHttpUrl(appConfig.getCommErpUrl())
                                                           * ;
                                                           * for (Map.Entry<String, Object> entry : data.entrySet()) {
                                                           * builder.queryParam(entry.getKey(), entry.getValue());
                                                           * }
                                                           * 
                                                           * String urlMsg = builder.toUriString();
                                                           * System.out.println(urlMsg);
                                                           * 
                                                           * try {
                                                           * HttpURLConnection connection = null;
                                                           * 
                                                           * URL url = new URL(urlMsg);
                                                           * connection = (HttpURLConnection) url.openConnection();
                                                           * connection.setRequestMethod("GET");
                                                           * 
                                                           * int responseCode = connection.getResponseCode();
                                                           * // response=responseCode+"";
                                                           * System.out.println("Response Code: " + responseCode);
                                                           * 
                                                           * if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                                                           * BufferedReader in = new BufferedReader(new
                                                           * InputStreamReader(connection.getInputStream()));
                                                           * String inputLine;
                                                           * StringBuilder response = new StringBuilder();
                                                           * while ((inputLine = in.readLine()) != null) {
                                                           * response.append(inputLine);
                                                           * }
                                                           * in.close();
                                                           * System.out.println("Response: " + response.toString());
                                                           * 
                                                           * ObjectMapper objectMapper = new ObjectMapper();
                                                           * JsonNode jsonNode =
                                                           * objectMapper.readTree(response.toString());
                                                           * 
                                                           * String resultStatus =
                                                           * jsonNode.get("ResultStatus").asText();
                                                           * 
                                                           * if (!resultStatus.isEmpty() &&
                                                           * resultStatus.equals("false")) {
                                                           * erpData.setStatus(false);
                                                           * erpPostDataRepository.save(erpData);
                                                           * }
                                                           * 
                                                           * } else {
                                                           * System.out.println("GET request failed.");
                                                           * }
                                                           * 
                                                           * } catch (Exception e) {
                                                           * e.printStackTrace();
                                                           * }
                                                           * }
                                                           */

    @PostMapping(value = "/updatetransaction")
    public String updatetransaction(Model model, @RequestParam(required = false) String txansactionId,
            @RequestParam(required = false) String msg) throws NoSuchAlgorithmException {

        Optional<TransactionResponse> transactionresponse = transactionResRepo.findByTxansactionId(txansactionId);
        TransactionResponse transactionres = transactionresponse.get();
        if ("temp".equals(transactionres.getStatusCode()) && !"0300".equals(transactionres.getStatusCode())) {
            String salt = this.paymentConfig.getEncryptionKey();

            String confirmToken = "";
            String status = this.paymentConfig.getTransactionSuccessCode();
            String s = new String(msg);
            String c = new String();
            String res[] = s.split("\\|");

            TransactionResponse response = this.paymentService.getTransactionResponseByTxansactionId(res[3], "temp");
            Payments payment = new Payments();
            payment.setTransactionId(response.getTxansactionId());
            double amount = 0;

            for (int i = 0; i < res.length; i++) {
                if (i == res.length - 1) {
                    c = c + salt;
                    confirmToken = res[i];
                } else if (res[i].isEmpty()) {
                    c = c + "|";
                } else if (res[i].equals("NA")) {
                    c = c + "NA|";
                } else {
                    c = c + res[i] + "|";
                }

                if (i == 5) {
                    response.setTpslTxnId(res[i]);
                    response.setAmount(res[6]);
                    response.setTxansactionId(res[3]);
                    // String[] tDate = res[8].split(" ");
                    response.setDate(res[8]);
                }
            }
            int bookingId = response.getBookingId();
            try {
                amount = Double.parseDouble(response.getAmount());
            } catch (Exception e) {
                // TODO: handle exception
                // System.out.println("payment Controller lineNo-67");
            }
            payment.setAmountReceived(amount);
            payment.setPaymentStatus("Not Completed");

            BookingDetails bookingsDetails = bookser.getBookingsById(bookingId);
            payment.setBookingDetails(bookingsDetails);
            response.setResponse(msg);
            response.setStatusCode(res[0]);
            bookingsDetails.setPayments(payment);
            payment.setCdate(new Date());

            TransactionResponse save = transactionResRepo.save(response);

            if (paymentService.generateToken(c).equals(confirmToken) && status.equals(res[0])) {
                payment.setPaymentStatus("Success");
                bookingsDetails.setPayments(payment);
                String confirmBooking = bookser.confirmBooking(response.getBookingId());

            }

            // System.out.println(response.toString());
            model.addAttribute("bookingsDetails", bookingsDetails); // Add single BookingDetails object
            // System.out.println(save.toString());
            paymentRepo.save(payment);
            return "success";
        } else {
            return "already exists";
        }
    }

    @PostMapping(value = "/refundcompleted")
    public String refundCompleted(Model model, @RequestParam String txansactionId,
            @RequestParam(required = false) String msg) throws NoSuchAlgorithmException {
        Optional<TransactionResponse> transactionresponse = transactionResRepo.findByTxansactionId(txansactionId);
        if (transactionresponse.isPresent()) {
            TransactionResponse transactionres = transactionresponse.get();
            Optional<BookingDetails> bookings = bookingDetailsRepository.findById(transactionres.getBookingId());
            if (bookings.isPresent()) {
                BookingDetails bookingDetails = bookings.get();
                bookingDetails.setRefundStatus(RefundStatus.CLOSED);
                bookingDetailsRepository.save(bookingDetails);
                return "success";
            } else {
                return "failure";
            }
        } else {
            return "failure";
        }
    }
}
