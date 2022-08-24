package com.revature.restaurant_api.menu;

import com.revature.restaurant_api.users.UsersModel;
import com.revature.restaurant_api.util.interfaces.Crudable;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.awt.*;
import java.util.List;

public class MenuItemDao implements Crudable<MenuItem> {

    private SessionFactory sessionFactory;

    public MenuItemDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public MenuItem create(MenuItem newObject) {

        try {
            Session s = sessionFactory.openSession();
            s.beginTransaction();
            s.save(newObject);
            s.getTransaction().commit();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return newObject;
    }

    @Override
    public boolean update(MenuItem updatedObject) {

        try {
            Session s = sessionFactory.openSession();
            s.beginTransaction();
            s.saveOrUpdate(updatedObject);
            s.getTransaction().commit();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean delete(MenuItem deletedObject) {
        try {
            Session s = sessionFactory.openSession();
            s.beginTransaction();
            s.delete(deletedObject);
            s.getTransaction().commit();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public List<MenuItem> getAll() {
        try {
            Session s = sessionFactory.openSession();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<MenuItem> criteria = builder.createQuery(MenuItem.class);
            criteria.from(MenuItem.class);
            List<MenuItem> data = s.createQuery(criteria).getResultList();
            s.close();

            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MenuItem getByID(int id) {
        try {
            Session newSession = sessionFactory.openSession();
            Query query = newSession.createQuery("from MenuItem where id = :id");
            query.setParameter("id" , id);
            List<MenuItem> data = query.getResultList();

            if(data.size() == 0) {
                return null;
            }

            return data.get(0);

        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }
    //what should we do instead of dishname since it can be misspelled?
    //maybe cast to a
    public boolean duplicateCheck(String dishName){
        try{
            Session userSession = sessionFactory.openSession();
            Transaction transaction = userSession.beginTransaction();
            Query query = userSession.createQuery("from MenuItem where dishname= :dishname");
            query.setParameter("dishname", dishName);

            MenuItem newMenuItem = (MenuItem) query.uniqueResult();
            transaction.commit();
            userSession.close();
            if(newMenuItem == null) return true;

            return newMenuItem.getDishName().equals(dishName);

         }catch(HibernateException e){
            e.printStackTrace();
            return false;
        }
    }
}
