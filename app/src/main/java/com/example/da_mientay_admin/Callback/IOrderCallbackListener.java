package com.example.da_mientay_admin.Callback;

import com.example.da_mientay_admin.Model.Category;
import com.example.da_mientay_admin.Model.Order;

import java.util.List;

public interface IOrderCallbackListener {
    void onOrderLoadSucess(List<Order> orderList);
    void onOrderLoadFailed(String message);
}
