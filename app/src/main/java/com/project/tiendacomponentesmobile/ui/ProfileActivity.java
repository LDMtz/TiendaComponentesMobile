package com.project.tiendacomponentesmobile.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.project.tiendacomponentesmobile.R;
import com.project.tiendacomponentesmobile.data.remote.api.ApiClient;
import com.project.tiendacomponentesmobile.data.remote.api.ApiService;
import com.project.tiendacomponentesmobile.data.repository.AccountRepository;
import com.project.tiendacomponentesmobile.domain.AccountInteractor;
import com.project.tiendacomponentesmobile.utils.MenuUtils;
import com.project.tiendacomponentesmobile.utils.SessionManager;

import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Response;

import com.project.tiendacomponentesmobile.domain.model.submodel.ClientAccount;

public class ProfileActivity extends AppCompatActivity {
    //
    EditText etUsernameProfile, etEmailProfile, etNamesProfile, etLastnamesProfile, etTelephoneProfile, etAccountIdProfile;
    private AccountInteractor accountInteractor;
    ImageView imgPictureProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Para el menu hamburguesa
        ImageView menuIcon = findViewById(R.id.menu_icon);
        MenuUtils.setupMenu(this, menuIcon);

        // Obtener el account_id
        SessionManager sessionManager = new SessionManager(this);
        int accountId = sessionManager.getAccountId();

        //Inputs
        etUsernameProfile = findViewById(R.id.etUsernameProfile);
        etEmailProfile = findViewById(R.id.etEmailProfile);
        etNamesProfile = findViewById(R.id.etNamesProfile);
        etLastnamesProfile = findViewById(R.id.etLastnamesProfile);
        etTelephoneProfile = findViewById(R.id.etTelephoneProfile);
        etAccountIdProfile = findViewById(R.id.etAccountIdProfile);
        imgPictureProfile = findViewById(R.id.imgPictureProfile);

        //Solic a la API
        accountInteractor = new AccountInteractor(new AccountRepository(ApiClient.getRetrofitInstance().create(ApiService.class)));

        accountInteractor.getAccountById(accountId, new Callback<ClientAccount>() {
            @Override
            public void onResponse(Call<ClientAccount> call, Response<ClientAccount> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ClientAccount clientAccount = response.body();

                    etUsernameProfile.setText(clientAccount.getAccount().getUsername());
                    etEmailProfile.setText(clientAccount.getAccount().getEmail());
                    etNamesProfile.setText(clientAccount.getClient().getNames());
                    etLastnamesProfile.setText(clientAccount.getClient().getLastNames());
                    etTelephoneProfile.setText(clientAccount.getClient().getNumber());
                    etAccountIdProfile.setText(String.valueOf(clientAccount.getAccount().getId()));
                    setImageFromBase64(clientAccount.getAccount().getPictureProfile());

                } else {
                    Toast.makeText(getApplicationContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClientAccount> call, Throwable t) {
                Log.e("API_ERROR", "Fallo al obtener datos: " + t.getMessage(), t);
            }
        });

    }

    private void setImageFromBase64(String base64String) {
        if (base64String != null && !base64String.isEmpty()) {
            try {
                byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                roundedDrawable.setCornerRadius(999f);
                imgPictureProfile.setImageDrawable(roundedDrawable);
            } catch (IllegalArgumentException e) {
                Log.e("Base64Image", "Error al decodificar la imagen en Base64", e);
                imgPictureProfile.setImageResource(R.mipmap.default_user_profile);
            }
        } else {
            imgPictureProfile.setImageResource(R.mipmap.default_user_profile);
        }
    }


}
