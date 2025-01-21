package com.project.tiendacomponentesmobile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;

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
import com.project.tiendacomponentesmobile.domain.model.Account;
import com.project.tiendacomponentesmobile.domain.model.Client;
import com.project.tiendacomponentesmobile.domain.model.submodel.ClientAccount;

import android.util.Log;
import android.widget.Toast;

import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    //
    private EditText etRegisterNames, etRegisterLastnames, etRegisterUsername, etRegisterNumber, etRegisterPassword, etRegisterEmail;
    private Button btnRegister;
    private AccountInteractor accountInteractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView txtRegister = findViewById(R.id.txtGoToLogin);
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Inicializar vistas
        etRegisterNames = findViewById(R.id.etRegisterNames);
        etRegisterLastnames = findViewById(R.id.etRegisterLastnames);
        etRegisterUsername = findViewById(R.id.etRegisterUsername);
        etRegisterNumber = findViewById(R.id.etRegisterNumber);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        btnRegister = findViewById(R.id.btnRegister);

        // Configurar el Interactor con Retrofit
        accountInteractor = new AccountInteractor(new AccountRepository(ApiClient.getRetrofitInstance().create(ApiService.class)));

        // Configurar el OnClickListener para el botón de registro
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los datos de los inputs
                String username = etRegisterUsername.getText().toString();
                String email = etRegisterEmail.getText().toString();
                String password = etRegisterPassword.getText().toString();
                String names = etRegisterNames.getText().toString();
                String lastNames = etRegisterLastnames.getText().toString();
                String number = etRegisterNumber.getText().toString();

                // Validar que los campos no estén vacíos (puedes agregar más validaciones aquí)
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || names.isEmpty() || lastNames.isEmpty() || number.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Crear el objeto Account con los datos de los inputs
                Account account = new Account();
                account.setUsername(username);
                account.setEmail(email);
                account.setPassword(password);

                // Crear el objeto Client con los datos de los inputs
                Client client = new Client();
                client.setNames(names);
                client.setLastNames(lastNames);
                client.setNumber(number);

                // Crear el objeto ClientAccount con Account y Client
                ClientAccount clientAccount = new ClientAccount(account, client);

                // Llamar al metodo para registrar la cuenta usando el Interactor
                accountInteractor.createClientAccount(clientAccount, new Callback<ClientAccount>() {
                    @Override
                    public void onResponse(Call<ClientAccount> call, Response<ClientAccount> response) {
                        if (response.isSuccessful()) {
                            // Si el registro es exitoso, redirigir a LoginActivity y mostrar un Toast
                            Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Manejar los errores con más detalles
                            String errorMessage = "Error en el registro: ";
                            if (response.errorBody() != null) {
                                try {
                                    // Si la respuesta contiene un cuerpo de error, leerlo y mostrarlo
                                    String errorBody = response.errorBody().string();
                                    errorMessage += errorBody;
                                } catch (Exception e) {
                                    errorMessage += "Error al procesar el mensaje de error.";
                                    Log.e("RegisterActivity", "Error al procesar el cuerpo del error", e);
                                }
                            } else {
                                errorMessage += response.message(); // Si no hay cuerpo de error, mostramos el mensaje
                            }

                            // Mostrar el mensaje de error completo
                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            Log.e("RegisterActivity", errorMessage);
                        }
                    }

                    @Override
                    public void onFailure(Call<ClientAccount> call, Throwable t) {
                        // Si ocurre algún error en la llamada (ej. no hay conexión), manejarlo aquí
                        Log.e("RegisterActivity", "Error en la llamada a la API", t);
                    }
                });



            }
        });

    }
}