package com.example.bookheaven;

public class SingleProductModel {

    private int id;
    private String book_title;
    private String description;
    private double price;
    private int qty;
    private double shipping_price;
    private String author;
    private String sellerContact;
    private String imageUrl;

    public SingleProductModel(int id, String book_title, String description, double price, int qty, double shipping_price, String author, String sellerContact, String imageUrl) {
        this.id = id;
        this.book_title = book_title;
        this.description = description;
        this.price = price;
        this.qty = qty;
        this.shipping_price = shipping_price;
        this.author = author;
        this.sellerContact = sellerContact;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getShipping_price() {
        return shipping_price;
    }

    public void setShipping_price(double shipping_price) {
        this.shipping_price = shipping_price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSellerContact() {
        return sellerContact;
    }

    public void setSellerContact(String sellerContact) {
        this.sellerContact = sellerContact;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
