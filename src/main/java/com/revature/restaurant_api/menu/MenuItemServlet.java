package com.revature.restaurant_api.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.restaurant_api.users.UsersModel;
import com.revature.restaurant_api.users.requests.EditMenuItemRequest;
import com.revature.restaurant_api.users.requests.EditUsersRequest;
import com.revature.restaurant_api.users.requests.NewMenuItemRequest;
import com.revature.restaurant_api.users.response.MenuItemResponse;
import com.revature.restaurant_api.util.TokenHandler;
import com.revature.restaurant_api.util.exceptions.InvalidUserInputException;
import com.revature.restaurant_api.util.exceptions.ResourcePersistanceException;
import com.revature.restaurant_api.util.interfaces.Authable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class MenuItemServlet extends HttpServlet implements Authable {

    private final MenuService menuService;
    private final ObjectMapper objectMapper;

    public MenuItemServlet(MenuService menuService, ObjectMapper objectMapper) {
        this.menuService = menuService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, IOException {
        String id = req.getParameter("id");
        String loginToken = (String) req.getSession().getAttribute("authMember");
        if (loginToken == null) {
            resp.setStatus(401);
            return;
        }

        UsersModel usersModel = TokenHandler.getInstance().getAuthUser(loginToken);
        if (usersModel == null) {
            resp.setStatus(401);
        }

        if (id != null) {
            //logger implementation
            try {
                MenuItemResponse menuitem = menuService.findById(Integer.valueOf(id));
                String payloadId = objectMapper.writeValueAsString(menuitem);
                resp.getWriter().write(payloadId);
            } catch (InvalidUserInputException e) {
                //logger
                resp.getWriter().write("Id entered is not a valid menu item. Please Try again.");
                resp.setStatus(404);
            }
        } else {
            List<MenuItemResponse> menuItem = menuService.readAll();
            String payload = objectMapper.writeValueAsString(menuItem);
            resp.getWriter().write(payload);
        }
        String auth = (String) req.getParameter("auth");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter respWriter = resp.getWriter();
        NewMenuItemRequest menuItemRequest = objectMapper.readValue(req.getInputStream(), NewMenuItemRequest.class);
        if (!checkAdmin(req, resp)) {
            resp.setStatus(403);
            return;
        }
        try {
            //logger recom
            MenuItemResponse newMenuItem = menuService.createMenuItem(menuItemRequest);
            String payload = objectMapper.writeValueAsString(newMenuItem);
            respWriter.write(payload);
            resp.setStatus(201);
        } catch (InvalidUserInputException | ResourcePersistanceException e) {
            respWriter.write(e.getMessage());
            resp.setStatus(404);
        } catch (Exception e) {
            respWriter.write(e.getMessage() + " " + e.getClass().getName());
            resp.setStatus(500); //catch all exception for any missed condition checks or unexpected behavior
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EditMenuItemRequest editMenuItemRequest = objectMapper.readValue(req.getInputStream(), EditMenuItemRequest.class);
        if (!checkAdmin(req, resp)) {
            resp.setStatus(403);
            return;
        }
        try {
            menuService.update(editMenuItemRequest);
            resp.getWriter().write("Menu Item Succesfully Updated");
        } catch (InvalidUserInputException e) {
            resp.getWriter().write(e.getMessage());
            resp.setStatus(404);
        } catch (Exception e) {
            resp.getWriter().write(e.getMessage() + " " + e.getClass().getName());
            resp.setStatus(500);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EditMenuItemRequest editMenuItemRequest = objectMapper.readValue(req.getInputStream(), EditMenuItemRequest.class);
        if (!checkAdmin(req, resp)) {
            resp.setStatus(403);
            return;
        }
        if (editMenuItemRequest == null) {
            resp.setStatus(400);
            return;
        }
        //object
        int id = editMenuItemRequest.getDishId();
        if (id > 0) {
            menuService.remove(id);
            resp.getWriter().write("True");
            //
        } else {
            resp.getWriter().write("id < 0");
            resp.setStatus(400);
        }
    }
}
