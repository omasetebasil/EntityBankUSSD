package com.entitybank.digital.ussd.service.impl;

import com.entitybank.digital.ussd.model.ActionResult;
import com.entitybank.digital.ussd.model.UssdContext;
import com.entitybank.digital.ussd.repository.UssdMenuRepository;
import com.entitybank.digital.ussd.service.framework.MenuAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.entitybank.digital.ussd.entity.UssdMenu;

import java.time.Instant;

@Component("authAction")
public class AuthAction implements MenuAction {

    @Autowired
    private AuthService authService;

    @Autowired
    private UssdMenuRepository menuRepo;

    @Override
    public ActionResult execute(UssdContext ctx, String input) {

        // First load → show WELCOME text from DB
        if (input == null) {
            return new ActionResult(null, false, "WELCOME");
        }

        boolean valid = authService.validatePin(ctx.getMsisdn(), input);
       // valid=true;
        if (valid) {
            ctx.getSession().setAuthenticated(true);
            ctx.getSession().setLoginTime(System.currentTimeMillis());

            // DB-driven NEXT_MENU (safe)
            String next = menuRepo.findByMenuCode("WELCOME")
                    .map(UssdMenu::getNextMenu)
                    .filter(n -> n != null && !n.isEmpty())
                    .orElse("MAIN");

            return new ActionResult(null, false, next);
        }

        // Invalid PIN → stay on WELCOME
        return new ActionResult(null, false, "WELCOME");
    }
}


