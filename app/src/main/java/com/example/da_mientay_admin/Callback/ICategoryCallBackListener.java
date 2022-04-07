package com.example.da_mientay_admin.Callback;

import com.example.da_mientay_admin.Model.Category;

import java.util.List;

public interface ICategoryCallBackListener {
    void onCategoryLoadSucess(List<Category> categoryModels);
    void onCategoryLoadFailed(String message);
}
