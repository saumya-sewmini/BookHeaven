package com.example.bookheaven;

public class OrderModel {

    private int id;
    private String date_time;
    private double total_Price;
    private String order_Status;

    public OrderModel(int id, String date_time, double total_Price, String order_Status) {
        this.id = id;
        this.date_time = date_time;
        this.total_Price = total_Price;
        this.order_Status = order_Status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public double getTotal_Price() {
        return total_Price;
    }

    public void setTotal_Price(double total_Price) {
        this.total_Price = total_Price;
    }

    public String getOrder_Status() {
        return order_Status;
    }

    public void setOrder_Status(String order_Status) {
        this.order_Status = order_Status;
    }
}

