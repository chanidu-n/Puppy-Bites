package com.example.puppybites;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String databaseName = "PuppyBites.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, databaseName, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        // Create Users Table
        MyDatabase.execSQL("CREATE TABLE Users(email TEXT PRIMARY KEY, password TEXT, username TEXT, contact TEXT, address TEXT, role TEXT)");

        // Create Items Table
        MyDatabase.execSQL("CREATE TABLE Items(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, price TEXT, category TEXT, description TEXT, location TEXT, contact TEXT, image_url TEXT, seller_email TEXT)");

    }



    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("DROP TABLE IF EXISTS Users");
        MyDB.execSQL("DROP TABLE IF EXISTS Items");
        onCreate(MyDB);

    }

    // User Methods
    public Boolean insertData(String email, String password, String role) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("role", role); // Ensure this line is present
        long result = MyDatabase.insert("Users", null, contentValues);
        return result != -1;
    }


    public Boolean checkEmail(String email) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM Users WHERE email = ?", new String[]{email});
        return cursor.getCount() > 0;
    }

    public Boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM Users WHERE email = ? and password = ?", new String[]{email, password});
        return cursor.getCount() > 0;
    }

    public Cursor getUserData(String email) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        return MyDatabase.rawQuery("SELECT * FROM Users WHERE email = ?", new String[]{email});
    }

    public boolean updateUserData(String email, String username, String contact, String address) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("contact", contact);
        contentValues.put("address", address);
        long result = MyDatabase.update("Users", contentValues, "email = ?", new String[]{email});
        return result != -1;
    }

    // Item Methods

    public boolean insertItem(String title, String price, String category, String description, String location, String contact, String imageUrl) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("price", price);
        contentValues.put("category", category);
        contentValues.put("description", description);
        contentValues.put("location", location);
        contentValues.put("contact", contact);
        contentValues.put("image_url", imageUrl);
        contentValues.put("seller_email", getCurrentUserEmail()); // Ensure this method returns a valid email

        long result = MyDatabase.insert("Items", null, contentValues);

        return result != -1;
    }

    // Method to get the current user's email (replace with your own implementation)
    private String getCurrentUserEmail() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_email", null); // Ensure this value is not null
    }

    public Cursor getAllItems() {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        return MyDatabase.rawQuery("SELECT * FROM Items", null);
    }

    public Cursor getItemById(int id) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        return MyDatabase.rawQuery("SELECT * FROM Items WHERE id = ?", new String[]{String.valueOf(id)});
    }
}

