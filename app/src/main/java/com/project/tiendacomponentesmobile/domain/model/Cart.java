package com.project.tiendacomponentesmobile.domain.model;

import com.google.gson.annotations.SerializedName;

public class Cart {
    @SerializedName("cart_id")
    private int id;

    @SerializedName("product_id")
    private int productId;

    @SerializedName("quantity")
    private int quantity;

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
