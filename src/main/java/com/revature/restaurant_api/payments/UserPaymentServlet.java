package com.revature.restaurant_api.payments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.restaurant_api.users.UsersModel;
import com.revature.restaurant_api.users.UsersService;
import com.revature.restaurant_api.users.response.UsersResponse;
import com.revature.restaurant_api.util.TokenHandler;
import com.revature.restaurant_api.util.TokenHeader;
import com.revature.restaurant_api.util.dto.PaymentDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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
            case "getPayment":
                if (req.getParameter("id") == null) {
                    resp.setStatus(400);
                    return;
                }

                int id = Integer.parseInt(req.getParameter("id"));
                try {
                    UserPaymentModel model = userPaymentService.getByID(id);//changed
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
                List<PaymentDTO> payloadDTOs = new ArrayList<>();

                for (UserPaymentModel payment : payments) {
                    PaymentDTO dto = new PaymentDTO();
                    dto.setId(payment.getId());
                    dto.setUserID(payment.getUserModel().getId());
                    dto.setBalance(payment.getBalance());
                    dto.setCcv(payment.getCcv());
                    dto.setProvider(payment.getProvider());
                    dto.setZipcode(payment.getZipcode());
                    dto.setExp_date(payment.getExp_date());

                    payloadDTOs.add(dto);
                }

                String payload = objectMapper.writeValueAsString(payloadDTOs);
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

        // try to get the UsersModel associated with the header
        UsersModel uModel = TokenHandler.getInstance().getAuthUser(loginToken);

        // if we have no UsersModel, or the TokenHandler cannot validate token, give unauthorized error
        if (uModel == null) {
            resp.setStatus(401);
            return;
        }

        // Retrieve the PaymentDTO
        // in this case, payment ID will be ignored since we are creating a value
        PaymentDTO paymentInfo = objectMapper.readValue(req.getInputStream(), PaymentDTO.class);

        try {
            UserPaymentModel paymentModel = userPaymentService.create(paymentInfo.getBalance(), paymentInfo.getExp_date(),
                    paymentInfo.getCcv(), paymentInfo.getZipcode(), paymentInfo.getProvider(),
                    usersService.getByID(paymentInfo.getUserID()));

            PaymentDTO dto = new PaymentDTO();
            dto.setId(paymentModel.getId());
            dto.setUserID(paymentModel.getUserModel().getId());
            dto.setBalance(paymentModel.getBalance());
            dto.setCcv(paymentModel.getCcv());
            dto.setProvider(paymentModel.getProvider());
            dto.setZipcode(paymentModel.getZipcode());
            dto.setExp_date(paymentModel.getExp_date());

            String payload = objectMapper.writeValueAsString(dto);
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

        UsersModel uModel = TokenHandler.getInstance().getAuthUser(loginToken);

        // if we have no UsersModel, or the TokenHandler cannot validate token, give unauthorized error
        if (uModel == null) {
            resp.setStatus(401);
            return;
        }

        PaymentDTO paymentInfo = objectMapper.readValue(req.getInputStream(), PaymentDTO.class);
        UserPaymentModel pModel = new UserPaymentModel();
        pModel.setBalance(paymentInfo.getBalance());
        pModel.setUserModel(usersService.getByID(paymentInfo.getUserID())); //added to user service to return id
        pModel.setExp_date(paymentInfo.getExp_date());
        pModel.setCcv(paymentInfo.getCcv());
        pModel.setZipcode(paymentInfo.getZipcode());
        pModel.setProvider(paymentInfo.getProvider());
        pModel.setId(paymentInfo.getId());

        try {
            boolean success = userPaymentService.delete(pModel);

            String payload = objectMapper.writeValueAsString(success);
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
            resp.setStatus(401);
            return;
        }

        // try to get the UsersModel associated with the header
        UsersModel uModel = TokenHandler.getInstance().getAuthUser(loginToken);

        // if we have no UsersModel, or the TokenHandler cannot validate token, give unauthorized error
        if (uModel == null) {
            resp.setStatus(401);
            return;
        }

        PaymentDTO paymentInfo = objectMapper.readValue(req.getInputStream(), PaymentDTO.class);
        UserPaymentModel pModel = new UserPaymentModel();
        pModel.setBalance(paymentInfo.getBalance());
        pModel.setUserModel(usersService.getByID(paymentInfo.getUserID())); //added
        pModel.setExp_date(paymentInfo.getExp_date());
        pModel.setCcv(paymentInfo.getCcv());
        pModel.setZipcode(paymentInfo.getZipcode());
        pModel.setProvider(paymentInfo.getProvider());
        pModel.setId(paymentInfo.getId());

        try {
            boolean success = userPaymentService.update(pModel);

            String payload = objectMapper.writeValueAsString(success);
            resp.getWriter().println(payload);

        } catch (Exception e) {
            e.printStackTrace(resp.getWriter());
            resp.setStatus(500);
        }
    }

}
