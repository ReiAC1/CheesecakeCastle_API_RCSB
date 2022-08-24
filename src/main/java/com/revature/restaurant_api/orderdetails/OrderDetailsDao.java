package com.revature.restaurant_api.orderdetails;

import com.revature.restaurant_api.orders.OrderModel;
import com.revature.restaurant_api.util.interfaces.Crudable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class OrderDetailsDao implements Crudable<OrderDetailsModel> {
    private SessionFactory sessionFactory;

    public OrderDetailsDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public OrderDetailsModel create(OrderDetailsModel newObject) {
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
    public boolean update(OrderDetailsModel updatedObject) {
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
    public boolean delete(OrderDetailsModel deletedObject) {
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
    public List<OrderDetailsModel> getAll() {
        try {
            // open a session
            Session s = sessionFactory.openSession();

            // Create a CriteriaBuilder using our UserPaymentModel as a basis
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<OrderDetailsModel> criteria = builder.createQuery(OrderDetailsModel.class);
            criteria.from(OrderDetailsModel.class);

            // We can then create a query based on our criteria
            List<OrderDetailsModel> data = s.createQuery(criteria).getResultList();
            s.close();

            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public OrderDetailsModel getByID(int id) {
        try
        {
            // Create our session
            Session s = sessionFactory.openSession();

            // Using a query, we can select a java class from the database
            Query query = s.createQuery("FROM OrderModel WHERE id = :id");
            // ensure we use parameters to avoid potential sql injection
            query.setParameter("id", id);

            // get our list of results
            List<OrderDetailsModel> results = query.getResultList();

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

    public List<OrderDetailsModel> getAllByOrderID(int orderId) {
        // Create our session
        Session s = sessionFactory.openSession();

        // Using a query, we can select a java class from the database
        Query query = s.createQuery("FROM OrderModel WHERE order_id = :id");
        // ensure we use parameters to avoid potential sql injection
        query.setParameter("id", orderId);

        // get our list of results
        List<OrderDetailsModel> results = query.getResultList();

        s.close();

        // ensure that we have results, if not return null
        if (results.size() == 0)
            return null;

        return results;
    }
}
