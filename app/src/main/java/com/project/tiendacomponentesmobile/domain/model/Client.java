package com.project.tiendacomponentesmobile.domain.model;

import com.google.gson.annotations.SerializedName;

public class Client {
    @SerializedName("names")
    private String names;

    @SerializedName("last_names")
    private String lastNames;

    @SerializedName("number")
    private String number;

    @SerializedName("id")
    private int id;

    @SerializedName("state")
    private boolean state;

    @SerializedName("account_id")
    private int accountId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
