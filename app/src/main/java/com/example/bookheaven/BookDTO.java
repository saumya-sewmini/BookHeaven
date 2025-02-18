package com.example.bookheaven;

public class BookDTO {

    private String bookTitle;
    private double price;
    private String imageUrl;
    private String description;

    public BookDTO(String bookTitle, double price, String imageUrl, String description) {
        this.bookTitle = bookTitle;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
