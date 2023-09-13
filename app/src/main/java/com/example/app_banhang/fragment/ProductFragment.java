package com.example.app_banhang.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.app_banhang.R;
import com.example.app_banhang.adapter.ProductAdapter;
import com.example.app_banhang.dao.productDAO;
import com.example.app_banhang.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductFragment extends Fragment {
    private RecyclerView recyclerProduct;
    private FloatingActionButton floatAdd;
    private productDAO ProductDAO;
    private String filePath = "";
    private ImageView ivImage;
    private TextView txtStatus;
    private String linkimage = "";

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        recyclerProduct = view.findViewById(R.id.RecyclerProduct);
        floatAdd = view.findViewById(R.id.FloatProduct);

        configCloudinary();

        ProductDAO = new productDAO(getContext());
        loaddata();

        floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAdd();
            }
        });

        checkStoragePermission();

        return view;
    }

    private void loaddata() {
        ArrayList<Product> list = ProductDAO.getDS();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerProduct.setLayoutManager(linearLayoutManager);
        ProductAdapter adapter = new ProductAdapter(getContext(), list, ProductDAO);
        recyclerProduct.setAdapter(adapter);
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        }
    }

    private String copyImageToTempFile(Uri imageUri, Activity activity) {
        try {
            InputStream inputStream = activity.getContentResolver().openInputStream(imageUri);
            File tempFile = new File(requireContext().getCacheDir(), "temp_image.jpg");
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return tempFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showDialogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        //khong cho cancel
        alertDialog.setCancelable(false);
        alertDialog.show();
        //bo goc dialog
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //xu ly chuc nang
        EditText edtAddTitle = view.findViewById(R.id.edtAddTitle);
        EditText edtAddPrice = view.findViewById(R.id.edtAddPrice);
        EditText edtAddQuantity = view.findViewById(R.id.edtAddQuantity);
        Button btnAddCancel = view.findViewById(R.id.btnAddCancel);
        Button btnAddProduct = view.findViewById(R.id.btnAddProduct);
        ivImage  = view.findViewById(R.id.ivImage);
        txtStatus = view.findViewById(R.id.txtStatus);


        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Title = edtAddTitle.getText().toString();
                String Price = edtAddPrice.getText().toString();
                String Quantity = edtAddQuantity.getText().toString();

                if (Title.length() == 0 || Price.length() == 0 || Quantity.length() == 0) {
                    Toast.makeText(getContext(), "Nhập Đầy Đủ Thông Tin", Toast.LENGTH_SHORT).show();
                } else {
                    Product product = new Product(Title, Integer.parseInt(Price), Integer.parseInt(Quantity), linkimage);
                    boolean check = ProductDAO.insertproduct(product);
                    if (check) {
                        Toast.makeText(getContext(), "Thêm Sản Phẩm Thành Công", Toast.LENGTH_SHORT).show();

                        // Hiển thị hình ảnh vừa tải lên
                        if (!linkimage.isEmpty()) {
                            Glide.with(getContext()).load(linkimage).into(ivImage);
                        }

                        loaddata();
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Thêm Sản Phẩm Thất Bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        btnAddCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accessTheGallery();
            }
        });
    }



    public void accessTheGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        myLauncher.launch(i);
    }

    private ActivityResultLauncher<Intent> myLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Uri imageUri = result.getData().getData();

                // Sao chép hình ảnh vào tệp tạm thời
                String tempFilePath = copyImageToTempFile(imageUri, requireActivity());

                if (tempFilePath != null) {
                    try {
                        // Load hình ảnh từ tệp tạm thời
                        Bitmap bitmap = BitmapFactory.decodeFile(tempFilePath);
                        ivImage.setImageBitmap(bitmap);

                        // Upload hình ảnh lên Cloudinary
                        uploadToCloudinary(tempFilePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi sao chép hình ảnh không thành công
                }
            }
        }
    });

    private String getRealPathFromUri(Uri imageUri, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(imageUri, null, null, null, null);

        if (cursor == null) {
            return imageUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    HashMap<String, String> config = new HashMap<>();

    private void configCloudinary() {
        config.put("cloud_name", "deq97is5t");
        config.put("api_key", "753183437881119");
        config.put("api_secret", "6nL8yiAnSfuTrXasDP1raakpOiA");
        MediaManager.init(getActivity(), config);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền truy cập được cấp, gọi hàm accessTheGallery() ở đây nếu bạn muốn
            } else {
                Toast.makeText(getContext(), "Ứng dụng cần quyền truy cập bộ nhớ để thêm hình ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void uploadToCloudinary(String filePath) {
        Log.d("A", "sign up uploadToCloudinary- ");
        MediaManager.get().upload(filePath).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
                txtStatus.setText("Bắt đầu upload");
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                txtStatus.setText("Đang upload... ");
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                txtStatus.setText("Thành công: " + resultData.get("url").toString());
                linkimage = resultData.get("url").toString();
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                txtStatus.setText("Lỗi " + error.getDescription());
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                txtStatus.setText("Reshedule " + error.getDescription());
            }
        }).dispatch();
    }

}