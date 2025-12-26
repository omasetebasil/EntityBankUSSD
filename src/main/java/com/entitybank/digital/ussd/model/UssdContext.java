package com.entitybank.digital.ussd.model;

import com.entitybank.digital.ussd.entity.UssdSession;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UssdContext {
    private UssdSession session;
    private String msisdn;
    private String sessionId;

    public UssdSession getSession() {
        return session;
    }

    public void setSession(UssdSession session) {
        this.session = session;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public UssdContext(UssdSession session, String msisdn, String sessionId) {
        this.session = session;
        this.msisdn = msisdn;
        this.sessionId = sessionId;
    }
}

