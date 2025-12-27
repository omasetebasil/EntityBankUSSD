package com.entitybank.digital.ussd.controller;

import com.entitybank.digital.ussd.entity.UssdSession;
import com.entitybank.digital.ussd.model.ActionResult;
import com.entitybank.digital.ussd.model.UssdContext;
import com.entitybank.digital.ussd.service.framework.SessionService;
import com.entitybank.digital.ussd.service.impl.UssdStateMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UssdController {

    @Autowired
    private  SessionService sessionService;

    @Autowired
    private UssdStateMachineService flow;
    @PostMapping("/ussd")
    public String ussd(
            @RequestParam String sessionId,
            @RequestParam String phoneNumber,
            @RequestParam(required = false) String text) {

        // Reset session on fresh dial (no input)
        UssdSession session = sessionService.getOrCreate(sessionId, phoneNumber);

     //  ONLY reset if session is NEW.Can only be null n new session
        if (text == null || text.trim().isEmpty()) {
            session.setAuthenticated(false);
            session.setCurrentMenu("WELCOME");
            session.setLoginTime(null);
        }



        String input = null;

        if (text != null && !text.isEmpty()) {
            String cleanedText = text.endsWith("*")
                    ? text.substring(0, text.length() - 1)
                    : text;

            int index = cleanedText.lastIndexOf("*");
            input = (index == -1)
                    ? cleanedText
                    : cleanedText.substring(index + 1);
        }

        ActionResult res = flow.process(session, input,
                new UssdContext(session, phoneNumber, sessionId));

        if (res.isEndSession()) {
            sessionService.delete(sessionId);
            return "END " + res.getMessage();
        }

        sessionService.save(session);
        return "CON " + res.getMessage();
    }
}

