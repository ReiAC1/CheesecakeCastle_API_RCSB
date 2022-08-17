package com.revature.restaurant_api.payments;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.revature.restaurant_api.users.UsersModel;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "users_payment")
public class UserPaymentModel {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    @JsonIgnore
    private double balance;
    private Date exp_date;
    private String ccv;
    private String zipcode;
    private String provider;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersModel userModel;

    public UserPaymentModel() { }

    public UserPaymentModel(int id, double balance, Date exp_date, String ccv, String zipcode, String provider, UsersModel model) {
        this.id = id;
        this.balance = balance;
        this.exp_date = exp_date;
        this.ccv = ccv;
        this.zipcode = zipcode;
        this.provider = provider;
        this.userModel = model;
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

    public UsersModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UsersModel userModel) {
        this.userModel = userModel;
    }
}
