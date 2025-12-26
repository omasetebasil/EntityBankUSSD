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

        UssdMenu menu = menuRepo.findByMenuCode(session.getCurrentMenu())
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        // Auth guard
        if ("Y".equals(menu.getRequiresAuth()) && !session.isAuthenticated()) {
            session.setCurrentMenu("WELCOME");
            return new ActionResult(
                    menuRepo.findByMenuCode("WELCOME")
                            .map(UssdMenu::getMenuText)
                            .orElse("Welcome"),
                    false,
                    "WELCOME"
            );
        }

        // ✅ SAFE INPUT VALIDATION (STEP 3)
        try {
            InputValidator.validate(menu, input);
        } catch (InputValidationException e) {
            // Stay on same menu and show friendly error
            return new ActionResult(
                    e.getMessage() + "\n" + menu.getMenuText(),
                    false,
                    menu.getMenuCode()
            );
        }

        // Store free-text input generically
        if (input != null && menu.getStoreKey() != null) {
            session.put(menu.getStoreKey(), input);
        }

        // ACTION MENU
        if (menu.getActionBean() != null) {
            MenuAction action = registry.get(menu.getActionBean());
            ActionResult result = action.execute(ctx, input);
            session.setCurrentMenu(result.getNextMenu());
            return enrich(result);
        }

        // First load
        if (input == null) {
            return new ActionResult(menu.getMenuText(), false, menu.getMenuCode());
        }

        // OPTION lookup
        return menuRepo
                .findByParentMenuAndOptionValue(menu.getMenuCode(), input)
                .map(next -> {
                    session.setCurrentMenu(next.getMenuCode());

                    if (next.getActionBean() != null) {
                        MenuAction action = registry.get(next.getActionBean());
                        ActionResult result = action.execute(ctx, input);
                        session.setCurrentMenu(result.getNextMenu());
                        return enrich(result);
                    }

                    return new ActionResult(
                            next.getMenuText(),
                            "Y".equals(next.getIsTerminal()),
                            next.getMenuCode()
                    );
                })
                // ✅ FREE TEXT FALLBACK (AUTO-EXECUTE ACTION)
                .orElseGet(() -> {

                    if (menu.getNextMenu() == null) {
                        throw new RuntimeException("Next menu not defined for " + menu.getMenuCode());
                    }

                    UssdMenu next = menuRepo.findByMenuCode(menu.getNextMenu())
                            .orElseThrow(() -> new RuntimeException(
                                    "Next menu not found: " + menu.getNextMenu()));

                    session.setCurrentMenu(next.getMenuCode());

                    if (next.getActionBean() != null) {
                        MenuAction action = registry.get(next.getActionBean());
                        ActionResult result = action.execute(ctx, input);
                        session.setCurrentMenu(result.getNextMenu());
                        return enrich(result);
                    }

                    return new ActionResult(
                            next.getMenuText(),
                            "Y".equals(next.getIsTerminal()),
                            next.getMenuCode()
                    );
                });
    }

    private ActionResult enrich(ActionResult result) {
        if (result.getMessage() != null) {
            return result;
        }

        String msg = menuRepo.findByMenuCode(result.getNextMenu())
                .map(UssdMenu::getMenuText)
                .orElse("");

        return new ActionResult(msg, result.isEndSession(), result.getNextMenu());
    }
}
