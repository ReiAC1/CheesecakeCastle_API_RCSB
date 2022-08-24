package com.revature.restaurant_api.menu;

import com.revature.restaurant_api.users.requests.EditMenuItemRequest;
import com.revature.restaurant_api.users.requests.NewMenuItemRequest;
import com.revature.restaurant_api.users.response.MenuItemResponse;
import com.revature.restaurant_api.util.exceptions.InvalidUserInputException;
import com.revature.restaurant_api.util.exceptions.ResourcePersistanceException;
import org.omg.CORBA.DynAnyPackage.Invalid;

import javax.annotation.Resource;
//import java.awt.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.*;

public class MenuService {

    private final MenuItemDao menuItemDao;
    private MenuItem newItem;

    public MenuService(MenuItemDao menuItemDao){this.menuItemDao = menuItemDao;}

    public MenuItemResponse createMenuItem(NewMenuItemRequest newMenuItemRequest) throws InvalidUserInputException, ResourcePersistanceException{
        MenuItem newItem = new MenuItem();

        newItem.setDishName(newMenuItemRequest.getDishName());
        newItem.setDescription(newMenuItemRequest.getDescription());
        newItem.setCost(newMenuItemRequest.getCost());
        newItem.setVegetarian(newMenuItemRequest.getIsVegetarian());

        if(!isMenuItemValid(newItem)){
            throw new InvalidUserInputException("Entry is invalid, please try again");
        }
        if(!isMenuItemDuplicate(newItem.getDishName())){
            throw new InvalidUserInputException("Menu item already exists, please enter a new item");
        }
        menuItemDao.create(newItem);
        return new MenuItemResponse(newItem);

    }

    public MenuItem findById(int id){
        return menuItemDao.getByID(id);
    }

    public boolean update (EditMenuItemRequest menuItemRequest) throws InvalidUserInputException{
        MenuItem menuItem = menuItemDao.getByID(menuItemRequest.getDishId());

        Predicate<String> notNullOrEmpty = (str) -> str != null && !str.trim().equals("");
        if (notNullOrEmpty.test(menuItemRequest.getDishName())){
            menuItem.setDishName(menuItemRequest.getDishName());
        }
        if (notNullOrEmpty.test(menuItemRequest.getDescription())){
            menuItem.setDescription(menuItemRequest.getDescription());
        }
        if (notNullOrEmpty.test(String.valueOf(menuItemRequest.getVegetarian()))){
            menuItem.setVegetarian(menuItemRequest.getVegetarian());
        }

        if (notNullOrEmpty.test(String.valueOf(menuItemRequest.getDishId()))){
            menuItem.setDishId(menuItemRequest.getDishId());
        }
        return menuItemDao.update(menuItem);
    }

    public boolean remove(int id){
        MenuItem menuItem = menuItemDao.getByID(id);
        if(menuItem == null)
            return false;
        return menuItemDao.delete(menuItem);
    }

    public boolean isMenuItemValid(MenuItem newItem){
        System.out.println("Validation check for existing menu item: " + newItem);

        if(newItem ==null) return false;
        if(newItem.getDishName() == null || newItem.getDishName().trim().equals("")) return false;
        if(newItem.getCost() == 0){return false;}
        if(newItem.getDescription() == null || newItem.getDishName().trim().equals("")) return false;
        if(newItem.isVegetarian() != false && newItem.isVegetarian() != true) return false;

        return true;
    }

    public List<MenuItemResponse> readAll(){
        List<MenuItemResponse> menuItemResponseList = menuItemDao.getAll()
                                                                    .stream()
                                                                    .map(MenuItemResponse::new)
                                                                    .collect(Collectors.toList());
        return menuItemResponseList;
    }

    public boolean isMenuItemDuplicate(String dishName){
        if (menuItemDao.duplicateCheck(dishName)){
            return true;
        }
        return false;
    }


}
