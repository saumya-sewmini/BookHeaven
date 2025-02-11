package com.example.bookheaven;

public class HomeItemModel {
    private String title;
    private double price;
    private int imageResId;

    public HomeItemModel(String title, double price, int imageResId) {
        this.title = title;
        this.price = price;
        this.imageResId = imageResId;
    }

    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public int getImageResId() { return imageResId; }
}

