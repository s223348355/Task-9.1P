package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupBottomNav(int menuItemId) {
        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        nav.setSelectedItemId(menuItemId);

        nav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == menuItemId) return true;

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_create) {
                startActivity(new Intent(this, new_advert.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_show) {
                startActivity(new Intent(this, show_all.class));
                finish();
                return true;
            }
            return false;
        });
    }
}
