package com.example.doanapk_qlqa_nhom9;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderHistoryFragment extends Fragment {
    private static final String ARG_EMAIL = "email";
    private String email;

    private ListView listViewOrderHistory;
    private OrderHistoryAdapter adapter;
    private ArrayList<Order> orderHistoryList;
    private ValueEventListener valueEventListener;

    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        initializeFirebase();
        setupListView(view);
        loadOrderHistory();
        return view;
    }

    public OrderHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(ARG_EMAIL);
        }
    }

    public static OrderHistoryFragment newInstance(String email) {
        OrderHistoryFragment fragment = new OrderHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    private void initializeFirebase() {
        if (email == null || email.isEmpty()) {
            Toast.makeText(getContext(), "Email not provided. Please log in.", Toast.LENGTH_SHORT).show();
            return;
        }
        String userKey = email.replace("@gmail.com", "");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(userKey).child("OrderHistory");
    }

    private void setupListView(View view) {
        listViewOrderHistory = view.findViewById(R.id.listViewOrderHistory);
        orderHistoryList = new ArrayList<>();
        adapter = new OrderHistoryAdapter(getContext(), orderHistoryList);
        listViewOrderHistory.setAdapter(adapter);
    }

    private void loadOrderHistory() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderHistoryList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    processOrderSnapshot(snapshot);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load order history: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }

    private void processOrderSnapshot(DataSnapshot snapshot) {
        String orderDate = snapshot.child("orderDate").getValue(String.class);
        Integer totalAmount = snapshot.child("totalAmount").getValue(Integer.class);
        for (DataSnapshot orderSnapshot : snapshot.child("cartList").getChildren()) {
            Order order = orderSnapshot.getValue(Order.class);
            if (order != null) {
                order.setOrderDate(orderDate);
                order.setTotalAmount(totalAmount);
                orderHistoryList.add(order);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (databaseReference != null && valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }
}
