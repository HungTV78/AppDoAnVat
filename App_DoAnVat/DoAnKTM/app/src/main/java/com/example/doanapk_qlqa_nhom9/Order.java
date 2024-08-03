package com.example.doanapk_qlqa_nhom9;

public class Order {
    private String name;
    private int quantity;
    private String orderDate;
    private int totalAmount;

    /**
     * Constructor mặc định cần thiết cho việc gọi DataSnapshot.getValue(Order.class)
     */
    public Order() {
        // Không có tham số được khởi tạo trong constructor mặc định
    }

    /**
     * Constructor với đầy đủ tham số để khởi tạo một đối tượng Order.
     */
    public Order(String name, int quantity, String orderDate, int totalAmount) {
        this.name = name;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }

    // Getters và Setters cho các thuộc tính của Order
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
}

