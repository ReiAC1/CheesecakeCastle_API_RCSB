package com.revature.restaurant_api.orderdetails;

import com.revature.restaurant_api.menu.MenuItem;
import com.revature.restaurant_api.orders.OrderModel;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class OrderDetailsModel {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private int quantity;
    private String comments;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private MenuItem item;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderModel order;

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

    public MenuItem getItem() {
        return item;
    }

    public void setItem(MenuItem item) {
        this.item = item;
    }

    public OrderModel getOrder() {
        return order;
    }

    public void setOrder(OrderModel order) {
        this.order = order;
    }
}
