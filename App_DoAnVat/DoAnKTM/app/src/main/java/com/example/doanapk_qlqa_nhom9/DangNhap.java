package com.example.doanapk_qlqa_nhom9;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DangNhap extends AppCompatActivity {
    private LinearLayout layoutDangNhap;
    private EditText username, password;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        initUi();
        initListener();

        checkSavedCredentials();
    }

    private void initUi() {
        layoutDangNhap = findViewById(R.id.layout_sign_up);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogin();
            }
        });
    }

    private void initListener() {
        layoutDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhap.this, DangKy.class);
                startActivity(intent);
            }
        });
    }

    private void onClickLogin() {
        String strUsername = username.getText().toString().trim();
        String strPassword = password.getText().toString().trim();

        if (strUsername.isEmpty() || strPassword.isEmpty()) {
            Toast.makeText(DangNhap.this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(strUsername, strPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (rememberCredentials()) {
                            saveCredentials(strUsername, strPassword);
                        }
                        Intent intent1 = new Intent(DangNhap.this, Navigation_main.class);
                        intent1.putExtra("email", strUsername);
                        startActivity(intent1);
                        updateUI(user);
                    } else {
                        Toast.makeText(DangNhap.this, "Đăng nhập thất bại.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private boolean rememberCredentials() {
        return true;
    }

    private void saveCredentials(String username, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }

    private void checkSavedCredentials() {
        // Kiểm tra xem có thông tin đăng nhập đã được lưu không
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");
        if (!savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            // Đã có thông tin đăng nhập được lưu, tự động điền vào EditText
            username.setText(savedUsername);
            password.setText(savedPassword);
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

            Intent intent = new Intent(DangNhap.this, Navigation_main.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(DangNhap.this, "Đăng nhập thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }
}
