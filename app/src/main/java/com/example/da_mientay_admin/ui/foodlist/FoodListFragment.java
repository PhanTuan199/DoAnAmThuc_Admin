package com.example.da_mientay_admin.ui.foodlist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.da_mientay_admin.Adapter.FoodListApdater;
import com.example.da_mientay_admin.Common.Common;
import com.example.da_mientay_admin.Common.SwipHelper;
import com.example.da_mientay_admin.EventBus.ChangeMenuClick;
import com.example.da_mientay_admin.EventBus.SizeAddOnEvent;
import com.example.da_mientay_admin.EventBus.ToastEvent;
import com.example.da_mientay_admin.Model.Food;
import com.example.da_mientay_admin.R;
import com.example.da_mientay_admin.SizeAddOnActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import butterknife.Action;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;


public class FoodListFragment extends Fragment {

    //Image upload
    private  static  final int PICK_IMG_REQUEST =1234;
    private ImageView img_food;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private android.app.AlertDialog dialogA;

    private FoodListViewModel foodListViewModel;

    private  List<Food> foodModelList;

    Unbinder unbinder;
    @BindView(R.id.rcv_food_list)
    RecyclerView rcv_food_list;

    LayoutAnimationController layoutAnimationController;

    FoodListApdater adapter;
    private Uri imageUri= null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        foodListViewModel =
                new ViewModelProvider(this).get(FoodListViewModel.class);

