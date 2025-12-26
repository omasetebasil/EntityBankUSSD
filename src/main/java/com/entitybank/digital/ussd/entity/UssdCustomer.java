package com.entitybank.digital.ussd.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USSD_CUSTOMER")
@Data
public class UssdCustomer {
    @Id
    private String msisdn;
    private String pinHash;
    private int pinRetries;
    private String locked;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getPinHash() {
        return pinHash;
    }

    public void setPinHash(String pinHash) {
        this.pinHash = pinHash;
    }

    public int getPinRetries() {
        return pinRetries;
    }

    public void setPinRetries(int pinRetries) {
        this.pinRetries = pinRetries;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }
}

