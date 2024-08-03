package com.example.doanapk_qlqa_nhom9;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ThemSP extends AppCompatActivity {
    TextInputEditText edtTen, edtGia, edtHinhAnh;
    Button btnThem, btnload;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sp);

        edtTen = findViewById(R.id.edtten);
        edtGia = findViewById(R.id.edtgia);
        edtHinhAnh = findViewById(R.id.edthinhanh);
        btnThem = findViewById(R.id.btnbutton);
        btnload = findViewById(R.id.btnhinh);
        email = getIntent().getStringExtra("email");
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProduct();
            }
        });

        btnload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageSelectDialog(ThemSP.this, new ImageSelectDialog.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(String imageName) {
                        edtHinhAnh.setText(imageName);
                    }
                }).show();
            }
        });
    }

    private void saveProduct() {
        String name = edtTen.getText().toString().trim();
        String priceStr = edtGia.getText().toString().trim();
        String pic = edtHinhAnh.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || pic.isEmpty()) {
            Toast.makeText(ThemSP.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(ThemSP.this, "Invalid price", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://baocaoapk-default-rtdb.firebaseio.com/");
        DatabaseReference myRef = database.getReference("Product");

        String productId = myRef.push().getKey();
        FoodItem newFoodItem = new FoodItem(name, price, pic);
        if (productId != null) {
            myRef.child(productId).setValue(newFoodItem).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ThemSP.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ThemSP.this, Navigation_main.class);
                    intent.putExtra("email", email);

                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ThemSP.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
