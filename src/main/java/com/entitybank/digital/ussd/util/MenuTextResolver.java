package com.entitybank.digital.ussd.util;

import com.entitybank.digital.ussd.entity.UssdCustomer;
import com.entitybank.digital.ussd.repository.UssdCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MenuTextResolver {

    @Autowired
    private UssdCustomerRepository customerRepo;

    public String resolve(String raw, String msisdn) {

        if (raw == null) return null;

        String text = raw;

        // 1️⃣ Replace escaped newlines (\\n) with real newlines
        text = text.replace("\\n", "\n");

        // 2️⃣ Greeting
        text = text.replace("{GREETING}", GreetingUtil.greeting());

        // 3️⃣ First name
        String firstName = "";

        if (msisdn != null) {
            firstName = customerRepo.findById(msisdn)
                    .map(UssdCustomer::getFirstName)
                    .orElse("");
        }

        text = text.replace("{FIRST_NAME}", firstName);

        // 4️⃣ Optional cleanup (recommended)
        text = text.replaceAll("[ \t]+", " ").trim();

        return text;
    }

}

