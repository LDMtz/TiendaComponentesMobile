package com.project.tiendacomponentesmobile.data.repository;

import com.project.tiendacomponentesmobile.data.remote.api.ApiService;
import com.project.tiendacomponentesmobile.domain.model.Sale;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SaleRepository {
    private final ApiService apiService;

    public SaleRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public void storeSale(int accountId, Callback<Sale> callback) {
        Call<Sale> call = apiService.storeSale(accountId);
        call.enqueue(callback);
    }

    public void getClientSales(int accountId, Callback<List<Sale>> callback) {
        Call<List<Sale>> call = apiService.getClientSales(accountId);
        call.enqueue(callback);
    }

    public void getSale(int saleId, Callback<Sale> callback) {
        Call<Sale> call = apiService.getSale(saleId);
        call.enqueue(callback);
    }
}
