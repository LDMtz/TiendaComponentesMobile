package com.project.tiendacomponentesmobile.data.remote.api;

import com.google.gson.JsonObject;
import com.project.tiendacomponentesmobile.domain.model.Cart;
import com.project.tiendacomponentesmobile.domain.model.Sale;
import com.project.tiendacomponentesmobile.domain.model.submodel.ClientAccount;
import com.project.tiendacomponentesmobile.domain.model.submodel.ProductCard;
import com.project.tiendacomponentesmobile.domain.model.submodel.ProductCart;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

        @POST("accounts/validate")
        Call<JsonObject> validateCredentials(@Body JsonObject credentials);

        @POST("clients/register")
        Call<ClientAccount> createClientAccount(@Body ClientAccount clientAccount);

        @GET("accounts/{id}")
        Call<ClientAccount> getAccountById(@Path("id") int id);

        @GET("products/many/{limit}")
        Call<List<ProductCard>> getProductsWithLimit(@Path("limit") int limit);

        @GET("products/cart/{account_id}")
        Call<List<ProductCart>> getClientCart(@Path("account_id") int accountId);

        @POST("products/cart/add")
        Call<Cart> addProductToCart(@Body JsonObject data);

        @DELETE("products/cart/delete/{account_id}/{product_id}")
        Call<Void> deleteProductFromCart(@Path("account_id") int accountId, @Path("product_id") int productId);

        @PATCH("products/cart/increase")
        Call<Cart> increaseQuantityCart(@Body JsonObject data);

        @PATCH("products/cart/decrease")
        Call<Cart> decreaseQuantityCart(@Body JsonObject data);

        @DELETE("products/cart/remove-all/{account_id}")
        Call<Void> removeAllProductsFromCart(@Path("account_id") int accountId);

        @POST("sales/store/{account_id}")
        Call<Sale> storeSale(@Path("account_id") int accountId);

        @GET("sales/client/{account_id}")
        Call<List<Sale>> getClientSales(@Path("account_id") int accountId);

        @GET("sales/{sale_id}")
        Call<Sale> getSale(@Path("sale_id") int saleId);

}
