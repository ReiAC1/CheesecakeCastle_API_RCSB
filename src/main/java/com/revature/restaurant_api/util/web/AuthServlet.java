package com.revature.restaurant_api.util.web;

import com.fasterxml.jackson.databind.ObjectMapper;
<<<<<<< HEAD
import com.revature.restaurant_api.users.UsersModel;
import com.revature.restaurant_api.users.UsersService;
import com.revature.restaurant_api.util.TokenHandler;
import com.revature.restaurant_api.util.dto.LoginCreds;
=======
import com.revature.restaurant_api.users.UsersService;
import com.revature.restaurant_api.users.UsersModel;
import com.revature.restaurant_api.util.web.DTO.LoginCreds;
>>>>>>> sbox_users_branch

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthServlet extends HttpServlet {
    private final UsersService usersService;
    private final ObjectMapper objectMapper;

<<<<<<< HEAD
    // Dependency Injection
    public AuthServlet(UsersService usersService, ObjectMapper objectMapper){
=======
    public AuthServlet(UsersService usersService, ObjectMapper objectMapper) {
>>>>>>> sbox_users_branch
        this.usersService = usersService;
        this.objectMapper = objectMapper;
    }

<<<<<<< HEAD
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LoginCreds loginCreds = objectMapper.readValue(req.getInputStream(), LoginCreds.class);

        System.out.println(loginCreds.getEmail());
        System.out.println(loginCreds.getPassword());

        UsersModel member = usersService.login(loginCreds.getEmail(), loginCreds.getPassword());

        String payload = objectMapper.writeValueAsString(member);

        HttpSession httpSession = req.getSession(true);
        httpSession.setAttribute("authMember", TokenHandler.getInstance().encodeJSON(payload, member.getId()));

        resp.getWriter().write("Thank you for signing in, " + member.getFirstName() + " " + member.getLastName());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate(); // this is how you kill/delete/logout of the session/cookie
        resp.getWriter().write("Member has successfully logged out");
    }
=======
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

>>>>>>> sbox_users_branch
}
