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
import com.project.tiendacomponentesmobile.data.repository.SaleRepository;
import com.project.tiendacomponentesmobile.domain.SaleInteractor;
import com.project.tiendacomponentesmobile.domain.model.Sale;
import com.project.tiendacomponentesmobile.ui.adapter.SaleAdapter;
import com.project.tiendacomponentesmobile.utils.MenuUtils;
import com.project.tiendacomponentesmobile.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PurchasesActivity extends AppCompatActivity {
    //
    private SaleInteractor saleInteractor;
    private SessionManager sessionManager;
    private RecyclerView salesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);

        //Configuracion del menú hamburguesa
        ImageView menuIcon = findViewById(R.id.menu_icon);
        MenuUtils.setupMenu(this, menuIcon);

        saleInteractor = new SaleInteractor(new SaleRepository(ApiClient.getRetrofitInstance().create(ApiService.class)));

        sessionManager = new SessionManager(this);
        int accountId = sessionManager.getAccountId();

        salesRecyclerView = findViewById(R.id.rvMyPurchases);
        salesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadSales(accountId, saleInteractor);

    }

    private void loadSales(int accountId, SaleInteractor saleInteractor) {
        saleInteractor.getClientSales(accountId, new Callback<List<Sale>>() {
            @Override
            public void onResponse(Call<List<Sale>> call, Response<List<Sale>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Sale> salesList = response.body();

                    SaleAdapter adapter = new SaleAdapter(PurchasesActivity.this, salesList);
                    salesRecyclerView.setAdapter(adapter);
                } else {
                    Log.e("SaleError", "Error en la respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Sale>> call, Throwable t) {
                Log.e("SaleError", "Error al realizar la llamada: " + t.getMessage());
            }
        });
    }
}