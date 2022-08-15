package com.revature.restaurant_api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.restaurant_api.menu.MenuCategory;
import com.revature.restaurant_api.menu.MenuItem;
import com.revature.restaurant_api.menu.MenuItemDao;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import javax.persistence.Persistence;
import javax.servlet.ServletException;
import java.io.File;
import java.sql.DriverManager;

// ServletContext - Sets up Servlets associated with the restaurant web api
public class ServletContext {

    SessionFactory sessionFactory;

    private void setupHibernate() {
        // Load our config file
        Configuration conf = new Configuration().configure("cfg.xml");

        // add our annotated classes (aka the classes that should be persisted in our database)
        conf.addAnnotatedClass(MenuItem.class);

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

            // Left for DI for later use
            MenuItemDao menuItemDao = new MenuItemDao(sessionFactory);
            //MemberService memberService = new MemberService(memberDao);
            ObjectMapper objectMapper = new ObjectMapper();

            sessionFactory.close();

            // tomcat.setPort(3000); // Do not change port from 8080, leave default. This is just to show you can alter the ports. BEcause certain cloud providers sometimes change their ports. they use just 80 or 8080

            tomcat.start(); // there is a default port on your computer for testing, 8080 this is a "developers port"
            tomcat.getServer().await();

        } catch (ServletException | LifecycleException e) {
            throw new RuntimeException(e);
        }
    }
}
