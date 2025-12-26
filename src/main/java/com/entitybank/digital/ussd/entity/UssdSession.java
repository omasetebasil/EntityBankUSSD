package com.entitybank.digital.ussd.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UssdSession implements Serializable {
    private String sessionId;
    private String msisdn;
    private String currentMenu;
    private boolean authenticated;
    private Long loginTime; // epoch millis


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(String currentMenu) {
        this.currentMenu = currentMenu;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public UssdSession() {
    }

    public UssdSession(String sessionId, String msisdn, String currentMenu, boolean authenticated, Long loginTime) {
        this.sessionId = sessionId;
        this.msisdn = msisdn;
        this.currentMenu = currentMenu;
        this.authenticated = authenticated;
        this.loginTime = loginTime;
    }
}

