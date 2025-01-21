package com.project.tiendacomponentesmobile.data.repository;

import com.google.gson.JsonObject;
import com.project.tiendacomponentesmobile.data.remote.api.ApiService;
import com.project.tiendacomponentesmobile.domain.model.Cart;
import com.project.tiendacomponentesmobile.domain.model.submodel.ProductCard;
import com.project.tiendacomponentesmobile.domain.model.submodel.ProductCart;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ProductRepository {
    private final ApiService apiService;

    public ProductRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public void getProductsWithLimit(int limit, Callback<List<ProductCard>> callback) {
        Call<List<ProductCard>> call = apiService.getProductsWithLimit(limit);
        call.enqueue(callback);
    }

    public void getClientCart(int accountId, Callback<List<ProductCart>> callback) {
        Call<List<ProductCart>> call = apiService.getClientCart(accountId);
        call.enqueue(callback);
    }

    public void addProductToCart(int accountId, int productId, Callback<Cart> callback) {
        JsonObject data = new JsonObject();
        data.addProperty("account_id", accountId);
        data.addProperty("product_id", productId);

        Call<Cart> call = apiService.addProductToCart(data);
        call.enqueue(callback);
    }

    public void deleteProductFromCart(int accountId, int productId, Callback<Void> callback) {
        Call<Void> call = apiService.deleteProductFromCart(accountId, productId);
        call.enqueue(callback);
    }

    public void increaseQuantityCart(int accountId, int productId, Callback<Cart> callback) {
        JsonObject data = new JsonObject();
        data.addProperty("account_id", accountId);
        data.addProperty("product_id", productId);

        Call<Cart> call = apiService.increaseQuantityCart(data);
        call.enqueue(callback);
    }

    public void decreaseQuantityCart(int accountId, int productId, Callback<Cart> callback) {
        JsonObject data = new JsonObject();
        data.addProperty("account_id", accountId);
        data.addProperty("product_id", productId);

        Call<Cart> call = apiService.decreaseQuantityCart(data);
        call.enqueue(callback);
    }

    public void removeAllProductsFromCart(int accountId, Callback<Void> callback) {
        Call<Void> call = apiService.removeAllProductsFromCart(accountId);
        call.enqueue(callback);
    }


}
