package com.gcc.communityHall.paymentService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.Payments;
import com.gcc.communityHall.entity.RefundTransaction;
import com.gcc.communityHall.paymentService.config.PaymentConfig;
import com.gcc.communityHall.paymentService.entity.RefundClientInfo;
import com.gcc.communityHall.paymentService.entity.RefundResponse;
import com.gcc.communityHall.paymentService.entity.TransactionResponse;
import com.gcc.communityHall.paymentService.entity.TransactionStatusLog;
import com.gcc.communityHall.paymentService.repository.RefundClientInfoRepository;
import com.gcc.communityHall.paymentService.repository.TransRefundResRepository;
import com.gcc.communityHall.paymentService.repository.TransactionResponseRepository;
import com.gcc.communityHall.paymentService.repository.TransactionStatusLogRepository;
import com.gcc.communityHall.paymentService.service.PaymentGatewayServiceImpl;
import com.gcc.communityHall.repository.PaymentsRepository;
import com.gcc.communityHall.service.BookingService;
import com.gcc.communityHall.service.TransactionService;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RefundController {

    @Autowired
    public RefundClientInfoRepository refundClientInfoRepository;

    @Autowired
    public TransactionResponseRepository transactionResponseRepository;

    @Autowired
    private TransactionStatusLogRepository transactionStatusLogRepository;

    @Autowired
    private PaymentConfig paymentConfig;

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PaymentGatewayServiceImpl paymentGatewayService;
    @Autowired
    private TransRefundResRepository transRefundResRepository;

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PaymentGatewayServiceImpl paymentGatewayServiceImpl;


    @GetMapping(value = "/refundProcess")
    public ResponseEntity refundProcess() {
        try {
            List<Map<String, Object>> pendingTransactions = transactionResponseRepository.getTransactionResponseByStatus("temp");

            for (Map<String, Object> transactionRecord : pendingTransactions) {
                try {
                    Map<String, Object> status = new HashMap<>();
                    Map<String, Object> merchant = new HashMap<>();
                    Map<String, Object> transaction = new HashMap<>();

                    status.put("merchant", merchant);
                    status.put("transaction", transaction);

                    merchant.put("identifier", paymentConfig.getMerchantId());
                    transaction.put("identifier", transactionRecord.get("txansaction_id"));
                    transaction.put("requestType", "O");
                    transaction.put("token", "");
                    transaction.put("reference", "");

                    System.out.println("=====================================" + transactionRecord.get("txansaction_id"));
                    System.out.println(merchant);
                    System.out.println(transaction);
                    System.out.println(status);
                    System.out.println("======================================");
                    String bookingDate = transactionRecord.get("booking_date").toString();
                    if (bookingDate != null && !bookingDate.isEmpty()) {
                        String[] dateParts = bookingDate.split("-");
                        String formattedDate = dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
                        transaction.put("dateTime", formattedDate);
                    } else {
                        transaction.put("dateTime", "");
                    }


                    Gson gson = new Gson();
                    String json = gson.toJson(status);
                    System.out.println("Hello json " + json);
                    String encryptHxRefund = paymentGatewayService.encryptHxRefund(json);

                    RestTemplate restTemplate = new RestTemplate();
                    String url = "https://www.tpsl-india.in/PaymentGateway/merchant2.pg/" + paymentConfig.getMerchantId();
                    ResponseEntity<String> postForEntity = restTemplate.postForEntity(url, encryptHxRefund, String.class);

                    String decryptHxTRefund = paymentGatewayService.decryptHxTRefund(postForEntity.getBody());
                    Map<String, Object> myObj = new ObjectMapper().readValue(decryptHxTRefund, Map.class);
                    Map<String, Object> paymentMethod = (Map<String, Object>) myObj.get("paymentMethod");
                    Map<String, Object> response = (Map<String, Object>) paymentMethod.get("paymentTransaction");

                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    System.out.println("hello2" + response);
                    System.out.println("hello " + myObj);
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

                    TransactionStatusLog log = new TransactionStatusLog();
                    log.setBookingId(transactionRecord.get("booking_id").toString());
                    log.setUserId(transactionRecord.get("user_id").toString());
                    log.setTransactionId(transactionRecord.get("txansaction_id").toString());
//                    log.setStatus(response.get("status").toString());
                    log.setErrorMessage(response.get("errorMessage").toString());
                    log.setStatusMessage(response.get("statusMessage").toString());
                    log.setStatusCode(response.get("statusCode").toString());
                    log.setRefundIdentifier(response.get("refundIdentifier").toString());
                    // log.setResponse(response.toString());
                    log.setAmount(response.get("amount").toString());
                    log.setBalanceAmt(response.get("balanceAmount").toString());
                    log.setReceivedDate(response.get("dateTime").toString());

                    TransactionStatusLog saveLog = transactionStatusLogRepository.save(log);


                    TransactionResponse transRes = transactionResponseRepository.findById(Integer.parseInt(transactionRecord.get("id").toString())
                    ).orElseThrow(() -> new Exception("TransactionResponse not found"));

                    transRes.setStatusCode(log.getStatusCode());
                    transRes.setAmount(log.getAmount());
                    transRes.setTpslTxnId(response.get("identifier").toString());
                    transRes.setResponse(response.toString());
                    transRes.setDate(log.getReceivedDate());

                    transactionResponseRepository.save(transRes);


                    Payments payments = paymentsRepository.findByBookingId(Integer.valueOf(String.valueOf(transRes.getBookingId())));

                    System.out.println("payments hello" + payments);
                    if (payments != null && response.get("statusCode").toString().equals("0300")) {

//                        payments.setBookingDetails(payments.getBookingDetails());
                        payments.setTransactionId(transRes.getTxansactionId());
                        payments.setAmountReceived(Double.parseDouble(response.get("amount").toString()));
                        payments.setPaymentStatus("Success");
                        payments.setCdate(new Date());

                        paymentsRepository.save(payments);

                        bookingService.updateBookingStatus(transRes.getBookingId(), BookingDetails.BookingStatus.REFUNDED, "Booking Not Confirmed. Amount will be refunded shortly.");
                        intiateRefund(transRes.getBookingId(), Integer.parseInt(transRes.getUserId()));

                    } else if (response.get("statusCode").toString().equals("0300")) {
                        payments = new Payments();
                        BookingDetails bookingsById = bookingService.getBookingsById(transRes.getBookingId());
                        payments.setBookingDetails(bookingsById);
                        payments.setTransactionId(transRes.getTxansactionId());
                        payments.setAmountReceived(Double.parseDouble(response.get("amount").toString()));
                        payments.setPaymentStatus("Success");
                        payments.setCdate(new Date());

                        paymentsRepository.save(payments);

                        bookingService.updateBookingStatus(transRes.getBookingId(), BookingDetails.BookingStatus.REFUNDED, "Booking Not Confirmed. Amount will be refunded shortly.");
                        intiateRefund(transRes.getBookingId(), Integer.parseInt(transRes.getUserId()));
                    } else {
                        payments = new Payments();
                        payments.setBookingDetails(payments.getBookingDetails());
                        payments.setTransactionId(transRes.getTxansactionId());
                        try {
                            payments.setAmountReceived(Double.parseDouble(response.get("amount") != null ? response.get("amount").toString() : "0"));//error here
                        }catch (Exception e ){
                            payments.setAmountReceived(0);
                        }
                        payments.setPaymentStatus("Not Completed");
                        payments.setCdate(new Date());

                        paymentsRepository.save(payments);
                        System.out.println("bookingId "+transRes.getBookingId());
                        bookingService.updateBookingStatus(transRes.getBookingId(), BookingDetails.BookingStatus.PAYMENT_FAILURE, "Booking Not Confirmed. Amount Not Received.");


                    }
                    updateRefundInfo(response, log);

                } catch (Exception ex) {
                    System.err.println("Error processing transaction record: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching or saving payment status: " + e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok("refundProcess");

    }


    public void updateRefundInfo(Map<String, Object> response, TransactionStatusLog log) {

        if (response.get("refundDdetails") != null) {

            Map<String, Object> refundInfo = (Map<String, Object>) response.get("refundDdetails");
            List<Map<String, Object>> refundDetails = (List<Map<String, Object>>) refundInfo.get("RefundDetails");
            System.out.println("inifcon :" + refundDetails);
            if (refundDetails != null && !refundDetails.isEmpty()) {
                RefundResponse refundResponse = new RefundResponse();
                refundResponse.setAmount(response.get("amount") != null ? response.get("amount").toString() : "");
                refundResponse.setBalaceAmount(response.get("balanceAmount") != null ? response.get("balanceAmount").toString() : "");
                refundResponse.setTxansactionId(log.getTransactionId());

                Map<String, Object> mapRef = refundDetails.get(refundDetails.size() - 1);
                LocalDate df = LocalDate.now();
                try {
                    df = LocalDate.parse(mapRef.get("REF_TXN_DATE").toString());
                } catch (Exception e) {
                    df = LocalDate.now();
                }
                ;
                refundResponse.setDate(df);
                if (mapRef.get("STATUS") != null && mapRef.get("STATUS").toString().equals("INITIATED")) {
                    refundResponse.setStatusCode("0400");
                } else {
                    refundResponse.setStatusCode("0399");
                }
                refundResponse.setResponse(refundDetails.toString());
                refundResponse.setBookingId(Integer.parseInt(log.getBookingId()));
                refundResponse.setUserId(log.getUserId());
                RefundResponse saverRefundResponse = transRefundResRepository.save(refundResponse);

                Double amt = Double.parseDouble(response.get("amount").toString());
                Double balAmt = Double.parseDouble(response.get("balanceAmount").toString());
                if (amt != balAmt) {
                    RefundTransaction refundTrans = transactionService.getRefundTrans(saverRefundResponse.getBookingId());
                    if (refundTrans != null) {

                        refundTrans.setAmountRefunded(amt - balAmt);
                        refundTrans.setRefundStatus("0400");
                        refundTrans.setRefundIdentifier(response.get("refundIdentifier").toString());
                        refundTrans.setCdate(new Date());


                        transactionService.saveRefundTransaction(refundTrans);
                    } else {
                        refundTrans = new RefundTransaction();
                        refundTrans.setAmountRefunded(amt - balAmt);
                        refundTrans.setRefundStatus("0400");
                        refundTrans.setRefundIdentifier(response.get("refundIdentifier").toString());
                        refundTrans.setCdate(new Date());
                        refundTrans.setTransactionId(log.getTransactionId());
                        BookingDetails bookingsById = bookingService.getBookingsById(Integer.parseInt(log.getBookingId()));

                        refundTrans.setBookingDetails(bookingsById);
                        transactionService.saveRefundTransaction(refundTrans);


                    }
                    bookingService.updateRefundStatus(saverRefundResponse.getBookingId(), BookingDetails.RefundStatus.COMPLETED);
                }

                saveRefundClientInfo(refundDetails, log.getUserId(), log.getBookingId(), log.getTransactionId());
            }


        }


    }

    public String saveRefundClientInfo(List<Map<String, Object>> refundDetail, String userId, String bookingId, String transactionId) {


        if (refundDetail == null || refundDetail.isEmpty()) {
            return "error";
        }


        for (Map<String, Object> refund : refundDetail) {

            RefundClientInfo refundClientInfo = new RefundClientInfo();
            refundClientInfo.setUserId(Integer.parseInt(userId));
            refundClientInfo.setBookingId(Integer.parseInt(bookingId));
            refundClientInfo.setTransactionId(transactionId);
            refundClientInfo.setClntRfndId(refund.get("clnt_rfnd_id") != null ? refund.get("clnt_rfnd_id").toString() : "");
            refundClientInfo.setArnNo(refund.get("ARN_Number") != null ? refund.get("ARN_Number").toString() : "");
            refundClientInfo.setGetRefTxnDate(refund.get("REF_TXN_DATE") != null ? refund.get("REF_TXN_DATE").toString() : "");
            refundClientInfo.setRefTxnAmt(refund.get("REF_TXN_AMT") != null ? refund.get("REF_TXN_AMT").toString() : "");
            refundClientInfo.setRefTxnId(refund.get("REF_TXN_ID") != null ? refund.get("REF_TXN_ID").toString() : "");
            refundClientInfo.setStatus(refund.get("STATUS") != null ? refund.get("STATUS").toString() : "");
            refundClientInfo.setSrno(refund.get("SR_NO") != null ? refund.get("SR_NO").toString() : "");

            refundClientInfoRepository.save(refundClientInfo);

        }
        return "success";

    }

    @Transactional
    public void intiateRefund(int bookingId, int userId) throws Exception {
        String s = paymentGatewayServiceImpl.refundTheAmount(bookingId, userId);
        if (s.equals("Refunded")) {
            bookingService.updateRefundStatus(bookingId, BookingDetails.RefundStatus.COMPLETED);

        } else {
            bookingService.updateRefundStatus(bookingId, BookingDetails.RefundStatus.FAILURE);
        }


    }
}


