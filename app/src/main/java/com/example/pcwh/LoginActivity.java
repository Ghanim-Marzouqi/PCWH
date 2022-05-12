package com.example.pcwh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pcwh.models.User;
import com.example.pcwh.models.UserCredentials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    // Declaration
    private EditText etEmail, etPassword;
    private Checkable cbRememberMe;
    private TextView tvResetPassword;
    private Button btnSignIn, btnSignUp;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialization
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        cbRememberMe = findViewById(R.id.cb_remember_me);
        tvResetPassword = findViewById(R.id.tv_reset_password);
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignUp = findViewById(R.id.btn_sign_up);

        // Initialize Firebase Auth & Firestore Database
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Click listeners
        btnSignIn.setOnClickListener(view -> {
            // Read user input
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            // Validate user input
            if (!isValid(email, password)) {
                return;
            }

            // Sign in user
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    // Store user in shared preferences after fetching from database
                    DocumentReference docRef = db.collection("users").document(firebaseUser.getUid());
                    docRef.get().addOnCompleteListener(dbTask -> {
                        if (dbTask.isSuccessful()) {
                            DocumentSnapshot document = dbTask.getResult();
                            assert document != null;
                            if (document.exists()) {
                                // Instantiate logged in user
                                user = document.toObject(User.class);
                                String data = new Gson().toJson(user);

                                // store data into a Shared Preferences
                                SharedPreferences sp = getSharedPreferences("USER", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("user", data);

                                // Store user credentials if checkbox checked
                                if (cbRememberMe.isChecked()) {
                                    UserCredentials userCredentials = new UserCredentials(email, password, cbRememberMe.isChecked());
                                    String credentials = new Gson().toJson(userCredentials);
                                    editor.putString("credentials", credentials);
                                } else {
                                    editor.putString("credentials", "");
                                }

                                // Apply changes
                                editor.apply();
                            } else {
                                Toast.makeText(LoginActivity.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Cannot get user data", Toast.LENGTH_SHORT).show();
                        }
                    });

                    goToHomePage();
                } else {
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        btnSignUp.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
        });

        tvResetPassword.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = auth.getCurrentUser();

        if(currentUser != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        SharedPreferences sp = getSharedPreferences("USER", MODE_PRIVATE);
        String credentials = sp.getString("credentials", "");

        // check of there is any available data
        if(!credentials.equals("")) {
            UserCredentials userCredentials = new Gson().fromJson(credentials, UserCredentials.class);

            // set Drawer views
            etEmail.setText(userCredentials.getEmail());
            etPassword.setText(userCredentials.getPassword());
            cbRememberMe.setChecked(userCredentials.isRemembered());
        }
    }

    private boolean isValid(String email, String password) {
        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Entered email address is invalid", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void goToHomePage() {
        startActivity(new Intent(this, MainActivity.class));
    }
}