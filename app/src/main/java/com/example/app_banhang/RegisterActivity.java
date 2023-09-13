package com.example.app_banhang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app_banhang.dao.userDAO;

public class RegisterActivity extends AppCompatActivity {
private userDAO nguoidungDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nguoidungDAO = new userDAO(this);
        EditText edtUser = findViewById(R.id.edtUser);
        EditText edtPass = findViewById(R.id.edtPass);
        EditText edtRePass = findViewById(R.id.edtRePass);
        EditText edtFulluser = findViewById(R.id.edtRFulluser);
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edtUser.getText().toString();
                String pass = edtPass.getText().toString();
                String repass = edtRePass.getText().toString();
                String fulluser = edtFulluser.getText().toString();

                if(!pass.equals(repass)){
                    Toast.makeText(RegisterActivity.this, "Mat khau khong trung nhau, Vui long nhap lai", Toast.LENGTH_SHORT).show(); // Sửa ở đây

                } else {
                    boolean check = nguoidungDAO.Register(user, pass, fulluser);
                    if(check){
                        Toast.makeText(RegisterActivity.this, "Dang ky thanh cong", Toast.LENGTH_SHORT).show(); // Sửa ở đây
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }
                }

            }
        });

    }
}