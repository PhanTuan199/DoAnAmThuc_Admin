package com.example.da_mientay_admin.ui.category;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.da_mientay_admin.Callback.ICategoryCallBackListener;
import com.example.da_mientay_admin.Common.Common;
import com.example.da_mientay_admin.Model.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewModel extends ViewModel implements  ICategoryCallBackListener {

    private  MutableLiveData<List<Category>> categoryListMultable;

    private  MutableLiveData<String> messageError = new MutableLiveData<>();

    private ICategoryCallBackListener categoryCallBackListener;

    public CategoryViewModel() {
        categoryCallBackListener = this;
    }

    public MutableLiveData<List<Category>> getCategoryListMultable() {
        if(categoryListMultable ==null)
        {
            categoryListMultable = new MutableLiveData<>();
            messageError = new MutableLiveData<>();
            loadCategory();
        }
        return  categoryListMultable;
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    public void loadCategory() {
        List<Category> temp = new ArrayList<>();
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference(Common.CATEGORY_REF);
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnapShot:snapshot.getChildren())
                {
                    Category categoryModel = itemSnapShot.getValue(Category.class);
                    categoryModel.setMenu_id(itemSnapShot.getKey());
                    temp.add(categoryModel);
                }
                categoryCallBackListener.onCategoryLoadSucess(temp);
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                categoryCallBackListener.onCategoryLoadFailed(error.getMessage());
            }
        });
    }


    @Override
    public void onCategoryLoadSucess(List<Category> categoryModelList) {
        categoryListMultable.setValue(categoryModelList);
    }

    @Override
    public void onCategoryLoadFailed(String message) {
        messageError.setValue(message);
    }
}
