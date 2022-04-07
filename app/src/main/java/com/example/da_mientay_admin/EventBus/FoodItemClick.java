package com.example.da_mientay_admin.EventBus;

import com.example.da_mientay_admin.Model.Food;

public class FoodItemClick {

    private  boolean success;
    private Food foodModel;

    public FoodItemClick(boolean success, Food foodModel) {
        this.success = success;
        this.foodModel = foodModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Food getFoodModel() {
        return foodModel;
    }

    public void setFoodModel(Food foodModel) {
        this.foodModel = foodModel;
    }
}
