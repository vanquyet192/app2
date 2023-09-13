package com.example.app_banhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.app_banhang.fragment.AboutFragment;
import com.example.app_banhang.fragment.ProductFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerlayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private final int PERMISSION_CODE = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //anh xa
        drawerlayout = findViewById(R.id.DrawerLayout);
        toolbar = findViewById(R.id.ToolBar);
        navigationView = findViewById(R.id.NavigationView);

        requestPermission();

        setSupportActionBar(toolbar);//đưa tất cả các thuọc tính của action bar chjo tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);//hien thi toolbar
//set fragment mac dinh khi chay
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.LinearLayout, new ProductFragment())
                .commit();


        //su kien xu ly nut trong fragment nhu product,gioi thieu,setting,...
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                if (item.getItemId() == R.id.mQlsp) {
                    fragment = new ProductFragment();
                } else if (item.getItemId() == R.id.mGioiThieu) {
                    fragment = new AboutFragment();
                } else {
                    fragment = new ProductFragment();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.LinearLayout, fragment)
                        .commit();
                getSupportActionBar().setTitle(item.getTitle());// xu ly hien thi tieu de

                drawerlayout.closeDrawer(GravityCompat.START);

                return false;
            }
        });
    }

    //bam vao menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerlayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"cap quyen thanh cong", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"cap quyen thanh cong", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "khong co quyen truy cap", Toast.LENGTH_SHORT).show();
            }
        }
    }

}