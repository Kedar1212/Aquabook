// com.example.aquabook.CartItem.java
package com.example.aquabook;

public class CartItem {
    private String key; // new field
    private String serviceName;
    private double totalPrice;
    private boolean selected;

    public CartItem() {
        // Required empty constructor for Firebase
    }

    public CartItem(String key, String serviceName, double totalPrice) {
        this.key = key;
        this.serviceName = serviceName;
        this.totalPrice = totalPrice;
        this.selected = false;
    }

    public String getKey() {
        return key;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
