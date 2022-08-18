package com.revature.restaurant_api.users;
import com.revature.restaurant_api.users.requests.EditUsersRequest;
import com.revature.restaurant_api.users.requests.NewRegistrationRequest;
import com.revature.restaurant_api.users.response.UsersResponse;
import com.revature.restaurant_api.util.exceptions.InvalidUserInputException;
import com.revature.restaurant_api.util.exceptions.ResourcePersistanceException;
import org.apache.catalina.User;
import org.omg.CORBA.DynAnyPackage.Invalid;

import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UsersService {
    private final UsersDao  usersDao;
    private UsersModel  sessionUser;

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
    public boolean update(EditUsersRequest editUsersRequest) throws InvalidUserInputException {
        UsersModel updateReqUser = usersDao.findById(editUsersRequest.getId());

        Predicate<String> notNullOrEmpty = (str) -> str != null && !str.trim().equals("");

        if (notNullOrEmpty.test(editUsersRequest.getFirstName())){
            updateReqUser.setFirstName(editUsersRequest.getFirstName());
        }
        if (notNullOrEmpty.test(editUsersRequest.getLastName())){
            updateReqUser.setLastName(editUsersRequest.getFirstName());
        }

        if (notNullOrEmpty.test(editUsersRequest.getEmail())){
            if(!isEmailAvailable(editUsersRequest.getEmail())){
                throw new ResourcePersistanceException("Email already in use. Please sign in with that or try again.");
            }
            updateReqUser.setEmail(updateReqUser.getEmail());
        }
        return usersDao.update(updateReqUser);
    }

    public boolean remove(String id){
        return usersDao.delete(id);
    }


    public UsersResponse registerUsers(NewRegistrationRequest newUserRegistration) throws InvalidUserInputException, ResourcePersistanceException {
        UsersModel newUser = new UsersModel();

        newUser.setId(newUserRegistration.getId());
        newUser.setFirstName(newUserRegistration.getFirstName());
        newUser.setLastName(newUserRegistration.getLastName());
        newUser.setEmail(newUserRegistration.getEmail());
        newUser.setRegistrationDate(new Date(System.currentTimeMillis()));
        newUser.setId(Integer.parseInt(UUID.randomUUID().toString()));

        //logger goes here for registration confirmation
        if(!isUserValid(newUser)){
            throw new InvalidUserInputException("User input was valid");
        }
        if (!isEmailAvailable(newUser.getEmail())){
            throw new InvalidUserInputException("Email already registered");
        }

        // TODO: fillout proper response

        return null;
    }

    public UsersModel login(String email, String password){
        UsersModel user = usersDao.loginCredentialCheck(email, password);
        sessionUser = user;
        return user;
    }
//conflict, renamed
    /*public UsersModel getByID(int id){
        return usersDao.getByID(id);
    }*/

    public UsersResponse findById(int id){
        UsersModel user = usersDao.findById(id);
        UsersResponse responseUser = new UsersResponse(user);
        return responseUser;
    }

    public boolean isUserValid(UsersModel newUser){
        //Predicate<String> notNullOrEmpty = (str) -> str != null && !str.trim().equals("");
        //charles suggested we implement the predicate
        //creating mvp first
        if(newUser == null) return false; //check to see if they have entered empty set
        if(newUser.getFirstName() == null || newUser.getFirstName().trim().equals("")) return false;
        if(newUser.getLastName() == null || newUser.getLastName().trim().equals("")) return false;
        if(newUser.getEmail() == null || newUser.getEmail().trim().equals("")) return false;
        if(newUser.getRegistrationDate() == null || newUser.getRegistrationDate().equals("")) return false;
        if(newUser.getPassword() == null || newUser.getPassword().trim().equals("")) return false;

        return true;
    }

    public List<UsersResponse> readAll(){
        List<UsersResponse> usersResponses = usersDao.findAll()
                                                     .stream()
                                                     .map(UsersResponse::new)
                                                     .collect(Collectors.toList());
             return usersResponses;
        }


    public boolean isEmailAvailable(String email){return usersDao.checkEmail(email); }

    public UsersModel getByID(int userID) {return usersDao.findById(userID);
    }
}
