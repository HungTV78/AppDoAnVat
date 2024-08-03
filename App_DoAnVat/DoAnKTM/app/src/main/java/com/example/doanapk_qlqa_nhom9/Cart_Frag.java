package com.example.doanapk_qlqa_nhom9;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Cart_Frag extends Fragment {
    private ArrayList<Cart> carts = new ArrayList<>();
    private CartAdapter adapter;
    private TextView txtTongTien;
    private Button btnThanhToan;
    private static final String ARG_EMAIL = "email";
    private String email;
    private DatabaseReference userDatabaseReference;
    private DatabaseReference cartRef;
    private boolean orderCompleted = false;
    ListView lvCart;

    public static Cart_Frag newInstance(String email) {
        Cart_Frag fragment = new Cart_Frag();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(ARG_EMAIL);
        }

        // Check if email is null
        if (email != null) {
            // Construct the Firebase Database reference
            cartRef = FirebaseDatabase.getInstance("https://baocaoapk-default-rtdb.firebaseio.com/")
                    .getReference().child("User").child(email.replace("@gmail.com", "")).child("Cart");
        }

        // Initialize Firebase Database reference for user
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("User");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_, container, false);

        lvCart = view.findViewById(R.id.lv_cart);
        btnThanhToan = view.findViewById(R.id.btn_ThanhToan);
        txtTongTien = view.findViewById(R.id.txt_TongTien);

        adapter = new CartAdapter(getActivity(), carts, txtTongTien);
        lvCart.setAdapter(adapter);
        getDataFromFirebase();

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Process order
                completeOrder();
            }
        });

        lvCart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                showEditDeleteDialog(position);
                return true;
            }
        });

        return view;
    }

    private void showEditDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose an action")
                .setItems(new String[]{"Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Delete action
                                deleteItem(position);
                                break;
                        }
                    }
                });
        builder.show();
    }

    private void deleteItem(int position) {
        Cart itemToDelete = carts.get(position);

        // Tham chiếu tới giỏ hàng của người dùng và mục cần xóa
        DatabaseReference cartItemRef = cartRef.child(itemToDelete.getKey());

        cartItemRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Product deleted successfully from cart", Toast.LENGTH_SHORT).show();
                getDataFromFirebase(); // Làm mới dữ liệu sau khi xóa
            } else {
                Toast.makeText(getContext(), "Failed to delete product from cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void completeOrder() {
        if (carts.isEmpty()) {
            Toast.makeText(getActivity(), "Cart is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (orderCompleted) {
            return;
        }

        String total = txtTongTien.getText().toString();
        // 1. Add order history
        DatabaseReference orderHistoryRef = userDatabaseReference.child(email.replace("@gmail.com", "")).child("OrderHistory");
        String orderId = orderHistoryRef.push().getKey(); // Generate unique order ID
        long orderTimeMillis = Calendar.getInstance().getTimeInMillis(); // Get current time in milliseconds

        // Tạo định dạng ngày giờ
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        String formattedDate = sdf.format(new Date(orderTimeMillis)); // Format ngày giờ thành chuỗi

        // Thêm orderHistory với ngày giờ đã được định dạng
        OrderHistory orderHistory = new OrderHistory(carts, Double.parseDouble(total.replace("Tổng Tiền: ", "").replaceAll("[^0-9]", "")), formattedDate);

        orderHistoryRef.child(orderId).setValue(orderHistory); // Add order history with timestamp

        // 2. Clear cart
        cartRef.removeValue(); // Remove all items from cart

        // Update UI and notify user
        carts.clear();
        adapter.notifyDataSetChanged();
        adapter.updateTotalPrice();
        Toast.makeText(getActivity(), "Order completed successfully!", Toast.LENGTH_SHORT).show();

        // Launch ThanhToan activity
        orderCompleted = true;
        Intent intent = new Intent(getActivity(), ThanhToan.class);
        intent.putExtra("totalAmount", total);
        intent.putExtra("email", email); // Gửi tổng số tiền sang activity ThanhToan
        startActivity(intent);
    }

    // Method to load cart data from Firebase Database
    public void getDataFromFirebase() {
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carts.clear();
                for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                    Cart cart = cartSnapshot.getValue(Cart.class);
                    carts.add(cart);
                }
                adapter.notifyDataSetChanged();
                adapter.updateTotalPrice();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });
    }
}
