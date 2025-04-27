package com.example.inventoryapp;

public class InventoryItem {
    public String name;
    public int id;
    public int quantity;
    public String location;

    public InventoryItem(int id, String name, int quantity, String location) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.location = location;
    }
}
