package com.entitybank.digital.ussd.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USSD_MENU")
@Data
public class UssdMenu {
    @Id
    private Long id;
    private String menuCode;
    private String menuText;
    private String parentMenu;
    private String optionValue;
    private String nextMenu;
    private String isTerminal;
    private String requiresAuth;
    private String actionBean;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuText() {
        return menuText;
    }

    public void setMenuText(String menuText) {
        this.menuText = menuText;
    }

    public String getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(String parentMenu) {
        this.parentMenu = parentMenu;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public String getNextMenu() {
        return nextMenu;
    }

    public void setNextMenu(String nextMenu) {
        this.nextMenu = nextMenu;
    }

    public String getIsTerminal() {
        return isTerminal;
    }

    public void setIsTerminal(String isTerminal) {
        this.isTerminal = isTerminal;
    }

    public String getRequiresAuth() {
        return requiresAuth;
    }

    public void setRequiresAuth(String requiresAuth) {
        this.requiresAuth = requiresAuth;
    }

    public String getActionBean() {
        return actionBean;
    }

    public void setActionBean(String actionBean) {
        this.actionBean = actionBean;
    }
}

