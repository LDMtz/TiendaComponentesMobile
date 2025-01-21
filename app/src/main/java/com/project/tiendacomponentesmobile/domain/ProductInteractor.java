package com.project.tiendacomponentesmobile.domain;

import com.project.tiendacomponentesmobile.data.repository.ProductRepository;
import com.project.tiendacomponentesmobile.domain.model.Cart;
import com.project.tiendacomponentesmobile.domain.model.submodel.ProductCard;
import com.project.tiendacomponentesmobile.domain.model.submodel.ProductCart;

import java.util.List;

import retrofit2.Callback;

public class ProductInteractor {
    private final ProductRepository productRepository;

    public ProductInteractor(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void getProductsWithLimit(int limit, Callback<List<ProductCard>> callback) {
        productRepository.getProductsWithLimit(limit, callback);
    }

    public void getClientCart(int accountId, Callback<List<ProductCart>> callback) {
        productRepository.getClientCart(accountId, callback);
    }

    public void addProductToCart(int accountId, int productId, Callback<Cart> callback){
        productRepository.addProductToCart(accountId, productId, callback);
    }

    public void deleteProductFromCart(int accountId, int productId, Callback<Void> callback){
        productRepository.deleteProductFromCart(accountId, productId, callback);
    }

        public void increaseQuantityCart(int accountId, int productId, Callback<Cart> callback){
            productRepository.increaseQuantityCart(accountId, productId, callback);
        }

    public void decreaseQuantityCart(int accountId, int productId, Callback<Cart> callback){
        productRepository.decreaseQuantityCart(accountId, productId, callback);
    }

    public void removeAllProductsFromCart(int accountId, Callback<Void> callback){
        productRepository.removeAllProductsFromCart(accountId, callback);
    }

}
