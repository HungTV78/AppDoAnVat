package com.example.doanapk_qlqa_nhom9;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile_frag extends Fragment {

    private static final String ARG_EMAIL = "email";
    private String email;
    private DatabaseReference userDatabaseReference;

    public Profile_frag() {
        // Required empty public constructor
    }

    public static Profile_frag newInstance(String email) {
        Profile_frag fragment = new Profile_frag();
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


        // Initialize Firebase Database reference
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("User");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView textViewEmail = view.findViewById(R.id.editTextEmail);
        TextView textViewFullName = view.findViewById(R.id.txt_HoTen);
        TextView textViewAddress = view.findViewById(R.id.editTextAddress);
        TextView textViewPhone = view.findViewById(R.id.editTextPhone);
        RadioButton nam = view.findViewById(R.id.rd_nam);
        RadioButton nu = view.findViewById(R.id.rd_nu);
        ImageView img = view.findViewById(R.id.img_profile);
        Button update = view.findViewById(R.id.btn_update);
        Button logOut = view.findViewById(R.id.btn_logout);
        if (email != null && !email.isEmpty()) {
            textViewEmail.setText(email);
            fetchUserData(email, textViewFullName, textViewAddress, textViewPhone, nam, nu , img);

        } else {
            textViewEmail.setText("Email not provided");
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData(textViewFullName.getText().toString(), textViewAddress.getText().toString(), textViewPhone.getText().toString(), nam.isChecked() ? "Nam" : "Nữ");
                if(nam.isChecked()){
                    img.setImageResource(R.drawable.nam);
                }
                else img.setImageResource(R.drawable.nu);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DangNhap.class);
                startActivity(intent);
            }
        });
        return view;

    }

    private void fetchUserData(String email, TextView fullName, TextView address, TextView phone, RadioButton nam, RadioButton nu, ImageView img) {
        String src = email.replace("@gmail.com", "");
        userDatabaseReference.child(src).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String fetchedFullName = dataSnapshot.child("FullName").getValue(String.class);
                    String fetchedAddress = dataSnapshot.child("Address").getValue(String.class);
                    String fetchedPhone = dataSnapshot.child("Phone").getValue(String.class);
                    String fetchedSex = dataSnapshot.child("Sex").getValue(String.class);

                    fullName.setText(fetchedFullName != null ? fetchedFullName : "");
                    address.setText(fetchedAddress != null ? fetchedAddress : "");
                    phone.setText(fetchedPhone != null ? fetchedPhone : "");

                    if ("Nam".equals(fetchedSex)) {
                        nam.setChecked(true);
                        nu.setChecked(false);
                        img.setImageResource(R.drawable.nam); // Set image for "Nam"
                    } else {
                        nu.setChecked(true);
                        nam.setChecked(false);
                        img.setImageResource(R.drawable.nu); // Set image for "Nữ"
                    }
                    if("".equals(fetchedSex)){
                        nu.setChecked(false);
                        nam.setChecked(false);
                    }
                } else {
                    Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserData(String fullName, String address, String phone, String sex) {
        String src = email.replace("@gmail.com","");
        DatabaseReference userRef = userDatabaseReference.child(src);
        userRef.child("FullName").setValue(fullName);
        userRef.child("Address").setValue(address);
        userRef.child("Phone").setValue(phone);

        userRef.child("Sex").setValue(sex).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "User data updated successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to update user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
