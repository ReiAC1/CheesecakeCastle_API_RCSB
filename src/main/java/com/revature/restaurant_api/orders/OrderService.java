package com.revature.restaurant_api.orders;

public class OrderService {

    private OrdersDao ordersDao;

    public OrderService(OrdersDao ordersDao) {
        this.ordersDao = ordersDao;
    }

    public OrderModel create() {
        OrderModel model = new OrderModel();

        return null;
    }
}
