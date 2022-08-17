package com.revature.restaurant_api.payments;

import com.revature.restaurant_api.util.interfaces.Crudable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class UserPaymentDao implements Crudable<UserPaymentModel> {

    private SessionFactory sessionFactory;

    public UserPaymentDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Creates a new row for our UserPaymentModel table
    @Override
    public UserPaymentModel create(UserPaymentModel newObject) {
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

    // Update an object from our database
    @Override
    public boolean update(UserPaymentModel updatedObject) {
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


    // Delete an object from our database
    @Override
    public boolean delete(UserPaymentModel deletedObject) {
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

    // Fancy method for getting every user payment
    @Override
    public List<UserPaymentModel> getAll() {
        try {
            // open a session
            Session s = sessionFactory.openSession();

            // Create a CriteriaBuilder using our UserPaymentModel as a basis
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<UserPaymentModel> criteria = builder.createQuery(UserPaymentModel.class);
            criteria.from(UserPaymentModel.class);

            // We can then create a query based on our criteria
            List<UserPaymentModel> data = s.createQuery(criteria).getResultList();
            s.close();

            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Returns a specific UserPaymentModel by its ID in the database
    @Override
    public UserPaymentModel getByID(int id) {
        try
        {
            // Create our session
            Session s = sessionFactory.openSession();

            // Using a query, we can select a java class from the database
            Query query = s.createQuery("FROM UserPaymentModel WHERE id = :id");
            // ensure we use parameters to avoid potential sql injection
            query.setParameter("id", id);

            // get our list of results
            List<UserPaymentModel> results = query.getResultList();

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
