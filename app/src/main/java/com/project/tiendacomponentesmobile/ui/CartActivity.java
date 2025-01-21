package com.project.tiendacomponentesmobile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.tiendacomponentesmobile.R;
import com.project.tiendacomponentesmobile.data.remote.api.ApiClient;
import com.project.tiendacomponentesmobile.data.remote.api.ApiService;
import com.project.tiendacomponentesmobile.data.repository.ProductRepository;
import com.project.tiendacomponentesmobile.data.repository.SaleRepository;
import com.project.tiendacomponentesmobile.domain.ProductInteractor;
import com.project.tiendacomponentesmobile.domain.SaleInteractor;
import com.project.tiendacomponentesmobile.domain.model.Sale;
import com.project.tiendacomponentesmobile.domain.model.submodel.ProductCart;
import com.project.tiendacomponentesmobile.ui.adapter.CartAdapter;
import com.project.tiendacomponentesmobile.utils.MenuUtils;
import com.project.tiendacomponentesmobile.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    //
    private CartAdapter cartAdapter;
    private ProductInteractor productInteractor;
    private List<ProductCart> productList;
    private RecyclerView cartRecyclerView;
    private SessionManager sessionManager;
    private TextView totalProductsTextView, totalAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Configuracion del menú hamburguesa
        ImageView menuIcon = findViewById(R.id.menu_icon);
        MenuUtils.setupMenu(this, menuIcon);

        //
        totalProductsTextView = findViewById(R.id.txtTotalProdCart);
        totalAmountTextView = findViewById(R.id.txtTotalAmountCart);

        // Inicializamos el ProductInteractor
        productInteractor = new ProductInteractor(new ProductRepository(ApiClient.getRetrofitInstance().create(ApiService.class)));
        cartRecyclerView = findViewById(R.id.rvProductsCart);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sessionManager = new SessionManager(this);
        int accountId = sessionManager.getAccountId();
        loadCartProducts(accountId);

        //Para vaciar el carrito
        Button btnEmptyCartCart = findViewById(R.id.btnEmptyCartCart);

        btnEmptyCartCart.setOnClickListener(v -> {
            emptyCart(accountId);
        });

        Button btnStoreSaleCart = findViewById(R.id.btnStoreSaleCart);
        btnStoreSaleCart.setOnClickListener(v -> {
            storeSale(accountId);
        });
    }

    private void loadCartProducts(int accountId) {
        if (accountId == -1) {
            Toast.makeText(this, "Usuario no logueado", Toast.LENGTH_SHORT).show();
            return;
        }

        productInteractor.getClientCart(accountId, new Callback<List<ProductCart>>() {
            @Override
            public void onResponse(Call<List<ProductCart>> call, Response<List<ProductCart>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList = response.body();
                    updateCartTotals();
                    cartAdapter = new CartAdapter(CartActivity.this,productList);
                    cartRecyclerView.setAdapter(cartAdapter);
                } else {
                    Toast.makeText(CartActivity.this, "Error en la respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductCart>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateCartTotals() {
        int totalProducts = 0;
        double totalAmount = 0.0;

        // Calcular el total de productos y el monto total
        for (ProductCart productCart : productList) {
            totalProducts += productCart.getCart().getQuantity();
            totalAmount += productCart.getCart().getQuantity() * productCart.getProduct().getPrice();
        }

        totalProductsTextView.setText("Productos (" + totalProducts + ")");
        totalAmountTextView.setText("Monto total: $" + String.format("%.2f", totalAmount));
    }

    private void emptyCart(int accountId) {
        if (accountId == -1) {
            Toast.makeText(this, "Usuario no logueado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Llama al metodo del ProductInteractor
        productInteractor.removeAllProductsFromCart(accountId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CartActivity.this, "Carrito vaciado correctamente", Toast.LENGTH_SHORT).show();

                    // Limpia la lista y actualiza el adaptador
                    productList.clear();
                    cartAdapter.notifyDataSetChanged();

                    // Opcional: Actualiza los totales
                    TextView totalProductsTextView = findViewById(R.id.txtTotalProdCart);
                    TextView totalAmountTextView = findViewById(R.id.txtTotalAmountCart);
                    totalProductsTextView.setText("Productos (0)");
                    totalAmountTextView.setText("Monto total: $0.00");
                } else {
                    Toast.makeText(CartActivity.this, "Error al vaciar el carrito", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeSale(int accountId){
        SaleInteractor saleInteractor = new SaleInteractor(new SaleRepository(ApiClient.getRetrofitInstance().create(ApiService.class)));
        saleInteractor.storeSale(accountId, new Callback<Sale>() {
            @Override
            public void onResponse(Call<Sale> call, Response<Sale> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Sale sale = response.body();

                    // Limpiar los productos en el carrito
                    productList.clear();
                    cartAdapter.notifyDataSetChanged();
                    updateCartTotals();

                    Intent intent = new Intent(CartActivity.this, PurchaseDetailsActivity.class);
                    intent.putExtra("saleId", sale.getId());
                    startActivity(intent);

                    Toast.makeText(CartActivity.this, "Compra realizada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CartActivity.this, "Error al hacer la compra", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Sale> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Error al hacer la llamada a la api", Toast.LENGTH_SHORT).show();
            }
        });
    }




}