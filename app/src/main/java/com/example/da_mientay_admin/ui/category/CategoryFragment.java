package com.example.da_mientay_admin.ui.category;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.GetChars;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.da_mientay_admin.Adapter.CategoriesAdapter;
import com.example.da_mientay_admin.Common.Common;
import com.example.da_mientay_admin.Common.SpaceItemDecoration;
import com.example.da_mientay_admin.Common.SwipHelper;
import com.example.da_mientay_admin.EventBus.ToastEvent;
import com.example.da_mientay_admin.Model.Category;
import com.example.da_mientay_admin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CategoryFragment extends Fragment {

    private static final int PICK_IMG_REQUEST = 1234 ;
    Unbinder unbinder;
    private CategoryViewModel categoryViewModel;
    @BindView(R.id.rcv_category)
    RecyclerView recyclerView;
    AlertDialog dialogA;
    LayoutAnimationController layoutAnimationController;
    CategoriesAdapter categoriesAdapter;

    List<Category> category;

    ImageView imgCategory;

    FirebaseStorage storage;
    StorageReference storageReference;

    private Uri imageUri = null;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        categoryViewModel =
                new ViewModelProvider(this).get(CategoryViewModel.class);


        View root = inflater.inflate(R.layout.fragment_category,container,false);
        unbinder = ButterKnife.bind(this,root);
        initView();
        categoryViewModel.getMessageError().observe(getViewLifecycleOwner(),s -> {
            Toast.makeText(getContext(),""+s,Toast.LENGTH_SHORT).show();
            dialogA.dismiss();
        });

        ///
        categoryViewModel.getCategoryListMultable().observe(getViewLifecycleOwner(),categoryList -> {
            dialogA.dismiss();
            category = categoryList;
            categoriesAdapter = new CategoriesAdapter(getContext(),categoryList);
            recyclerView.setAdapter(categoriesAdapter);
            recyclerView.setLayoutAnimation(layoutAnimationController);
        });

        return root;
    }

    private void initView() {

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //Loading dialog
        dialogA = new SpotsDialog.Builder()
                .setContext(getContext())
                .setCancelable(false)
                .setMessage("Hello")
                .build();
       // dialogA.show();

        layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),layoutManager.getOrientation()));

        //Swiper
        SwipHelper swipHelper = new SwipHelper(getContext(),recyclerView,200) {
            @Override
            public void initMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buf) {
                buf.add(new MyButton(getContext(),"Cập nhật",30,0, Color.parseColor("#FF3C30"),
                        pos -> {
                            Common.categorySelected = category.get(pos);
                            showUpdateDialog();

                        }));
                buf.add(new MyButton(getContext(),"Xóa",30,0, Color.parseColor("#FF0000"),
                        pos -> {
                            Common.categorySelected = category.get(pos);
                            showDeleteDialog();

                        }));

            }
        };

        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_bar_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_create)
        {
           showAddDialog();
        }
        return super.onOptionsItemSelected(item);

    }

    private void showAddDialog() {

        androidx.appcompat.app.AlertDialog.Builder builder  = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Thêm mới");
        builder.setMessage("Vui lòng điền thông tin");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_update_category,null);
        EditText edt_categoryName = (EditText) view.findViewById(R.id.edt_categoryName);
        imgCategory = (ImageView) view.findViewById(R.id.imgCategory);

        //Set Data

        Glide.with(getContext()).load(R.drawable.ic_menu_camera).into(imgCategory);
        //Set Event
        imgCategory.setOnClickListener(v -> {
            Intent intent  = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMG_REQUEST);
        });

        builder.setNegativeButton("HỦY", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("THÊM MỚI", (dialog, which) -> {

            Category category = new Category();
            category.setName(edt_categoryName.getText().toString());

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

                        category.setImage(uri.toString());
                        addCategory(category);
                    });
                }).addOnProgressListener(taskSnapshot->{
                    double progress = (100.0* taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    dialogA.setMessage(new StringBuilder("Uploading:").append(progress).append("%"));
                });
            }
            else
            {
                addCategory(category);
            }
        });
        builder.setView(view);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showUpdateDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder  = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Update");
        builder.setMessage("Vui lòng điền thông tin");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_update_category,null);
        EditText edt_categoryName = (EditText) view.findViewById(R.id.edt_categoryName);
        imgCategory = (ImageView) view.findViewById(R.id.imgCategory);

        //Set Data
        edt_categoryName.setText(new StringBuilder("").append(Common.categorySelected.getName()));
        Glide.with(getContext()).load(Common.categorySelected.getImage()).into(imgCategory);
        //Set Event
        imgCategory.setOnClickListener(v -> {
            Intent intent  = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMG_REQUEST);
        });

        builder.setNegativeButton("HỦY", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("CẬP NHẬT", (dialog, which) -> {
            Map<String,Object> updateData = new HashMap<>();
            updateData.put("name",edt_categoryName.getText().toString());
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
                                    updateData.put("image",uri.toString());
                                    updateCategory(updateData);
                                });
                        }).addOnProgressListener(taskSnapshot->{
                            double progress = (100.0* taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            dialogA.setMessage(new StringBuilder("Uploading:").append(progress).append("%"));
                });
            }
            else
            {
                updateCategory(updateData);
            }
        });
        builder.setView(view);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder  = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Bạn đang xóa danh mục ....");
        builder.setMessage("Bạn có thật sự muốn xóa danh mục này?");
        builder.setNegativeButton("HỦY",(dialog,i)->{
            dialog.dismiss();
        });
        builder.setPositiveButton("XÓA",((dialog, i) ->{
            deleteCategory();
        }));
        androidx.appcompat.app.AlertDialog dialogA = builder.create();
        dialogA.show();


    }

    private void deleteCategory() {
        FirebaseDatabase.getInstance()
                .getReference(Common.CATEGORY_REF)
                .child(Common.categorySelected.getMenu_id())
                .removeValue()
                .addOnCompleteListener(task->{
                    categoryViewModel.loadCategory();
                    EventBus.getDefault().postSticky(new ToastEvent(Common.ACTION.DELETE,false));

                });
    }


    private void updateCategory(Map<String, Object> updateData) {
        FirebaseDatabase.getInstance()
                .getReference(Common.CATEGORY_REF)
                .child(Common.categorySelected.getMenu_id())
                .updateChildren(updateData)
                .addOnCompleteListener(task->{
                   categoryViewModel.loadCategory();
                   EventBus.getDefault().postSticky(new ToastEvent(Common.ACTION.UPDATE,false));

                });
    }

    private void addCategory(Category category) {
        FirebaseDatabase.getInstance()
                .getReference(Common.CATEGORY_REF)

                .push()
                .setValue(category)
                .addOnFailureListener(e-> Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnCompleteListener(task->{
                    categoryViewModel.loadCategory();
                    EventBus.getDefault().postSticky(new ToastEvent(Common.ACTION.CREATE,false));

                    });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMG_REQUEST && resultCode == Activity.RESULT_OK)
            if(data !=null && data.getData() !=null)
            {
                imageUri = data.getData();
                imgCategory.setImageURI(imageUri);
            }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}