package com.entitybank.digital.ussd.service.impl;

import com.entitybank.digital.ussd.model.ActionResult;
import com.entitybank.digital.ussd.model.UssdContext;
import com.entitybank.digital.ussd.service.framework.MenuAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("balanceAction")
public class BalanceAction implements MenuAction {

    @Autowired
    private CoreBankingService core;

    @Override
    public ActionResult execute(UssdContext ctx, String input) {
        // If user presses 0 → exit
        if ("0".equals(input)) {
            return new ActionResult("Thank you for banking with Entity", true, "EXIT");
        }

        // Show balance
        String balanceMessage = "Your balance is " + core.getBalance(ctx.getMsisdn());
        String message = balanceMessage + "\n0. Exit";

        // Next menu remains BAL so user can press 0 to exit next
        return new ActionResult(message, false, "BAL");
    }
}


