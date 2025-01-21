package com.project.tiendacomponentesmobile.domain;

import com.project.tiendacomponentesmobile.data.repository.AccountRepository;
import com.google.gson.JsonObject;
import com.project.tiendacomponentesmobile.domain.model.submodel.ClientAccount;

import retrofit2.Callback;

public class AccountInteractor {

    private final AccountRepository accountRepository;

    public AccountInteractor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void validateAccount(String email, String password, Callback<JsonObject> callback) {
        accountRepository.validateAccount(email, password, callback);
    }

    public void createClientAccount(ClientAccount clientAccount, Callback<ClientAccount> callback) {
        accountRepository.createClientAccount(clientAccount, callback);
    }

    public void getAccountById(int id, Callback<ClientAccount> callback) {
        accountRepository.getAccountById(id, callback);
    }
}
