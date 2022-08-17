package com.revature.restaurant_api.util.dto;

public class LoginCreds {
    private String email;
    private String password;

    // Jackson by default, leverages the default constructor (no-args)

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
