package com.example.inventoryapp;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DividerItemDecoration;

import java.util.ArrayList;

public class InventoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    InventoryAdapter adapter;
    DatabaseHelper db;
    ArrayList<InventoryItem> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewInventory);

        itemList = db.getAllInventoryItems(); // pulls from SQLite
        adapter = new InventoryAdapter(this, itemList, db);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        findViewById(R.id.buttonAddItem).setOnClickListener(view -> showAddItemDialog());
        findViewById(R.id.buttonNotificationSettings).setOnClickListener(view -> {
            Intent intent = new Intent(InventoryActivity.this, SmsNotificationActivity.class);
            startActivity(intent);
        });
    }

    // Add item dialog box
    private void showAddItemDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_item, null);
        EditText itemIdInput = dialogView.findViewById(R.id.editTextItemId);
        EditText itemNameInput = dialogView.findViewById(R.id.editTextItemName);
        EditText itemQtyInput = dialogView.findViewById(R.id.editTextItemQuantity);
        EditText itemLocationInput = dialogView.findViewById(R.id.editTextItemLocation);

        new AlertDialog.Builder(this)
                .setTitle("Add Inventory Item")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String idText = itemIdInput.getText().toString().trim();
                    String name = itemNameInput.getText().toString().trim();
                    String qtyText = itemQtyInput.getText().toString().trim();
                    String location = itemLocationInput.getText().toString().trim();

                    if (idText.isEmpty() || name.isEmpty() || qtyText.isEmpty()) {
                        Toast.makeText(this, "ID, name, and quantity are required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        int id = Integer.parseInt(idText);
                        int qty = Integer.parseInt(qtyText);

                        boolean success = db.addInventoryItem(id, name, qty, location);
                        if (!success) {
                            Toast.makeText(this, "An item with this ID already exists.", Toast.LENGTH_SHORT).show();
                        } else {
                            refreshInventory();
                            Toast.makeText(this, "Item added successfully.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "ID and Quantity must be valid numbers.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Refresh inventory after changes are made
    private void refreshInventory() {
        itemList.clear();
        itemList.addAll(db.getAllInventoryItems());
        adapter.notifyDataSetChanged();
    }

    // Toast whether or not SMS permissions are granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
