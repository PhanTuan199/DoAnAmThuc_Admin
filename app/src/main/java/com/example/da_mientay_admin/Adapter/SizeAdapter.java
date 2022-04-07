package com.example.da_mientay_admin.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.da_mientay_admin.Callback.IRecyclerClickListener;
import com.example.da_mientay_admin.EventBus.SelectSize;
import com.example.da_mientay_admin.EventBus.UpdateSize;
import com.example.da_mientay_admin.Model.Size;
import com.example.da_mientay_admin.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.MyViewHolder> {

    Context context;
    List<Size> sizeList;
    UpdateSize updateSize;
    int editPos;


    public SizeAdapter(Context context, List<Size> sizeList) {
        this.context = context;
        this.sizeList = sizeList;
        editPos = -1;
        updateSize = new UpdateSize();
    }


    //Định nghĩa các Item layout và khởi tạo Holder
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
        .inflate(R.layout.layout_size_addon,parent,false));
    }


    //Thiết lập các thuộc tính của View và dữ liệu
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
     holder.txt_name.setText(sizeList.get(position).getName());
     holder.txt_price.setText(String.valueOf(sizeList.get(position).getPrice()));
     //Event
        holder.img_del.setOnClickListener(v -> {
            //Xóa item
            sizeList.remove(position);
            notifyItemRemoved(position);
            updateSize.setSizeList(sizeList); //set for event
            EventBus.getDefault().postSticky(updateSize); //Send event

        });

        holder.setListener(new IRecyclerClickListener() {
            @Override
            public void onItemClickListener(View view, int pos) {
                editPos = position;
                EventBus.getDefault().postSticky(new SelectSize(sizeList.get(pos)));
            }
        });
    }


    @Override
    public int getItemCount() {
        return sizeList.size();
    }

    public void addNewSize(Size size) {
        sizeList.add(size);
        notifyItemInserted(sizeList.size());
        updateSize.setSizeList(sizeList);
        EventBus.getDefault().postSticky(updateSize);
    }

    public void editSize(Size size) {
        if(editPos !=1)
        {
            sizeList.set(editPos,size);
            notifyItemChanged(editPos);
            editPos =-1; //khởi tạo lại khi sửa thành công
            // Send update
            updateSize.setSizeList(sizeList);
            EventBus.getDefault().postSticky(updateSize);

        }
    }


    public class MyViewHolder extends  RecyclerView.ViewHolder {

        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.txt_price)
        TextView txt_price;
        @BindView(R.id.img_del)
        ImageView img_del;

        Unbinder unbinder;

        IRecyclerClickListener listener;

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClickListener(v,getAdapterPosition());
                }
            });
        }
    }
}
