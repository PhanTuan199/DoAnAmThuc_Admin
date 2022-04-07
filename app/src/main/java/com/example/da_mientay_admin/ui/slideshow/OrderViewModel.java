package com.example.da_mientay_admin.ui.slideshow;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.da_mientay_admin.Callback.IOrderCallbackListener;
import com.example.da_mientay_admin.Common.Common;
import com.example.da_mientay_admin.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class OrderViewModel extends ViewModel implements IOrderCallbackListener {

    private MutableLiveData<List<Order>> orderModelMultableLiveData;
    private MutableLiveData<String> messageError;

    private IOrderCallbackListener listener;

    public OrderViewModel() {
        orderModelMultableLiveData = new MutableLiveData<>();
        messageError = new MutableLiveData<>();
        listener = this;
    }

    public MutableLiveData<List<Order>> getOrderModelMultableLiveData() {
        loadOrderByStatus(0);
        return orderModelMultableLiveData;
    }

    private void loadOrderByStatus(int status) {
        List<Order> tempList = new ArrayList<>();
        Query orderRef = FirebaseDatabase.getInstance().getReference(Common.ORDER_REF)
                .orderByChild("orderStatus");
                //.equalTo(status);
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             for(DataSnapshot itemSnapShot:snapshot.getChildren())
             {
                 Order order = itemSnapShot.getValue(Order.class);
                 order.setKey(itemSnapShot.getKey());
                 tempList.add(order);
             }
             listener.onOrderLoadSucess(tempList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onOrderLoadFailed(error.getMessage());
            }
        });
    }

    public MutableLiveData<String> getMessageError()
    {
        return  messageError;
    }

    @Override
    public void onOrderLoadSucess(List<Order> orderList) {
        if(orderList.size()>0)
        {
            Collections.sort(orderList,(order,t1)->
            {
                if(order.getCreateDate()<t1.getCreateDate())
                    return  -1;
                return order.getCreateDate() == t1.getCreateDate()?0:1;
            });

            orderModelMultableLiveData.setValue(orderList);
        }

    }

    @Override
    public void onOrderLoadFailed(String message) {
        messageError.setValue(message);
    }
}