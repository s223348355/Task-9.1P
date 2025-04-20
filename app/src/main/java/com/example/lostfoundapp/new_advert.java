package com.example.lostfoundapp;

import android.os.Bundle;
import android.widget.*;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class new_advert extends BaseActivity {

    EditText name, phone, desc, date, location;
    RadioGroup typeGroup;
    Button btnSave;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_advert);
        setupBottomNav(R.id.nav_create);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.newmain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = findViewById(R.id.txtbName);
        phone = findViewById(R.id.txtbPhone);
        desc = findViewById(R.id.txtbDescription);
        date = findViewById(R.id.txtbDate);
        location = findViewById(R.id.txtbLocation);
        typeGroup = findViewById(R.id.rgroupPostype);
        btnSave = findViewById(R.id.btnSave);
        db = new DBHelper(this);

        typeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbtnLost) {
                desc.setHint("What did you lose?");
                location.setHint("Where did you lose it?");
            } else if (checkedId == R.id.rbtnFound) {
                desc.setHint("What did you find?");
                location.setHint("Where did you find it?");
            }
        });

        btnSave.setOnClickListener(v -> {
            String n = name.getText().toString();
            String p = phone.getText().toString();
            String d = desc.getText().toString();
            String dt = date.getText().toString();
            String loc = location.getText().toString();
            int selectedId = typeGroup.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(this, "Please select Lost or Found", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedBtn = findViewById(selectedId);
            String type = selectedBtn.getText().toString();

            boolean inserted = db.insertAdvert(n, p, d, dt, loc, type);
            Toast.makeText(this, inserted ? "Advert saved!" : "Save failed!", Toast.LENGTH_SHORT).show();
            if (inserted) finish();
        });
    }
}
