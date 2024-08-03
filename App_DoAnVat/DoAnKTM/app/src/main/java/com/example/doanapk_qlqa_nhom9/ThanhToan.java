package com.example.doanapk_qlqa_nhom9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class ThanhToan extends AppCompatActivity {
    TextView gia;
    Button back;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);

        gia = findViewById(R.id.textView7_gia);
        back = findViewById(R.id.btn_back);


        // Kết thúc activity hiện tại

        String totalAmount = getIntent().getStringExtra("totalAmount");
        email = getIntent().getStringExtra("email");
        gia.setText(totalAmount.replace("Tổng Tiền: ", ""));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThanhToan.this, Navigation_main.class);
                intent.putExtra("email", email);  // Pass the email to Navigation_main
                startActivity(intent);
                finish();
            }
        });
    }
}
