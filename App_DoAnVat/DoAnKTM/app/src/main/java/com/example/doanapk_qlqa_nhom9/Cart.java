package com.example.doanapk_qlqa_nhom9;
public class Cart {
    private String key; // Key của cart
    private String name;
    private double price;
    private int quantity;
    private String picture; // URL of the image

    public Cart() {
        // Empty constructor needed for Firebase
    }

    public Cart(String name, double price, int quantity, String picture) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.picture = picture;
    }

    public Cart(String name, double cost, int quantity) {
        this.name = name;
        this.price = cost;
        this.quantity = quantity;
    }

    public Cart(String name, String picture, double cost, int quantity) {
        this.name = name;
        this.price = cost;
        this.picture=picture;
        this.quantity = quantity;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getpicture() {
        return picture;
    }

    public void setpicture(String picture) {
        this.picture = picture;
    }

    public double getTotalPrice() {
        return price * quantity;
    }
}
