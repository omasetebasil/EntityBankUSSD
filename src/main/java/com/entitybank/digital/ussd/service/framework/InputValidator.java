package com.entitybank.digital.ussd.service.framework;

import com.entitybank.digital.ussd.entity.UssdMenu;

public class InputValidator {

    public static void validate(UssdMenu menu, String input) {

        if (menu.getInputType() == null || input == null) return;

        switch (menu.getInputType()) {

            case "NUMBER":
                if (!input.matches("\\d+"))
                    throw new InputValidationException("Invalid number");
                break;

            case "PIN":
                if (!input.matches("\\d{4}"))
                    throw new InputValidationException("Invalid PIN. Please enter 4 digits");
                break;

            case "CONFIRM":
                if (!("1".equals(input) || "0".equals(input)))
                    throw new InputValidationException("Invalid option");
                break;

            case "OPTION":
                if (!input.matches("\\d+"))
                    throw new InputValidationException("Invalid selection");
                break;
        }
    }
}