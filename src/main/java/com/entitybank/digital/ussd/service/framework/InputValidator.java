package com.entitybank.digital.ussd.service.framework;

import com.entitybank.digital.ussd.entity.UssdScreen;
import com.entitybank.digital.ussd.model.InputType;

public class InputValidator {

    public static void validate(UssdScreen screen, String input) {

        if (screen.getInputType() == null || input == null) {
            return;
        }

        InputType type = screen.getInputType();

        switch (type) {

            case NUMBER:
                if (!input.matches("\\d+")) {
                    throw new InputValidationException("Invalid number");
                }
                break;

            case PIN:
                if (!input.matches("\\d{4}")) {
                    throw new InputValidationException(
                            "Invalid PIN. Please enter 4 digits"
                    );
                }
                break;

            case CONFIRM:
                if (!("1".equals(input) || "0".equals(input))) {
                    throw new InputValidationException("Invalid option");
                }
                break;

            case OPTION:
                if (!input.matches("\\d+")) {
                    throw new InputValidationException("Invalid selection");
                }
                break;

            case TEXT:
            case NONE:
                // no validation
                break;
        }
    }
}
