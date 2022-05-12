package com.example.pcwh;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pcwh.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    // User name and email
    EditText etName, etEmail, etPhone;
    ImageView ivProfileImage;
    Button btnUpdate;
    FirebaseAuth auth;
    FirebaseFirestore db;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        ivProfileImage = findViewById(R.id.iv_profile);
        btnUpdate = findViewById(R.id.btn_update);

        // Initialize Firebase Authentication & Database
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // show home button with title
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Profile");
        }

        // check for shared preferences
        SharedPreferences sp = getSharedPreferences("USER", MODE_PRIVATE);
        String data = sp.getString("user", "");

        if(!data.equals("")) {
            user = new Gson().fromJson(data, User.class);

            String name = user.getName();
            String email = user.getEmail();
            String phone = user.getPhone();
            String imageUrl = user.getImageUrl();

            // set Drawer views
            etName.setText(name);
            etEmail.setText(email);
            etPhone.setText(phone);

            if (!imageUrl.isEmpty()) {
                Picasso.with(ProfileActivity.this)
                        .load(imageUrl)
                        .resize(96, 96)
                        .centerCrop()
                        .placeholder(R.drawable.person_placeholder)
                        .into(ivProfileImage);
            }
        }

        // Button listener
        btnUpdate.setOnClickListener(view -> {
            if (auth.getCurrentUser() == null && !data.isEmpty()) {
                Toast.makeText(ProfileActivity.this, "User has not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            user = new Gson().fromJson(data, User.class);
            user.setName(etName.getText().toString());
            user.setPhone(etPhone.getText().toString());

            // Add user to Firebase Cloud Firestore
            Map<String, Object> userRecord = new HashMap<>();
            userRecord.put("id", user.getId());
            userRecord.put("name", user.getName());
            userRecord.put("email", user.getEmail());
            userRecord.put("phone", user.getPhone());
            userRecord.put("imageUrl", user.getImageUrl());

            db.collection("users")
                    .document(auth.getCurrentUser().getUid())
                    .update(userRecord)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(ProfileActivity.this, "User profile has been updated", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ProfileActivity.this, "User profile update failed", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}