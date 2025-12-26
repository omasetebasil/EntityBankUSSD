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

            // Update session current menu based on action result
            session.setCurrentMenu(result.getNextMenu());

            // Load next menu text from DB if available
            String message = menuRepo.findByMenuCode(result.getNextMenu())
                    .map(UssdMenu::getMenuText)
                    .orElse(result.getMessage());

            return new ActionResult(message, result.isEndSession(), result.getNextMenu());
        }

        // 📋 Option-based navigation (menus with numbered options)
        if (input == null) {
            return new ActionResult(menu.getMenuText(), false, menu.getMenuCode());
        }

        // Find next menu based on parent menu + input option
        UssdMenu next = menuRepo
                .findByParentMenuAndOptionValue(menu.getMenuCode(), input)
                .orElseThrow(() -> new RuntimeException(
                        "Next menu not found: parent=" + menu.getMenuCode() + ", input=" + input));

        // If the next menu has an action, execute it immediately
        if (next.getActionBean() != null) {
            MenuAction action = registry.get(next.getActionBean());
            ActionResult result = action.execute(ctx, input);
            session.setCurrentMenu(result.getNextMenu());

            String message = menuRepo.findByMenuCode(result.getNextMenu())
                    .map(UssdMenu::getMenuText)
                    .orElse(result.getMessage());

            return new ActionResult(message, result.isEndSession(), result.getNextMenu());
        }

        // Otherwise, show the menu text
        session.setCurrentMenu(next.getMenuCode());
        boolean endSession = "Y".equals(next.getIsTerminal());

        return new ActionResult(next.getMenuText(), endSession, next.getMenuCode());
    }
}
