package com.example.da_mientay_admin.Model;

public class Size {
    private  String name;
    private  int price;

    public Size()
    {

    }

    public Size(String name, int price) {
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
