package com.example.doanapk_qlqa_nhom9;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.util.Log;

public class EditSP extends AppCompatActivity {
    private DatabaseReference myRef;
    private String itemKey, email;
    private EditText editName, editPrice, edtHinhAnh;
    private Button save, btnload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sp);
        email = getIntent().getStringExtra("email");
        editName = findViewById(R.id.edttten);
        editPrice = findViewById(R.id.edtgia);
        edtHinhAnh = findViewById(R.id.edthinhanh); // Assuming you have an EditText for image name in the layout
        save = findViewById(R.id.btnsua);
        btnload = findViewById(R.id.btnhinh);
        itemKey = getIntent().getStringExtra("itemKey");

        myRef = FirebaseDatabase.getInstance("https://baocaoapk-default-rtdb.firebaseio.com/").getReference("Product");

        // Load existing data
        myRef.child(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FoodItem foodItem = snapshot.getValue(FoodItem.class);
                if (foodItem != null) {
                    editName.setText(foodItem.getName());
                    editPrice.setText(String.valueOf(foodItem.getCost()));
                    edtHinhAnh.setText(foodItem.getPicture()); // Assuming FoodItem has an imageName field
                } else {
                    Log.d("EditSP", "FoodItem is null for itemKey: " + itemKey);
                    Toast.makeText(EditSP.this, "Failed to load product data", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity if no data found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditSP.this, "Failed to load product data", Toast.LENGTH_SHORT).show();
                Log.d("EditSP", "Failed to load product data", error.toException());
                finish(); // Close activity on error
            }
        });

        // Set click listener for Save button
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges(v);
            }
        });

        btnload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageSelectDialog(EditSP.this, new ImageSelectDialog.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(String imageName) {
                        edtHinhAnh.setText(imageName);
                    }
                }).show();
            }
        });
    }

    public void saveChanges(View view) {
        String newName = editName.getText().toString();
        String newPriceString = editPrice.getText().toString();
        String newImageName = edtHinhAnh.getText().toString();

        if (newName.isEmpty() || newPriceString.isEmpty() || newImageName.isEmpty()) {
            Toast.makeText(EditSP.this, "Name, price, and image must not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double newPrice = Double.parseDouble(newPriceString);
            Log.d("EditSP", "Updating itemKey: " + itemKey);

            myRef.child(itemKey).child("name").setValue(newName).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("EditSP", "Name updated successfully");
                } else {
                    Log.d("EditSP", "Failed to update name", task.getException());
                }
            });

            myRef.child(itemKey).child("cost").setValue(newPrice).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(EditSP.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditSP.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                    Log.d("EditSP", "Failed to update cost", task.getException());
                }
            });

            myRef.child(itemKey).child("picture").setValue(newImageName).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(EditSP.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditSP.this, Navigation_main.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EditSP.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                    Log.d("EditSP", "Failed to update image name", task.getException());
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(EditSP.this, "Invalid price format", Toast.LENGTH_SHORT).show();
            Log.d("EditSP", "Invalid price format: " + newPriceString, e);
        }
    }
}
