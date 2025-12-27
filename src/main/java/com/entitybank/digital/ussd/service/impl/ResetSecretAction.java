package com.entitybank.digital.ussd.service.impl;

import com.entitybank.digital.ussd.model.ActionResult;
import com.entitybank.digital.ussd.model.UssdContext;
import com.entitybank.digital.ussd.service.framework.MenuAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("resetSecretAction")
public class ResetSecretAction implements MenuAction {

    @Autowired
    private AuthService authService;

    @Override
    public ActionResult execute(UssdContext ctx, String input) {

        if (!authService.validateSecretWord(ctx.getMsisdn(), input)) {
            return new ActionResult(
                    "Invalid secret word.\nEnter your secret word",
                    false,
                    "RESET_SECRET"
            );
        }

        return new ActionResult(null, false, "RESET_NEW_PIN");
    }
}

