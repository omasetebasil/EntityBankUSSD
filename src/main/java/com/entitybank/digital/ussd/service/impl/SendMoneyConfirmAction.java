package com.entitybank.digital.ussd.service.impl;

import com.entitybank.digital.ussd.model.ActionResult;
import com.entitybank.digital.ussd.model.UssdContext;
import com.entitybank.digital.ussd.service.framework.MenuAction;
import org.springframework.stereotype.Component;

@Component("sendMoneyConfirmAction")
public class SendMoneyConfirmAction implements MenuAction {

    @Override
    public ActionResult execute(UssdContext ctx, String input) {

        String msg =
                "Confirm transaction\n" +
                        "Send KES " + ctx.getSession().getAmount() +
                        " to " + ctx.getSession().getRecipient() +
                        "\n1. Confirm\n0. Cancel";

        // NEXT_MENU should be SEND_POST
        return new ActionResult(msg, false, "SEND_POST");
    }
}



