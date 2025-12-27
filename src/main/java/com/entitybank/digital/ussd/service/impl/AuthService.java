package com.entitybank.digital.ussd.service.impl;

import com.entitybank.digital.ussd.entity.UssdCustomer;
import com.entitybank.digital.ussd.repository.UssdCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UssdCustomerRepository repo;
    private final PasswordEncoder encoder =
            PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public boolean validatePin(String msisdn, String pin) {

        UssdCustomer c = repo.findById(msisdn)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + msisdn));

        if ("Y".equals(c.getLocked())) return false;

        /*
         * 🔐 PRODUCTION LOGIC (DISABLED FOR NOW)
         *
         * if (encoder.matches(pin, c.getPinHash())) {
         *     c.setPinRetries(0);
         *     repo.save(c);
         *     return true;
         * }
         */

        // ✅ TEMP / DEV LOGIC — plain PIN comparison
        if (pin.equals(c.getPinPlain())) {
            c.setPinRetries(0);
            repo.save(c);
            return true;
        }

        c.setPinRetries(c.getPinRetries() + 1);
        if (c.getPinRetries() >= 3) {
            c.setLocked("Y");
        }

        repo.save(c);
        return false;
    }
    public boolean customerExists(String msisdn) {
        return repo.existsById(msisdn);
    }
    public boolean validateSecretWord(String msisdn, String input) {

        UssdCustomer c = repo.findById(msisdn)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return input.equalsIgnoreCase(c.getSecretWord());
    }

    public void updatePin(String msisdn, String newPin) {

        UssdCustomer c = repo.findById(msisdn)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        c.setPinPlain(newPin);
        c.setPinRetries(0);
        c.setLocked("N");

        repo.save(c);
    }

}