        View root = inflater.inflate(R.layout.fragment_food,container,false);
        unbinder = ButterKnife.bind(this,root);
        initView();
        foodListViewModel.getMutableLiveDataFoodList().observe(getViewLifecycleOwner(), new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> food) {
                if(food !=null) {
                    foodModelList = food;
                    adapter = new FoodListApdater(getContext(), food);
                    rcv_food_list.setAdapter(adapter);
                    rcv_food_list.setLayoutAnimation(layoutAnimationController);
                }
            }
        });


        return root;
    }

    private void initView() {

        setHasOptionsMenu(true);
        dialogA = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        ((AppCompatActivity)getActivity())
                .getSupportActionBar()
                .setTitle(Common.categorySelected.getName());
        rcv_food_list.setHasFixedSize(true);
        rcv_food_list.setLayoutManager(new LinearLayoutManager(getContext()));

        layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);
        // Get Size Button Swipe
        DisplayMetrics displayMetrics = new DisplayMetrics();

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;


        //Swiper
        SwipHelper swipHelper = new SwipHelper(getContext(),rcv_food_list,width/6) {
            @Override
            public void initMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buf) {
                buf.add(new MyButton(getContext(),"Xóa",30,0, Color.parseColor("#FF3C30"),
                        pos -> {
                            if(foodModelList!=null)
                            Common.selectedFood = foodModelList.get(pos);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Bạn đang xóa món ăn....!")
                                    .setMessage("Bạn có muốn xóa món ăn này?")
                                    .setNegativeButton("HỦY",((dialog, which) ->dialog.dismiss()))
                                    .setPositiveButton("XÓA",((dialog, which) -> {
                                                Common.categorySelected.getFoods().remove(pos);
                                                updateFood(Common.categorySelected.getFoods(), Common.ACTION.DELETE);
                                    }));
                                    AlertDialog delDialog = builder.create();
                                    delDialog.show();

                        }));
                buf.add(new MyButton(getContext(),"Cập nhật",30,0, Color.parseColor("#0033FF"),
                        pos -> {
                                showUpdateDialog(pos);

                        }));

                buf.add(new MyButton(getContext(),"Size",30,0,Color.parseColor("#FF66CC"),
                       pos ->  {
                                Common.selectedFood = foodModelList.get(pos);
                                startActivity(new Intent(getContext(), SizeAddOnActivity.class));
                                // Gửi 1 ticket kiểm tra xem là addon hay size và vị trí
                                EventBus.getDefault().postSticky(new SizeAddOnEvent(false,pos));
                        }));
                buf.add(new MyButton(getContext(),"Chọn thêm",30,0,Color.parseColor("#0000CC"),
                        pos ->  {
                            Common.selectedFood = foodModelList.get(pos);
                            startActivity(new Intent(getContext(), SizeAddOnActivity.class));
                            // Gửi 1 ticket kiểm tra xem là addon hay size và vị trí
                            EventBus.getDefault().postSticky(new SizeAddOnEvent(true,pos));
                        }));

            }
        };

    }

    private void showUpdateDialog(int pos) {
        androidx.appcompat.app.AlertDialog.Builder builder  = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Update");
        builder.setMessage("Vui lòng điền thông tin");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_update_food,null);
        EditText edt_food_name = (EditText) view.findViewById(R.id.edt_food_name);
        EditText edt_food_price = (EditText) view.findViewById(R.id.edt_food_price);
        EditText edt_food_des = (EditText) view.findViewById(R.id.edt_food_des);
        img_food = (ImageView) view.findViewById(R.id.img_food_image);

        //Set data
        edt_food_name.setText(new StringBuilder("")
        .append(Common.categorySelected.getFoods().get(pos).getName()));
        edt_food_price.setText(new StringBuilder("")
        .append(Common.categorySelected.getFoods().get(pos).getPrice()));

        edt_food_des.setText(new StringBuilder("")
        .append((Common.categorySelected.getFoods().get(pos).getDescription())));

        Glide.with(getContext()).load((Common.categorySelected.getFoods().get(pos))).into(img_food);

        //Set event
        img_food.setOnClickListener(v -> {
            Intent intent  = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMG_REQUEST);

        });

        builder.setNegativeButton("HỦY", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("CẬP NHẬT", (dialog, which) -> {




            Food updateFood = Common.categorySelected.getFoods().get(pos);
            updateFood.setName(edt_food_name.getText().toString());
            updateFood.setPrice(TextUtils.isEmpty(edt_food_price.getText())?0:
                    Integer.parseInt(edt_food_price.getText().toString()));
            updateFood.setDescription(edt_food_des.getText().toString());
            if(imageUri != null)
            {
                //upload hình lên Firebase Storage
                dialogA.setMessage("Uploading...");
                dialogA.show();

                String imageName = UUID.randomUUID().toString();
                StorageReference imgFolder = storageReference.child("image/"+imageName);

                imgFolder.putFile(imageUri)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialogA.dismiss();
                                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnCompleteListener(task -> {
                    dialogA.dismiss();
                    imgFolder.getDownloadUrl().addOnSuccessListener(uri -> {
                        updateFood.setImage(uri.toString());
                        updateFood(Common.categorySelected.getFoods(), Common.ACTION.UPDATE);
                    });
                }).addOnProgressListener(taskSnapshot->{
                    double progress = (100.0* taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    dialogA.setMessage(new StringBuilder("Uploading:").append(progress).append("%"));
                });
            }
            else
            {
                Common.categorySelected.getFoods().set(pos,updateFood);
                updateFood(Common.categorySelected.getFoods(),Common.ACTION.UPDATE);
            }
        });

        builder.setView(view);
        AlertDialog updateDialog = builder.create();
        updateDialog.show();;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMG_REQUEST && resultCode == Activity.RESULT_OK)
            if(data !=null && data.getData() !=null)
            {
                imageUri = data.getData();
                img_food.setImageURI(imageUri);
            }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.food_action_menu,menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_create)
            showAddDialog();
        return super.onOptionsItemSelected(item);
    }

    private void showAddDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder  = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Tạo mới");
        builder.setMessage("Vui lòng điền thông tin");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_update_food,null);
        EditText edt_food_name = (EditText) view.findViewById(R.id.edt_food_name);
        EditText edt_food_price = (EditText) view.findViewById(R.id.edt_food_price);
        EditText edt_food_des = (EditText) view.findViewById(R.id.edt_food_des);
        img_food = (ImageView) view.findViewById(R.id.img_food_image);

        //Set data


        Glide.with(getContext()).load(R.drawable.ic_menu_camera).into(img_food);

        //Set event
        img_food.setOnClickListener(v -> {
            Intent intent  = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMG_REQUEST);

        });

        builder.setNegativeButton("HỦY", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("THÊM", (dialog, which) -> {


            Food updateFood = new Food();
            Random random = new Random();
            int val = random.nextInt(1000);
            String Fid = "F"+val;
            updateFood.setId(Fid);
            updateFood.setName(edt_food_name.getText().toString());
            updateFood.setPrice(TextUtils.isEmpty(edt_food_price.getText())?0:
                    Integer.parseInt(edt_food_price.getText().toString()));
            updateFood.setDescription(edt_food_des.getText().toString());
            if(imageUri != null)
            {
                //upload hình lên Firebase Storage
                dialogA.setMessage("Uploading...");
                dialogA.show();

                String imageName = UUID.randomUUID().toString();
                StorageReference imgFolder = storageReference.child("image/"+imageName);

                imgFolder.putFile(imageUri)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialogA.dismiss();
                                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnCompleteListener(task -> {
                    dialogA.dismiss();
                    imgFolder.getDownloadUrl().addOnSuccessListener(uri -> {
                        updateFood.setImage(uri.toString());
                        if(Common.categorySelected.getFoods() == null)
                            Common.categorySelected.setFoods(new ArrayList<>());
                        Common.categorySelected.getFoods().add(updateFood);

                        updateFood(Common.categorySelected.getFoods(),Common.ACTION.CREATE);
                    });
                }).addOnProgressListener(taskSnapshot->{
                    double progress = (100.0* taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    dialogA.setMessage(new StringBuilder("Uploading:").append(progress).append("%"));
                });
            }
            else
            {
                if(Common.categorySelected.getFoods() == null)
                    Common.categorySelected.setFoods(new ArrayList<>());
                Common.categorySelected.getFoods().add(updateFood);

                updateFood(Common.categorySelected.getFoods(), Common.ACTION.CREATE);
            }
        });

        builder.setView(view);
        AlertDialog updateDialog = builder.create();
        updateDialog.show();;
    }

    private void updateFood(List<Food> foods, Common.ACTION action) {
        Map<String,Object> updateData = new HashMap<>();
        updateData.put("foods",foods);

        FirebaseDatabase.getInstance()
                .getReference(Common.CATEGORY_REF)
                .child(Common.categorySelected.getMenu_id())
                .updateChildren(updateData)
                .addOnFailureListener(e->{
                    Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            foodListViewModel.getMutableLiveDataFoodList();
                            EventBus.getDefault().postSticky(new ToastEvent(action,true));

                        }
                    }
                });

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().postSticky(new ChangeMenuClick(true));
        super.onDestroy();
    }
}