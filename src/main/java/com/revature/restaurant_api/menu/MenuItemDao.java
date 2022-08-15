package com.revature.restaurant_api.menu;

import com.revature.restaurant_api.util.interfaces.Crudable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
        return null;
    }
}
