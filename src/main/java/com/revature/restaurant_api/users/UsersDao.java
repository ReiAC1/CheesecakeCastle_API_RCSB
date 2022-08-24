package com.revature.restaurant_api.users;

import com.revature.restaurant_api.payments.UserPaymentModel;
import com.revature.restaurant_api.util.interfaces.Crudable;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;


public class UsersDao implements Crudable<UsersModel> {

    private SessionFactory sessionFactory;

    public UsersDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public UsersModel create(UsersModel newUser) {
        try {
            Session newSession = sessionFactory.openSession();
            newSession.beginTransaction();
            newSession.save(newUser);
            newSession.getTransaction().commit();
            newSession.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return newUser;
    }

    @Override
    public boolean update(UsersModel updatedUser) {
        try {
            Session newSession = sessionFactory.openSession();
            newSession.beginTransaction();
            newSession.update(updatedUser);
            newSession.getTransaction().commit();
            newSession.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(UsersModel deletedObject) {
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
    public List<UsersModel> getAll() {
        try {
            // open a session
            Session s = sessionFactory.openSession();

            // Create a CriteriaBuilder using our UserPaymentModel as a basis
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<UsersModel> criteria = builder.createQuery(UsersModel.class);
            criteria.from(UsersModel.class);

            // We can then create a query based on our criteria
            List<UsersModel> data = s.createQuery(criteria).getResultList();
            s.close();

            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UsersModel getByID(int id) {
        try {
            Session newSession = sessionFactory.openSession();
            Query query = newSession.createQuery("from UsersModel where id = :id");
            query.setParameter("id" , id);
            List<UsersModel> data = query.getResultList();

            if(data.size() == 0) {
                return null;
            }

            return data.get(0);

        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    //used by UsersService to check auth

    public UsersModel loginCredentialCheck(String email, String password) {
        try {
            Session newSession = sessionFactory.openSession();

            Query query = newSession.createQuery("from UsersModel where email= :email and password= :password");
            query.setParameter("email", email);
            query.setParameter("password", password);
            List<UsersModel> data = query.getResultList();

            if(data.size() == 0) {
                return null;
            }

            return data.get(0);

        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean checkEmail(String email){
        try{
            Session userSession = sessionFactory.openSession();
            Transaction transaction = userSession.beginTransaction();
            Query query = userSession.createQuery("from UsersModel where email= :email");
            query.setParameter("email", email);

            UsersModel user = (UsersModel) query.uniqueResult();
            transaction.commit();
            userSession.close();
            if(user == null) return true;

            return user.getEmail().equals(email);

        } catch (HibernateException e){
            e.printStackTrace();
            return false;
        }
    }
}