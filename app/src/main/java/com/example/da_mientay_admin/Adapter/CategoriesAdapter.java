package com.example.da_mientay_admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.da_mientay_admin.Callback.IRecyclerClickListener;
import com.example.da_mientay_admin.Common.Common;
import com.example.da_mientay_admin.EventBus.CategoryClick;
import com.example.da_mientay_admin.Model.Category;
import com.example.da_mientay_admin.R;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {

    Context context;
    List<Category> categoryList;

    public CategoriesAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
        .inflate(R.layout.layout_category_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        Glide.with(context).load(categoryList.get(position).getImage())
                .into(holder.img_Category);
            holder.txt_Category.setText(new StringBuilder(categoryList.get(position).getName()));
            //Event Send su kien sang home activity de xu ly
        holder.setListener((view, pos) -> {
            //
            Common.categorySelected = categoryList.get(pos);
            //Post stick
            EventBus.getDefault().postSticky(new CategoryClick(true,categoryList.get(pos)));
        });
    }

    @Override
    public int getItemCount() {

        return categoryList.size();
    }
    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        Unbinder unbinder;
        @BindView(R.id.img_Category)
        ImageView img_Category;
        @BindView(R.id.txt_Category)
        TextView txt_Category;

        //
        IRecyclerClickListener listener;

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }
        // itemView setOnClick
        @Override
        public void onClick(View v) {
            listener.onItemClickListener(v,getAdapterPosition());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(categoryList.size()==1)
            return Common.DEFAULT_COLUMN_COUNT;
        else
        {
            if(categoryList.size()%2==0)
                return Common.DEFAULT_COLUMN_COUNT;
            else
                return  (position >1 && position == categoryList.size()-1)?Common.FULL_WIDTH_COLUMN:Common.DEFAULT_COLUMN_COUNT;
        }

    }


}
