package com.example.doanapk_qlqa_nhom9;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends ArrayAdapter<Cart> {
    private Context context;
    private List<Cart> carts;
    private TextView txtTongTien;

    public CartAdapter(Context context, List<Cart> carts, TextView txtTongTien) {
        super(context, 0, carts);
        this.context = context;
        this.carts = carts;
        this.txtTongTien = txtTongTien;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        }

        Cart cart = carts.get(position);

        // Load hình ảnh từ assets sử dụng Glide
        ImageView picture = convertView.findViewById(R.id.img_sanPham);
        Glide.with(context)
                .load("file:///android_asset/" + cart.getpicture())
                .placeholder(R.drawable.add) // Ảnh placeholder khi đang tải
                .into(picture);

        TextView txtTenSP = convertView.findViewById(R.id.txt_tenSP);
        TextView txtCost = convertView.findViewById(R.id.txt_cost);
        EditText txtSoLuong = convertView.findViewById(R.id.txt_soLuong);
        Button btnGiam = convertView.findViewById(R.id.btn_giam);
        Button btnThem = convertView.findViewById(R.id.btn_them);

        txtTenSP.setText(cart.getName());
        txtCost.setText(String.format("Giá: %.0f VNĐ", cart.getPrice()));
        txtSoLuong.setText(String.valueOf(cart.getQuantity()));

        // Add listener for quantity change
        txtSoLuong.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                int newQuantity = Integer.parseInt(txtSoLuong.getText().toString());
                cart.setQuantity(newQuantity);
                notifyDataSetChanged(); // Notify adapter that data has changed
                updateTotalPrice();
            }
        });

        // Button click listeners
        btnGiam.setOnClickListener(v -> {
            int quantity = Integer.parseInt(txtSoLuong.getText().toString());
            if (quantity > 0) {
                quantity--;
                txtSoLuong.setText(String.valueOf(quantity));
                cart.setQuantity(quantity);
                notifyDataSetChanged();
                updateTotalPrice();
            }
        });

        btnThem.setOnClickListener(v -> {
            int quantity = Integer.parseInt(txtSoLuong.getText().toString());
            quantity++;
            txtSoLuong.setText(String.valueOf(quantity));
            cart.setQuantity(quantity);
            notifyDataSetChanged();
            updateTotalPrice();
        });

        return convertView;
    }

    public double getTotalPrice() {
        double total = 0;
        for (Cart cart : carts) {
            total += cart.getTotalPrice();
        }
        return total;
    }

    // Method to update the total price
    public void updateTotalPrice() {
        double total = 0;
        for (Cart cart : carts) {
            total += cart.getTotalPrice();
        }
        txtTongTien.setText(String.format("Tổng Tiền: %.0f VNĐ", total));
    }
}
