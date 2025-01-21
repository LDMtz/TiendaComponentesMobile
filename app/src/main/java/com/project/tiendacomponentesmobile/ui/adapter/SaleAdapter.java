package com.project.tiendacomponentesmobile.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.project.tiendacomponentesmobile.R;
import com.project.tiendacomponentesmobile.domain.model.Sale;
import com.project.tiendacomponentesmobile.ui.PurchaseDetailsActivity;

import java.util.List;

public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.SaleCardViewHolder> {

    private List<Sale> salesList;
    private Context context;

    public SaleAdapter(Context context, List<Sale> salesList) {
        this.context = context;
        this.salesList = salesList;
    }

    @NonNull
    @Override
    public SaleCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sale_card, parent, false);
        return new SaleCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleCardViewHolder holder, int position) {
        Sale sale = salesList.get(position);
        holder.orderNumber.setText("Folio: " + sale.getOrderNumber());
        holder.saleDate.setText("Fecha: " + sale.getSaleDate());
        holder.itemsCount.setText("Productos: " + sale.getItemsCount());
        holder.totalAmount.setText("Total: $" + String.format("%.2f", sale.getTotalAmount()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PurchaseDetailsActivity.class);
            intent.putExtra("saleId", sale.getId());
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return salesList.size();
    }

    public static class SaleCardViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumber, saleDate, itemsCount, totalAmount;
        public SaleCardViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNumber = itemView.findViewById(R.id.txtOrderNumberPItem);
            saleDate = itemView.findViewById(R.id.txtDatePItem);
            itemsCount = itemView.findViewById(R.id.txtProductsPItem);
            totalAmount = itemView.findViewById(R.id.txtTotalAmountPItem);
        }
    }
}
