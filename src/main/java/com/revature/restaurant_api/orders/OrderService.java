package com.revature.restaurant_api.orders;

import com.revature.restaurant_api.orderdetails.OrderDetailsModel;
import com.revature.restaurant_api.orderdetails.OrderDetailsService;
import com.revature.restaurant_api.payments.UserPaymentModel;
import com.revature.restaurant_api.payments.UserPaymentService;
import com.revature.restaurant_api.users.UsersModel;

import java.sql.Date;
import java.util.List;

public class OrderService {

    private OrdersDao ordersDao;
    private UserPaymentService paymentService;
    private OrderDetailsService orderDetailsService;

    public OrderService(OrdersDao ordersDao, UserPaymentService paymentService) {
        this.ordersDao = ordersDao;
        this.paymentService = paymentService;
    }

    public void setOrderDetailsService(OrderDetailsService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
    }

    public OrderModel create(double amount, String address, String zip, UsersModel user, UserPaymentModel payment) {

        OrderModel model = new OrderModel();
        model.setAmount(amount);
        model.setAddress(address);
        model.setZip(zip);
        model.setUser(user);
        model.setPayment(payment);

        model.setDate(new Date(System.currentTimeMillis()));

        model = ordersDao.create(model);

        return model;
    }

    public boolean update(OrderModel model) {
        if (!validateOrder(model))
            return false;

        double newValue = 0;
        double difference = 0;

        List<OrderDetailsModel> orderDetailsModelList = orderDetailsService.getAllByOrderID(model.getId());

        for (OrderDetailsModel om : orderDetailsModelList) {
            newValue += om.getItem().getCost() * om.getQuantity();
        }
        difference = newValue - model.getAmount();

        model.setAmount(newValue);
        model.getPayment().setBalance(model.getPayment().getBalance() - difference);

        paymentService.update(model.getPayment());

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
        if (model.getAmount() < 0 || model.getAmount() > model.getPayment().getBalance()) return false;
        if (model.getAddress() == null || model.getAddress().isEmpty()) return false;
        if (model.getZip() == null || model.getZip().isEmpty()) return false;
        if (model.getUser() == null) return false;
        if (model.getPayment() == null) return false;
        if (model.getDate() == null) return false;

        return true;
    }
}
