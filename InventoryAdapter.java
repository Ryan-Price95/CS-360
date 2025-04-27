package com.example.inventoryapp;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.telephony.SmsManager;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {

    private final List<InventoryItem> itemList;
    private final DatabaseHelper db;
    private final Context context;

    public InventoryAdapter(Context context, List<InventoryItem> itemList, DatabaseHelper db) {
        this.context = context;
        this.itemList = itemList;
        this.db = db;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inventory, parent, false);
        return new InventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        InventoryItem item = itemList.get(position);

        holder.name.setText(item.name);
        holder.code.setText(String.valueOf(item.id));
        holder.quantity.setText(String.valueOf(item.quantity));
        holder.location.setText(item.location);

        holder.itemView.setOnLongClickListener(v -> {
            showItemOptionsDialog(item, position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class InventoryViewHolder extends RecyclerView.ViewHolder {
        TextView name, code, quantity, location;

        public InventoryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textItemName);
            code = itemView.findViewById(R.id.textItemCode);
            quantity = itemView.findViewById(R.id.textItemQuantity);
            location = itemView.findViewById(R.id.textItemLocation);
        }
    }

    // Long press on item brings up Options dialog to delete or update quantity
    private void showItemOptionsDialog(InventoryItem item, int position) {
        String[] options = {"Update Quantity", "Delete Item"};
        new AlertDialog.Builder(context)
                .setTitle(item.name)
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showUpdateDialog(item, position);
                    } else {
                        db.deleteInventoryItem(item.id);
                        itemList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    // Update Quantity dialog box
    private void showUpdateDialog(InventoryItem item, int position) {
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(String.valueOf(item.quantity));
        input.setHint("Enter new quantity");

        new AlertDialog.Builder(context)
                .setTitle("Update Quantity for " + item.name)
                .setView(input)
                .setPositiveButton("Update", (dialog, which) -> {
                    String inputText = input.getText().toString().trim();

                    if (inputText.isEmpty()) {
                        Toast.makeText(context, "Quantity cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int newQty = Integer.parseInt(inputText);
                    db.updateInventoryQuantity(item.id, newQty);
                    item.quantity = newQty;
                    notifyItemChanged(position);

                    if (newQty == 0) {
                        sendOutOfStockSms(item.id, item.name);
                    }

                    Toast.makeText(context, "Quantity updated", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Out of Stock SMS
    private void sendOutOfStockSms(int itemId, String itemName) {
        String phoneNumber = "+15551234567";
        String message = "ALERT: Item ID " + itemId + " (" + itemName + ") is now out of stock.";

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(context, "Out-of-stock SMS sent", Toast.LENGTH_SHORT).show();
        } else {
            // No SMS sent — permission not granted
            Toast.makeText(context, "SMS not sent: permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
