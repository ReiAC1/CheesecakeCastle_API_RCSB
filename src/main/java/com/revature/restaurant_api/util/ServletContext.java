package com.revature.restaurant_api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.restaurant_api.menu.MenuItem;
import com.revature.restaurant_api.menu.MenuItemDao;
import com.revature.restaurant_api.menu.MenuService;
import com.revature.restaurant_api.orderdetails.OrderDetailsDao;
import com.revature.restaurant_api.orderdetails.OrderDetailsModel;
import com.revature.restaurant_api.orderdetails.OrderDetailsService;
import com.revature.restaurant_api.orders.OrderModel;
import com.revature.restaurant_api.orders.OrderService;
import com.revature.restaurant_api.orders.OrdersDao;
import com.revature.restaurant_api.orders.OrdersServlet;
import com.revature.restaurant_api.payments.UserPaymentDao;
import com.revature.restaurant_api.payments.UserPaymentModel;
import com.revature.restaurant_api.payments.UserPaymentService;
import com.revature.restaurant_api.payments.UserPaymentServlet;
import com.revature.restaurant_api.users.UsersDao;
import com.revature.restaurant_api.users.UsersModel;
import com.revature.restaurant_api.users.UsersService;
import com.revature.restaurant_api.users.UsersServlet;
import com.revature.restaurant_api.util.web.AuthServlet;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.servlet.ServletException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

// ServletContext - Sets up Servlets associated with the restaurant web api
public class ServletContext {

    SessionFactory sessionFactory;

    private void setupHibernate() throws IOException {
        Configuration conf = new Configuration();
        Properties properties = new Properties();

        // Searching the thread for the file specified and streaming it into the properties.load()
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        properties.load(loader.getResourceAsStream("cfg.properties"));
        //properties.load(new FileReader("src/main/resources/cfg.properties"));

        conf.addProperties(properties);

        // add our annotated classes (aka the classes that should be persisted in our database)
        conf.addAnnotatedClass(UsersModel.class);
        conf.addAnnotatedClass(MenuItem.class);
        conf.addAnnotatedClass(UserPaymentModel.class);
        conf.addAnnotatedClass(OrderModel.class);
        conf.addAnnotatedClass(OrderDetailsModel.class);

        // and finally build the session factory
        sessionFactory = conf.buildSessionFactory();
    }

    public void run() {

        String webappDirLocation = new File("src/main/webapp/").getAbsolutePath();
        String additonalClasses = new File("target/classes").getAbsolutePath();

        Tomcat tomcat = new Tomcat();

        try {
            setupHibernate();
            StandardContext standardContext = (StandardContext) tomcat.addWebapp("", webappDirLocation);
            WebResourceRoot resourceRoot = new StandardRoot(standardContext);
            resourceRoot.addPreResources(new DirResourceSet(resourceRoot, "/WEB-INF/classes", additonalClasses, "/")); // everything you need prior to build the application

            standardContext.setResources(resourceRoot); // at this point the tomcat server now has access and knowledge of classes information

            // Create our Daos and Services for Dependency Injection
            MenuItemDao menuItemDao = new MenuItemDao(sessionFactory);
            UsersDao usersDao = new UsersDao(sessionFactory);
            UserPaymentDao userPaymentDao = new UserPaymentDao(sessionFactory);
            OrdersDao ordersDao = new OrdersDao(sessionFactory);
            OrderDetailsDao orderDetailsDao = new OrderDetailsDao(sessionFactory);

            ObjectMapper objectMapper = new ObjectMapper();

            UserPaymentService userPaymentService = new UserPaymentService(userPaymentDao);
            UsersService usersService = new UsersService(usersDao);
            OrderService orderService = new OrderService(ordersDao, userPaymentService);
            MenuService menuService = new MenuService(menuItemDao);
            OrderDetailsService orderDetailsService = new OrderDetailsService(orderDetailsDao, orderService, menuService);

            orderService.setOrderDetailsService(orderDetailsService);

            TokenHandler.setupInstance(objectMapper, usersService);

            tomcat.addServlet("", "UsersServlet", new UsersServlet(usersService, objectMapper));
            standardContext.addServletMappingDecoded("/users", "UsersServlet");

            tomcat.addServlet("", "UserPaymentServlet", new UserPaymentServlet(userPaymentService, usersService, objectMapper));
            standardContext.addServletMappingDecoded("/payments", "UserPaymentServlet");

            tomcat.addServlet("", "OrdersServlet", new OrdersServlet(orderService,
                    usersService, userPaymentService, objectMapper));
            standardContext.addServletMappingDecoded("/orders", "OrdersServlet");

            tomcat.addServlet("", "AuthServlet", new AuthServlet(usersService, objectMapper));
            standardContext.addServletMappingDecoded("/auth", "AuthServlet");

            // tomcat.setPort(3000); // Do not change port from 8080, leave default. This is just to show you can alter the ports. BEcause certain cloud providers sometimes change their ports. they use just 80 or 8080

            tomcat.start(); // there is a default port on your computer for testing, 8080 this is a "developers port"
            tomcat.getServer().await();

        } catch (ServletException | IOException | LifecycleException e) {
            throw new RuntimeException(e);
        }
    }
}
