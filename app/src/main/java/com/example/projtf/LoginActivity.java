package com.example.projtf;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private RadioGroup roleRadioGroup; // RadioGroup for Role Selection
    private DBHandler dbHandler; // SQLite Database Helper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Views
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        Button loginButton = findViewById(R.id.login_button);
        roleRadioGroup = findViewById(R.id.role_radio_group); // RadioGroup for role selection
        TextView signupRedirectText = findViewById(R.id.signupRedirectText); // Redirect Text

        dbHandler = new DBHandler(this); // Initialize SQLite DB Helper

        // Login Button Click Listener
        loginButton.setOnClickListener(view -> {
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            // Input Validation
            if (email.isEmpty()) {
                loginEmail.setError("Email cannot be empty");
                return;
            }
            if (password.isEmpty()) {
                loginPassword.setError("Password cannot be empty");
                return;
            }

            // Get selected role from RadioGroup
            int selectedRoleId = roleRadioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedRoleId);

            if (selectedRadioButton == null) {
                Toast.makeText(LoginActivity.this, "Please select a role.", Toast.LENGTH_SHORT).show();
                return;
            }

            String role = selectedRadioButton.getText().toString(); // Get selected role (Admin or User)

            // Validate User based on role
            String storedRole = dbHandler.getUserRole(email, password);

            if (storedRole == null) {
                Toast.makeText(LoginActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
            } else {
                if (storedRole.equals(role)) {
                    // Role matches, navigate to the respective dashboard
                    if (role.equals("User")) {
                        startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
                    } else if (role.equals("Admin")) {
                        startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                    }
                    finish(); // Close Login Activity
                } else {
                    Toast.makeText(LoginActivity.this, "Role mismatch. Please select the correct role.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Redirect to Sign-Up Activity if user is not registered
        signupRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, Signup_activity.class);
            startActivity(intent);
        });
    }
}
