package com.project.tiendacomponentesmobile.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.tiendacomponentesmobile.R;
import com.project.tiendacomponentesmobile.data.remote.api.ApiClient;
import com.project.tiendacomponentesmobile.data.remote.api.ApiService;
import com.project.tiendacomponentesmobile.data.repository.ProductRepository;
import com.project.tiendacomponentesmobile.domain.ProductInteractor;
import com.project.tiendacomponentesmobile.domain.model.Cart;
import com.project.tiendacomponentesmobile.domain.model.submodel.ProductCard;
import com.project.tiendacomponentesmobile.domain.model.submodel.ProductCart;
import com.project.tiendacomponentesmobile.ui.CartActivity;
import com.project.tiendacomponentesmobile.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<ProductCart> productList;
    private ProductInteractor productInteractor;
    private SessionManager sessionManager;
    private Context context;

    public CartAdapter(Context context, List<ProductCart> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        ProductCart product = productList.get(position);
        ProductCard productCard = product.getProduct();
        Cart cart = product.getCart();
        holder.productName.setText(productCard.getName());
        holder.productPrice.setText("Subtotal: $" + productCard.getPrice());
        holder.quantity.setText(String.valueOf(cart.getQuantity()));

        // Decodificar la imagen Base64
        String base64Image = productCard.getProductImage();
        if (base64Image != null && !base64Image.isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.productImageView.setImageBitmap(decodedBitmap);
            } catch (IllegalArgumentException e) {
                holder.productImageView.setImageResource(R.mipmap.default_product_foreground);
            }
        } else {
            holder.productImageView.setImageResource(R.mipmap.default_product_foreground);
        }

        productInteractor = new ProductInteractor(new ProductRepository(ApiClient.getRetrofitInstance().create(ApiService.class)));
        sessionManager = new SessionManager(context);
        int accountId = sessionManager.getAccountId();

        holder.increaseQuantity.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) return;

            productInteractor.increaseQuantityCart(accountId, productCard.getId(), new Callback<Cart>() {
                @Override
                public void onResponse(Call<Cart> call, Response<Cart> response) {
                    if (response.isSuccessful()) {
                        // Si la respuesta es exitosa, actualizar la cantidad en la vista
                        Cart updatedCart = response.body();
                        product.setCart(updatedCart); // Actualizar el objeto del carrito
                        productList.set(adapterPosition, product); // Actualizar el item en la lista
                        holder.quantity.setText(String.valueOf(updatedCart.getQuantity()));

                        // Llamar al metodo para actualizar los totales
                        if (context instanceof CartActivity) {
                            CartActivity activity = (CartActivity) context;
                            activity.updateCartTotals();
                        }
                    } else {
                        Toast.makeText(holder.itemView.getContext(), "Error al aumentar cantidad", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Cart> call, Throwable t) {
                    Toast.makeText(holder.itemView.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
                }
            });
        });

        holder.decreaseQuantity.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) return;

            productInteractor.decreaseQuantityCart(accountId, productCard.getId(), new Callback<Cart>() {
                @Override
                public void onResponse(Call<Cart> call, Response<Cart> response) {
                    if (response.isSuccessful()) {
                        // Si la respuesta es exitosa, actualizar la cantidad en la vista
                        Cart updatedCart = response.body();
                        product.setCart(updatedCart); // Actualizar el objeto del carrito
                        productList.set(adapterPosition, product); // Actualizar el item en la lista
                        holder.quantity.setText(String.valueOf(updatedCart.getQuantity()));

                        // Llamar al metodo para actualizar los totales
                        if (context instanceof CartActivity) {
                            CartActivity activity = (CartActivity) context;
                            activity.updateCartTotals();
                        }
                    } else {
                        Toast.makeText(holder.itemView.getContext(), "Error al disminuir cantidad", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Cart> call, Throwable t) {
                    Toast.makeText(holder.itemView.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
                }
            });
        });

        holder.removeItem.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) return;

            // Usa las variables ya definidas
            productInteractor.deleteProductFromCart(accountId, productCard.getId(), new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(holder.itemView.getContext(), "Producto eliminado", Toast.LENGTH_SHORT).show();
                        productList.remove(adapterPosition);
                        notifyItemRemoved(adapterPosition);
                        notifyItemRangeChanged(adapterPosition, productList.size());
                        // Llamar al metodo para actualizar los totales
                        if (context instanceof CartActivity) {
                            CartActivity activity = (CartActivity) context;
                            activity.updateCartTotals(); // Llamamos al metodo de la actividad para actualizar los totales
                        }

                    } else {
                        Toast.makeText(holder.itemView.getContext(), "Error al eliminar producto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(holder.itemView.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, quantity;
        ImageView productImageView;
        Button decreaseQuantity, increaseQuantity, removeItem;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.tv_product_name);
            productPrice = itemView.findViewById(R.id.tv_product_price);
            quantity = itemView.findViewById(R.id.tv_quantity);
            productImageView = itemView.findViewById(R.id.iv_product_image);
            decreaseQuantity = itemView.findViewById(R.id.btn_decrease_quantity);
            increaseQuantity = itemView.findViewById(R.id.btn_increase_quantity);
            removeItem = itemView.findViewById(R.id.btnRemoveProductCart);
        }
    }
}