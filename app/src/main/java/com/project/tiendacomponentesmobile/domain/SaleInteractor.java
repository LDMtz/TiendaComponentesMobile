package com.project.tiendacomponentesmobile.domain;

import com.project.tiendacomponentesmobile.data.repository.SaleRepository;
import com.project.tiendacomponentesmobile.domain.model.Sale;

import java.util.List;

import retrofit2.Callback;

public class SaleInteractor {
    private final SaleRepository saleRepository;

    public SaleInteractor(SaleRepository productRepository) {
        this.saleRepository = productRepository;
    }

    public void storeSale(int accountId, Callback<Sale> callback) {
        saleRepository.storeSale(accountId, callback);
    }

    public void getClientSales(int accountId, Callback<List<Sale>> callback) {
        saleRepository.getClientSales(accountId, callback);
    }

    public void getSale(int saleId, Callback<Sale> callback) {
        saleRepository.getSale(saleId, callback);
    }

}
