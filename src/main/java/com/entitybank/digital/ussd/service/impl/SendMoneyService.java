package com.entitybank.digital.ussd.service.impl;

import org.springframework.stereotype.Service;

@Service
public class SendMoneyService {

    public boolean post(String from, String to, String amount) {
        // Mock posting
        System.out.println("POSTING SEND MONEY");
        System.out.println("FROM: " + from);
        System.out.println("TO: " + to);
        System.out.println("AMOUNT: " + amount);
        return true;
    }
}
