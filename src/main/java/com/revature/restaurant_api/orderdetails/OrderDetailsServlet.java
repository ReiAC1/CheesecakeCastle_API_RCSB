package com.revature.restaurant_api.orderdetails;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.restaurant_api.orders.OrderModel;
import com.revature.restaurant_api.orders.OrderService;
import com.revature.restaurant_api.users.UsersModel;
import com.revature.restaurant_api.util.TokenHandler;
import com.revature.restaurant_api.util.dto.OrderDTO;
import com.revature.restaurant_api.util.dto.OrderDetailsDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailsServlet extends HttpServlet {

    private OrderDetailsService orderDetailsService;
    private OrderService orderService;
    private ObjectMapper objectMapper;

    public OrderDetailsServlet(OrderDetailsService orderDetailsService, OrderService orderService, ObjectMapper objectMapper) {
        this.orderDetailsService = orderDetailsService;
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // check for null action, if it is, it's a bad request
        if (req.getParameter("action") == null) {
            resp.setStatus(400);
            return;
        }

        // the login token should be stored in our session
        String loginToken = (String)req.getSession().getAttribute("authMember");

        // if no login session is found, give an unauthorized status
        if (loginToken == null) {
            resp.setStatus(401);
            return;
        }

        UsersModel uModel = TokenHandler.getInstance().getAuthUser(loginToken);

        // if we have no UsersModel, or the TokenHandler cannot validate token, give unauthorized error
        if (uModel == null) {
            resp.setStatus(401);
            return;
        }

        String action = req.getParameter("action");

        // check to see if we have a valid action
        // if we don't, set a 400 error, as it's a bad request
        if (action.isEmpty()) {
            resp.setStatus(400);
            return;
        }

        switch (action) {
            case "getOrder":
                if (req.getParameter("id") == null) {
                    resp.setStatus(400);
                    return;
                }

                int id = Integer.parseInt(req.getParameter("id"));
                try {
                    OrderDetailsModel model = orderDetailsService.getByID(id);//changed
                    if (model == null) {
                        resp.setStatus(400);
                        return;
                    }

                    resp.getWriter().println(objectMapper.writeValueAsString(model));
                } catch (JsonProcessingException e) {
                    e.printStackTrace(resp.getWriter());
                }
                break;
            case "getAll":
                if (req.getParameter("id") == null) {
                    resp.setStatus(400);
                    return;
                }

                int userId = Integer.parseInt(req.getParameter("id"));
                List<OrderDetailsModel> orders = orderDetailsService.getAllByOrderID(userId);
                ArrayList<OrderDetailsDTO> dtos = new ArrayList<>();

                for (OrderDetailsModel model : orders) {
                    dtos.add(new OrderDetailsDTO(model));
                }

                resp.setStatus(200);

                if (orders == null) {
                    resp.getWriter().println("[]");
                    return;
                }

                String payload = objectMapper.writeValueAsString(dtos);
                resp.getWriter().println(payload);
                break;
            default: // default option is that the request was invalid, so we can throw a 400 error
                resp.setStatus(400);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // the login token should be stored in our session
        String loginToken = (String)req.getSession().getAttribute("authMember");

        // if no login session is found, give an unauthorized status
        if (loginToken == null) {
            resp.getWriter().println("Unauthorized user");
            resp.setStatus(401);
            return;
        }

        // try to get the UsersModel associated with the header
        UsersModel uModel = TokenHandler.getInstance().getAuthUser(loginToken);

        // if we have no UsersModel, or the TokenHandler cannot validate token, give unauthorized error
        if (uModel == null) {
            resp.getWriter().println("Unauthorized user");
            resp.setStatus(401);
            return;
        }

        // Retrieve the PaymentDTO
        // in this case, payment ID will be ignored since we are creating a value
        OrderDetailsDTO orderDTO = objectMapper.readValue(req.getInputStream(), OrderDetailsDTO.class);
        OrderModel order = orderService.getByID(orderDTO.getOrderId());

        if (order == null) {
            resp.getWriter().println("Invalid order");
            resp.setStatus(403);
        }

        // ensure we only create orders for ourselves or if they're an admin
        if (order.getUser().getId() != uModel.getId() && !uModel.getAdmin()) {
            resp.getWriter().println("Invalid user account");
            resp.setStatus(401);
            return;
        }

        try {
            OrderDetailsModel orderModel = orderDetailsService.create(orderDTO.getQuantity(), orderDTO.getComments(),
                    orderDTO.getItemId(), orderDTO.getOrderId());

            if (orderModel == null) {
                resp.getWriter().println("Invalid input. Ensure you have enough money");
                resp.setStatus(400);
                return;
            }

            OrderDetailsDTO dto = new OrderDetailsDTO(orderModel);

            String payload = objectMapper.writeValueAsString(dto);
            resp.setStatus(201);
            resp.getWriter().println(payload);

        } catch (Exception e) {
            e.printStackTrace(resp.getWriter());
            resp.setStatus(500);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // the login token should be stored in our session
        String loginToken = (String)req.getSession().getAttribute("authMember");

        // if no login session is found, give an unauthorized status
        if (loginToken == null) {
            resp.getWriter().println("Unauthorized user");
            resp.setStatus(401);
            return;
        }

        // try to get the UsersModel associated with the header
        UsersModel uModel = TokenHandler.getInstance().getAuthUser(loginToken);

        // if we have no UsersModel, or the TokenHandler cannot validate token, give unauthorized error
        if (uModel == null) {
            resp.getWriter().println("Unauthorized user");
            resp.setStatus(401);
            return;
        }

        try {
            // Retrieve the PaymentDTO
            // in this case, payment ID will be ignored since we are creating a value
            OrderDetailsDTO orderDTO = objectMapper.readValue(req.getInputStream(), OrderDetailsDTO.class);
            OrderModel order = orderService.getByID(orderDTO.getOrderId());

            if (order == null) {
                resp.getWriter().println("Invalid order");
                resp.setStatus(403);
            }

            // ensure we only create orders for ourselves or if they're an admin
            if (order.getUser().getId() != uModel.getId() && !uModel.getAdmin()) {
                resp.getWriter().println("Invalid user account");
                resp.setStatus(401);
                return;
            }

            OrderDetailsModel model = orderDetailsService.getByID(orderDTO.getId());

            if (orderDTO.getComments() != null) {
                model.setComments(orderDTO.getComments());
            }

            if (orderDTO.getQuantity() > 0) {
                model.setQuantity(orderDTO.getQuantity());
            }

            if (orderDTO.getOrderId() > 0) {
                model.setOrder(orderService.getByID(orderDTO.getOrderId()));
            }

            String payload = objectMapper.writeValueAsString(orderDetailsService.update(model));

            resp.setStatus(200);
            resp.getWriter().println(payload);
        } catch (Exception e) {
            e.printStackTrace(resp.getWriter());
            resp.setStatus(500);
        }
    }
}
