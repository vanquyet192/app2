package com.example.app_banhang;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_banhang.dao.userDAO;
import com.example.app_banhang.util.SendMail;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private userDAO userDAO;
    private SendMail sendMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //anh xa
        EditText editUser = findViewById(R.id.edtUser);
        EditText editPass = findViewById(R.id.edtPass);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView txtForgot = findViewById(R.id.txtForgot);
        TextView txtSignup = findViewById(R.id.txtSignup);
        userDAO = new userDAO(this);
        sendMail = new SendMail();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = editUser.getText().toString(); // Sửa ở đây
                String pass = editPass.getText().toString();

                boolean check = userDAO.checkLogin(user, pass);
                if (check) {
                    Toast.makeText(LoginActivity.this, "Dang nhap thanh cong", Toast.LENGTH_SHORT).show(); // Sửa ở đây
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "dang nhap that bai", Toast.LENGTH_SHORT).show(); // Sửa ở đây
                }
            }
        });
        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        txtForgot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDiaLogForgot();
            }
        });

    }

    private void showDiaLogForgot() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_forgot, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        EditText edtEmail = view.findViewById(R.id.edtEmail);
        Button btnEmail = view.findViewById(R.id.btnEmail);
        Button btnCanle = view.findViewById(R.id.btnCancel);

        btnCanle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String password = userDAO.ForgotPassword(email);
              //  Toast.makeText(LoginActivity.this, password, Toast.LENGTH_SHORT).show();
            if(password.equals("")){
                Toast.makeText(LoginActivity.this, "Khong tim thay tai khoan",Toast.LENGTH_SHORT).show();
            }
            else{
                sendMail.Send(LoginActivity.this, email, "Lay Lai Mat Khau", "Mat Khau Cua Ban La:" + password);
                alertDialog.dismiss();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            }
        });


    }

}

