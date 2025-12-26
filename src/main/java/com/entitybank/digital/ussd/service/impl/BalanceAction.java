package com.entitybank.digital.ussd.service.impl;

import com.entitybank.digital.ussd.model.ActionResult;
import com.entitybank.digital.ussd.model.UssdContext;
import com.entitybank.digital.ussd.service.framework.MenuAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("balanceAction")
public class BalanceAction implements MenuAction {

    @Autowired
    private  CoreBankingService core;

    public ActionResult execute(UssdContext ctx, String input) {
        return new ActionResult(
                "Your balance is " + core.getBalance(ctx.getMsisdn()) + "\n0. Exit",
                false,
                "BAL"
        );
    }
}
