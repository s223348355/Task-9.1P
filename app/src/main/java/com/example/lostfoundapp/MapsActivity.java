package com.example.lostfoundapp;

import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.maps.model.LatLng;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        dbHelper = new DBHelper(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Cursor c = dbHelper.getAllAdverts();
        if (c != null && c.moveToFirst()) {
            do {
                String desc = c.getString(3); // description
                String locationText = c.getString(5); // location
                String type = c.getString(6); // Lost or Found

                LatLng latLng = parseLatLng(locationText);
                if (latLng != null) {
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(type + ": " + desc));
                }
            } while (c.moveToNext());
            c.close();
        }

        // Default map position (e.g. Melbourne)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-37.8136, 144.9631), 5));
    }

    private LatLng parseLatLng(String locationText) {
        try {
            if (locationText.contains(",")) {
                String[] parts = locationText.split(",");
                double lat = Double.parseDouble(parts[0].trim());
                double lng = Double.parseDouble(parts[1].trim());
                return new LatLng(lat, lng);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
