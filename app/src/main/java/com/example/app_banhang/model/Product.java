package com.example.app_banhang.model;

public class Product {
    private int id;
    private String title;
    private int price;
    private int quantity;
    private String image;

    public Product(int id, String title, int price, int quantity) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(String title, int price, int quantity) {
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(int id, String title, int price, int quantity, String image) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }


    public Product(String title, int price, int quantity, String image) {
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
