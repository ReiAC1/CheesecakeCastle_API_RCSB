package com.revature.restaurant_api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.restaurant_api.menu.MenuItem;
import com.revature.restaurant_api.menu.MenuItemDao;
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

// ServletContext - Sets up Servlets associated with the restaurant web api
public class ServletContext {

    SessionFactory sessionFactory;

    private void setupHibernate() {
        // Load our config file
        Configuration conf = new Configuration().configure("cfg.xml");

        // add our annotated classes (aka the classes that should be persisted in our database)
        conf.addAnnotatedClass(UsersModel.class);
        conf.addAnnotatedClass(MenuItem.class);
        conf.addAnnotatedClass(UserPaymentModel.class);


        // and finally build the session factory
        sessionFactory = conf.buildSessionFactory();
    }

    public void run() {
        setupHibernate();

        String webappDirLocation = new File("src/main/webapp/").getAbsolutePath();
        String additonalClasses = new File("target/classes").getAbsolutePath();

        Tomcat tomcat = new Tomcat();

        try {
            StandardContext standardContext = (StandardContext) tomcat.addWebapp("", webappDirLocation);
            WebResourceRoot resourceRoot = new StandardRoot(standardContext);
            resourceRoot.addPreResources(new DirResourceSet(resourceRoot, "/WEB-INF/classes", additonalClasses, "/")); // everything you need prior to build the application

            standardContext.setResources(resourceRoot); // at this point the tomcat server now has access and knowledge of classes information

            // Create our Daos and Services for Dependency Injection
            MenuItemDao menuItemDao = new MenuItemDao(sessionFactory);
            UsersDao usersDao = new UsersDao(sessionFactory);
            UserPaymentDao userPaymentDao = new UserPaymentDao(sessionFactory, usersDao);

            ObjectMapper objectMapper = new ObjectMapper();

            UserPaymentService userPaymentService = new UserPaymentService(userPaymentDao, objectMapper);
            UsersService usersService = new UsersService(usersDao);

            TokenHandler.setupInstance(objectMapper, usersService);

            //duserPaymentService.create(100, new Date(124, 8, 1), "032", "32792", "Test", 0);

            tomcat.addServlet("", "UserPaymentServlet", new UserPaymentServlet(userPaymentService, usersService, objectMapper));
            standardContext.addServletMappingDecoded("/payments", "UserPaymentServlet");

            tomcat.addServlet("", "UsersServlet", new UsersServlet(usersService, objectMapper));
            standardContext.addServletMappingDecoded("/users", "UsersServlet");

            tomcat.addServlet("", "AuthServlet", new AuthServlet(usersService, objectMapper));
            standardContext.addServletMappingDecoded("/auth", "AuthServlet");

            // tomcat.setPort(3000); // Do not change port from 8080, leave default. This is just to show you can alter the ports. BEcause certain cloud providers sometimes change their ports. they use just 80 or 8080

            tomcat.start(); // there is a default port on your computer for testing, 8080 this is a "developers port"
            tomcat.getServer().await();

        } catch (ServletException | LifecycleException e) {
            throw new RuntimeException(e);
        }
    }
}
