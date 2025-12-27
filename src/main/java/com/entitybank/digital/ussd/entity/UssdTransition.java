package com.entitybank.digital.ussd.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USSD_TRANSITION")
@Data
public class UssdTransition {
    @Id
    private Long id;

    private String fromScreen;
    private String inputValue;
    private String toScreen;
    private int priority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromScreen() {
        return fromScreen;
    }

    public void setFromScreen(String fromScreen) {
        this.fromScreen = fromScreen;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public String getToScreen() {
        return toScreen;
    }

    public void setToScreen(String toScreen) {
        this.toScreen = toScreen;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
