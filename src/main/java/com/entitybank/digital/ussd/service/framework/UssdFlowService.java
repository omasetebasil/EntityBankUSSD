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
                .orElseThrow(() ->
                        new RuntimeException("Menu not found: " + session.getCurrentMenu()));

        // 🔐 Auth guard
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

        // 📝 Persist free-text input
        if (input != null) {
            if ("SEND_MONEY".equals(menu.getMenuCode())) {
                session.setRecipient(input);
            } else if ("SEND_AMOUNT".equals(menu.getMenuCode())) {
                session.setAmount(input);
            }
        }

        // 🧠 ACTION MENU (current menu has action)
        if (menu.getActionBean() != null) {
            MenuAction action = registry.get(menu.getActionBean());
            ActionResult result = action.execute(ctx, input);

            session.setCurrentMenu(result.getNextMenu());

            String message = result.getMessage();
            if (message == null) {
                message = menuRepo.findByMenuCode(result.getNextMenu())
                        .map(UssdMenu::getMenuText)
                        .orElse("");
            }

            return new ActionResult(message, result.isEndSession(), result.getNextMenu());
        }

        // 📌 First load
        if (input == null) {
            return new ActionResult(menu.getMenuText(), false, menu.getMenuCode());
        }

        // 🔹 OPTION MENU
        return menuRepo
                .findByParentMenuAndOptionValue(menu.getMenuCode(), input)
                .map(next -> {

                    session.setCurrentMenu(next.getMenuCode());

                    // 🔥 If next menu has action, EXECUTE IT IMMEDIATELY
                    if (next.getActionBean() != null) {
                        MenuAction action = registry.get(next.getActionBean());
                        ActionResult result = action.execute(ctx, input);

                        session.setCurrentMenu(result.getNextMenu());

                        String message = result.getMessage();
                        if (message == null) {
                            message = menuRepo.findByMenuCode(result.getNextMenu())
                                    .map(UssdMenu::getMenuText)
                                    .orElse("");
                        }

                        return new ActionResult(
                                message,
                                result.isEndSession(),
                                result.getNextMenu()
                        );
                    }

                    boolean end = "Y".equals(next.getIsTerminal());
                    return new ActionResult(next.getMenuText(), end, next.getMenuCode());
                })
                // 🔹 FREE-TEXT FALLBACK (FIXED)
                .orElseGet(() -> {

                    if (menu.getNextMenu() == null) {
                        throw new RuntimeException(
                                "Next menu not found: parent=" +
                                        menu.getMenuCode() + ", input=" + input);
                    }

                    UssdMenu nextMenu = menuRepo.findByMenuCode(menu.getNextMenu())
                            .orElseThrow(() ->
                                    new RuntimeException("Menu not found: " + menu.getNextMenu()));

                    session.setCurrentMenu(nextMenu.getMenuCode());

                    // 🔥 EXECUTE ACTION IF PRESENT
                    if (nextMenu.getActionBean() != null) {
                        MenuAction action = registry.get(nextMenu.getActionBean());
                        ActionResult result = action.execute(ctx, input);

                        session.setCurrentMenu(result.getNextMenu());

                        String message = result.getMessage();
                        if (message == null) {
                            message = menuRepo.findByMenuCode(result.getNextMenu())
                                    .map(UssdMenu::getMenuText)
                                    .orElse("");
                        }

                        return new ActionResult(
                                message,
                                result.isEndSession(),
                                result.getNextMenu()
                        );
                    }

                    // Otherwise just show menu text
                    return new ActionResult(
                            nextMenu.getMenuText(),
                            "Y".equals(nextMenu.getIsTerminal()),
                            nextMenu.getMenuCode()
                    );
                });
    }
}
