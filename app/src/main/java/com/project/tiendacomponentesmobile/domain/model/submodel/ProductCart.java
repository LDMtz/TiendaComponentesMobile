package com.project.tiendacomponentesmobile.domain.model.submodel;

import com.project.tiendacomponentesmobile.domain.model.Cart;

public class ProductCart {
    private ProductCard product;
    private Cart cart;

    // Getters y setters
    public ProductCard getProduct() {
        return product;
    }

    public void setProduct(ProductCard product) {
        this.product = product;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
