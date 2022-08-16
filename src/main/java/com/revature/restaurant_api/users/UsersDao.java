package com.revature.restaurant_api.users;

import com.revature.restaurant_api.util.interfaces.Crudable;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class UsersDao implements Crudable<UsersModel> {

    private SessionFactory sessionFactory;

    public UsersDao(SessionFactory sessionFactory){this.sessionFactory = sessionFactory;}


    @Override
    public UsersModel create(UsersModel newUser){
        try{
            Session newSession = sessionFactory.openSession();
            newSession.beginTransaction();
            newSession.save(newUser);
            newSession.getTransaction().commit();
            newSession.close();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return newUser;
    }

    @Override
    public boolean update(UsersModel updatedUser){
        try {
            Session newSession = sessionFactory.openSession();
            newSession.beginTransaction();
            newSession.save(updatedUser);
            newSession.getTransaction().commit();
            newSession.close();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(UsersModel deletedUser){
        try{
            Session newSession = sessionFactory.openSession();
            newSession.beginTransaction();
            newSession.save(deletedUser);
            newSession.getTransaction().commit();
            newSession.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    @Override
    public List<UsersModel> getAll(){
        try{
            Session newSession = sessionFactory.openSession();
            CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
            CriteriaQuery<UsersModel> criteria = builder.createQuery(UsersModel.class);
            criteria.from(UsersModel.class);
            List<UsersModel> data = newSession.createQuery(criteria).getResultList();
            newSession.close();
            return data;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UsersModel getByID(int id){return null;}
}
