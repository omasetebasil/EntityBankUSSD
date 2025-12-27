package com.entitybank.digital.ussd.service.impl;

import com.entitybank.digital.ussd.entity.UssdScreen;
import com.entitybank.digital.ussd.entity.UssdSession;
import com.entitybank.digital.ussd.model.ActionResult;
import com.entitybank.digital.ussd.model.UssdContext;
import com.entitybank.digital.ussd.repository.UssdScreenRepository;
import com.entitybank.digital.ussd.repository.UssdTransitionRepository;
import com.entitybank.digital.ussd.service.framework.InputValidator;
import com.entitybank.digital.ussd.service.framework.MenuAction;
import com.entitybank.digital.ussd.service.framework.MenuActionRegistry;
import com.entitybank.digital.ussd.util.MenuTextResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UssdStateMachineService {

    @Autowired
    private UssdScreenRepository screenRepo;
    @Autowired private UssdTransitionRepository transitionRepo;
    @Autowired private MenuActionRegistry registry;
    @Autowired private MenuTextResolver resolver;

    public ActionResult process(UssdSession session, String input, UssdContext ctx) {

        UssdScreen screen = screenRepo.findById(session.getCurrentMenu())
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        /* AUTH GUARD */
        if ("Y".equals(screen.getRequiresAuth()) && !session.isAuthenticated()) {
            session.setCurrentMenu("WELCOME");
            return render("WELCOME", session);
        }

        /* INPUT VALIDATION */
        InputValidator.validate(screen, input);

        /* STORE INPUT */
        if (input != null && screen.getStoreKey() != null) {
            session.put(screen.getStoreKey(), input);
        }

        /* ACTION */
        if (screen.getActionBean() != null) {
            MenuAction action = registry.get(screen.getActionBean());
            ActionResult result = action.execute(ctx, input);

            session.setCurrentMenu(result.getNextMenu());

            // 🚨 DO NOT RENDER action-only screens
            if (result.getMessage() == null && screen.getScreenText() == null) {
                throw new IllegalStateException(
                        "Action screen " + screen.getScreenCode() +
                                " returned null message"
                );
            }

            return enrich(result, session);
        }


        /* TRANSITION */
        String next = resolveNext(screen.getScreenCode(), input);
        session.setCurrentMenu(next);

// 🔥 ENTRY ACTION SUPPORT
        UssdScreen nextScreen = screenRepo.findById(next)
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        if (nextScreen.getActionBean() != null) {
            MenuAction action = registry.get(nextScreen.getActionBean());
            ActionResult result = action.execute(ctx, null);
            session.setCurrentMenu(result.getNextMenu());
            return enrich(result, session);
        }

        return render(next, session);

    }

    private String resolveNext(String screen, String input) {

        return transitionRepo.findByFromScreenOrderByPriorityDesc(screen)
                .stream()
                .filter(t ->
                        "*".equals(t.getInputValue())
                                || t.getInputValue().equals(input))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("No transition from " + screen))
                .getToScreen();
    }

    private ActionResult render(String screenCode, UssdSession session) {

        UssdScreen s = screenRepo.findById(screenCode)
                .orElseThrow(() -> new RuntimeException(
                        "Screen not found: " + screenCode
                ));

        return new ActionResult(
                resolver.resolve(s.getScreenText(), session.getMsisdn()),
                "Y".equals(s.getIsTerminal()),
                screenCode
        );
    }

    private ActionResult enrich(ActionResult r, UssdSession session) {
        if (r.getMessage() != null) {
            return new ActionResult(
                    resolver.resolve(r.getMessage(), session.getMsisdn()),
                    r.isEndSession(),
                    r.getNextMenu()
            );
        }
        return render(r.getNextMenu(), session);
    }
}

