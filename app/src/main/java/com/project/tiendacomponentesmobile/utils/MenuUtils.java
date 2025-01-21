package com.project.tiendacomponentesmobile.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuInflater;
import android.widget.ImageView;

import androidx.appcompat.widget.PopupMenu;

import com.project.tiendacomponentesmobile.R;
import com.project.tiendacomponentesmobile.ui.CartActivity;
import com.project.tiendacomponentesmobile.ui.MainActivity;
import com.project.tiendacomponentesmobile.ui.ProfileActivity;
import com.project.tiendacomponentesmobile.ui.PurchasesActivity;

public class MenuUtils {

    @SuppressLint("NonConstantResourceId")
    public static void setupMenu(Activity activity, ImageView menuIcon) {
        if (menuIcon != null) {
            menuIcon.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(activity, menuIcon, 0, 0, R.style.PopupMenuStyle);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.menu_hamburguesa, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(item -> {
                    int itemId = item.getItemId();

                    if (itemId == R.id.menu_home) {
                        activity.startActivity(new Intent(activity, MainActivity.class));
                        return true;
                    } else if (itemId == R.id.menu_profile) {
                        activity.startActivity(new Intent(activity, ProfileActivity.class));
                        return true;
                    } else if (itemId == R.id.menu_cart) {
                        activity.startActivity(new Intent(activity, CartActivity.class));
                        return true;
                    } else if (itemId == R.id.menu_purchases) {
                        activity.startActivity(new Intent(activity, PurchasesActivity.class));
                        return true;
                    }

                    return false;
                });

                popupMenu.show();
            });
        }
    }
}
