package com.revature.restaurant_api.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.restaurant_api.users.dto.interfaces.Authable;
import com.revature.restaurant_api.users.response.UsersResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, IOException {
                int id = req.getParameter("id"); //what datatype is ok here? am I pull json, so strings are fine?
                UsersModel authUser = (UsersModel) req.getSession().getAttribute(("authUsers"));

                //if (id != null){
                //    logger.info("Email entered {}", id);
                //}
            if (id != null)
                try {
                    UsersResponse usersResponse = usersService.getByID(id);
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
