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
                .orElseThrow(() -> new RuntimeException("Menu not found: " + session.getCurrentMenu()));

        if (menu.getActionBean() != null) {
            MenuAction action = registry.get(menu.getActionBean());
            ActionResult res = action.execute(ctx, input);
            session.setCurrentMenu(res.getNextMenu());
            return res;
        }

        UssdMenu next = menuRepo
                .findByParentMenuAndOptionValue(menu.getMenuCode(), input)
                .orElseThrow(() -> new RuntimeException(
                        "Next menu not found for parent=" + menu.getMenuCode() + ", option=" + input));

        session.setCurrentMenu(next.getMenuCode());
        return new ActionResult(next.getMenuText(), false, next.getMenuCode());
    }
}

