/*
package com.gcc.communityHall.paymentService.scheduler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.ExtraPayments;
import com.gcc.communityHall.entity.Payments;
import com.gcc.communityHall.entity.erpPostData;
import com.gcc.communityHall.others.AppConfig;
import com.gcc.communityHall.paymentService.entity.TransactionResponse;
import com.gcc.communityHall.paymentService.repository.TransactionResponseRepository;
import com.gcc.communityHall.repository.ErpPostDataRepository;
import com.gcc.communityHall.repository.ExtraPaymentsRepository;
import com.gcc.communityHall.repository.PaymentsRepository;
import com.gcc.communityHall.service.BookingService;
import com.gcc.communityHall.service.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.swing.text.html.Option;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Component
//@EnableScheduling
public class ERPScheduler {

    @Autowired
    private ErpPostDataRepository erpPostDataRepository;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private TransactionResponseRepository transactionResponseRepository;

    @Autowired
    private ExtraPaymentsRepository extraPaymentsRepository;


    @Scheduled(fixedRate = 30000)
    public void checkERPPostData() {

        Payments payment = new Payments();
        ExtraPayments expayment = new ExtraPayments();

        List<erpPostData> notpostedlist = erpPostDataRepository.getNotPostedDetails();

        System.out.println("notpostedlist....." + notpostedlist);

        if (!notpostedlist.isEmpty()) {
            for (erpPostData data : notpostedlist) {

                Long bookingId = (long) data.getBooking_det_booking_id();
                String txId = data.getTx_id();
                int status = data.getPay_status();

                if (bookingId == null) {
                    System.out.println("Invalid Booking ID: " + bookingId);
                } else {
                    System.out.println("Processing Booking ID: " + bookingId);
                }
                if (status == 1) {
                    payment = paymentsRepository.findByBookingId(Math.toIntExact(bookingId));
                } else if (status == 2) {
                    expayment = extraPaymentsRepository.getExtraPaymentsByBookingId(Math.toIntExact(bookingId)).orElse(null);
                }
                //                TransactionResponse txRes = transactionResponseRepository.findById(Integer.valueOf(txId)).orElse(null);

                TransactionResponse txRes = transactionResponseRepository.findByTxansactionId(txId).orElse(null);

                if (payment == null) {
                    System.out.println("No Payment found for Booking ID: " + bookingId);
                }
                if (txRes == null) {
                    System.out.println("No Transaction Response found for TX ID: " + txId);
                }
                if (expayment == null) {
                    System.out.println("No Extra Payment found for Booking ID: " + bookingId);
                }


                if (status == 1) {
                    if (payment != null && txRes != null) {
                        postDataToERP(payment, txRes);
                    }
                } else if (status == 2) {
                    if (expayment != null && txRes != null) {
                        postEDDataToERP(expayment, txRes);
                    }
                }
            }
        } else {
            System.out.println("No records found to process.");
        }
    }

    public void postDataToERP(Payments payment, TransactionResponse res) {
        System.out.println("postDataToERP...");

        erpPostData erpData = new erpPostData();

        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        String txDate = outputFormat.format(payment.getCdate());
        String assesNo = LocalDate.now().getYear() + "/COMM/" + res.getBookingId();

        Map<String, Object> data = new TreeMap<>();
        */
/*static mandatory  input to be sent *//*

        data.put("serviceId", "getOtherCollection");
        data.put("COLLECTION_TYPE", "Community Hall");
        data.put("TOTALTRANSACTIONCODE", "450310000");
//         /end of static/
//         /unique data to be send/
        data.put("ASSESSMENTNO", assesNo);
        data.put("UNIQUERECEIPTID", res.getBookingId());
        data.put("TRANSACTIONNO", payment.getTransactionId());
        data.put("TRANSACTIONREFNO", res.getTpslTxnId());
        data.put("TRANSACTIONDATE", txDate); //payment date format dd/mm/yyyy -> 19/02/2025
        data.put("TOTALTRANSACTIONAMOUNT", payment.getBookingDetails().getAmountReceived());

        data.put("MSTRCODEONE", "061");
        data.put("SPLITAMOUNTONE", payment.getBookingDetails().getHallAmount());
        data.put("MSTRCODETWO", "062");
        data.put("SPLITAMOUNTTWO", payment.getBookingDetails().getDepositAmount());
        data.put("MSTRCODETHREE", "063");
        data.put("SPLITAMOUNTTHREE", payment.getBookingDetails().getGst());
//         /Amount Collected from party/
        data.put("PARTYNAME", payment.getBookingDetails().getUsers().getName());
        data.put("PARTYADDRESS", payment.getBookingDetails().getUsers().getStreet() + "," + payment.getBookingDetails().getUsers().getArea() + "," + payment.getBookingDetails().getUsers().getLocation() + "," + payment.getBookingDetails().getUsers().getPinCode());
        data.put("PARTYMOBILE", payment.getBookingDetails().getUsers().getMobileNo());
        */
