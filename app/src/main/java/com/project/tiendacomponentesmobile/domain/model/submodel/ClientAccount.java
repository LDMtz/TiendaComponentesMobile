package com.project.tiendacomponentesmobile.domain.model.submodel;

import com.project.tiendacomponentesmobile.domain.model.Account;
import com.project.tiendacomponentesmobile.domain.model.Client;

public class ClientAccount {
    private Account account;
    private Client client;

    public ClientAccount(Account account, Client client) {
        this.account = account;
        this.client = client;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}