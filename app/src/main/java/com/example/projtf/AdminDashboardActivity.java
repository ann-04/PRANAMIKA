package com.example.projtf;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class AdminDashboardActivity extends AppCompatActivity {

    private DBHandler dbHandler;
    private ListView departmentListView;
    private ArrayAdapter<String> departmentAdapter;
    private ArrayList<String> departmentList;
    private ArrayList<Integer> departmentIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        dbHandler = new DBHandler(this);
        departmentListView = findViewById(R.id.departmentListView);
        Button addDepartmentButton = findViewById(R.id.addDepartmentButton);
        Button deleteDepartmentButton = findViewById(R.id.deleteDepartmentButton);

        departmentList = new ArrayList<>();
        departmentIds = new ArrayList<>();
        departmentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, departmentList);
        departmentListView.setAdapter(departmentAdapter);

        loadDepartments();

        addDepartmentButton.setOnClickListener(v -> showAddDepartmentDialog());

        // Set up the delete department functionality
        deleteDepartmentButton.setOnClickListener(v -> showDeleteDepartmentDialog());

        departmentListView.setOnItemClickListener((parent, view, position, id) -> {
            int deptId = departmentIds.get(position);
            Intent intent = new Intent(AdminDashboardActivity.this, AddOfficialActivity.class);
            intent.putExtra("DEPARTMENT_ID", deptId);  // Use "DEPARTMENT_ID" as the key to match AddOfficialActivity
            intent.putExtra("DEPT_NAME", departmentList.get(position));
            startActivity(intent);

        });
    }

    private void loadDepartments() {
        departmentList.clear();
        departmentIds.clear();
        Cursor cursor = dbHandler.getAllDepartments();
        if (cursor.moveToFirst()) {
            do {
                int deptIdIndex = DBHandler.getColumnIndex(cursor, DBHandler.COLUMN_DEPT_ID);
                int deptNameIndex = DBHandler.getColumnIndex(cursor, DBHandler.COLUMN_DEPT_NAME);
                int deptId = cursor.getInt(deptIdIndex);
                String deptName = cursor.getString(deptNameIndex);
                departmentIds.add(deptId);
                departmentList.add(deptName);
            } while (cursor.moveToNext());
        }
        cursor.close();
        departmentAdapter.notifyDataSetChanged();
    }

    private void showAddDepartmentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Department");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("Add", (dialog, which) -> {
            String deptName = input.getText().toString();
            if (!deptName.isEmpty()) {
                long result = dbHandler.addDepartment(deptName);
                if (result != -1) {
                    Toast.makeText(AdminDashboardActivity.this, "Department added successfully", Toast.LENGTH_SHORT).show();
                    loadDepartments();
                } else {
                    Toast.makeText(AdminDashboardActivity.this, "Failed to add department", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showDeleteDepartmentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Department");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("Delete", (dialog, which) -> {
            String deptName = input.getText().toString();
            if (!deptName.isEmpty()) {
                // Get the department ID by name and delete the department
                int deptId = dbHandler.getDepartmentIdByName(deptName);
                if (deptId != -1) {
                    boolean result = dbHandler.deleteDepartment(deptId);
                    if (result) {
                        Toast.makeText(AdminDashboardActivity.this, "Department deleted successfully", Toast.LENGTH_SHORT).show();
                        loadDepartments();  // Reload departments list
                    } else {
                        Toast.makeText(AdminDashboardActivity.this, "Failed to delete department", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminDashboardActivity.this, "Department not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}

