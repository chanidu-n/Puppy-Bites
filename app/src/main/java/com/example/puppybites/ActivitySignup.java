package com.example.puppybites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.puppybites.databinding.ActivitySignupBinding;

public class ActivitySignup extends AppCompatActivity {

    ActivitySignupBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        Spinner roleSpinner = findViewById(R.id.signup_role);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.role_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.signupEmail.getText().toString();
                String password = binding.signupPassword.getText().toString();
                String confirmPassword = binding.signupConfirm.getText().toString();
                String role = binding.signupRole.getSelectedItem().toString();

                if (email.equals("") || password.equals("") || confirmPassword.equals("") || role.equals("Select your Role")) {
                    Toast.makeText(ActivitySignup.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(email)) {
                    Toast.makeText(ActivitySignup.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(ActivitySignup.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkUserEmail = databaseHelper.checkEmail(email);

                    if (!checkUserEmail) {
                        Boolean insert = databaseHelper.insertData(email, password, role);

                        if (insert) {
                            Toast.makeText(ActivitySignup.this, "Sign up Successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ActivitySignup.this, "Sign up Failed!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ActivitySignup.this, "User already exists!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivitySignup.this, ActivityLogin.class);
                startActivity(intent);
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        return email.matches(emailPattern);
    }
}
