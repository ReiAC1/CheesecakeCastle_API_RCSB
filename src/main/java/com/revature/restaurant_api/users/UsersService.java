package com.revature.restaurant_api.users;
import java.sql.Date;

public class UsersService {
    UsersDao  usersDao;
    //our service is to handle user requests
    //we are using the dao for CRUD mehods
    //also using it to retrieve data for the user
    //the model pulls the data from the database
    public UsersService(UsersDao usersDao){
        this.usersDao = usersDao;
    }
    //method to create our user
    // ensure paramters match
    //inquire hibernate column (should we force by variable manipulation)
    public UsersModel create(int id, String firstName, String lastName, String email, Date registrationDate, String password) {
        UsersModel newUser = new UsersModel();
        newUser.setId(id);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setRegistrationDate(registrationDate);
        newUser.setPassword(password);

        return usersDao.create(newUser);

    }

    public UsersModel getByID(int id){
        return usersDao.getByID(id);
    }




}
