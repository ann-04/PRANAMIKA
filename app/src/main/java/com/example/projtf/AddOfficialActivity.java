package com.example.projtf;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddOfficialActivity extends AppCompatActivity {

    private EditText officialNameInput, officialPositionInput;
    private Button addOfficialButton, deleteOfficialButton;
    private DBHandler dbHandler;
    private int deptId;  // Department ID that will be assigned to the official

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_official);

        // Initialize views
        officialNameInput = findViewById(R.id.officialNameInput);
        officialPositionInput = findViewById(R.id.officialPositionInput);
        addOfficialButton = findViewById(R.id.addOfficialButton);
        deleteOfficialButton = findViewById(R.id.deleteOfficialButton);

        // Initialize the DBHandler
        dbHandler = new DBHandler(this);

        // Get department ID passed from previous activity (you could also pass department name if needed)
        deptId = getIntent().getIntExtra("DEPARTMENT_ID", -1);

        addOfficialButton.setOnClickListener(v -> {
            String officialName = officialNameInput.getText().toString().trim();
            String officialPosition = officialPositionInput.getText().toString().trim();

            if (officialName.isEmpty() || officialPosition.isEmpty()) {
                Toast.makeText(AddOfficialActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if (deptId == -1) {
                Toast.makeText(AddOfficialActivity.this, "Invalid department selected", Toast.LENGTH_SHORT).show();
            } else {
                boolean isAdded = dbHandler.addOfficial(officialName, officialPosition, deptId);
                if (isAdded) {
                    Toast.makeText(AddOfficialActivity.this, "Official added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddOfficialActivity.this, "Official already exists in this department", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Delete Official Button functionality
        deleteOfficialButton.setOnClickListener(v -> showDeleteOfficialDialog());
    }

    private void showDeleteOfficialDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Official");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("Delete", (dialog, which) -> {
            String officialName = input.getText().toString().trim();
            if (!officialName.isEmpty()) {
                // Get the official ID by name and delete the official
                int officialId = dbHandler.getOfficialIdByName(officialName, deptId);
                if (officialId != -1) {
                    boolean result = dbHandler.deleteOfficial(officialId);
                    if (result) {
                        Toast.makeText(AddOfficialActivity.this, "Official deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();  // Close the activity
                    } else {
                        Toast.makeText(AddOfficialActivity.this, "Error deleting official", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddOfficialActivity.this, "Official not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AddOfficialActivity.this, "Please enter the official name", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}