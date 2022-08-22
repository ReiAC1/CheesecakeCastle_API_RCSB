package com.revature.restaurant_api.orders;

import com.revature.restaurant_api.payments.UserPaymentModel;
import com.revature.restaurant_api.users.UsersModel;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "orders")
public class OrderModel {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private double amount;
    private Date date;
    private String address;
    private String zip;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private UsersModel user;
    @ManyToOne
    @JoinColumn(name = "payment_id")
    private UserPaymentModel payment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public UsersModel getUser() {
        return user;
    }

    public void setUser(UsersModel user) {
        this.user = user;
    }

    public UserPaymentModel getPayment() {
        return payment;
    }

    public void setPayment(UserPaymentModel payment) {
        this.payment = payment;
    }
}
