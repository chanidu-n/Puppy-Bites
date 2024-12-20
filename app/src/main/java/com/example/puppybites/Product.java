package com.example.puppybites;

public class Product {
    private int id;
    private String name;
    private double price;
    private String category;
    private String condition;
    private String description;
    private String location;
    private byte[] image;

    public Product(int id, String name, double price, String category, String condition, String description, String location, byte[] image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.condition = condition;
        this.description = description;
        this.location = location;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getCondition() {
        return condition;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public byte[] getImage() {
        return image;
    }
}
