package com.example.doanapk_qlqa_nhom9;

import java.util.ArrayList;

public class OrderHistory {
    private ArrayList<Cart> cartList;
    private double totalAmount;
    private String orderDate;

    public OrderHistory(ArrayList<Cart> cartList, double totalAmount, String orderDate) {
        this.cartList = cartList;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
    }

    public OrderHistory() {
        // Default constructor required for Firebase
    }

    // Getters and setters

    public ArrayList<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(ArrayList<Cart> cartList) {
        this.cartList = cartList;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
