package com.revature.restaurant_api.orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.restaurant_api.payments.UserPaymentModel;
import com.revature.restaurant_api.users.UsersModel;
import com.revature.restaurant_api.util.TokenHandler;
import com.revature.restaurant_api.util.dto.OrderDTO;
import com.revature.restaurant_api.util.dto.PaymentDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrdersServlet  extends HttpServlet {

    private OrderService orderService;
    private ObjectMapper objectMapper;

    public OrdersServlet (OrderService orderService, ObjectMapper objectMapper) {
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
        resp.getWriter().write("<h1>Welcome to the wonderful world of servlets, in doPost!!! yayyyyyy</h1>");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doOptions(req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doHead(req, resp);
    }
}
