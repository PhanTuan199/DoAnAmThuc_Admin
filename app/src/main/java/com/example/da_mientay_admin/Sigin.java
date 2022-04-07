package com.example.da_mientay_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.da_mientay_admin.Common.Common;
import com.example.da_mientay_admin.Model.Admin;
import com.example.da_mientay_admin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Sigin extends AppCompatActivity {

    Button btnSignin;
    EditText edtPass,edtPhone;

    /// Khởi tạo Firebase
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference table_user = database.getReference("Admin");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigin);
        AddControl();
        AddEvent();
    }

    void AddControl(){
        edtPhone=(EditText)findViewById(R.id.edtPhone);
        edtPass=(EditText)findViewById(R.id.edtPass);
        btnSignin = (Button)findViewById(R.id.btnSignin);
    }

    void AddEvent()
    {
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gán event
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String password = Common.MD5(edtPass.getText().toString());
                        //Kiểm tra số phone khi get text có tồn tại trong database không
                        if(dataSnapshot.child(edtPhone.getText().toString()).exists())
                        {
                            //Đi vào node User và gán cho Model
                            Admin admin = dataSnapshot.child(edtPhone.getText().toString()).getValue(Admin.class);
                            // So sánh Password được lưu với password của text có giống nhau ko
                            if(admin.getPassword().equals(password))
                            {
//                                // In thông báo thành công
                              Toast.makeText(Sigin.this,"Đăng nhập thành công!",Toast.LENGTH_SHORT).show();
                                // Mở activity Home
                                Common.currentAdmin = admin;
                                Intent homeIntent = new Intent(Sigin.this,HomeActivity.class);
                                startActivity(homeIntent);
                                finish();
                            }
                            // Không giống nhau
                            else
                            {
                                // In thông báo failed
                                Toast.makeText(Sigin.this,"Vui lòng kiểm tra lại mật khẩu",Toast.LENGTH_SHORT).show();
                            }
                        }
                        //Không tồn tại
                        else
                        {
                            Toast.makeText(Sigin.this,"User không tồn tại",Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

}