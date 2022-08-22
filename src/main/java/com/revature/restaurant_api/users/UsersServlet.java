package com.revature.restaurant_api.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.restaurant_api.users.requests.EditUsersRequest;
import com.revature.restaurant_api.users.requests.NewRegistrationRequest;
import com.revature.restaurant_api.users.response.UsersResponse;
import com.revature.restaurant_api.util.exceptions.InvalidUserInputException;
import com.revature.restaurant_api.util.exceptions.ResourcePersistanceException;
import com.revature.restaurant_api.util.interfaces.Authable;
import com.revature.restaurant_api.util.TokenHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UsersServlet extends HttpServlet implements Authable {

    private final UsersService usersService;
    private final ObjectMapper objectMapper;

    //logger not implemented
    //private final Logger logger = LogManager.getLogger();
    public UsersServlet(UsersService usersService, ObjectMapper objectMapper) {
        this.usersService = usersService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                String sId = req.getParameter("id");
                //UsersModel authUser = TokenHandler.getInstance().getAuthUser((String)req.getSession().getAttribute(("authUser")));

                if (sId == null || sId.isEmpty()) {
                    // handle for the "id" parameter not being set
                    resp.getWriter().write("Invalid User, please log in as a valid user");
                    resp.setStatus(404);
                } else{
                    int id = Integer.parseInt(sId);
                    List<UsersResponse> usersResponseList = usersService.readAll();
                    String payload = objectMapper.writeValueAsString(usersResponseList);

                    resp.getWriter().write(payload);

                }

                //if (id != null){
                //    logger.info("Email entered {}", id);
                //}
            //if (sId != null)
                //UsersResponse usersResponse = usersService.getByID(Integer.parseInt(sId));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("<h1>Welcome to the wonderful world of servlets, in doPost!!! yayyyyyy</h1>");

        PrintWriter respWriter = resp.getWriter();
        NewRegistrationRequest regUser = objectMapper.readValue(req.getInputStream(), NewRegistrationRequest.class);

        try{
            //logger not implemented
            UsersResponse newUser = usersService.registerUsers(regUser);
            String payload = objectMapper.writeValueAsString(newUser);

            respWriter.write(payload);
            resp.setStatus(201);
        } catch(InvalidUserInputException | ResourcePersistanceException e){
            //no logger
            e.printStackTrace();
            respWriter.write(e.getMessage());
            resp.setStatus(404); // 404 not found thrown if user input is invalid
        } catch (Exception e){
            e.printStackTrace();
            respWriter.write(e.getMessage() + " " + e.getClass().getName());
            resp.setStatus(500); //rollover for other exceptions
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        EditUsersRequest editUser = objectMapper.readValue(req.getInputStream(), EditUsersRequest.class);
        if (!checkAuth(req,res)) return;
        int id = editUser.getId();
        String email = editUser.getEmail();
        if (id > 0){
            usersService.remove(id);
            res.getWriter().write("User: " + id + "\n email: " + email + "has been deleted");
        }else {
            res.getWriter().write("Request requires valid email entry");
            res.setStatus(400); // not found status with written response
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        EditUsersRequest editUsersRequest = objectMapper.readValue(req.getInputStream(), EditUsersRequest.class);
        if (!checkAuth(req,res)) return;
        try{
            usersService.update(editUsersRequest);
            res.getWriter().write("User has successfully updated");
        } catch (InvalidUserInputException e){
            res.getWriter().write(e.getMessage());
            res.setStatus(404);
        } catch (Exception e){
            res.getWriter().write(e.getMessage() + " " + e.getClass().getName());
            res.setStatus(500);
        }
    }

}
