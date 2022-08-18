package com.revature.restaurant_api.util.interfaces;

import com.revature.restaurant_api.users.UsersModel;

import java.util.List;

public interface Crudable<T> {

    T create(T newObject);
    boolean update(T updatedObject);
    boolean delete(T deletedObject);

    boolean delete(String deletedUser);

    List<T> getAll();

    List<UsersModel> findAll();

    T getByID(int id);

    UsersModel getById(int id);

    UsersModel findById(int id);
}
