package com.entitybank.digital.ussd.service.framework;

import com.entitybank.digital.ussd.model.ActionResult;
import com.entitybank.digital.ussd.model.UssdContext;

public interface MenuAction {
    ActionResult execute(UssdContext context, String input);
}

