package com.entitybank.digital.ussd.service.framework;

import com.entitybank.digital.ussd.entity.UssdMenu;
import com.entitybank.digital.ussd.entity.UssdSession;
import com.entitybank.digital.ussd.model.ActionResult;
import com.entitybank.digital.ussd.model.UssdContext;
import com.entitybank.digital.ussd.repository.UssdMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UssdFlowService {

    @Autowired
    private UssdMenuRepository menuRepo;

    @Autowired
    private MenuActionRegistry registry;

    public ActionResult process(UssdSession session, String input, UssdContext ctx) {

        // Load current menu
        UssdMenu menu = menuRepo.findByMenuCode(session.getCurrentMenu())
                .orElseThrow(() ->
                        new RuntimeException("Menu not found: " + session.getCurrentMenu()));

        // 🔐 Auth guard (DB-driven)
        if ("Y".equals(menu.getRequiresAuth()) && !session.isAuthenticated()) {
            session.setCurrentMenu("WELCOME");
            return new ActionResult(
                    menuRepo.findByMenuCode("WELCOME").get().getMenuText(),
                    false,
                    "WELCOME"
            );
        }

        // 🧠 Action-driven menu (PIN, balance, transfers, exit)
        if (menu.getActionBean() != null) {

            MenuAction action = registry.get(menu.getActionBean());
            ActionResult result = action.execute(ctx, input);

            session.setCurrentMenu(result.getNextMenu());

            // Load next menu text FROM DB
            String message = menuRepo.findByMenuCode(result.getNextMenu())
                    .map(UssdMenu::getMenuText)
                    .orElse(result.getMessage());

            return new ActionResult(message, result.isEndSession(), result.getNextMenu());
        }

        // 📋 Option-based navigation
        if (input == null) {
            return new ActionResult(menu.getMenuText(), false, menu.getMenuCode());
        }

        UssdMenu next = menuRepo
                .findByParentMenuAndOptionValue(menu.getMenuCode(), input)
                .orElseThrow(() -> new RuntimeException(
                        "Next menu not found: parent=" + menu.getMenuCode() + ", input=" + input));

        session.setCurrentMenu(next.getMenuCode());
        return new ActionResult(next.getMenuText(), false, next.getMenuCode());
    }
}
