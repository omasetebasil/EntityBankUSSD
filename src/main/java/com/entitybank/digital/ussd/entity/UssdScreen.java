package com.entitybank.digital.ussd.entity;

import com.entitybank.digital.ussd.model.InputType;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "USSD_SCREEN")
@Data
public class UssdScreen {
    @Id
    private String screenCode;

    private String screenText;

    @Enumerated(EnumType.STRING)
    private InputType inputType;

    private String storeKey;
    private String actionBean;
    private String requiresAuth;
    private String isTerminal;

    public String getScreenCode() {
        return screenCode;
    }

    public void setScreenCode(String screenCode) {
        this.screenCode = screenCode;
    }

    public String getScreenText() {
        return screenText;
    }

    public void setScreenText(String screenText) {
        this.screenText = screenText;
    }

    public InputType getInputType() {
        return inputType;
    }

    public void setInputType(InputType inputType) {
        this.inputType = inputType;
    }

    public String getStoreKey() {
        return storeKey;
    }

    public void setStoreKey(String storeKey) {
        this.storeKey = storeKey;
    }

    public String getActionBean() {
        return actionBean;
    }

    public void setActionBean(String actionBean) {
        this.actionBean = actionBean;
    }

    public String getRequiresAuth() {
        return requiresAuth;
    }

    public void setRequiresAuth(String requiresAuth) {
        this.requiresAuth = requiresAuth;
    }

    public String getIsTerminal() {
        return isTerminal;
    }

    public void setIsTerminal(String isTerminal) {
        this.isTerminal = isTerminal;
    }
}

