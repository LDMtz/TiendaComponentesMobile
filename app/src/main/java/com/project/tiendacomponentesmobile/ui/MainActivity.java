package com.project.tiendacomponentesmobile.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.tiendacomponentesmobile.R;
import com.project.tiendacomponentesmobile.data.remote.api.ApiClient;
import com.project.tiendacomponentesmobile.data.remote.api.ApiService;
import com.project.tiendacomponentesmobile.data.repository.ProductRepository;
import com.project.tiendacomponentesmobile.domain.ProductInteractor;
import com.project.tiendacomponentesmobile.domain.model.submodel.ProductCard;
import com.project.tiendacomponentesmobile.ui.adapter.ProductAdapter;
import com.project.tiendacomponentesmobile.utils.MenuUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ProductAdapter productAdapter;
    private List<ProductCard> productList;
    private RecyclerView productRecyclerView;

    private ProductInteractor productInteractor;

    public static final int PRODUCT_LIMIT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Configuracion del menú hamburguesa
        ImageView menuIcon = findViewById(R.id.menu_icon);
        MenuUtils.setupMenu(this, menuIcon);

        productInteractor = new ProductInteractor(new ProductRepository(ApiClient.getRetrofitInstance().create(ApiService.class)));
        productRecyclerView = findViewById(R.id.rvProductsMain);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadProducts();
    }

    private void loadProducts() {
        productInteractor.getProductsWithLimit(PRODUCT_LIMIT, new Callback<List<ProductCard>>() {
            @Override
            public void onResponse(Call<List<ProductCard>> call, Response<List<ProductCard>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    productList = response.body();
                    productAdapter = new ProductAdapter(productList, MainActivity.this);
                    productRecyclerView.setAdapter(productAdapter);
                } else {
                    Log.e("MainActivity", "Error en la respuesta");
                }
            }

            @Override
            public void onFailure(Call<List<ProductCard>> call, Throwable t) {
                Log.e("MainActivity", "Error de conexión: "+t.getMessage());
                Log.e("MainActivity", "Recargando los productos");
                loadProducts();
            }
        });
    }
}
