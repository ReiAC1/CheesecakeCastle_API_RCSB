package com.revature.restaurant_api.payments;

import java.sql.Date;

public class UserPaymentService {

    UserPaymentDao userPaymentDao;

    public UserPaymentService(UserPaymentDao userPaymentDao) {
        this.userPaymentDao = userPaymentDao;
    }

    // Creates a new UserPaymentModel and inserts it into the database
    public UserPaymentModel create(double balance, Date exp_date, String ccv, String zipcode, String provider, long cID) {

        // Todo: validate request

        // create our model
        UserPaymentModel model = new UserPaymentModel();
        model.setBalance(balance);
        model.setExp_date(exp_date);
        model.setCcv(ccv);
        model.setZipcode(zipcode);
        model.setProvider(provider);
        model.setCustomerId(cID);

        // persist to the database, and return
        return userPaymentDao.create(model);
    }

    // returns a UserPaymentModel by ID
    public UserPaymentModel getByID(int id) {
        return userPaymentDao.getByID(id);
    }
}
