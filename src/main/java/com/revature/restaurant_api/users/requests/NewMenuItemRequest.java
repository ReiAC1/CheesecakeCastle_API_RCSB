package com.revature.restaurant_api.users.requests;

public class NewMenuItemRequest {
    //id number applied in model
    private String dishName;
    private String category; //main/app/dessert

    private String description;
    private float cost;
    private boolean isVegetarian;
    //private double price;

    public NewMenuItemRequest(){
    }

    public NewMenuItemRequest(String dishName, String category, String description,float cost, double price){
        this.dishName = dishName;
        this.category = category;
        this.description = description;
        this.cost = cost;
        //this.price = price;
    }

    public String getDishName() {
        return dishName;
    }

    public void setItem(String dishName) {
        this.dishName = dishName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String itemType) {
        this.category = itemType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    */
    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public boolean getIsVegetarian(){
        return isVegetarian;
    }

    public void setVegetarian(boolean isVegetarian) {
        this.isVegetarian = isVegetarian;
    }

    @Override
    public String toString() {
        return "NewMenuItemRequest{" +
                ", dishName='" + dishName + '\'' +
                ", itemType='" + category + '\'' +
                ", description='" + description + '\'' +
                //", price='" + price + '\'' +
                ", cost='" + cost + '\'' +
                '}';
    }
}

