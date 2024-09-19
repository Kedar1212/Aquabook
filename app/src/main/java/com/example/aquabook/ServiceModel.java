package com.example.aquabook;

public class ServiceModel {

    private String serviceName;
    private String location;
    private String selectedItem;
    private String imageUrl;
    private double jarPrice ;
    private double bottlePrice ;
    private String phoneNumber; // Added phone number field

    public ServiceModel() {
        // Default constructor
    }

    public ServiceModel(String serviceName, String location, String selectedItem, String imageUrl,
                        double jarPrice  , double bottlePrice, String phoneNumber) {
        this.serviceName = serviceName;
        this.location = location;
        this.selectedItem = selectedItem;
        this.imageUrl = imageUrl;
        this.jarPrice = jarPrice;
        this.bottlePrice = bottlePrice;
        this.phoneNumber = phoneNumber;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getLocation() {
        return location;
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getJarPrice() {
        return jarPrice;
    }

    public void setJarPrice(double jarPrice) {
        this.jarPrice = jarPrice;
    }

    public double getBottlePrice() {
        return bottlePrice;
    }

    public void setBottlePrice(double bottlePrice) {
        this.bottlePrice = bottlePrice;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
