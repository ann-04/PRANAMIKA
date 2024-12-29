package com.example.projtf;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;  // Import ArrayList class

public class OfficialListActivity extends AppCompatActivity {

    private DBHandler dbHandler;
    private ListView officialsListView;
    private ArrayList<String> officialsList;
    private ArrayAdapter<String> officialsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officials);

        // Initialize the database handler
        dbHandler = new DBHandler(this);

        // Set up the ListView for officials
        officialsListView = findViewById(R.id.officialsListView);
        officialsList = new ArrayList<>();  // Initialize the ArrayList
        officialsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, officialsList);
        officialsListView.setAdapter(officialsAdapter);

        // Get the department name passed from previous activity
        String departmentName = getIntent().getStringExtra("DEPARTMENT_NAME");

        if (departmentName != null) {
            // Load officials for the selected department
            loadOfficials(departmentName);
        } else {
            Toast.makeText(this, "No department selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadOfficials(String departmentName) {
        officialsList.clear();
        // Query the database for officials belonging to the selected department
        Cursor cursor = dbHandler.getOfficialsByDepartment(departmentName);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Get the official name and position
                String officialName = cursor.getString(cursor.getColumnIndex(DBHandler.COLUMN_OFFICIAL_NAME));
                String officialPosition = cursor.getString(cursor.getColumnIndex(DBHandler.COLUMN_OFFICIAL_POSITION));
                officialsList.add(officialName + " - " + officialPosition);  // Add to the list
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Toast.makeText(this, "No officials found for this department", Toast.LENGTH_SHORT).show();
        }

        // Notify the adapter to update the ListView
        officialsAdapter.notifyDataSetChanged();
    }
}