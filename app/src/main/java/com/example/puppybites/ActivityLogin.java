package com.example.puppybites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.puppybites.databinding.ActivityLoginBinding;

public class ActivityLogin extends AppCompatActivity {


ActivityLoginBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.loginEmail.getText().toString();
                String password = binding.loginPassword.getText().toString();

                if (email.equals("") || password.equals("")) {
                    Toast.makeText(ActivityLogin.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkCredentials = databaseHelper.checkEmailPassword(email, password);

                    if (checkCredentials) {
                        Cursor cursor = databaseHelper.getUserData(email);
                        if (cursor.moveToFirst()) {
                            @SuppressLint("Range") String role = cursor.getString(cursor.getColumnIndex("role"));

                            Toast.makeText(ActivityLogin.this, "Login Successfully!", Toast.LENGTH_SHORT).show();

                            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_email", email);
                            editor.apply();

                            Intent intent;
                            if (role.equals("Seller")) {
                                intent = new Intent(getApplicationContext(), ActivitySellerMain.class);
                            } else {
                                intent = new Intent(getApplicationContext(), ActivityEducationalContent.class);
                            }
                            intent.putExtra("user_email", email);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(ActivityLogin.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLogin.this, ActivitySignup.class);
                startActivity(intent);
            }
        });
    }
}
