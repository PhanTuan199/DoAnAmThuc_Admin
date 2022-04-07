package com.example.da_mientay_admin.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.da_mientay_admin.Adapter.OrderAdapter;
import com.example.da_mientay_admin.R;
import com.example.da_mientay_admin.ui.category.CategoryViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class OrderFragment extends Fragment {

   @BindView(R.id.rcv_order)
    RecyclerView rcv_order;

   Unbinder unbinder;
   LayoutAnimationController layoutAnimationController;
   OrderAdapter adapter;



   private OrderViewModel orderViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        View root =inflater.inflate(R.layout.fragment_order,container,false);
        unbinder = ButterKnife.bind(this,root);
        initViews();

        orderViewModel.getMessageError().observe(getViewLifecycleOwner(),s -> {
            Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();

        });
        orderViewModel.getOrderModelMultableLiveData().observe(getViewLifecycleOwner(),orders -> {
            if(orders !=null)
            {
                adapter = new OrderAdapter(getContext(),orders);
                rcv_order.setAdapter(adapter);
                rcv_order.setLayoutAnimation(layoutAnimationController);
            }
        });
        return  root;
    }

    private void initViews() {
    rcv_order.setHasFixedSize(true);
    rcv_order.setLayoutManager(new LinearLayoutManager(getContext()));

    layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);
    }
}