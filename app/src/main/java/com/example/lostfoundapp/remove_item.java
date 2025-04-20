package com.example.lostfoundapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class remove_item extends BaseActivity {

    int advertId;
    Button btnRemove;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remove_item);
        setupBottomNav(0); // No nav item highlighted for detail/delete screen

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.txtdItem), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView itemView = findViewById(R.id.txtdItem);
        TextView timeView = findViewById(R.id.txtdTimeago);
        TextView locationView = findViewById(R.id.txtdLocation);
        btnRemove = findViewById(R.id.btnRemove);
        db = new DBHelper(this);

        // Get data from intent
        advertId = getIntent().getIntExtra("id", -1);
        String name = getIntent().getStringExtra("name");
        String desc = getIntent().getStringExtra("desc");
        String date = getIntent().getStringExtra("date");
        String location = getIntent().getStringExtra("location");
        String type = getIntent().getStringExtra("type");

        itemView.setText(type + ": " + desc);
        timeView.setText("Posted on: " + date);
        locationView.setText("Location: " + location);

        btnRemove.setOnClickListener(v -> {
            if (advertId != -1) {
                int deleted = db.deleteAdvert(advertId);
                Toast.makeText(this, deleted > 0 ? "Advert removed" : "Not found", Toast.LENGTH_SHORT).show();
                if (deleted > 0) finish();
            } else {
                Toast.makeText(this, "Invalid advert ID", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
