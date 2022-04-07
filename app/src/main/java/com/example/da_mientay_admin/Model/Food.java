package com.example.da_mientay_admin.Model;

import java.util.List;

public class Food {

    private  String name,image,id,description;
    private  int price;

    private Size userSelected;
    private  List<Addon> addon;
    private  List<Size> size;
    private  List<Addon> userSelectedAddon;





    public List<Addon> getUserSelectedAddon() {
        return userSelectedAddon;
    }

    public void setUserSelectedAddon(List<Addon> userSelectedAddon) {
        this.userSelectedAddon = userSelectedAddon;
    }

    public List<Addon> getAddon() {
        return addon;
    }

    public void setAddons(List<Addon> addon) {
        this.addon = addon;
    }

    public Size getUserSelected() {
        return userSelected;
    }

    public void setUserSelected(Size userSelected) {
        this.userSelected = userSelected;
    }

    public Food()
    {

    }

    public List<Size> getSize() {
        return size;
    }



    public void setSize(List<Size> size) {
        this.size = size;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
