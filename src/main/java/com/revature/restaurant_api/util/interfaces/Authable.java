package com.revature.restaurant_api.util.interfaces;


import com.revature.restaurant_api.users.UsersModel;
import com.revature.restaurant_api.util.TokenHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public interface Authable {

    // default allows for implementation at the interface
    default boolean checkAuth(int userID, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // the login token should be stored in our session
        String loginToken = (String)req.getSession().getAttribute("authMember");

        // if no login session is found, give an unauthorized status
        if (loginToken == null) {
            resp.setStatus(401);
            return false;
        }

        // try to get the UsersModel associated with the header
        UsersModel uModel = TokenHandler.getInstance().getAuthUser(loginToken);

        if (uModel == null) {
            resp.setStatus(401);
            return false;
        }

        if (uModel.getId() != userID && !uModel.getAdmin()) {
            resp.setStatus(401);
            return false;
        }

        return true;
    }

    // here's another version if you had a hypothetical admin user
    default boolean checkAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String loginToken = (String)req.getSession().getAttribute("authMember");

        // if no login session is found, give an unauthorized status
        if (loginToken == null) {
            resp.setStatus(401);
            return false;
        }

        // try to get the UsersModel associated with the header
        UsersModel uModel = TokenHandler.getInstance().getAuthUser(loginToken);

        if (uModel == null) {
            resp.setStatus(401);
            return false;
        }
        return uModel.getAdmin();
    }
}
