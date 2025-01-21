package com.project.tiendacomponentesmobile.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.tiendacomponentesmobile.R;
import com.project.tiendacomponentesmobile.data.remote.api.ApiClient;
import com.project.tiendacomponentesmobile.data.remote.api.ApiService;
import com.project.tiendacomponentesmobile.data.repository.ProductRepository;
import com.project.tiendacomponentesmobile.domain.ProductInteractor;
import com.project.tiendacomponentesmobile.domain.model.Cart;
import com.project.tiendacomponentesmobile.domain.model.submodel.ProductCard;
import com.project.tiendacomponentesmobile.utils.SessionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

    private Context context;
    private List<ProductCard> productList;
    private ProductInteractor productInteractor;
    private SessionManager sessionManager;

    //Constructor
    public ProductAdapter(List<ProductCard> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_card, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        ProductCard product = productList.get(position);

        // Limpiando las etiquetas existentes del contenedor
        holder.tagsContainerLinLay.removeAllViews();

        // Mapeamos los nombres de las etiquetas con los recursos correspondientes
        Map<String, Integer> tagIcons = new HashMap<>();
        tagIcons.put("EXCLUSIVO", R.mipmap.tag_exclusive_foreground);
        tagIcons.put("DESCUENTO", R.mipmap.tag_desc_foreground);
        tagIcons.put("NUEVO", R.mipmap.tag_new_foreground);
        tagIcons.put("ENVIO GRATIS", R.mipmap.tag_delivery_foreground);

        // Tamaño de las etiquetas
        int tagSize = (int) context.getResources().getDimension(R.dimen.tag_size);

        // Agregar dinámicamente los ImageView correspondientes
        if (product.getTagNames() != null) {
            for (String tagName : product.getTagNames()) {
                Integer iconRes = tagIcons.get(tagName);
                if (iconRes != null) {
                    ImageView tagImageView = new ImageView(context);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(tagSize, tagSize);
                    layoutParams.setMargins(8, 0, 8, 0);
                    tagImageView.setLayoutParams(layoutParams);
                    tagImageView.setImageResource(iconRes);
                    tagImageView.setContentDescription(tagName);

                    holder.tagsContainerLinLay.addView(tagImageView);
                }
            }
        }

        // Decodificar la imagen Base64
        String base64Image = product.getProductImage(); // Tu variable Base64
        if (base64Image != null && !base64Image.isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.productImageView.setImageBitmap(decodedBitmap);
            } catch (IllegalArgumentException e) {
                holder.productImageView.setImageResource(R.mipmap.default_product_foreground); //Imagen fija de error
            }
        } else {
            holder.productImageView.setImageResource(R.mipmap.default_product_foreground); //Imagen fija de error
        }

        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText(context.getString(R.string.product_price, product.getPrice()));

        // Validar el stock
        if (product.getStock() > 0) { // Si hay disponibles
            String availableText = context.getString(R.string.product_available) + " (" + product.getStock() + ")";
            holder.stockTextView.setText(availableText); // Texto de existencia con cantidad
            holder.stockTextView.setTextColor(ContextCompat.getColor(context, R.color.stock_available)); // Color verde
        } else { // si no hay disponibles
            String notavailableText = context.getString(R.string.product_not_available) + " (" + product.getStock() + ")";
            holder.stockTextView.setText(notavailableText); // Texto de no existencia
            holder.stockTextView.setTextColor(ContextCompat.getColor(context, R.color.stock_not_available)); // Color rojo
        }

        //Al hacer click en la card. si el producto está disponible mostramos el carrito
        holder.itemView.setOnClickListener(v -> {
            if(product.getStock() > 0)
                if (holder.addToCartButton.getVisibility() == View.VISIBLE)
                    holder.addToCartButton.setVisibility(View.GONE); // Ocultar si está visible
                else
                    holder.addToCartButton.setVisibility(View.VISIBLE); // Mostrar si está oculto
        });

        // Inicializamos el ProductInteractor
        productInteractor = new ProductInteractor(new ProductRepository(ApiClient.getRetrofitInstance().create(ApiService.class)));

        // Obtener el account_id
        sessionManager = new SessionManager(context);

        // Evento click del boton del carrito
        holder.addToCartButton.setOnClickListener(v -> {
            int accountId = sessionManager.getAccountId();
            int productId = product.getId();

            productInteractor.addProductToCart(accountId, productId, new Callback<Cart>() {
                @Override
                public void onResponse(Call<Cart> call, Response<Cart> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Cart cart = response.body();
                        Toast.makeText(context, "Producto agregado al carrito: " + cart.getProductId(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error al agregar al carrito", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Cart> call, Throwable t) {
                    // Manejar fallos de red o excepciones
                    Toast.makeText(context, "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // ViewHolder class
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        LinearLayout tagsContainerLinLay;
        ImageView productImageView;
        TextView nameTextView;
        TextView priceTextView;
        TextView stockTextView;
        FloatingActionButton addToCartButton;

        public ProductViewHolder(View itemView) {
            super(itemView);
            tagsContainerLinLay = itemView.findViewById(R.id.product_tags_container);
            productImageView = itemView.findViewById(R.id.product_image);
            nameTextView = itemView.findViewById(R.id.product_name);
            priceTextView = itemView.findViewById(R.id.product_price);
            stockTextView = itemView.findViewById(R.id.product_stock);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
        }
    }

}