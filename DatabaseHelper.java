package com.example.inventoryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.widget.Toast;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "InventoryApp.db";
    public static final int DATABASE_VERSION = 1;

    // Users Table
    public static final String USER_TABLE = "users";
    public static final String USER_ID = "id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    // Inventory Table
    public static final String INVENTORY_TABLE = "inventory";
    public static final String ITEM_ID = "id";
    public static final String ITEM_NAME = "name";
    public static final String ITEM_QUANTITY = "quantity";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Table creation
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USER_TABLE + " (" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERNAME + " TEXT UNIQUE, " +
                PASSWORD + " TEXT)");

        db.execSQL("CREATE TABLE " + INVENTORY_TABLE + " (" +
                ITEM_ID + " INTEGER PRIMARY KEY, " +
                ITEM_NAME + " TEXT, " +
                ITEM_QUANTITY + " INTEGER, " +
                "location TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INVENTORY_TABLE);
        onCreate(db);
    }

    // User functions
    public boolean createUser(String username, String password) {
        ContentValues values = new ContentValues();
        values.put(USERNAME, username);
        values.put(PASSWORD, password);

        long result = getWritableDatabase().insert(USER_TABLE, null, values);
        return result != -1;
    }

    // Check if user exists in database
    public boolean checkUser(String username, String password) {
        Cursor cursor = getReadableDatabase().query(
                USER_TABLE,
                null,
                USERNAME + "=? AND " + PASSWORD + "=?",
                new String[]{username, password},
                null, null, null
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Get all inventory items
    public ArrayList<InventoryItem> getAllInventoryItems() {
        ArrayList<InventoryItem> itemList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + INVENTORY_TABLE, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            int qty = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
            String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
            itemList.add(new InventoryItem(id, name, qty, location));
        }
        cursor.close();
        return itemList;
    }

    // Add new inventory item
    public boolean addInventoryItem(int id, String name, int quantity, String location) {
        if (itemIdExists(id)) {
            return false; // ID already exists
        }

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("location", location);

        long result = getWritableDatabase().insert(INVENTORY_TABLE, null, values);
        return result != -1;
    }

    // Delete inventory item by ID
    public void deleteInventoryItem(int id) {
        getWritableDatabase().delete(INVENTORY_TABLE, "id=?", new String[]{String.valueOf(id)});
    }

    // Update item quantity
    public void updateInventoryQuantity(int id, int newQty) {
        ContentValues values = new ContentValues();
        values.put("quantity", newQty);
        getWritableDatabase().update(INVENTORY_TABLE, values, "id=?", new String[]{String.valueOf(id)});
    }

    // Checks if an item ID already exists in the inventory table
    public boolean itemIdExists(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM inventory WHERE id = ?", new String[]{String.valueOf(id)});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
}
