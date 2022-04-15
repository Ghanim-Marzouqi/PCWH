package com.example.pcwh;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    // Declaration
    private EditText etEmail;
    private ImageView ivBack;
    private Button btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialization
        etEmail = findViewById(R.id.et_email);
        ivBack = findViewById(R.id.iv_back);
        btnResetPassword = findViewById(R.id.btn_reset_password);

        // Click listeners
        btnResetPassword.setOnClickListener(view -> {

        });

        ivBack.setOnClickListener(view -> {
            finish();
        });
    }
}