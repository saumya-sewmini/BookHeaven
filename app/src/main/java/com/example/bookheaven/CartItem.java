package com.example.bookheaven;

import android.net.Uri;

public class CartItem {

    private int id;
    private String title;
    private double price;
    private double tot_price;
    private double tot_shipping;
    private String imageUrl;
    private int quantity;

    public CartItem(int id, String title, double price, double tot_price, double tot_shipping, String imageUrl, int quantity) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.tot_price = tot_price;
        this.tot_shipping = tot_shipping;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTot_price() {
        return tot_price;
    }

    public void setTot_price(double tot_price) {
        this.tot_price = tot_price;
    }

    public double getTot_shipping() {
        return tot_shipping;
    }

    public void setTot_shipping(double tot_shipping) {
        this.tot_shipping = tot_shipping;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
