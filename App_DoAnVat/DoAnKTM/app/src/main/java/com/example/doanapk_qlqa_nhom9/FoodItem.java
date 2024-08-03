package com.example.doanapk_qlqa_nhom9;

public class FoodItem {
    private String key;  // Add this line
    private String Name;
    private double Cost;
    private String Picture;

    public FoodItem() {
    }

    public FoodItem(String Name, double Cost, String Picture) {
        this.Name = Name;
        this.Cost = Cost;
        this.Picture = Picture;
    }

    // Getter và setter cho key
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        this.Picture = picture;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public double getCost() {
        return Cost;
    }

    public void setCost(double cost) {
        this.Cost = cost;
    }
}
