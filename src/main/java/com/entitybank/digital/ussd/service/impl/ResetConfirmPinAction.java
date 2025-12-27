package com.entitybank.digital.ussd.service.impl;

import com.entitybank.digital.ussd.model.ActionResult;
import com.entitybank.digital.ussd.model.UssdContext;
import com.entitybank.digital.ussd.service.framework.MenuAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("resetConfirmPinAction")
public class ResetConfirmPinAction implements MenuAction {

    @Autowired
    private AuthService authService;

    @Override
    public ActionResult execute(UssdContext ctx, String input) {

        /* ===========================
         * FIRST LOAD — SHOW PROMPT
         * =========================== */
        if (input == null) {
            return new ActionResult(
                    null,              // let engine load menu text
                    false,
                    "RESET_CONFIRM_PIN"
            );
        }

        String newPin = ctx.getSession().get("new_pin");

        /* ===========================
         * PIN MISMATCH
         * =========================== */
        if (!input.equals(newPin)) {

            // Clear wrong PIN so user must re-enter
            ctx.getSession().getAttributes().remove("new_pin");

            return new ActionResult(
                    "PINs do not match.\nEnter new PIN",
                    false,
                    "RESET_NEW_PIN"
            );
        }

        /* ===========================
         * SUCCESS — UPDATE PIN
         * =========================== */
        authService.updatePin(ctx.getMsisdn(), newPin);

        // Cleanup session
        ctx.getSession().getAttributes().clear();
        ctx.getSession().setAuthenticated(false);

        return new ActionResult(
                "PIN reset successful.\nPlease login with your new PIN.",
                false,
                "WELCOME"
        );
    }
}

