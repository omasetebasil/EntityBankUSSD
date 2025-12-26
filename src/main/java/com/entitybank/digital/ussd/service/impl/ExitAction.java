package com.entitybank.digital.ussd.service.impl;

import com.entitybank.digital.ussd.model.ActionResult;
import com.entitybank.digital.ussd.model.UssdContext;
import com.entitybank.digital.ussd.service.framework.MenuAction;
import org.springframework.stereotype.Component;

@Component("exitAction")
public class ExitAction implements MenuAction {
    public ActionResult execute(UssdContext ctx, String input) {
        return new ActionResult("Thank you for banking with Entity", true, "EXIT");
    }
}

