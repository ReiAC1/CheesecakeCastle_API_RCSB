package com.revature.restaurant_api.util.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.restaurant_api.users.UsersService;
import com.revature.restaurant_api.users.UsersModel;
import com.revature.restaurant_api.util.web.DTO.LoginCreds;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthServlet extends HttpServlet {
    private final UsersService usersService;
    private final ObjectMapper objectMapper;

    public AuthServlet(UsersService usersService, ObjectMapper objectMapper) {
        this.usersService = usersService;
        this.objectMapper = objectMapper;
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        LoginCreds loginCredentials = objectMapper.readValue(req.getInputStream(), LoginCreds.class);
        //creates a sessionUser's credentials to authenticate them when a post attempt is made
        UsersModel sessionUser = usersService.login(loginCredentials.getEmail(), loginCredentials.getPassword());

        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("authUser", sessionUser);

        res.getWriter().write("Welcome back, " + sessionUser.getFirstName() + " " + sessionUser.getLastName());

    }
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        req.getSession().invalidate();
        res.getWriter().write("User has been logged out");
    }

}
