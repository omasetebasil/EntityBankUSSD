package com.entitybank.digital.ussd.service.impl;

import com.entitybank.digital.ussd.model.ActionResult;
import com.entitybank.digital.ussd.model.UssdContext;
import com.entitybank.digital.ussd.service.framework.MenuAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("authAction")
public class AuthAction implements MenuAction {

    @Autowired
    private AuthService authService;

    @Override
    public ActionResult execute(UssdContext ctx, String input) {

        /* ===========================
         * FIRST LOAD (no input yet)
         * =========================== */
        if (input == null) {

            boolean exists = authService.customerExists(ctx.getMsisdn());
            if (!exists) {
                return new ActionResult(null, false, "NON_CUSTOMER");
            }

            return new ActionResult(null, false, "WELCOME");
        }

        /* ===========================
         * FORGOT PIN
         * =========================== */
        if ("1".equals(input)) {
            return new ActionResult(null, false, "RESET_SECRET");
        }

        /* ===========================
         * PIN FORMAT VALIDATION
         * (business rule, NOT engine)
         * =========================== */
        if (!input.matches("\\d{4}")) {
            return new ActionResult(
                    "Invalid PIN. Please enter 4 digits\nEnter PIN\n1. Forgot PIN",
                    false,
                    "WELCOME"
            );
        }

        /* ===========================
         * PIN AUTHENTICATION
         * =========================== */
        boolean valid = authService.validatePin(ctx.getMsisdn(), input);

        if (valid) {
            ctx.getSession().setAuthenticated(true);
            ctx.getSession().setLoginTime(System.currentTimeMillis());
            return new ActionResult(null, false, "MAIN");
        }

        /* ===========================
         * WRONG PIN
         * =========================== */
        return new ActionResult(
                "Invalid PIN. Please try again.\nEnter PIN\n1. Forgot PIN",
                false,
                "WELCOME"
        );
    }
}
