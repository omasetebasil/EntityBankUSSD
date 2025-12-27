package com.entitybank.digital.ussd.service.framework;

import com.entitybank.digital.ussd.entity.UssdMenu;
import com.entitybank.digital.ussd.entity.UssdSession;
import com.entitybank.digital.ussd.model.ActionResult;
import com.entitybank.digital.ussd.model.UssdContext;
import com.entitybank.digital.ussd.repository.UssdMenuRepository;
import com.entitybank.digital.ussd.util.MenuTextResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UssdFlowService {

    @Autowired
    private UssdMenuRepository menuRepo;

    @Autowired
    private MenuActionRegistry registry;

    @Autowired
    private MenuTextResolver resolver;

    public ActionResult process(UssdSession session, String input, UssdContext ctx) {

        UssdMenu menu = menuRepo.findByMenuCode(session.getCurrentMenu())
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        /* ===========================
         * AUTH GUARD
         * =========================== */
        if ("Y".equals(menu.getRequiresAuth()) && !session.isAuthenticated()) {

            session.setCurrentMenu("WELCOME");

            String welcomeText = menuRepo.findByMenuCode("WELCOME")
                    .map(UssdMenu::getMenuText)
                    .orElse("Welcome");

            return new ActionResult(
                    resolver.resolve(welcomeText, session.getMsisdn()),
                    false,
                    "WELCOME"
            );
        }

        /* ===========================
         * INPUT VALIDATION
         * =========================== */
        try {
            InputValidator.validate(menu, input);
        } catch (InputValidationException e) {
            return new ActionResult(
                    e.getMessage() + "\n" +
                            resolver.resolve(menu.getMenuText(), session.getMsisdn()),
                    false,
                    menu.getMenuCode()
            );
        }

        /* ===========================
         * STORE INPUT
         * =========================== */
        if (input != null && menu.getStoreKey() != null) {
            session.put(menu.getStoreKey(), input);
        }

        /* ===========================
         * ACTION EXECUTION
         * =========================== */
        if (menu.getActionBean() != null && shouldExecuteAction(menu, input)) {

            MenuAction action = registry.get(menu.getActionBean());
            ActionResult result = action.execute(ctx, input);

            session.setCurrentMenu(result.getNextMenu());
            return enrich(result, session);
        }

        /* ===========================
         * FIRST LOAD
         * =========================== */
        if (input == null) {
            return new ActionResult(
                    resolver.resolve(menu.getMenuText(), session.getMsisdn()),
                    false,
                    menu.getMenuCode()
            );
        }

        /* ===========================
         * OPTION LOOKUP
         * =========================== */
        return menuRepo
                .findByParentMenuAndOptionValue(menu.getMenuCode(), input)
                .map(next -> {

                    session.setCurrentMenu(next.getMenuCode());

                    if (next.getActionBean() != null &&
                            shouldExecuteAction(next, input)) {

                        MenuAction action = registry.get(next.getActionBean());
                        ActionResult result = action.execute(ctx, input);

                        session.setCurrentMenu(result.getNextMenu());
                        return enrich(result, session);
                    }

                    return new ActionResult(
                            resolver.resolve(next.getMenuText(), session.getMsisdn()),
                            "Y".equals(next.getIsTerminal()),
                            next.getMenuCode()
                    );
                })

                /* ===========================
                 * FREE TEXT FALLBACK (NAVIGATION)
                 * =========================== */
                .orElseGet(() -> {

                    if (menu.getNextMenu() == null) {
                        throw new RuntimeException(
                                "Next menu not defined for " + menu.getMenuCode());
                    }

                    UssdMenu next = menuRepo.findByMenuCode(menu.getNextMenu())
                            .orElseThrow(() ->
                                    new RuntimeException("Next menu not found: " + menu.getNextMenu()));

                    session.setCurrentMenu(next.getMenuCode());

                    // IMPORTANT: execute action ONLY if allowed
                    if (next.getActionBean() != null &&
                            shouldExecuteAction(next, null)) {

                        MenuAction action = registry.get(next.getActionBean());
                        ActionResult result = action.execute(ctx, null);

                        session.setCurrentMenu(result.getNextMenu());
                        return enrich(result, session);
                    }

                    return new ActionResult(
                            resolver.resolve(next.getMenuText(), session.getMsisdn()),
                            "Y".equals(next.getIsTerminal()),
                            next.getMenuCode()
                    );
                });
    }

    /* ===========================
     * ACTION EXECUTION RULE
     * =========================== */
    private boolean shouldExecuteAction(UssdMenu menu, String input) {

        // Case 1: user provided input
        if (input != null) {
            return true;
        }

        // Case 2: display-only CONFIRM (Send Money)
        return "CONFIRM".equals(menu.getInputType())
                && menu.getStoreKey() == null;
    }

    /* ===========================
     * ENRICH RESULT
     * =========================== */
    private ActionResult enrich(ActionResult result, UssdSession session) {

        if (result.getMessage() != null) {
            return new ActionResult(
                    resolver.resolve(result.getMessage(), session.getMsisdn()),
                    result.isEndSession(),
                    result.getNextMenu()
            );
        }

        String msg = menuRepo.findByMenuCode(result.getNextMenu())
                .map(UssdMenu::getMenuText)
                .orElse("");

        return new ActionResult(
                resolver.resolve(msg, session.getMsisdn()),
                result.isEndSession(),
                result.getNextMenu()
        );
    }
}
