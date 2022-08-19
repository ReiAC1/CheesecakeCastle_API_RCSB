package com.revature.restaurant_api.users.response;

import com.revature.restaurant_api.menu.MenuItem;

import java.awt.*;

public class MenuItemResponse {

    private int dishId;
    private String dishName;
    private float cost;
    private String description;
    private boolean isVegetarian;

    public MenuItemResponse(){}

    public MenuItemResponse(MenuItem menuItem){
        this.dishId = menuItem.getDishId();;
        this.dishName = menuItem.getDishName();
        this.cost = menuItem.getCost();
        this.description = menuItem.getDescription();
        this.isVegetarian = menuItem.isVegetarian();
    }

    public int getDishId() {return dishId;}

    public void setDishId(int dishId) {this.dishId = dishId;}

    public String getDishName() {return dishName;}

    public void setDishName(String dishName) {this.dishName = dishName;}

    public float getCost() {return cost;}

    public void setCost(float cost) {this.cost = cost;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public boolean isVegetarian() {return isVegetarian;}

    public void setVegetarian(boolean vegetarian) {isVegetarian = vegetarian;}

    @Override
    public String toString() {
        return "MemberResponse{" +
                "dishId='" + dishId + '\'' +
                ", dishName='" + dishName + '\'' +
                ", cost='" + cost + '\'' +
                ", description='" + description + '\'' +
                ", isVegetarian='" + isVegetarian + '\'' +
                '}';
    }
}
