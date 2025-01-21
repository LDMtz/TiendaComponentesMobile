package com.project.tiendacomponentesmobile.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.project.tiendacomponentesmobile.R;
import com.project.tiendacomponentesmobile.data.remote.api.ApiClient;
import com.project.tiendacomponentesmobile.data.remote.api.ApiService;
import com.project.tiendacomponentesmobile.data.repository.AccountRepository;
import com.project.tiendacomponentesmobile.domain.AccountInteractor;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import android.util.Log;

import com.google.gson.JsonObject;
import com.project.tiendacomponentesmobile.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    //
    private static final String TAG = "LoginActivity"; // Etiqueta para el log
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private AccountInteractor accountInteractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*------------------------------------------------------------------------*/
        edtEmail = findViewById(R.id.etLoginEmail);
        edtPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);

        accountInteractor = new AccountInteractor(new AccountRepository(ApiClient.getRetrofitInstance().create(ApiService.class)));

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();

            accountInteractor.validateAccount(email, password, new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        JsonObject responseBody = response.body();
                        String message = responseBody.get("message").getAsString();
                        boolean status = responseBody.get("status").getAsBoolean();
                        int roleID = responseBody.get("role_id").getAsInt();
                        int accountID = responseBody.get("account_id").getAsInt();

                        if (status) {
                            if(roleID == 4){ //<-- Si es cliente
                                // Guardar el account_id en SessionManager
                                SessionManager sessionManager = new SessionManager(LoginActivity.this);
                                sessionManager.saveAccountId(accountID);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, "Solo se aceptan cuentas con rol Cliente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error en la solicitud", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error en la solicitud: " + t.getMessage());
                }
            });
        });
        /*------------------------------------------------------------------------*/


        /*------------------------------------------------------------------------*/
        TextView txtRegister = findViewById(R.id.txtGoToRegister);

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        /*------------------------------------------------------------------------*/
    }
}