package com.revature.restaurant_api.users.requests;

public class EditMenuItemRequest {
    private int dishId;
    private String dishName;

    private double cost;
    private String description;
    private boolean vegetarian;

    public EditMenuItemRequest(){super();}

    public int getDishId() {return dishId;}

    public void setDishId(int dishId) {this.dishId = dishId;}

    public String getDishName() {return dishName;}

    public void setDishName(String dishName) {this.dishName = dishName;}

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public boolean getVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    @Override
    public String toString(){
        return "EditMemberRequest{" +
                "dishId='" + dishId + '\'' +
                "dishName='" + dishName + '\'' +
                "description='" + description + '\'' +
                ", vegetarian='" + vegetarian + '\'' +
                '}';
    }
}
