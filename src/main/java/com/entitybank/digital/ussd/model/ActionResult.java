package com.entitybank.digital.ussd.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActionResult {
    private String message;
    private boolean endSession;
    private String nextMenu;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isEndSession() {
        return endSession;
    }

    public void setEndSession(boolean endSession) {
        this.endSession = endSession;
    }

    public String getNextMenu() {
        return nextMenu;
    }

    public void setNextMenu(String nextMenu) {
        this.nextMenu = nextMenu;
    }

    public ActionResult(String message, boolean endSession, String nextMenu) {
        this.message = message;
        this.endSession = endSession;
        this.nextMenu = nextMenu;
    }
}

