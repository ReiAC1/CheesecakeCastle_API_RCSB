package com.revature.restaurant_api.orderdetails;

import com.revature.restaurant_api.menu.MenuService;
import com.revature.restaurant_api.orders.OrderService;

import java.util.List;

public class OrderDetailsService {
    private OrderDetailsDao orderDetailsDao;
    private OrderService orderService;
    private MenuService menuService;

    public OrderDetailsService(OrderDetailsDao orderDetailsDao, OrderService orderService, MenuService menuService) {
        this.orderDetailsDao = orderDetailsDao;
        this.orderService = orderService;
        this.menuService = menuService;
    }

    public OrderDetailsModel create(int quantity, String comments, int dishID, int orderID) {

        OrderDetailsModel model = new OrderDetailsModel();
        model.setQuantity(quantity);
        model.setComments(comments);
        model.setItem(menuService.findById(dishID));
        model.setOrder(orderService.getByID(orderID));

        if (!validateOrderDetails(model))
            return null;

        if (model.getOrder().getPayment().getBalance() < quantity * model.getItem().getCost())
            return null;

        model = orderDetailsDao.create(model);
        orderService.update(model.getOrder());

        return model;
    }

    public boolean update(OrderDetailsModel model) {
        if (!validateOrderDetails(model))
            return false;

        boolean ret = orderDetailsDao.update(model);
        if (!ret)
            return false;

        orderService.update(model.getOrder());
        return true;
    }

    public boolean delete(OrderDetailsModel model) {
        return orderDetailsDao.delete(model);
    }

    public OrderDetailsModel getByID(int id) {
        return orderDetailsDao.getByID(id);
    }

    public List<OrderDetailsModel> getAllByOrderID(int userId) {
        return orderDetailsDao.getAllByOrderID(userId);
    }

    private boolean validateOrderDetails(OrderDetailsModel model) {
        if (model.getQuantity() <= 0) return false;
        if (model.getOrder() == null) return false;
        if (model.getItem() == null) return false;

        return true;
    }
}
