package com.revature.restaurant_api.users.requests;

public class EditMenuItemRequest {
    private int dishId;
    private String dishName;
    private String description;
    private boolean isVegetarian;

    public EditMenuItemRequest(){super();}

    public int getDishId() {return dishId;}

    public void setDishId(int dish_id) {this.dishId = dishId;}

    public String getDishName() {return dishName;}

    public void setDishName(String dishName) {this.dishName = dishName;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public boolean isVegetarian() {return isVegetarian;}

    public void setVegetarian(boolean vegetarian) {isVegetarian = vegetarian;}

    @Override
    public String toString(){
        return "EditMemberRequest{" +
                "dishId='" + dishId + '\'' +
                "dishName='" + dishName + '\'' +
                "description='" + description + '\'' +
                ", isVegetarian='" + isVegetarian + '\'' +
                '}';
    }
}
