package com.entitybank.digital.ussd.service.impl;

import org.springframework.stereotype.Service;

@Service
public class CoreBankingService {
    public String getBalance(String msisdn) {
        return "KES 12,450";
    }

    public void transfer(String from, String to, int amount) {}
}

