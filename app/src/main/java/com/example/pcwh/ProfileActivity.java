package com.example.pcwh;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pcwh.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

        // Get user data
        FirebaseUser firebaseUser = auth.getCurrentUser();

        if (firebaseUser != null && !firebaseUser.getUid().isEmpty()) {
            DocumentReference docRef = db.collection("users").document(firebaseUser.getUid());
            docRef.get().addOnCompleteListener(dbTask -> {
                if (dbTask.isSuccessful()) {
                    DocumentSnapshot document = dbTask.getResult();
                    assert document != null;
                    if (document.exists()) {
                        // Instantiate logged in user
                        user = document.toObject(User.class);

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
                    } else {
                        Toast.makeText(this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Cannot get user data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Button listener
        btnUpdate.setOnClickListener(view -> {
            if (auth.getCurrentUser() == null) {
                Toast.makeText(ProfileActivity.this, "User has not logged in", Toast.LENGTH_SHORT).show();
                return;
            }
            User updatedUser = new User();
            updatedUser.setId(firebaseUser.getUid());
            updatedUser.setName(etName.getText().toString());
            updatedUser.setEmail(firebaseUser.getEmail());
            updatedUser.setPhone(etPhone.getText().toString());
            updatedUser.setImageUrl("");

            // Add user to Firebase Cloud Firestore
            Map<String, Object> userRecord = new HashMap<>();
            userRecord.put("id", updatedUser.getId());
            userRecord.put("name", updatedUser.getName());
            userRecord.put("email", updatedUser.getEmail());
            userRecord.put("phone", updatedUser.getPhone());
            userRecord.put("imageUrl", updatedUser.getImageUrl());

            db.collection("users")
                    .document(auth.getCurrentUser().getUid())
                    .update(userRecord)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(ProfileActivity.this, "User profile has been updated", Toast.LENGTH_SHORT).show();
                        finish();
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