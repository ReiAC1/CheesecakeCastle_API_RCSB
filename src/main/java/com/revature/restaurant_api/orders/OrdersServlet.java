package com.revature.restaurant_api.payments.orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.restaurant_api.payments.UserPaymentService;
import com.revature.restaurant_api.users.UsersModel;
import com.revature.restaurant_api.users.UsersService;
import com.revature.restaurant_api.util.TokenHandler;
import com.revature.restaurant_api.util.dto.OrderDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrdersServlet  extends HttpServlet {

    private OrderService orderService;
    private UsersService usersService;
    private UserPaymentService userPaymentService;
    private ObjectMapper objectMapper;

    public OrdersServlet (OrderService orderService, UsersService usersService, UserPaymentService userPaymentService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
        this.usersService = usersService;
        this.userPaymentService = userPaymentService;
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
                    OrderModel model = orderService.getByID(id);//changed
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
                List<OrderModel> orders = orderService.getAllByUserID(userId);
                ArrayList<OrderDTO> dtos = new ArrayList<>();

                for (OrderModel model : orders) {
                    dtos.add(new OrderDTO(model));
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
        OrderDTO orderDTO = objectMapper.readValue(req.getInputStream(), OrderDTO.class);

        // ensure we only create orders for ourselves or if they're an admin
        if (orderDTO.getUserId() != uModel.getId() && !uModel.getAdmin()) {
            resp.getWriter().println("Invalid user account");
            resp.setStatus(401);
            return;
        }

        if (userPaymentService.getByID(orderDTO.getPaymentId()).getUserModel().getId() != uModel.getId()) {
            resp.getWriter().println("Invalid payment method");
            resp.setStatus(401);
            return;
        }

        try {
            OrderModel orderModel = orderService.create(0, orderDTO.getAddress(),
                    orderDTO.getZip(), usersService.getByID(orderDTO.getUserId()),
                    userPaymentService.getByID(orderDTO.getPaymentId()));

            if (orderModel == null) {
                resp.getWriter().println("Payment declined");
                resp.setStatus(402);
                return;
            }

            OrderDTO dto = new OrderDTO(orderModel);

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
            OrderDTO orderDTO = objectMapper.readValue(req.getInputStream(), OrderDTO.class);

            // ensure we only create orders for ourselves or if they're an admin
            if (orderDTO.getUserId() != uModel.getId() && !uModel.getAdmin()) {
                resp.getWriter().println("Invalid user account");
                resp.setStatus(401);
                return;
            }

            OrderModel model = orderService.getByID(orderDTO.getId());

            if (orderDTO.getAddress() != null) {
                model.setAddress(orderDTO.getAddress());
            }

            if (orderDTO.getZip() != null) {
                model.setZip(orderDTO.getZip());
            }

            if (orderDTO.getPaymentId() > 0) {
                model.setPayment(userPaymentService.getByID(orderDTO.getPaymentId()));
            }

            if (model.getUser().getId() != model.getPayment().getUserModel().getId()) {
                resp.getWriter().println("Invalid payment method");
                resp.setStatus(401);
                return;
            }

            String payload = objectMapper.writeValueAsString(orderService.update(model));

            resp.setStatus(200);
            resp.getWriter().println(payload);
        } catch (Exception e) {
            e.printStackTrace(resp.getWriter());
            resp.setStatus(500);
        }
    }
}
