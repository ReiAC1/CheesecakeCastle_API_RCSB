package com.revature.restaurant_api.payments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.restaurant_api.users.UsersModel;
import com.revature.restaurant_api.util.TokenHandler;
import com.revature.restaurant_api.util.TokenHeader;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

public class UserPaymentService {

    UserPaymentDao userPaymentDao;
    ObjectMapper objectMapper;

    public UserPaymentService(UserPaymentDao userPaymentDao, ObjectMapper objectMapper) {
        this.userPaymentDao = userPaymentDao;
        this.objectMapper = objectMapper;
    }

    // Creates a new UserPaymentModel and inserts it into the database
    public UserPaymentModel create(double balance, Date exp_date, String ccv, String zipcode, String provider, UsersModel uModel) {
        // create our model
        UserPaymentModel model = new UserPaymentModel();
        model.setBalance(balance);
        model.setExp_date(exp_date);
        model.setCcv(ccv);
        model.setZipcode(zipcode);
        model.setProvider(provider);
        model.setUserModel(uModel);

        // if our payment model is invalid, return null
        if (!validatePaymentModel(model))
            return null;

        // persist to the database, and return
        return userPaymentDao.create(model);
    }


    public boolean update(UserPaymentModel model) {
        if (!validatePaymentModel(model))
            return false;

        return userPaymentDao.update(model);
    }

    public boolean delete(UserPaymentModel model) {
        return userPaymentDao.delete(model);
    }

    // returns a UserPaymentModel by ID
    public UserPaymentModel getByID(int id) {
        return userPaymentDao.getByID(id);
    }

    public List<UserPaymentModel> getAllByUserID(int userId) {
        return userPaymentDao.getAllByUserID(userId);
    }


    public UserPaymentModel getByToken(String token) {
        // validate token
        if (token == null || token.isEmpty())
            return null;

        String[] split = token.split("\\.");

        // ensure we have a proper SHA256 hash
        if (split[1].length() != 64)
            return null;

        try {
            // get the token's header, and then from that, get the UserPaymentModel based on header
            TokenHeader header = objectMapper.readValue(split[0], TokenHeader.class);
            UserPaymentModel model = userPaymentDao.getByID(header.id);

            // if our model is null, or if the token handler cannot validate, return null
            if (model == null || !TokenHandler.getInstance().isTokenValid(token, objectMapper.writeValueAsString(model)))
                return null;

            // otherwise return model
            return model;
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            // returns null on exception
            return null;
        }
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
                (model.getExp_date().getTime() >= Instant.now().toEpochMilli()) &&
                (model.getCcv().length() == 3) &&
                (model.getZipcode().length() == 5) &&
                (!model.getProvider().isEmpty()) &&
                (model.getId() >= 0);
    }
}
