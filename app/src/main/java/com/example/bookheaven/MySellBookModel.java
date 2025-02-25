package com.example.bookheaven;

public class MySellBookModel {

    private int id;
    private String title;
    private String imageUrl;
    private int addUser;
    private int bookId;
    private int availableQty;
    private int sellingQty;
    private int status;
    private double profit;

    public MySellBookModel(int id, String title, String imageUrl, int addUser, int bookId, int availableQty, int sellingQty, int status, double profit) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.addUser = addUser;
        this.bookId = bookId;
        this.availableQty = availableQty;
        this.sellingQty = sellingQty;
        this.status = status;
        this.profit = profit;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getAddUser() {
        return addUser;
    }

    public void setAddUser(int addUser) {
        this.addUser = addUser;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(int availableQty) {
        this.availableQty = availableQty;
    }

    public int getSellingQty() {
        return sellingQty;
    }

    public void setSellingQty(int sellingQty) {
        this.sellingQty = sellingQty;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }
}
