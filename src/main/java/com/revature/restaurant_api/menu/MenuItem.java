package com.revature.restaurant_api.menu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.revature.restaurant_api.menu.MenuCategory;

import javax.persistence.*;

@Entity
@Table(name = "menuitems")
public class MenuItem {

    @Id
    @Column(name = "dish_id", nullable = false) //nullable false makes primary key
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int dishId;

    // Base category
    @JsonIgnore
    // name and description
    private String dishName;
    private float cost;
    private String description;
    private boolean isVegetarian;
    // price before tax

    //---------------------------//
    //    Getters and Setters    //
    //---------------------------//


    public int getDishId() {
        return dishId;
    }

    public void setDishId(int id) {
        this.dishId = id;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public float getCost() {return cost;}
    public void setCost(float cost) {this.cost = cost;}

    public boolean isVegetarian() {
        return isVegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        isVegetarian = vegetarian;
    }



    //looks like he does not have this in mvp coumns
    /*
    public MenuCategory getCategory() {
        return category;
    }
    public void setCategory(MenuCategory category) {
        this.category = category;
    }
    *
     */
}
