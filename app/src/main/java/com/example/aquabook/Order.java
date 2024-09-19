package com.example.aquabook;

public class Order {

    private String name;
    private String phone;
    private String address;
    private double totalPrice;

    private String serviceName;

    // Required default constructor
    public Order() {
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    // Corrected constructor with totalPrice parameter
    public Order(String name, String phone, String address,String serviceName, double totalPrice) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.serviceName = serviceName;
        this.totalPrice = totalPrice;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}

