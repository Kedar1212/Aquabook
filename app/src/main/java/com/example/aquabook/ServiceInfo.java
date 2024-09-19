package com.example.aquabook;

public class ServiceInfo {
    private String name;
    private String location;

    public ServiceInfo(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
