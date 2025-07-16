package com.example.userservice.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    // Twilio credentials (replace with your actual SID & token)
    private final String ACCOUNT_SID = "your_twilio_account_sid";
    private final String AUTH_TOKEN = "your_twilio_auth_token";
    private final String FROM_PHONE = "+1234567890"; // your Twilio number

    public SmsService() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void sendSms(String toPhone, String content) {
        Message.creator(
                new com.twilio.type.PhoneNumber(toPhone),
                new com.twilio.type.PhoneNumber(FROM_PHONE),
                content
        ).create();
    }
}
