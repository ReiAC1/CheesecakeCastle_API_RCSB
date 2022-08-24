package com.revature.restaurant_api.util.dto;

import com.revature.restaurant_api.orders.OrderModel;

import java.sql.Date;

public class OrderDTO {
    private int id;
    private Date date;
    private String address;
    private String zip;
    private int userId;
    private int paymentId;

    public OrderDTO() { }

    public OrderDTO(OrderModel model) {
        id = model.getId();
        date = model.getDate();
        address = model.getAddress();
        zip = model.getZip();
        userId = model.getUser().getId();
        paymentId = model.getPayment().getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }
}
