package com.example.app_banhang.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_banhang.R;
import com.example.app_banhang.dao.productDAO;
import com.example.app_banhang.model.Product;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Product> list;
    private productDAO productDAO;

    public ProductAdapter(Context context, ArrayList<Product> list, com.example.app_banhang.dao.productDAO productDAO) {
        this.context = context;
        this.list = list;
        this.productDAO = productDAO;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_product, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtTitle.setText(list.get(position).getTitle());
//sua ky tu price
        NumberFormat formatter = new DecimalFormat("#,###");
        double myNumber = list.get(position).getPrice();
        String formattedNumber = formatter.format(myNumber);
        holder.txtPrice.setText(formattedNumber + "VND");
        holder.txtQuantity.setText("SL: " + (list.get(position).getQuantity()));
        //xu ly hinh anh
        if (!TextUtils.isEmpty(list.get(position).getImage())) {
            Glide.with(context).load(list.get(position).getImage()).into(holder.ivImageProduct);
        } else {
            holder.ivImageProduct.setImageResource(R.mipmap.product);
        }


//edit product
        holder.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogUpdate(list.get(holder.getAdapterPosition()));
            }
        });
        // delete product
        holder.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDelete(list.get(holder.getAdapterPosition()).getTitle(), list.get(holder.getAdapterPosition()).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtPrice, txtQuantity, txtDelete, txtEdit;
ImageView ivImageProduct;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtDelete = itemView.findViewById(R.id.txtDelete);
            txtEdit = itemView.findViewById(R.id.txtEdit);
            ivImageProduct = itemView.findViewById(R.id.ivImageProduct);
        }
    }

    private void showDialogUpdate(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_update, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText edtAddTitle = view.findViewById(R.id.edtAddTitle);
        EditText edtAddPrice = view.findViewById(R.id.edtAddPrice);
        EditText edtAddQuantity = view.findViewById(R.id.edtAddQuantity);
        Button btnUpdateCancel = view.findViewById(R.id.btnUpdateCancel);
        Button btnUpdateProduct = view.findViewById(R.id.btnUpdateProduct);

        edtAddTitle.setText(product.getTitle());
        edtAddPrice.setText(String.valueOf(product.getPrice()));
        edtAddQuantity.setText(String.valueOf(product.getQuantity()));


        btnUpdateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int Id = product.getId();
                String Title = edtAddTitle.getText().toString();
                String Price = edtAddPrice.getText().toString();
                String Quantity = edtAddQuantity.getText().toString();

                if (Title.length() == 0 || Price.length() == 0 || Quantity.length() == 0) {
                    Toast.makeText(context, "Nhap Day Du thong tin", Toast.LENGTH_SHORT).show();
                } else {
                    Product productedit = new Product(Id, Title, Integer.parseInt(Price), Integer.parseInt(Quantity));
                    boolean check = productDAO.editproduct(productedit);
                    if (check) {
                        Toast.makeText(context, "Edit Thanh cong", Toast.LENGTH_SHORT).show();
//xoa va nhap lai
                        list.clear();
                        list = productDAO.getDS();
                        notifyDataSetChanged();
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(context, "edit that bai", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }

   //xu ly delete

    private void showDialogDelete(String title, int id) {
AlertDialog.Builder builder = new AlertDialog.Builder(context);
builder.setTitle("Thong Bao");
builder.setMessage("ban co muon xoa \""+ title +"\" Khong?");
builder.setPositiveButton("xoa", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        boolean check = productDAO.DeleteProduct(id);
        if(check){
            Toast.makeText(context, "xoa thanh cong", Toast.LENGTH_SHORT).show();
            list. clear();
            list = productDAO.getDS();
            notifyDataSetChanged();

        } else {
        Toast.makeText(context, "delete that bai", Toast.LENGTH_SHORT).show();
    }
    }
});
        builder.setNegativeButton("Huy", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


}
