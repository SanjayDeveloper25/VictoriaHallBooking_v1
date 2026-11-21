package com.gcc.communityHall.service.Impl;

import com.gcc.communityHall.service.WhatsappMsgService;

import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WhatsappMsgServiceImpl implements WhatsappMsgService {
    @Override
    public String sendMsg(String msgUrl) {
        System.out.println("Whatsapp URL :-"+msgUrl);

        String response ="";
        try{
            HttpURLConnection connection =null;

            URL url = new URL(msgUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            String str = connection.getResponseMessage();
            response=responseCode+"";   
            System.out.println("Response Code: " + responseCode);
            System.out.println("Response message: " + str);

        }catch (Exception e){
            e.printStackTrace();
        }

        return response;
    }
    
    @Override
    public String sendSMS(String SMS_Url) {
    	System.out.println("SMS URL:- "+ SMS_Url);
        RestTemplate restTemplate = new RestTemplate();
        String response =restTemplate.getForObject(SMS_Url,String.class);
        return response;
    }
}
