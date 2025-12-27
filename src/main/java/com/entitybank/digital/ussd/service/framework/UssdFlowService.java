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
         * STORE FREE-TEXT INPUT
         * =========================== */
        if (input != null && menu.getStoreKey() != null) {
            session.put(menu.getStoreKey(), input);
        }

        /* ===========================
         * ACTION MENU
         * =========================== */
        /* ===========================
         * ACTION MENU (ONLY ON INPUT)
         * =========================== */
        if (menu.getActionBean() != null && input != null) {

            MenuAction action = registry.get(menu.getActionBean());
            ActionResult result = action.execute(ctx, input);

            session.setCurrentMenu(result.getNextMenu());
            return enrich(result, session);
        }


        /* ===========================
         * FIRST LOAD (NO INPUT)
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

                    if (next.getActionBean() != null && input != null) {
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
                 * FREE TEXT FALLBACK
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

                    if (next.getActionBean() != null && input != null) {
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
                });
    }

    /* ===========================
     * ENRICH ACTION RESULT
     * =========================== */
    private ActionResult enrich(ActionResult result, UssdSession session) {

        // Action already produced text → still resolve placeholders
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
