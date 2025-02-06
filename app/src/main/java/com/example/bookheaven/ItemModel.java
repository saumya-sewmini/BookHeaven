package com.example.bookheaven;

public class ItemModel {

    private int imageResId;
    private String itemName;

    public ItemModel(int imageResId, String itemName) {
        this.imageResId = imageResId;
        this.itemName = itemName;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getItemName() {
        return itemName;
    }

}
