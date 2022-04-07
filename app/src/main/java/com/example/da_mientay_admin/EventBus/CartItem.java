package com.example.da_mientay_admin.EventBus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;



public class CartItem {

    private  String foodID;


    private   String foodName;


    private  String foodIMG;


    private  int foodQTT;


    private  int foodPrice;


    private  String userPhone;

    private  int foodExtraP;


    private  String foodAddon;

    private  String foodSize;

    private  String userID;

    public CartItem()
    {

    }

    @NonNull
    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(@NonNull String foodID) {
        this.foodID = foodID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodIMG() {
        return foodIMG;
    }

    public void setFoodIMG(String foodIMG) {
        this.foodIMG = foodIMG;
    }

    public int getFoodQTT() {
        return foodQTT;
    }

    public void setFoodQTT(int foodQTT) {
        this.foodQTT = foodQTT;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public int getFoodExtraP() {
        return foodExtraP;
    }

    public void setFoodExtraP(int foodExtraP) {
        this.foodExtraP = foodExtraP;
    }

    public String getFoodAddon() {
        return foodAddon;
    }

    public void setFoodAddon(String foodAddon) {
        this.foodAddon = foodAddon;
    }

    public String getFoodSize() {
        return foodSize;
    }

    public void setFoodSize(String foodSize) {
        this.foodSize = foodSize;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == this)
            return  true;
        if(!(obj instanceof  CartItem))
            return  false;
        CartItem cartItem = (CartItem)obj;
        return  cartItem.getFoodID().equals(this.foodID) &&
                cartItem.getFoodAddon().equals(this.foodAddon) &&
                cartItem.getFoodSize().equals(this.foodSize);
    }
}
