package com.entitybank.digital.ussd.service.framework;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MenuActionRegistry {

    private final Map<String, MenuAction> actions;

    public MenuActionRegistry(Map<String, MenuAction> actions) {
        this.actions = actions;
    }

    public MenuAction get(String name) {
        return actions.get(name);
    }
}