/* end *//*

        System.out.println(data);
        System.out.println(appConfig.getCommErpUrl());
//         ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(appConfig.getErpUrl(), data, String.class);
//         System.out.println(stringResponseEntity.getBody());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(appConfig.getCommErpUrl());
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }

        String urlMsg = builder.toUriString();
        System.out.println(urlMsg);

        try {
            HttpURLConnection connection = null;

            URL url = new URL(urlMsg);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
//             response=responseCode+"";
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println("Response: " + response.toString());

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.toString());

                String resultStatus = jsonNode.get("ResultStatus").asText();

                if (!resultStatus.isEmpty() && resultStatus.equals("false")) {
                    erpPostDataRepository.updateERPStatus(false, erpData.getId());
                } else{
                    erpPostDataRepository.updateERPStatus(true,erpData.getId());
                }

            } else {
                System.out.println("GET request failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postEDDataToERP(ExtraPayments payment, TransactionResponse res) {
        System.out.println("Post ED Data to ERP....");
        RestTemplate restTemplate = new RestTemplate();
        erpPostData erpData = new erpPostData();

        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        String txDate = outputFormat.format(payment.getCdate());
        String assesNo = LocalDate.now().getYear() + "/COMM/ED/" + res.getBookingId();

        Map<String, Object> data = new TreeMap<>();
        */
/*static mandatory  input to be sent *//*

        data.put("serviceId", "getOtherCollection");
        data.put("COLLECTION_TYPE", "Community Hall");
        data.put("TOTALTRANSACTIONCODE", "450310000");
//         /end of static/
//         /unique data to be send/
        data.put("ASSESSMENTNO", assesNo);
        data.put("UNIQUERECEIPTID", res.getBookingId());
        data.put("TRANSACTIONNO", payment.getTransactionId());
        data.put("TRANSACTIONREFNO", res.getTpslTxnId());
        data.put("TRANSACTIONDATE", txDate); //payment date format dd/mm/yyyy -> 19/02/2025
        data.put("TOTALTRANSACTIONAMOUNT", payment.getAmountReceived());

        data.put("MSTRCODEONE", "061");
        data.put("SPLITAMOUNTONE", 0);
        data.put("MSTRCODETWO", "062");
        data.put("SPLITAMOUNTTWO", payment.getAmountReceived());
        data.put("MSTRCODETHREE", "063");
        data.put("SPLITAMOUNTTHREE", 0);
//         /Amount Collected from party/
        data.put("PARTYNAME", payment.getBookingDetails().getUsers().getName());
        data.put("PARTYADDRESS", payment.getBookingDetails().getUsers().getStreet() + "," +
                payment.getBookingDetails().getUsers().getArea() + "," + payment.getBookingDetails().getUsers().getLocation() + "," +
                payment.getBookingDetails().getUsers().getPinCode());
        data.put("PARTYMOBILE", payment.getBookingDetails().getUsers().getMobileNo());
        */
/* end *//*

        System.out.println(data);
        System.out.println(appConfig.getCommErpUrl());
//         ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(appConfig.getErpUrl(), data, String.class);
//         System.out.println(stringResponseEntity.getBody());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(appConfig.getCommErpUrl());
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }

        String urlMsg = builder.toUriString();
        System.out.println(urlMsg);

        try {
            HttpURLConnection connection = null;

            URL url = new URL(urlMsg);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
//             response=responseCode+"";
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println("Response: " + response.toString());

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.toString());

                String resultStatus = jsonNode.get("ResultStatus").asText();

                if (!resultStatus.isEmpty() && resultStatus.equals("false")) {
                    erpPostDataRepository.updateERPStatus(false,erpData.getId());
                } else{
                    erpPostDataRepository.updateERPStatus(true,erpData.getId());
                }

            } else {
                System.out.println("GET request failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
