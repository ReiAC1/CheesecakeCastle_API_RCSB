package com.revature.restaurant_api;

import com.revature.restaurant_api.util.ServletContext;

// MainDriver - Basic entry point for our restaurant API
// creates and runs a ServletContext object
public class MainDriver {
    public static void main(String[] args) {
        ServletContext servletContext = new ServletContext();
        servletContext.run();
    }
}