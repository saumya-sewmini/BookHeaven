package com.example.bookheaven;

public class BookCatergoryDTO {
    private int id;
    private String catergory;

    public BookCatergoryDTO(int id, String catergory) {
        this.id = id;
        this.catergory = catergory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCatergory() {
        return catergory;
    }

    public void setCatergory(String catergory) {
        this.catergory = catergory;
    }
}
