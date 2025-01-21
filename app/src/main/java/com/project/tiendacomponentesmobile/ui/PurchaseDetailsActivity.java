package com.project.tiendacomponentesmobile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.tiendacomponentesmobile.R;
import com.project.tiendacomponentesmobile.data.remote.api.ApiClient;
import com.project.tiendacomponentesmobile.data.remote.api.ApiService;
import com.project.tiendacomponentesmobile.data.repository.SaleRepository;
import com.project.tiendacomponentesmobile.domain.SaleInteractor;
import com.project.tiendacomponentesmobile.domain.model.Sale;
import com.project.tiendacomponentesmobile.domain.model.submodel.SaleDetail;
import com.project.tiendacomponentesmobile.ui.adapter.SaleDetailAdapter;
import com.project.tiendacomponentesmobile.utils.MenuUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseDetailsActivity extends AppCompatActivity {
    //
    private SaleInteractor saleInteractor;
    private RecyclerView rvProductsPurchaseDetails;
    private SaleDetailAdapter saleDetailAdapter;
    private TextView txtOrderNumber, txtDateSale, txtProductsCount, txtTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_details);

        //Configuracion del menú hamburguesa
        ImageView menuIcon = findViewById(R.id.menu_icon);
        MenuUtils.setupMenu(this, menuIcon);

        //Obtenemos los datos del intent
        Intent intent = getIntent();
        int saleId = intent.getIntExtra("saleId", 0);

        rvProductsPurchaseDetails = findViewById(R.id.rvProductsPurchaseDetails);
        rvProductsPurchaseDetails.setLayoutManager(new LinearLayoutManager(this));

        txtOrderNumber = findViewById(R.id.txtOrderNumberPurchaseDetails);
        txtDateSale = findViewById(R.id.txtDatePurchaseDetails);
        txtProductsCount = findViewById(R.id.txtProductsCountPurchaseDetails);
        txtTotalAmount = findViewById(R.id.txtTotalAmountPurchaseDetails);


        saleInteractor = new SaleInteractor(new SaleRepository(ApiClient.getRetrofitInstance().create(ApiService.class)));

        if(saleId>0)
            getSale(saleId);
        else
            Toast.makeText(PurchaseDetailsActivity.this, "Error al cargar los datos de la venta", Toast.LENGTH_SHORT).show();



    }

    private void getSale(int saleId) {
        saleInteractor.getSale(saleId, new Callback<Sale>() {
            @Override
            public void onResponse(Call<Sale> call, Response<Sale> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Sale sale = response.body();
                    List<SaleDetail> saleDetails = sale.getDetails();

                    txtOrderNumber.setText("Folio: " + sale.getOrderNumber());
                    txtDateSale.setText("Fecha: "+ sale.getSaleDate());
                    txtProductsCount.setText("Productos: " + sale.getItemsCount());
                    txtTotalAmount.setText("Monto total: $" + String.format("%.2f", sale.getTotalAmount()));

                    saleDetailAdapter = new SaleDetailAdapter(PurchaseDetailsActivity.this, saleDetails);
                    rvProductsPurchaseDetails.setAdapter(saleDetailAdapter);
                } else {
                    Log.e("SaleError", "Error en la respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Sale> call, Throwable t) {
                Log.e("SaleError", "Error al realizar la llamada: " + t.getMessage());
            }
        });
    }

}