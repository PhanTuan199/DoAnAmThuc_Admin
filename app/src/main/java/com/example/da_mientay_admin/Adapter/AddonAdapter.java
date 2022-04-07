package com.example.da_mientay_admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.da_mientay_admin.Callback.IRecyclerClickListener;
import com.example.da_mientay_admin.EventBus.SelectAddon;
import com.example.da_mientay_admin.EventBus.SelectSize;
import com.example.da_mientay_admin.EventBus.UpdateAddon;
import com.example.da_mientay_admin.EventBus.UpdateSize;
import com.example.da_mientay_admin.Model.Addon;
import com.example.da_mientay_admin.Model.Size;
import com.example.da_mientay_admin.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddonAdapter extends RecyclerView.Adapter<AddonAdapter.MyViewHolder> {
    Context context;
    List<Addon> addonList;
    UpdateAddon updateAddon;
    int editPos;


    public AddonAdapter(Context context, List<Addon> addonList) {
        this.context = context;
        this.addonList = addonList;
        editPos = -1;
        updateAddon = new UpdateAddon();
    }


    //Định nghĩa các Item layout và khởi tạo Holder
    @NonNull
    @Override
    public AddonAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddonAdapter.MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_size_addon,parent,false));
    }


    //Thiết lập các thuộc tính của View và dữ liệu
    @Override
    public void onBindViewHolder(@NonNull AddonAdapter.MyViewHolder holder, int position) {
        holder.txt_name.setText(addonList.get(position).getName());
        holder.txt_price.setText(String.valueOf(addonList.get(position).getPrice()));
        //Event
        holder.img_del.setOnClickListener(v -> {
            //Xóa item
            addonList.remove(position);
            notifyItemRemoved(position);
            updateAddon.setAddons(addonList); //set for event
            EventBus.getDefault().postSticky(updateAddon); //Send event

        });

        holder.setListener(new IRecyclerClickListener() {
            @Override
            public void onItemClickListener(View view, int pos) {


                editPos = position;
                EventBus.getDefault().postSticky(new SelectAddon(addonList.get(pos)));
            }
        });
    }


    @Override
    public int getItemCount() {
        return addonList.size();
    }

    public void addNewSize(Addon addon) {
        addonList.add(addon);
        notifyItemInserted(addonList.size()-1);
        updateAddon.setAddons(addonList);
        EventBus.getDefault().postSticky(updateAddon);
    }

    public void editSize(Addon addon) {
        if(editPos !=1)
        {
            addonList.set(editPos,addon);
            notifyItemChanged(editPos);
            editPos =-1; //khởi tạo lại khi sửa thành công
            // Send update
            updateAddon.setAddons(addonList);
            EventBus.getDefault().postSticky(updateAddon);

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
