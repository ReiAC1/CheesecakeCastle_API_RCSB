package com.revature.restaurant_api.payments;

import java.sql.Date;
import java.time.Instant;

public class UserPaymentService {

    UserPaymentDao userPaymentDao;

    public UserPaymentService(UserPaymentDao userPaymentDao) {
        this.userPaymentDao = userPaymentDao;
    }

    // Creates a new UserPaymentModel and inserts it into the database
    public UserPaymentModel create(double balance, Date exp_date, String ccv, String zipcode, String provider, long cID) {
        // create our model
        UserPaymentModel model = new UserPaymentModel();
        model.setBalance(balance);
        model.setExp_date(exp_date);
        model.setCcv(ccv);
        model.setZipcode(zipcode);
        model.setProvider(provider);
        model.setCustomerId(cID);

        // if our payment model is invalid, return null
        if (!validatePaymentModel(model))
            return null;

        // persist to the database, and return
        return userPaymentDao.create(model);
    }

    // returns a UserPaymentModel by ID
    public UserPaymentModel getByID(int id) {
        return userPaymentDao.getByID(id);
    }

    // Checks if the given UserPaymentModel is valid
    // model | The UserPaymentModel to check
    // returns true if valid, false otherwise
    private boolean validatePaymentModel(UserPaymentModel model) {
        // null checks
        if (model.getExp_date() == null || model.getCcv() == null || model.getZipcode() == null || model.getProvider() == null)
            return false;

        // otherwise check if data is valid
        return (model.getBalance() >= 0) &&
                (model.getExp_date().toInstant().toEpochMilli() > Instant.now().toEpochMilli()) &&
                (model.getCcv().length() == 3) &&
                (model.getZipcode().length() == 5) &&
                (!model.getProvider().isEmpty()) &&
                (model.getId() > 0);
    }
}
