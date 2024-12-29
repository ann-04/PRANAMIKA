package com.example.projtf;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Signup_activity extends AppCompatActivity {

    private EditText signupEmail, signupPassword;
    private RadioGroup roleRadioGroup; // RadioGroup for Role Selection
    private DBHandler dbHandler; // SQLite Database Helper

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Views
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        roleRadioGroup = findViewById(R.id.role_radio_group); // RadioGroup for role selection
        Button signupButton = findViewById(R.id.signup_button);
        TextView loginRedirectText = findViewById(R.id.loginRedirectText);

        dbHandler = new DBHandler(this); // Initialize SQLite DB Helper

        // Sign-Up Button Click Listener
        signupButton.setOnClickListener(view -> {
            String email = signupEmail.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();

            // Get the selected role
            int selectedRoleId = roleRadioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedRoleId);
            String role = selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";

            // Input Validation
            if (email.isEmpty()) {
                signupEmail.setError("Email cannot be empty");
                return;
            }
            if (password.isEmpty()) {
                signupPassword.setError("Password cannot be empty");
                return;
            }
            if (role.isEmpty()) {
                Toast.makeText(Signup_activity.this, "Please select a role.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if Email Already Exists
            if (dbHandler.checkEmailExists(email)) {
                Toast.makeText(Signup_activity.this, "Email already registered!", Toast.LENGTH_SHORT).show();
            } else {
                // Add User/Admin to Database
                String feedback = dbHandler.addUserWithFeedback(email, password, role);
                Toast.makeText(Signup_activity.this, feedback, Toast.LENGTH_SHORT).show();

                if (feedback.equals("User registered successfully.")) {
                    Toast.makeText(Signup_activity.this, "Registration successful! You can now log in.", Toast.LENGTH_SHORT).show();
                    // Redirect to Login Screen
                    Intent intent = new Intent(Signup_activity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        // Redirect to Login Page
        loginRedirectText.setOnClickListener(view -> startActivity(new Intent(Signup_activity.this, LoginActivity.class)));
    }
}
