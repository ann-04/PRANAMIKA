package com.example.projtf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "project_data.db";
    private static final int DATABASE_VERSION = 1;

    // Table Users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";

    // Table departments
    public static final String TABLE_DEPARTMENTS = "departments";
    public static final String COLUMN_DEPT_ID = "dept_id";
    public static final String COLUMN_DEPT_NAME = "dept_name";

    // Table officials

    public static final String TABLE_OFFICIALS = "officials";
    public static final String COLUMN_OFFICIAL_ID = "official_id";
    public static final String COLUMN_OFFICIAL_NAME = "official_name";
    public static final String COLUMN_OFFICIAL_POSITION = "official_position";

    // Table reviews
    public static final String TABLE_REVIEWS = "reviews";
    public static final String COLUMN_REVIEW_ID = "review_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_EFFICIENCY_RATING = "efficiency_rating";
    public static final String COLUMN_TRANSPARENCY_RATING = "transparency_rating";
    public static final String COLUMN_RESPONSIVENESS_RATING = "responsiveness_rating";
    public static final String COLUMN_INTEGRITY_RATING = "integrity_rating";
    public static final String COLUMN_LEADERSHIP_RATING = "leadership_rating";
    public static final String COLUMN_DECISION_MAKING_RATING = "decision_making_rating";
    public static final String COLUMN_REVIEW_TEXT = "review_text";


    // SQL Query to Create Users Table
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_ROLE + " TEXT NOT NULL);";

    // SQL Queries to Create New Tables
    private static final String CREATE_TABLE_DEPARTMENTS =
            "CREATE TABLE " + TABLE_DEPARTMENTS + " (" +
                    COLUMN_DEPT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DEPT_NAME + " TEXT UNIQUE NOT NULL);";

    private static final String CREATE_TABLE_OFFICIALS =
            "CREATE TABLE " + TABLE_OFFICIALS + " (" +
                    COLUMN_OFFICIAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_OFFICIAL_NAME + " TEXT NOT NULL, " +
                    COLUMN_OFFICIAL_POSITION + " TEXT NOT NULL, " +
                    COLUMN_DEPT_ID + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_DEPT_ID + ") REFERENCES " + TABLE_DEPARTMENTS + "(" + COLUMN_DEPT_ID + "));";

    private static final String CREATE_TABLE_REVIEWS =
            "CREATE TABLE " + TABLE_REVIEWS + " (" +
                    COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_ID + " INTEGER, " +
                    COLUMN_OFFICIAL_ID + " INTEGER, " +
                    COLUMN_EFFICIENCY_RATING + " FLOAT, " +
                    COLUMN_TRANSPARENCY_RATING + " FLOAT, " +
                    COLUMN_RESPONSIVENESS_RATING + " FLOAT, " +
                    COLUMN_INTEGRITY_RATING + " FLOAT, " +
                    COLUMN_LEADERSHIP_RATING + " FLOAT, " +
                    COLUMN_DECISION_MAKING_RATING + " FLOAT, " +
                    COLUMN_REVIEW_TEXT + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "), " +
                    "FOREIGN KEY(" + COLUMN_OFFICIAL_ID + ") REFERENCES " + TABLE_OFFICIALS + "(" + COLUMN_OFFICIAL_ID + "));";


    // Constructor
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the Users Table
        try {
            db.execSQL(CREATE_TABLE_USERS);
            db.execSQL(CREATE_TABLE_DEPARTMENTS);
            db.execSQL(CREATE_TABLE_OFFICIALS);
            db.execSQL(CREATE_TABLE_REVIEWS);
        } catch (SQLException e) {
            Log.e("DBHandler", "Error creating table: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the old table if it exists and recreate
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEWS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFICIALS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPARTMENTS);
            onCreate(db);
        } catch (SQLException e) {
            Log.e("DBHandler", "Error upgrading database: " + e.getMessage());
        }
    }

    // Method to Add a New User or Admin to the Database
    public String addUserWithFeedback(String email, String password, String role) {


        try (SQLiteDatabase db = this.getWritableDatabase()){
            ContentValues values = new ContentValues();
            values.put(COLUMN_EMAIL, email);
            values.put(COLUMN_PASSWORD, password);
            values.put(COLUMN_ROLE, role);

            long result = db.insert(TABLE_USERS, null, values);
            if (result == -1) {
                return "Error: Unable to register user.";
            } else {
                return "User registered successfully.";
            }
        } catch (SQLException e) {
            Log.e("DBHandler", "Error inserting user: " + e.getMessage());
            return "Database error: " + e.getMessage();
        }
    }

    // Method to Check if an Email Already Exists in the Database
    public boolean checkEmailExists(String email) {
        // Using try-with-resources to automatically close the resources
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.query(TABLE_USERS,
                     new String[]{COLUMN_EMAIL},
                     COLUMN_EMAIL + "=?",
                     new String[]{email},
                     null, null, null)) {
            // Check if the email exists in the database
            return cursor.getCount() > 0;
        } catch (SQLException e) {
            Log.e("DBHandler", "Error checking email existence: " + e.getMessage());
            return false;
        }
    }

    // Method to Verify Login Credentials (with Role Check)
    public boolean checkLogin(String email, String password, String role) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.query(TABLE_USERS,
                     new String[]{COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_ROLE},
                     COLUMN_EMAIL + "=?",
                     new String[]{email},
                     null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
                int roleIndex = cursor.getColumnIndex(COLUMN_ROLE);

                if (passwordIndex != -1 && roleIndex != -1) {
                    String storedPassword = cursor.getString(passwordIndex);
                    String storedRole = cursor.getString(roleIndex);

                    // Check if both password and role match
                    return password.equals(storedPassword) && role.equals(storedRole);
                } else {
                    Log.e("DBHandler", "Password or Role column not found in the database.");
                }
            }
        } catch (SQLException e) {
            Log.e("DBHandler", "Error checking login credentials: " + e.getMessage());
        }
        return false;
    }


    // Method to Get User Role by Email
    public String getUserRole(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String role = null;

        try {
            cursor = db.query(TABLE_USERS,
                    new String[]{COLUMN_ROLE},
                    COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                    new String[]{email, password},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int roleIndex = cursor.getColumnIndex(COLUMN_ROLE);
                if (roleIndex != -1) {
                    role = cursor.getString(roleIndex);
                }
            }
        } catch (SQLException e) {
            Log.e("DBHandler", "Error retrieving user role: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return role; // Returns null if not found
    }

    //Methods for Department Operations
    public long addDepartment(String deptName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DEPT_NAME, deptName);
        return db.insert(TABLE_DEPARTMENTS, null, values);
    }

    public Cursor getAllDepartments() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_DEPARTMENTS, null, null, null, null, null, null);
    }

    // Method to add an official to the database
    public boolean addOfficial(String officialName, String officialPosition, int deptId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OFFICIAL_NAME, officialName);
        values.put(COLUMN_OFFICIAL_POSITION, officialPosition);
        values.put(COLUMN_DEPT_ID, deptId);  // Link the official to a department

        long result = db.insert(TABLE_OFFICIALS, null, values);
        db.close();
        return result != -1;  // Return true if insertion was successful, false if it failed

    }
    public int getDepartmentIdByName(String deptName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DEPARTMENTS, new String[]{COLUMN_DEPT_ID},
                COLUMN_DEPT_NAME + "=?", new String[]{deptName}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int deptId = cursor.getInt(cursor.getColumnIndex(COLUMN_DEPT_ID));
            cursor.close();
            return deptId;
        }
        return -1;  // Return -1 if department not found
    }

    public boolean deleteDepartment(int deptId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_DEPARTMENTS, COLUMN_DEPT_ID + " = ?", new String[]{String.valueOf(deptId)}) > 0;
    }

    public Cursor getOfficialsByDepartment(String departmentName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + TABLE_OFFICIALS + "." + DBHandler.COLUMN_OFFICIAL_NAME + ", " +
                TABLE_OFFICIALS + "." + DBHandler.COLUMN_OFFICIAL_POSITION + ", " +
                TABLE_OFFICIALS + "." + DBHandler.COLUMN_DEPT_ID + " " +
                "FROM " + DBHandler.TABLE_OFFICIALS + " " +
                "INNER JOIN " + DBHandler.TABLE_DEPARTMENTS + " ON " +
                DBHandler.TABLE_OFFICIALS + "." + DBHandler.COLUMN_DEPT_ID + " = " +
                DBHandler.TABLE_DEPARTMENTS + "." + DBHandler.COLUMN_DEPT_ID + " " +
                "WHERE " + DBHandler.TABLE_DEPARTMENTS + "." + DBHandler.COLUMN_DEPT_NAME + " = ?";

        return db.rawQuery(query, new String[]{departmentName});
    }
    public int getOfficialIdByName(String officialName, int deptId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_OFFICIALS, new String[]{COLUMN_OFFICIAL_ID},
                COLUMN_OFFICIAL_NAME + "=? AND " + COLUMN_DEPT_ID + "=?",
                new String[]{officialName, String.valueOf(deptId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int officialId = cursor.getInt(cursor.getColumnIndex(COLUMN_OFFICIAL_ID));
            cursor.close();
            return officialId;
        }
        return -1;  // Return -1 if official not found
    }

    public boolean deleteOfficial(int officialId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_OFFICIALS, COLUMN_OFFICIAL_ID + " = ?", new String[]{String.valueOf(officialId)}) > 0;
    }


    //Methods for Review Operations
    public long addReview(int userId, int officialId, float efficiencyRating, float transparencyRating,
                          float responsivenessRating, float integrityRating, float leadershipRating,
                          float decisionMakingRating, String reviewText) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_OFFICIAL_ID, officialId);
        values.put(COLUMN_EFFICIENCY_RATING, efficiencyRating);
        values.put(COLUMN_TRANSPARENCY_RATING, transparencyRating);
        values.put(COLUMN_RESPONSIVENESS_RATING, responsivenessRating);
        values.put(COLUMN_INTEGRITY_RATING, integrityRating);
        values.put(COLUMN_LEADERSHIP_RATING, leadershipRating);
        values.put(COLUMN_DECISION_MAKING_RATING, decisionMakingRating);
        values.put(COLUMN_REVIEW_TEXT, reviewText);
        return db.insert(TABLE_REVIEWS, null, values);
    }

    public Cursor getReviewsByOfficial(int officialId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_REVIEWS, null, COLUMN_OFFICIAL_ID + "=?", new String[]{String.valueOf(officialId)}, null, null, null);
    }

    public static int getColumnIndex(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex == -1) {
            throw new IllegalArgumentException("Column '" + columnName + "' does not exist in the cursor");
        }
        return columnIndex;
    }
}

