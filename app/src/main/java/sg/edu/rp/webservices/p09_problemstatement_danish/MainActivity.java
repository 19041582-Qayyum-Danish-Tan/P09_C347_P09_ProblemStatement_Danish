package sg.edu.rp.webservices.p09_problemstatement_danish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {
    Button btnCheckRecords, btnGetLocationUpdate,btnRemoveLocationUpdate;
    TextView tvLocation, tvLatitude, tvLongitude;
    private GoogleMap map;
    FusedLocationProviderClient client;
    LocationCallback mLocationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCheckRecords = findViewById(R.id.btnCheckRecords);
        btnGetLocationUpdate = findViewById(R.id.btnGetLocationUpdate);
        btnRemoveLocationUpdate = findViewById(R.id.btnRemoveLocationUpdate);
        tvLocation = findViewById(R.id.tvLocation);
        tvLatitude = findViewById(R.id.tvLatitude);
        tvLongitude = findViewById(R.id.tvLongitude);

        client = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment)
                fm.findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback(){
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                checkPermission();

                LatLng North = new LatLng(1.4299234813852086, 103.78000711086116);
                Marker north = map.addMarker(new
                        MarkerOptions()
                        .position(North)
                        .title("North - HQ:")
                        .snippet("Block 333, Admiralty Ave 3, 765654 Operating hours: 10am-5pm\n" +
                                "Tel:65433456\n")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        String markerToast = marker.getTitle();
                        Toast.makeText(MainActivity.this, markerToast, Toast.LENGTH_LONG).show();
                        return false;
                    }
                });
            }

        });





        btnCheckRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (map != null){
//                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                    LatLng checkLocation = new LatLng();
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(checkLocation,
//                            15));
//
//                }
            }
        });

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location data = locationResult.getLastLocation();
                    double lat = data.getLatitude();
                    double lng = data.getLongitude();
                    tvLatitude.setText("Latitude: " + lat);
                    tvLongitude.setText("Longtitude: " + lng);


                    LatLng updateLocation = new LatLng(lat, lng);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(updateLocation,
                            15));
                    Marker north = map.addMarker(new
                            MarkerOptions()
                            .position(updateLocation)
                            .title("North - HQ:")
                            .snippet("Block 333, Admiralty Ave 3, 765654 Operating hours: 10am-5pm\n" +
                                    "Tel:65433456\n")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));



                    Toast.makeText(MainActivity.this,"New Location Detected\n" + "Lat: " + lat + ", " + " Lat: " + lng, Toast.LENGTH_SHORT).show();
                }
            }
        };

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(100);

        btnGetLocationUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                client.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

            }
        });

        btnRemoveLocationUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.removeLocationUpdates(mLocationCallback);
            }
        });

    }
    private boolean checkPermission(){
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return false;
        }
    }
}