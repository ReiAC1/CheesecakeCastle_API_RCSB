package com.revature.restaurant_api.util.dto;

import java.sql.Date;

public class PaymentDTO {
    private double balance;
    private Date exp_date;
    private String ccv;
    private String zipcode;
    private String provider;
    private int userID;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Date getExp_date() {
        return exp_date;
    }

    public void setExp_date(Date exp_date) {
        this.exp_date = exp_date;
    }

    public String getCcv() {
        return ccv;
    }

    public void setCcv(String ccv) {
        this.ccv = ccv;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

}
