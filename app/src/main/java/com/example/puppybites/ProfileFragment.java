package com.example.puppybites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.puppybites.ActivityLogin;
import com.example.puppybites.DatabaseHelper;
import com.example.puppybites.R;
import com.example.puppybites.SearchFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private EditText emailEditText, usernameEditText, contactEditText, addressEditText;
    private Button saveButton, logoutButton;
    private DatabaseHelper databaseHelper;
    private String userEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity().getIntent().hasExtra("user_email")) {
            userEmail = getActivity().getIntent().getStringExtra("user_email");
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        databaseHelper = new DatabaseHelper(getContext());

        emailEditText = view.findViewById(R.id.profile_email);
        usernameEditText = view.findViewById(R.id.profile_username);
        contactEditText = view.findViewById(R.id.profile_contact);
        addressEditText = view.findViewById(R.id.profile_address);
        saveButton = view.findViewById(R.id.profile_save_button);
        logoutButton = view.findViewById(R.id.profile_logout_button);

        loadUserData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return view;
    }

    private void loadUserData() {
        Cursor cursor = databaseHelper.getUserData(userEmail);
        if (cursor.moveToFirst()) {
            int emailIndex = cursor.getColumnIndex("email");
            int usernameIndex = cursor.getColumnIndex("username");
            int contactIndex = cursor.getColumnIndex("contact");
            int addressIndex = cursor.getColumnIndex("address");

            if (emailIndex != -1 && usernameIndex != -1 && contactIndex != -1 && addressIndex != -1) {
                String email = cursor.getString(emailIndex);
                String username = cursor.getString(usernameIndex);
                String contact = cursor.getString(contactIndex);
                String address = cursor.getString(addressIndex);

                emailEditText.setText(email);
                usernameEditText.setText(username);
                contactEditText.setText(contact);
                addressEditText.setText(address);
            }
        }
        cursor.close();
    }

    private void saveChanges() {
        String username = usernameEditText.getText().toString();
        String contact = contactEditText.getText().toString();
        String address = addressEditText.getText().toString();

        boolean isUpdated = databaseHelper.updateUserData(userEmail, username, contact, address);

        if (isUpdated) {
            Toast.makeText(getContext(), "Changes saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to save changes", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        // Clear user session, for example, SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to login activity
        Intent intent = new Intent(getActivity(), ActivityLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

