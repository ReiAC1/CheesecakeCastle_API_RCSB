package com.revature.restaurant_api.menu;

import com.revature.restaurant_api.menu.MenuCategory;

import javax.persistence.*;

@Entity(name = "menu_items")
public class MenuItem {

    // Database identifier
    // annotate that this will be our Identifier as well as we want to generate the value
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    // Base category
    private MenuCategory category;

    // name and description
    private String name;
    private String description;

    // price before tax
    private float price;

    //---------------------------//
    //    Getters and Setters    //
    //---------------------------//


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public MenuCategory getCategory() {
        return category;
    }

    public void setCategory(MenuCategory category) {
        this.category = category;
    }
}
