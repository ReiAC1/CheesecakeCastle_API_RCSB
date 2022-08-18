package com.revature.restaurant_api.users.dto.interfaces;

import java.util.List;

public interface Crudable<T> {

    T create(T newObject);
    boolean update(T updatedObject);
    boolean delete(T deletedObject);

    List<T> getAll();
    T getByID(int id);

}
