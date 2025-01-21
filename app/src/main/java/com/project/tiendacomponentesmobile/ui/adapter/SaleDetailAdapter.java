package com.project.tiendacomponentesmobile.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.tiendacomponentesmobile.R;
import com.project.tiendacomponentesmobile.domain.model.submodel.SaleDetail;

import java.util.List;

public class SaleDetailAdapter extends RecyclerView.Adapter<SaleDetailAdapter.SaleDetailViewHolder> {

    private List<SaleDetail> saleDetailsList;
    private Context context;

    public SaleDetailAdapter(Context context, List<SaleDetail> saleDetailsList) {
        this.context = context;
        this.saleDetailsList = saleDetailsList;
    }

    @NonNull
    @Override
    public SaleDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sale_details, parent, false);
        return new SaleDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleDetailViewHolder holder, int position) {
        SaleDetail saleDetail = saleDetailsList.get(position);

        // Asignar datos al ViewHolder
        holder.productName.setText(saleDetail.getProductName());
        holder.quantity.setText("Cantidad: " + saleDetail.getQuantity());
        holder.subtotal.setText("Subtotal: $" + String.format("%.2f", saleDetail.getSubtotal()));

        // Decodificar la imagen Base64
        String base64Image = saleDetail.getImage();
        if (base64Image != null && !base64Image.isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.productImage.setImageBitmap(decodedBitmap);
            } catch (IllegalArgumentException e) {
                holder.productImage.setImageResource(R.mipmap.default_product_foreground);
            }
        } else {
            holder.productImage.setImageResource(R.mipmap.default_product_foreground);
        }

    }

    @Override
    public int getItemCount() {
        return saleDetailsList.size();
    }

    public static class SaleDetailViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, quantity, subtotal;

        public SaleDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imgProductSaleDetails);
            productName = itemView.findViewById(R.id.txtProductNameSaleDetails);
            quantity = itemView.findViewById(R.id.txtQuantitySaleDetails);
            subtotal = itemView.findViewById(R.id.txtSubtotalSaleDetails);
        }
    }
}
