package com.example.doanapk_qlqa_nhom9;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DangKy extends AppCompatActivity {
    private EditText firstName, lastName, Username, Password, retype_Password;
    private Button DangKy;
    private FirebaseAuth mAuth;
    private static final String TAG = "DangKy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        mAuth = FirebaseAuth.getInstance();

        initUi();
        initListener();
    }

    private void initUi() {
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        Username = findViewById(R.id.username);
        Password = findViewById(R.id.password);
        retype_Password = findViewById(R.id.retype_password);
        DangKy = findViewById(R.id.btnDangKy);
    }

    private void initListener() {
        DangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignUp();
            }
        });
    }

    private void onClickSignUp() {
        String strUsename = Username.getText().toString().trim();
        String strPassword = Password.getText().toString().trim();
        String strRetype_Password = retype_Password.getText().toString().trim();

        if (!strPassword.equals(strRetype_Password)) {
            Toast.makeText(DangKy.this, "Mật khẩu không phù hợp.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(strUsename, strPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng ký thành công, cập nhật UI với thông tin người dùng đã đăng ký
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Thêm node mới vào Firebase với tên là email
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User");
                            String emailNode = strUsename.replace("@gmail.com", "");

                            userRef.child(emailNode).child("FullName").setValue(firstName.getText() + " " + lastName.getText());
                            userRef.child(emailNode).child("email").setValue(strUsename);
                            userRef.child(emailNode).child("Role").setValue("Khách Hàng");

                        }
                        updateUI(user);
                    } else {
                        // Nếu đăng ký thất bại, hiển thị thông báo tới người dùng.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(DangKy.this, "Quá trình xác thực đã thất bại.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }


    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent =new Intent(DangKy.this,DangNhap.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(DangKy.this, "Đăng ký thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }

}
