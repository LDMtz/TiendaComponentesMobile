package com.project.tiendacomponentesmobile.domain.model;

import com.google.gson.annotations.SerializedName;
import com.project.tiendacomponentesmobile.domain.model.submodel.SaleDetail;

import java.util.List;

public class Sale {
    @SerializedName("id")
    private int id;

    @SerializedName("order_number")
    private String orderNumber;

    @SerializedName("total_amount")
    private double totalAmount;

    @SerializedName("created_at")
    private String saleDate;

    @SerializedName("products_count")
    private int itemsCount;

    @SerializedName("details")
    private List<SaleDetail> details;

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public int getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
    }

    public List<SaleDetail> getDetails() {
        return details;
    }

    public void setDetails(List<SaleDetail> details) {
        this.details = details;
    }
}