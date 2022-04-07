package com.example.da_mientay_admin;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.da_mientay_admin.Adapter.AddonAdapter;
import com.example.da_mientay_admin.Adapter.SizeAdapter;
import com.example.da_mientay_admin.Common.Common;
import com.example.da_mientay_admin.EventBus.SelectAddon;
import com.example.da_mientay_admin.EventBus.SelectSize;
import com.example.da_mientay_admin.EventBus.SizeAddOnEvent;
import com.example.da_mientay_admin.EventBus.UpdateAddon;
import com.example.da_mientay_admin.EventBus.UpdateSize;
import com.example.da_mientay_admin.Model.Addon;
import com.example.da_mientay_admin.Model.Size;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.MaybeSource;

public class SizeAddOnActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.edt_price)
    EditText edt_price;
    @BindView(R.id.btn_add)
    Button btn_add;
    @BindView(R.id.btn_edit)
    Button btn_edit;
    @BindView(R.id.rcv_addon_size)
    RecyclerView rcv_addon_size;


    SizeAdapter sizeAdapter;
    AddonAdapter addonAdapter;

    private  int foodEditPos =-1;
    private  boolean needSave = false;
    private boolean isAddon = false;



    @OnClick(R.id.btn_add)
    void onCreateNew()
    {
        if(!isAddon)
        {
            if(sizeAdapter !=null)
            {
                Size size = new Size();
                size.setName(edt_name.getText().toString());
                size.setPrice(Integer.valueOf(edt_price.getText().toString()));
                sizeAdapter.addNewSize(size);
            }
        }
        else
        {
            if(addonAdapter !=null)
            {
                Addon addon = new Addon();
                addon.setName(edt_name.getText().toString());
                addon.setPrice(Integer.valueOf(edt_price.getText().toString()));
                addonAdapter.addNewSize(addon);
            }
        }
    }

    @OnClick(R.id.btn_edit)
    void onEdit()
    {
        if(!isAddon)
        {
            if(sizeAdapter !=null)
            {
                Size size = new Size();
                size.setName(edt_name.getText().toString());
                size.setPrice(Integer.valueOf(edt_price.getText().toString()));
                sizeAdapter.editSize(size);
            }
        }
        else
        {
            Addon addon = new Addon();
            addon.setName(edt_name.getText().toString());
            addon.setPrice(Integer.valueOf(edt_price.getText().toString()));
            addonAdapter.editSize(addon);
        }
    }

    //Menu


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addon_size_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_save:
                saveData();
                break;
            case android.R.id.home:
            {
                if(needSave)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Bạn đang thoát....")
                            .setMessage("Bạn có chắc muốn thoát nhưng không lưu lại không?")
                            .setNegativeButton("HỦY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    needSave = false;
                                    closeActivity();
                                }
                            });

                    AlertDialog dialogA =builder.create();
                    dialogA.show();
                }
                else
                {
                    closeActivity();
                }

            }
            break;
        }
        return  super.onOptionsItemSelected(item);
    }

    private void saveData() {
    if(foodEditPos !=-1)
    {
        Common.categorySelected.getFoods().set(foodEditPos,Common.selectedFood); //Lưu món ăn vào category
        Map<String,Object> updateData = new HashMap<>();
        updateData.put("foods",Common.categorySelected.getFoods());

        FirebaseDatabase.getInstance()
            .getReference(Common.CATEGORY_REF)
                .child(Common.categorySelected.getMenu_id())
                .updateChildren(updateData)
                .addOnFailureListener(e -> Toast.makeText( SizeAddOnActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(SizeAddOnActivity.this, "Làm mới thành công", Toast.LENGTH_SHORT).show();
                    needSave = false;
                    edt_price.setText("0");
                    edt_name.setText("");
                }
            }
        });
    }

   }

    private void closeActivity() {
    edt_name.setText("");
    edt_price.setText("0");
    finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_size_addon);
        init();
    }
    private  void init()
    {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rcv_addon_size.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcv_addon_size.setLayoutManager(layoutManager);
        rcv_addon_size.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));
    }

    //Đăng ký event

    @Override
    protected void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().removeStickyEvent(UpdateSize.class);
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onStop();
    }

    //Nhận event
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public  void onSizeAddOnEvent(SizeAddOnEvent event)
    {
        // nếu event là size
        if(!event.isAddon())
        {
            if(Common.selectedFood.getSize() == null)

                Common.selectedFood.setSize(new ArrayList<>());


                sizeAdapter = new SizeAdapter(this,Common.selectedFood.getSize());
                foodEditPos = event.getPos(); // Lưu lại món ăn sửa đổi để cập nhật
                rcv_addon_size.setAdapter(sizeAdapter);

                isAddon = event.isAddon();

        }
        else
        {
            if(Common.selectedFood.getAddon() == null)

                Common.selectedFood.setAddons(new ArrayList<>());

                addonAdapter = new AddonAdapter(this, Common.selectedFood.getAddon());
                foodEditPos = event.getPos(); // Lưu lại món ăn sửa đổi để cập nhật
                rcv_addon_size.setAdapter(addonAdapter);

                isAddon = event.isAddon();

        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onSizeUpdate(UpdateSize event)
    {
        if(event.getSizeList() !=null)
        {
            needSave = true;
            Common.selectedFood.setSize(event.getSizeList());
        }
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onAddonUpdate(UpdateAddon event)
    {
        if(event.getAddons() !=null)
        {
            needSave = true;
            Common.selectedFood.setAddons(event.getAddons());
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onSelectSize(SelectSize event)
    {
        if(event.getSize()!=null)
        {
                edt_name.setText(event.getSize().getName());
                edt_price.setText(String.valueOf(event.getSize().getPrice()));
                btn_edit.setEnabled(true);
        }
        else
        {
            btn_edit.setEnabled(false);
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onSelectAddon(SelectAddon event)
    {
        if(event.getAddon()!=null)
        {
            edt_name.setText(event.getAddon().getName());
            edt_price.setText(String.valueOf(event.getAddon().getPrice()));
            btn_edit.setEnabled(true);
        }
        else
        {
            btn_edit.setEnabled(false);
        }
    }
}
