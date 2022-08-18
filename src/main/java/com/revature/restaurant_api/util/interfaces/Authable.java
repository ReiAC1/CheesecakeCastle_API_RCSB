package com.revature.restaurant_api.util.interfaces;


import com.revature.restaurant_api.users.UsersModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public interface Authable {

    // default allows for implementation at the interface
    default boolean checkAuth(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession httpSession = req.getSession();
        if(httpSession.getAttribute("authUsers") == null){
            resp.getWriter().write("Unauthorized request - not logged in as a registered member");
            resp.setStatus(401);
            return false;
        }
        return true;
    }

    // here's another version if you had a hypothetical admin user
    default boolean checkAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession httpSession = req.getSession();
        UsersModel user = (UsersModel) httpSession.getAttribute("authUsers");
        if(user.getFirstName() == null){
            resp.getWriter().write("Unauthorized request - not logged in as an admin");
            resp.setStatus(401);
            return false;
        }
        return true;
    }
}
