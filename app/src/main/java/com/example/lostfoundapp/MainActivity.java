package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends BaseActivity {

    Button btnCreate;
    Button btnShowall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setupBottomNav(R.id.nav_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        Button btnMap = findViewById(R.id.btnMap);
        btnMap.setOnClickListener(v -> {
            startActivity(new Intent(this, MapsActivity.class));
        });


        btnCreate = findViewById(R.id.btnCreate);
        btnShowall = findViewById(R.id.btnShowall);
        btnMap = findViewById(R.id.btnMap);

        btnCreate.setOnClickListener(v -> startActivity(new Intent(this, new_advert.class)));
        btnShowall.setOnClickListener(v -> startActivity(new Intent(this, show_all.class)));
        btnMap.setOnClickListener(v -> startActivity(new Intent(this, MapsActivity.class)));
    }
}
