package com.project.tiendacomponentesmobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "MyAppPreferences";
    private static final String KEY_ACCOUNT_ID = "account_id";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Guardar el account_id
    public void saveAccountId(int accountId) {
        editor.putInt(KEY_ACCOUNT_ID, accountId);
        editor.apply();
    }

    // Obtener el account_id
    public int getAccountId() {
        return sharedPreferences.getInt(KEY_ACCOUNT_ID, -1); // -1 si no está logueado
    }

    // Limpiar la sesión
    public void clearSession() {
        editor.clear();
        editor.apply();
    }

    // Verificar si el usuario está logueado
    public boolean isLoggedIn() {
        return getAccountId() != -1;
    }
}
