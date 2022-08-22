package com.revature.restaurant_api.orders;

import com.revature.restaurant_api.payments.UserPaymentModel;
import com.revature.restaurant_api.users.UsersModel;

import java.sql.Date;
import java.util.List;

public class OrderService {

    private OrdersDao ordersDao;

    public OrderService(OrdersDao ordersDao) {
        this.ordersDao = ordersDao;
    }

    public OrderModel create(double amount, String address, String zip, UsersModel user, UserPaymentModel payment) {

        OrderModel model = new OrderModel();
        model.setAmount(amount);
        model.setAddress(address);
        model.setZip(zip);
        model.setUser(user);
        model.setPayment(payment);

        model.setDate(new Date(System.currentTimeMillis()));

        if (!validateOrder(model))
            return null;

        model = ordersDao.create(model);

        return model;
    }

    public boolean update(OrderModel model) {
        if (!validateOrder(model))
            return false;

        return ordersDao.update(model);
    }

    public boolean delete(OrderModel model) {
        return ordersDao.delete(model);
    }

    public OrderModel getByID(int id) {
        return ordersDao.getByID(id);
    }

    public List<OrderModel> getAllByUserID(int userId) {
        return ordersDao.getAllByUserID(userId);
    }

    private boolean validateOrder(OrderModel model) {
        if (model.getAmount() <= 0) return false;
        if (model.getAddress() == null || model.getAddress().isEmpty()) return false;
        if (model.getZip() == null || model.getZip().isEmpty()) return false;
        if (model.getUser() == null) return false;
        if (model.getPayment() == null) return false;
        if (model.getDate() == null) return false;

        return true;
    }
}
