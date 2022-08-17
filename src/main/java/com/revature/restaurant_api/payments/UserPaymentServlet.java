package com.revature.restaurant_api.payments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.restaurant_api.users.UsersModel;
import com.revature.restaurant_api.users.UsersService;
import com.revature.restaurant_api.util.TokenHandler;
import com.revature.restaurant_api.util.TokenHeader;
import com.revature.restaurant_api.util.dto.PaymentDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserPaymentServlet extends HttpServlet {
    private UserPaymentService userPaymentService;
    private UsersService usersService;
    private ObjectMapper objectMapper;

    public UserPaymentServlet(UserPaymentService userPaymentService, UsersService usersService, ObjectMapper objectMapper) {
        this.userPaymentService = userPaymentService;
        this.usersService = usersService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, IOException {
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

        // get the header of our token
        String headerS = loginToken.split("\\.")[0];
        TokenHeader header = objectMapper.readValue(headerS, TokenHeader.class);

        // try to get the UsersModel associated with the header
        UsersModel uModel = usersService.getByID(header.id);

        // if we have no UsersModel, or the TokenHandler cannot validate token, give unauthorized error
        if (uModel == null || !TokenHandler.getInstance().isTokenValid(loginToken, objectMapper.writeValueAsString(uModel))) {
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
            case "getPayment":
                if (req.getParameter("id") == null) {
                    resp.setStatus(400);
                    return;
                }

                int id = Integer.parseInt(req.getParameter("id"));
                try {
                    UserPaymentModel model = userPaymentService.getByID(id);
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
                List<UserPaymentModel> payments = userPaymentService.getAllByUserID(userId);
                String payload = objectMapper.writeValueAsString(payments);
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
            resp.setStatus(401);
            return;
        }

        // get the header of our token
        String headerS = loginToken.split("\\.")[0];
        TokenHeader header = objectMapper.readValue(headerS, TokenHeader.class);

        // try to get the UsersModel associated with the header
        UsersModel uModel = usersService.getByID(header.id);

        // if we have no UsersModel, or the TokenHandler cannot validate token, give unauthorized error
        if (uModel == null || !TokenHandler.getInstance().isTokenValid(loginToken, objectMapper.writeValueAsString(uModel))) {
            resp.setStatus(401);
            return;
        }

        PaymentDTO paymentInfo = objectMapper.readValue(req.getInputStream(), PaymentDTO.class);

        try {
            UserPaymentModel paymentModel = userPaymentService.create(paymentInfo.getBalance(), paymentInfo.getExp_date(),
                    paymentInfo.getCcv(), paymentInfo.getZipcode(), paymentInfo.getProvider(),
                    usersService.getByID(paymentInfo.getUserID()));

            String payload = objectMapper.writeValueAsString(paymentModel);
            resp.getWriter().println(payload);

        } catch (Exception e) {
            e.printStackTrace(resp.getWriter());
            resp.setStatus(500);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // the login token should be stored in our session
        String loginToken = (String)req.getSession().getAttribute("authMember");

        // if no login session is found, give an unauthorized status
        if (loginToken == null) {
            resp.setStatus(401);
            return;
        }

        // get the header of our token
        String headerS = loginToken.split("\\.")[0];
        TokenHeader header = objectMapper.readValue(headerS, TokenHeader.class);

        // try to get the UsersModel associated with the header
        UsersModel uModel = usersService.getByID(header.id);

        // if we have no UsersModel, or the TokenHandler cannot validate token, give unauthorized error
        if (uModel == null || !TokenHandler.getInstance().isTokenValid(loginToken, objectMapper.writeValueAsString(uModel))) {
            resp.setStatus(401);
            return;
        }

        // TODO: add logic for delete
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // the login token should be stored in our session
        String loginToken = (String)req.getSession().getAttribute("authMember");

        // if no login session is found, give an unauthorized status
        if (loginToken == null) {
            resp.setStatus(401);
            return;
        }

        // get the header of our token
        String headerS = loginToken.split("\\.")[0];
        TokenHeader header = objectMapper.readValue(headerS, TokenHeader.class);

        // try to get the UsersModel associated with the header
        UsersModel uModel = usersService.getByID(header.id);

        // if we have no UsersModel, or the TokenHandler cannot validate token, give unauthorized error
        if (uModel == null || !TokenHandler.getInstance().isTokenValid(loginToken, objectMapper.writeValueAsString(uModel))) {
            resp.setStatus(401);
            return;
        }

        // Todo: add logic for update
    }

}
