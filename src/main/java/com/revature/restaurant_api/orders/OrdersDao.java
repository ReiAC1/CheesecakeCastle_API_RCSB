package com.revature.restaurant_api.orders;

import com.revature.restaurant_api.util.interfaces.Crudable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class OrdersDao implements Crudable<OrderModel> {

    private SessionFactory sessionFactory;

    public OrdersDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public OrderModel create(OrderModel newObject) {
        try {
            Session s = sessionFactory.openSession();

            // begin transaction, save, and commit changes
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
    public boolean update(OrderModel updatedObject) {
        try {
            Session s = sessionFactory.openSession();

            // begin transaction, utilize our save/update function, and commit changes
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
    public boolean delete(OrderModel deletedObject) {
        try {
            Session s = sessionFactory.openSession();

            // begin transaction, utilize the delete function, and commit changes
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
    public List<OrderModel> getAll() {
        try {
            // open a session
            Session s = sessionFactory.openSession();

            // Create a CriteriaBuilder using our UserPaymentModel as a basis
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<OrderModel> criteria = builder.createQuery(OrderModel.class);
            criteria.from(OrderModel.class);

            // We can then create a query based on our criteria
            List<OrderModel> data = s.createQuery(criteria).getResultList();
            s.close();

            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public OrderModel getByID(int id) {
        try
        {
            // Create our session
            Session s = sessionFactory.openSession();

            // Using a query, we can select a java class from the database
            Query query = s.createQuery("FROM OrderModel WHERE id = :id");
            // ensure we use parameters to avoid potential sql injection
            query.setParameter("id", id);

            // get our list of results
            List<OrderModel> results = query.getResultList();

            s.close();

            // ensure that we have results, if not return null
            if (results.size() == 0)
                return null;

            // we only need the first found result, as id should be unique
            return results.get(0);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
