package com.revature.restaurant_api.util.dto;

import com.revature.restaurant_api.menu.MenuItem;
import com.revature.restaurant_api.orderdetails.OrderDetailsModel;
import com.revature.restaurant_api.orders.OrderModel;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class OrderDetailsDTO {
    private int id;
    private int quantity;
    private String comments;
    private int itemId;
    private int orderId;

    public OrderDetailsDTO() { }

    public OrderDetailsDTO(OrderDetailsModel model) {
        id = model.getId();
        quantity = model.getQuantity();
        comments = model.getComments();
        itemId = model.getItem().getDishId();
        orderId = model.getOrder().getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
