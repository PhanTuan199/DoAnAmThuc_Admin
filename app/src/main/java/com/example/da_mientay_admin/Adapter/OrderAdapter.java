package com.example.da_mientay_admin.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.da_mientay_admin.Common.Common;
import com.example.da_mientay_admin.Model.Order;
import com.example.da_mientay_admin.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    Context context;
    List<Order> orderList;
    SimpleDateFormat simpleDateFormat;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
        this.simpleDateFormat =  new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_order_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context)
                .load(orderList.get(position).getCartItemList().get(0).getFoodIMG())
                .into(holder.img_food);
        holder.txt_order_number.setText(orderList.get(position).getKey());
        Common.setSpanStringColor("Ngày đặt hàng",simpleDateFormat.format(orderList.get(position).getCreateDate()),
                holder.txt_time, Color.parseColor("#333639"));

        Common.setSpanStringColor("Trạng thái: ",Common.covertStatusToString(orderList.get(position).getOrderStatus()),
                holder.txt_order_status, Color.parseColor("#00579A"));
        Common.setSpanStringColor("Khách hàng: ",orderList.get(position).getUserName(),
                holder.txt_name, Color.parseColor("#00579A"));
        Common.setSpanStringColor("Số lượng hàng",orderList.get(position).getCartItemList() == null?"0":
                String.valueOf(orderList.get(position).getCartItemList().size()),
                holder.txt_num_item, Color.parseColor("#4B647D"));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_food)
        ImageView img_food;
        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.txt_time)
        TextView txt_time;
        @BindView(R.id.txt_order_status)
        TextView txt_order_status;
        @BindView(R.id.txt_order_number)
        TextView txt_order_number;
        @BindView(R.id.txt_num_item)
        TextView txt_num_item;

        private Unbinder unbinder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }
}
