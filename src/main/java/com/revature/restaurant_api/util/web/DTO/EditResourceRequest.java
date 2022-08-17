package com.revature.restaurant_api.util.web.DTO;

import com.revature.restaurant_api.users.requests.EditUsersRequest;

public abstract class EditResourceRequest extends EditUsersRequest {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "EditUsersRequest{" +
                "id='" + id + '\'' +
                "firstName='" + firstName + '\'' +
                "lastName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
