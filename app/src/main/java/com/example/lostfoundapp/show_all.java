package com.example.lostfoundapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class show_all extends BaseActivity {

    LinearLayout linearLayout;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_all);
        setupBottomNav(R.id.nav_show);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.newmain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        linearLayout = findViewById(R.id.linearLayout);
        db = new DBHelper(this);
        showAllAdverts();
    }

    private void showAllAdverts() {
        Cursor c = db.getAllAdverts();
        if (c.getCount() == 0) {
            TextView tv = new TextView(this);
            tv.setText("No data to display.");
            linearLayout.addView(tv);
            return;
        }

        while (c.moveToNext()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            String phone = c.getString(2);
            String desc = c.getString(3);
            String date = c.getString(4);
            String location = c.getString(5);
            String type = c.getString(6);

            TextView tv = new TextView(this);
            tv.setText("• " + type + ": " + desc);
            tv.setTextSize(16);
            tv.setPadding(20, 20, 20, 20);
            tv.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 16);
            tv.setLayoutParams(params);

            tv.setOnClickListener(v -> {
                Intent i = new Intent(show_all.this, remove_item.class);
                i.putExtra("id", id);
                i.putExtra("name", name);
                i.putExtra("phone", phone);
                i.putExtra("desc", desc);
                i.putExtra("date", date);
                i.putExtra("location", location);
                i.putExtra("type", type);
                startActivity(i);
            });

            linearLayout.addView(tv);
        }
    }
}
