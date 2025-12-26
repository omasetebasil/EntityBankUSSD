package com.entitybank.digital.ussd.service.impl;

import com.entitybank.digital.ussd.model.ActionResult;
import com.entitybank.digital.ussd.model.UssdContext;
import com.entitybank.digital.ussd.service.framework.MenuAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("sendMoneyPostAction")
public class SendMoneyPostAction implements MenuAction {

    @Autowired
    private SendMoneyService service;

    @Override
    public ActionResult execute(UssdContext ctx, String input) {

        if (!"1".equals(input)) {
            return new ActionResult("Transaction cancelled", true, "EXIT");
        }

        service.post(
                ctx.getMsisdn(),
                ctx.getSession().getRecipient(),
                ctx.getSession().getAmount()
        );

        return new ActionResult("Transaction successful\n0. Exit", false, "SEND_SUCCESS");
    }
}


