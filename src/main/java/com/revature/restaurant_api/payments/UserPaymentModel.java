package com.revature.restaurant_api.payments;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "users_payment")
public class UserPaymentModel {
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @JsonIgnore
    private double balance;
    private Date exp_date;
    private String ccv;
    private String zipcode;
    private String provider;

    // todo: change this to an actual Customer type
    private long customerId;

    public UserPaymentModel() { }

    public UserPaymentModel(int id, double balance, Date exp_date, String ccv, String zipcode, String provider, long customerId) {
        this.id = id;
        this.balance = balance;
        this.exp_date = exp_date;
        this.ccv = ccv;
        this.zipcode = zipcode;
        this.provider = provider;
        this.customerId = customerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
}
