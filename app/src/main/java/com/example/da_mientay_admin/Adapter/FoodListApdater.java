package com.example.da_mientay_admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.da_mientay_admin.Callback.IRecyclerClickListener;
import com.example.da_mientay_admin.Common.Common;

import com.example.da_mientay_admin.EventBus.CounterCartEvent;
import com.example.da_mientay_admin.EventBus.FoodItemClick;
import com.example.da_mientay_admin.Model.Food;
import com.example.da_mientay_admin.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FoodListApdater extends RecyclerView.Adapter<FoodListApdater.MyViewHolder>{

    private Context context;
    private List<Food> foodModelList;

    public FoodListApdater(Context context, List<Food> foodModelList) {
        this.context = context;
        this.foodModelList = foodModelList;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private Unbinder unbinder;
            @BindView(R.id.txt_food_name)
            TextView txt_food_name;
            @BindView(R.id.txt_food_price)
            TextView txt_food_price;
            @BindView(R.id.img_food_image)
            ImageView img_food_image;



        //Goi de xy ly food detail
        IRecyclerClickListener listener;
        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public MyViewHolder( View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this) ;
        }

        @Override
        public void onClick(View v) {
            listener.onItemClickListener(v,getAdapterPosition());
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
        .inflate(R.layout.layout_food_item,parent,false));
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //Get image và đặt vào ViewHolder
        Glide.with(context).load(foodModelList.get(position).getImage())
        .into(holder.img_food_image);
        // Set text VND + Giá
        holder.txt_food_price.setText(new StringBuilder("VND")
                .append(foodModelList.get(position).getPrice()));
        //Set tên
        holder.txt_food_name.setText(new StringBuilder("")
                .append(foodModelList.get(position).getName()));

        //Event cho food detail
        holder.setListener((view, pos) -> {
            // Lưu lại food đã chọn trong danh sách food và lưu vào cache
            Common.selectedFood = foodModelList.get(pos);

        }) ;




    }
    @Override
    public int getItemCount() {
        return foodModelList.size();
    }


}
