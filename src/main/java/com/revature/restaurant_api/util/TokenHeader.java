package com.revature.restaurant_api.util;

// basic data structure for handling the header of a token
public class TokenHeader {
    public int id;
    public long exp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }
}
