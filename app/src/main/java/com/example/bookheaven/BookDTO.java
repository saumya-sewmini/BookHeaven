package com.example.bookheaven;

public class BookDTO {

    private int id;
    private String bookTitle;
    private String description;
    private Double price;
    private int qty;
    private Double shipping_price;
    private AuthorDTO author;
    private static String latitude;
    private static String longitude;
    private BookStatusDTO bookStatus;
    private BookCatergoryDTO bookCatergory;

    private String imageUrl;

    public BookDTO(String bookTitle, double price, String imageUrl, String description) {
        this.bookTitle = bookTitle;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public static String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public static String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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
