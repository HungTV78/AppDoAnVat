package com.example.doanapk_qlqa_nhom9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Navigation_main extends AppCompatActivity {
    ActionBar actionBar;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_main);
        actionBar = getSupportActionBar();
        bottomNavigationView = findViewById(R.id.bottom_nav_b5);
        frameFragment = findViewById(R.id.frameFragment);
        TextView txt = findViewById(R.id.textView6);
        // Retrieve the email passed from DangNhap activity
        String email = getIntent().getStringExtra("email");
        if(email.isEmpty())
        {
            txt.setText("NT");
        }

        // Load the Profile_frag with the email argument
        loadProcFragment(email);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_profile) {
                    loadProfileFragment(email);
                    return true;
                }
                else if (item.getItemId() == R.id.navigation_shop) {
                    loadProcFragment(email);
                    return true;
                }
                else if (item.getItemId() == R.id.navigation_cart) {
                    loadCartFragment(email);
                    return true;
                }
                else if (item.getItemId() == R.id.navigation_gifts) {
                   loadOHFragment(email);
                    //loadFragment(Od);
                    return true;
                }
                return false;
            }
        });
    }

    private void loadProfileFragment(String email) {
        Profile_frag profileFragment = Profile_frag.newInstance(email);
        loadFragment(profileFragment);
    }

    private void loadCartFragment(String email) {
        Cart_Frag  cartFrag= Cart_Frag.newInstance(email);
        loadFragment(cartFrag);
    }
    private void loadOHFragment(String email) {
        OrderHistoryFragment OHF = OrderHistoryFragment.newInstance(email);
        loadFragment(OHF);
    }
    private void loadProcFragment(String email) {
        Menu_Product menu = Menu_Product.newInstance(email);
        loadFragment(menu);
    }
    public void loadFragment(androidx.fragment.app.Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameFragment, fragment);
        ft.commit();
    }
}
