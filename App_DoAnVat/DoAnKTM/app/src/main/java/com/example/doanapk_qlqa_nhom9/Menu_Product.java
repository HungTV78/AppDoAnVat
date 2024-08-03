package com.example.doanapk_qlqa_nhom9;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Menu_Product extends Fragment {
    private static final String ARG_EMAIL = "email";
    private String email;

    private DatabaseReference userDatabaseReference;
    boolean isAdmin;

    ImageView img_them;
    Toolbar toolbar;
    ArrayList<FoodItem> lsFood = new ArrayList<>();
    ListView lvKq;
    CustomAdapter adapter2;

    public Menu_Product() {
        // Required empty public constructor
    }

    public static Menu_Product newInstance(String email) {
        Menu_Product fragment = new Menu_Product();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(ARG_EMAIL);
        }
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("User");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_product, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addControl(view);

        FirebaseApp.initializeApp(getContext());
        getDataFromFirebase();

        checkAdmin(email);
        adapter2 = new CustomAdapter(getContext(), lsFood);
        lvKq.setAdapter(adapter2);
        addEvent();
    }


    public void getDataFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://baocaoapk-default-rtdb.firebaseio.com/");
        DatabaseReference myRef = database.getReference("Product");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lsFood.clear();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    FoodItem food = productSnapshot.getValue(FoodItem.class);
                    if (food != null) {
                        food.setKey(productSnapshot.getKey()); // Lưu key để sử dụng sau này
                        lsFood.add(food);
                        Log.d("firebase", "FoodItem: " + food.getName());
                    } else {
                        Log.d("firebase", "FoodItem is null");
                    }
                }
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("firebase", "Failed to read value.", error.toException());
            }
        });
    }

    private void addControl(View view) {
        img_them = view.findViewById(R.id.img_them);
        lvKq = view.findViewById(R.id.lvKQ);
        toolbar=view.findViewById(R.id.toolbar);
    }
    public void checkAdmin(String email){
        String src = email.replace("@gmail.com", "");
        userDatabaseReference.child(src).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String fetcheRole = dataSnapshot.child("Role").getValue(String.class);
                     if (fetcheRole.equals("Admin"))
                     {

                         img_them.setVisibility(View.VISIBLE);
                         isAdmin=true;
                     }
                } else {
                    isAdmin=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addEvent() {
        img_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ThemSP.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        lvKq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isAdmin) {
                    showQuantityDialog(lsFood.get(position));
                }

            }
        });

        lvKq.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (isAdmin) {
                    // Nếu là admin, hiển thị hộp thoạ
                    showEditDeleteDialog(position);
                    return true;
                } else {return false;
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        lsFood.clear();
        CustomAdapter adapter = new CustomAdapter(getContext(), lsFood);
        lvKq.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        getDataFromFirebase(); // Refresh the data
    }

    private void showEditDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose an action")
                .setItems(new String[]{"Edit", "Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Edit action
                                editItem(position);
                                break;
                            case 1:
                                // Delete action
                                deleteItem(position);
                                break;
                        }
                    }
                });
        builder.show();
    }

    private void deleteItem(int position) {
        FoodItem itemToDelete = lsFood.get(position);
        DatabaseReference myRef = FirebaseDatabase.getInstance("https://baocaoapk-default-rtdb.firebaseio.com/").getReference("Product");
        myRef.child(itemToDelete.getKey()).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Product deleted successfully", Toast.LENGTH_SHORT).show();
                getDataFromFirebase(); // Refresh the data
            } else {
                Toast.makeText(getContext(), "Failed to delete product", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void editItem(int position) {
        FoodItem itemToEdit = lsFood.get(position);
        Intent intent = new Intent(getActivity(), EditSP.class);
        intent.putExtra("itemKey", itemToEdit.getKey()); // Pass the key to the edit activity
        intent.putExtra("email", email);
        startActivity(intent);
    }

    private void showQuantityDialog(FoodItem foodItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_quantity, null);
        builder.setView(dialogView);

        TextView tvItemName = dialogView.findViewById(R.id.tvItemName);
        TextView tvQuantity = dialogView.findViewById(R.id.tvQuantity);
        Button btnDecrease = dialogView.findViewById(R.id.btnDecrease);
        Button btnIncrease = dialogView.findViewById(R.id.btnIncrease);
        Button btnAddToCart = dialogView.findViewById(R.id.btnAddToCart);

        tvItemName.setText(foodItem.getName());

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(tvQuantity.getText().toString());
                if (quantity > 1) {
                    tvQuantity.setText(String.valueOf(quantity - 1));
                }
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(tvQuantity.getText().toString());
                tvQuantity.setText(String.valueOf(quantity + 1));
            }
        });

        AlertDialog dialog = builder.create();

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thêm sản phẩm vào giỏ hàng khi người dùng nhấn nút "Thêm vào giỏ hàng"
                int quantity = Integer.parseInt(tvQuantity.getText().toString());
                addToCart(foodItem, quantity);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void addToCart(FoodItem foodItem, int quantity) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = user.getEmail();
        if (email == null) {
            Toast.makeText(getContext(), "Failed to get user email", Toast.LENGTH_SHORT).show();
            return;
        }

        String userKey = email.replace("@gmail.com", "");
        DatabaseReference cartRef = FirebaseDatabase.getInstance("https://baocaoapk-default-rtdb.firebaseio.com/")
                .getReference("User").child(userKey).child("Cart");

        // Check if the item already exists in the cart
        cartRef.orderByChild("name").equalTo(foodItem.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // If the item already exists, update its quantity
                    for (DataSnapshot cartItemSnapshot : snapshot.getChildren()) {
                        Cart cartItem = cartItemSnapshot.getValue(Cart.class);
                        if (cartItem != null) {
                            int newQuantity = cartItem.getQuantity() + quantity;
                            cartItem.setQuantity(newQuantity);
                            // Update the item in the database
                            cartRef.child(cartItemSnapshot.getKey()).setValue(cartItem).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Cart updated successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Failed to update cart", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                } else {
                    // If the item doesn't exist, add it to the cart using a unique key
                    String cartItemKey = cartRef.push().getKey();
                    Cart cartItem = new Cart(foodItem.getName(), foodItem.getPicture(), foodItem.getCost(), quantity);
                    if (cartItemKey != null) {
                        cartRef.child(cartItemKey).setValue(cartItem).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to read cart data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
