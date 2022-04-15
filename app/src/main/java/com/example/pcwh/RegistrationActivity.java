package com.example.pcwh;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    // Declaration
    private EditText etName, etEmail, etPhone, etPassword, etConfirmPassword;
    private ImageView ivBack;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialization
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        ivBack = findViewById(R.id.iv_back);
        btnSignUp = findViewById(R.id.btn_sign_up);

        // Click listeners
        btnSignUp.setOnClickListener(view -> {

        });

        ivBack.setOnClickListener(view -> {
            finish();
        });
    }
}