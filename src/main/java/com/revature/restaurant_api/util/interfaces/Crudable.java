package com.revature.restaurant_api.util.interfaces;

import com.revature.restaurant_api.users.UsersModel;

import java.util.List;

public interface Crudable<T> {

    T create(T newObject);
    boolean update(T updatedObject);
    boolean delete(T deletedObject);

    List<T> getAll();

    T getByID(int id);
}
