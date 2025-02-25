package com.example.bookheaven;

import java.util.List;

public class ResponseDTO {


    private boolean success;
    private Object content;
    private List<CartItem> cartItemsList;
    private List<OrderModel> orderModelList;
    private List<MySellBookModel> mySellBookModelList;
    private List<HomeItemModel> homeBookModels;

    public ResponseDTO(){}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<CartItem> getCartItemsList() {
        return cartItemsList;
    }

    public void setCartItemsList(List<CartItem> cartItemsList) {
        this.cartItemsList = cartItemsList;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public List<OrderModel> getOrderModelList() {
        return orderModelList;
    }

    public void setOrderModelList(List<OrderModel> orderModelList) {
        this.orderModelList = orderModelList;
    }

    public List<MySellBookModel> getMySellBookModelList() {
        return mySellBookModelList;
    }

    public void setMySellBookModelList(List<MySellBookModel> mySellBookModelList) {
        this.mySellBookModelList = mySellBookModelList;
    }

    public List<HomeItemModel> getHomeBookModels() {
        return homeBookModels;
    }

    public void setHomeBookModels(List<HomeItemModel> homeBookModels) {
        this.homeBookModels = homeBookModels;
    }
}
