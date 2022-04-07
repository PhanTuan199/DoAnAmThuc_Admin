package com.example.da_mientay_admin.Model;

public class Addon {
    private  String name;
    private  int price;

    public Addon()
    {

    }

    public Addon(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
