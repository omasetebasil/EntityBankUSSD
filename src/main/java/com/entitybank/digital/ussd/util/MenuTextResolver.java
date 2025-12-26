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

        /* ===========================
         * SERVER TIME GREETING
         * =========================== */
        text = text.replace("{GREETING}", GreetingUtil.greeting());

        /* ===========================
         * CUSTOMER FIRST NAME
         * =========================== */
        if (msisdn != null) {
            Optional<UssdCustomer> customer =
                    customerRepo.findById(msisdn);

            if (customer.isPresent() && customer.get().getFirstName() != null) {
                text = text.replace(
                        "{FIRST_NAME}",
                        customer.get().getFirstName()
                );
            }
        }

        return text;
    }
}
