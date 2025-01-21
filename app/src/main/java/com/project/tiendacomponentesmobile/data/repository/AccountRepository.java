package com.project.tiendacomponentesmobile.data.repository;

import com.google.gson.JsonObject;
import com.project.tiendacomponentesmobile.data.remote.api.ApiService;
import com.project.tiendacomponentesmobile.domain.model.submodel.ClientAccount;

import retrofit2.Call;
import retrofit2.Callback;

public class AccountRepository {
    private final ApiService apiService;

    public AccountRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public void validateAccount(String email, String password, Callback<JsonObject> callback) {
        JsonObject credentials = new JsonObject();
        credentials.addProperty("email", email);
        credentials.addProperty("password", password);

        Call<JsonObject> call = apiService.validateCredentials(credentials);
        call.enqueue(callback);
    }

    public void createClientAccount(ClientAccount clientAccount, Callback<ClientAccount> callback) {
        Call<ClientAccount> call = apiService.createClientAccount(clientAccount);
        call.enqueue(callback);
    }

    public void getAccountById(int id, Callback<ClientAccount> callback) {
        Call<ClientAccount> call = apiService.getAccountById(id);
        call.enqueue(callback);
    }


}
