package com.example.haran.agritec;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;

public class LocationSet extends FragmentActivity implements OnMapReadyCallback {

    private DatabaseReference SettingsuserRef;
    private FirebaseAuth mAuth;
    private GoogleMap mMap;
    private Button SaveButton;
    FirebaseDatabase database;
    String longitude,latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_set);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mAuth=FirebaseAuth.getInstance();
        String currentUserId=mAuth.getCurrentUser().getUid();
        SettingsuserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        SaveButton=findViewById(R.id.button3);
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        database = FirebaseDatabase.getInstance();
        if(!isEnabled) {
            Toast.makeText(LocationSet.this,"GPS off", Toast.LENGTH_SHORT).show();
            Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }

        if (ContextCompat.checkSelfPermission(LocationSet.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(LocationSet.this, "Access Denied ", Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(LocationSet.this, "Access granted", Toast.LENGTH_SHORT).show();
        }

// Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {

            private double lat,lon;


            public void onLocationChanged(Location location) {

                lat = location.getLatitude();
                lon = location.getLongitude();
                longitude = Double.toString(location.getLongitude());
                latitude = Double.toString(location.getLatitude());

                LatLng sydney = new LatLng(lat, lon);
                mMap.addMarker(new MarkerOptions().position(sydney).title("current location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,16));

                Toast.makeText(LocationSet.this,longitude+" "+latitude,Toast.LENGTH_SHORT).show();


            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

// Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap userMap = new HashMap();
                userMap.put("location", longitude+"@"+latitude);

                SettingsuserRef.updateChildren(userMap)
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isComplete()){
                                    Toast.makeText(LocationSet.this,"success",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(LocationSet.this,"fail",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }
}
