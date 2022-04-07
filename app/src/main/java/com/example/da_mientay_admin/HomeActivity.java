package com.example.da_mientay_admin;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.da_mientay_admin.Common.Common;
import com.example.da_mientay_admin.EventBus.CategoryClick;
import com.example.da_mientay_admin.EventBus.ChangeMenuClick;
import com.example.da_mientay_admin.EventBus.ToastEvent;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.da_mientay_admin.databinding.ActivityHomeBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private DrawerLayout drawer;
    private  NavigationView navigationView;
    private  NavController navController;
    private int menuClick =-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarHome.toolbar);

         drawer = binding.drawerLayout;
         navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_category, R.id.nav_foodlist, R.id.nav_order)
                .setOpenableLayout(drawer)

                .build();
         navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);

        super.onStop();

    }



    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onCategoryClick(CategoryClick event)
    {
        if(event.isSuccess())
        {
                if(menuClick!= R.id.nav_foodlist)
                {
                    navController.navigate(R.id.nav_foodlist);
                    menuClick = R.id.nav_foodlist;
                }
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onToastEvent(ToastEvent event)
    {
        if(event.getAction() == Common.ACTION.CREATE)
        {
            Toast.makeText(this ,"Thêm thành công", Toast.LENGTH_SHORT).show();

        }
        else if(event.getAction() == Common.ACTION.UPDATE)
        {
            Toast.makeText(this ,"Cập nhật thành công", Toast.LENGTH_SHORT).show();

        }
        else
        {
            Toast.makeText(this ,"Xóa thành công", Toast.LENGTH_SHORT).show();
        }

        EventBus.getDefault().postSticky(new ChangeMenuClick(event.isFromFoodList()));
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onChangeMenuClick(ChangeMenuClick event)
    {
        if(event.isFromFoodList())
        {
            //Clear
            navController.popBackStack(R.id.nav_category,true);
            navController.navigate(R.id. nav_category);
        }
        else
        {
             //Clear
            navController.popBackStack(R.id.nav_foodlist,true);
            navController.navigate(R.id.nav_foodlist);
        }
        menuClick = -1;
    }
}