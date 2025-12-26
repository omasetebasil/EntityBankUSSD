package com.entitybank.digital.ussd.service.impl;

import com.entitybank.digital.ussd.model.ActionResult;
import com.entitybank.digital.ussd.model.UssdContext;
import com.entitybank.digital.ussd.repository.UssdMenuRepository;
import com.entitybank.digital.ussd.service.framework.MenuAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("authAction")
public class AuthAction implements MenuAction {

    @Autowired
    private AuthService authService;

    @Autowired
    private UssdMenuRepository menuRepo;

    @Override
    public ActionResult execute(UssdContext ctx, String input) {

        // First load
        if (input == null) {

            boolean exists = authService.customerExists(ctx.getMsisdn());
            if (!exists) {
                return new ActionResult(null, false, "NON_CUSTOMER");
            }

            return new ActionResult(null, false, "WELCOME");
        }

        boolean valid = authService.validatePin(ctx.getMsisdn(), input);

        if (valid) {
            ctx.getSession().setAuthenticated(true);
            ctx.getSession().setLoginTime(System.currentTimeMillis());

            return new ActionResult(null, false, "MAIN");
        }

        return new ActionResult(
                "Invalid PIN. Please try again.\nEnter PIN\n(Forgot PIN reply with 1)",
                false,
                "WELCOME"
        );
    }

}
