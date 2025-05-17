package com.example.lostfoundapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.*;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.*;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import java.util.Arrays;
import java.util.List;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class new_advert extends BaseActivity {

    EditText name, phone, desc, date, location;
    RadioGroup typeGroup;
    Button btnSave;
    DBHelper db;
    FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 2000;

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Button btnGetLocation = findViewById(R.id.btnGetlocation);

        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyAwdZraW4u9hF-oaEz3bKbNQPPSroPLFRc");
        }

        // Get Current Location
        btnGetLocation.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
                return;
            }

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000);
            locationRequest.setNumUpdates(1);

            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null && locationResult.getLastLocation() != null) {
                        double lat = locationResult.getLastLocation().getLatitude();
                        double lon = locationResult.getLastLocation().getLongitude();
                        location.setText(lat + "," + lon);
                    } else {
                        Toast.makeText(new_advert.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            }, Looper.getMainLooper());
        });

        // Location Autocomplete
        location.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });

        // Update hints based on Lost/Found type
        typeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbtnLost) {
                desc.setHint("What did you lose?");
                location.setHint("Where did you lose it?");
            } else if (checkedId == R.id.rbtnFound) {
                desc.setHint("What did you find?");
                location.setHint("Where did you find it?");
            }
        });

        // Save Button Logic
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
            if (inserted) {
                Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });
    }

    // Handle Permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted. Tap the button again.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Handle Autocomplete Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            if (place.getLatLng() != null) {
                double lat = place.getLatLng().latitude;
                double lon = place.getLatLng().longitude;
                location.setText(lat + "," + lon);
            }
        }
    }
}
