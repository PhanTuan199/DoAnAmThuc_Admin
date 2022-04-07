package com.example.da_mientay_admin.Model;

import java.util.List;

public class Category {
    private  String menu_id,name,image;
    List<Food> foods;
    public  Category()
    {

    }

    public Category(String menu_id, String name, String image, List<Food> foods) {
        this.menu_id = menu_id;
        this.name = name;
        this.image = image;
        this.foods = foods;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }
}
