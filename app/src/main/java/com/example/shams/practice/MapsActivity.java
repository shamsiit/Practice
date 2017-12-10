package com.example.shams.practice;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    LocationManager locationManager;

    LocationListener locationListener;

    GoogleApiClient mGoogleApiClient;

    PlaceAutocompleteFragment autocompleteFragment;

    ArrayList<LocationObject> locationLists = new ArrayList<LocationObject>();


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(lastKnownLocation == null){
                    lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                }
                if(lastKnownLocation != null){
                    LatLng usersLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(usersLocation).draggable(true).title("Your Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(usersLocation,12));
                    Circle circle = mMap.addCircle(new CircleOptions()
                            .center(usersLocation)
                            .radius(5000)
                            .strokeColor(Color.RED));
                    //.fillColor(Color.YELLOW));

                    showLocationsInsideCircle(mMap, usersLocation.latitude, usersLocation.longitude,locationLists);

                    mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(Marker marker) {

                        }

                        @Override
                        public void onMarkerDrag(Marker marker) {

                        }

                        @Override
                        public void onMarkerDragEnd(Marker marker) {

                            LatLng usersLocation = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(usersLocation).draggable(true).title("Your Location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(usersLocation,12));
                            Circle circle = mMap.addCircle(new CircleOptions()
                                    .center(usersLocation)
                                    .radius(5000)
                                    .strokeColor(Color.RED));
                            //.fillColor(Color.YELLOW));

                            showLocationsInsideCircle(mMap, marker.getPosition().latitude, marker.getPosition().longitude,locationLists);

                        }
                    });

                }

            }


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        locationLists.add(new LocationObject(23.7829396,90.4065545));
        locationLists.add(new LocationObject(23.7728661,90.4042706));
        locationLists.add(new LocationObject(23.7782392,90.3955507));


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        mapFragment.getMapAsync(this);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("BD")
                .build();

        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                LatLng usersLocation = place.getLatLng();
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(usersLocation).draggable(true).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(usersLocation,12));
                Circle circle = mMap.addCircle(new CircleOptions()
                        .center(usersLocation)
                        .radius(5000)
                        .strokeColor(Color.RED));
                //.fillColor(Color.YELLOW));

                showLocationsInsideCircle(mMap, usersLocation.latitude, usersLocation.longitude,locationLists);

            }

            @Override
            public void onError(Status status) {

            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.i("INFO_MAP_READY","map is ready..");

        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                LatLng usersLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(usersLocation).draggable(true).title("Your Location"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(usersLocation,12));
                Circle circle = mMap.addCircle(new CircleOptions()
                        .center(usersLocation)
                        .radius(5000)
                        .strokeColor(Color.RED));
                        //.fillColor(Color.YELLOW));

                showLocationsInsideCircle(mMap, location.getLatitude(), location.getLongitude(),locationLists);

                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {

                        LatLng usersLocation = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(usersLocation).draggable(true).title("Your Location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(usersLocation,12));
                        Circle circle = mMap.addCircle(new CircleOptions()
                                .center(usersLocation)
                                .radius(5000)
                                .strokeColor(Color.RED));
                        //.fillColor(Color.YELLOW));

                        showLocationsInsideCircle(mMap, marker.getPosition().latitude, marker.getPosition().longitude,locationLists);

                    }
                });

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(Build.VERSION.SDK_INT < 23){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(lastKnownLocation == null){
                    lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                }
                if(lastKnownLocation != null){
                    LatLng usersLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(usersLocation).draggable(true).title("Your Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(usersLocation,12));
                    Circle circle = mMap.addCircle(new CircleOptions()
                            .center(usersLocation)
                            .radius(5000)
                            .strokeColor(Color.RED));
                            //.fillColor(Color.YELLOW));

                    showLocationsInsideCircle(mMap, usersLocation.latitude, usersLocation.longitude,locationLists);

                    mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(Marker marker) {

                        }

                        @Override
                        public void onMarkerDrag(Marker marker) {

                        }

                        @Override
                        public void onMarkerDragEnd(Marker marker) {

                            LatLng usersLocation = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(usersLocation).draggable(true).title("Your Location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(usersLocation,12));
                            Circle circle = mMap.addCircle(new CircleOptions()
                                    .center(usersLocation)
                                    .radius(5000)
                                    .strokeColor(Color.RED));
                            //.fillColor(Color.YELLOW));

                            showLocationsInsideCircle(mMap, marker.getPosition().latitude, marker.getPosition().longitude,locationLists);

                        }
                    });

                }

            }
        }


    }

    private void showLocationsInsideCircle(GoogleMap mMap, double myLat, double myLong, ArrayList<LocationObject> locationLists){

        for(LocationObject loc : locationLists){

            float[] distance = new float[2];

            Location.distanceBetween(loc.getLat(),loc.getLon(),myLat,myLong,distance);

            LatLng usersLocation = new LatLng(loc.getLat(),loc.getLon());

            if(distance[0] <= 5000){
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
                mMap.addMarker(new MarkerOptions().position(usersLocation).draggable(false).title("").icon(bitmapDescriptor));
            }

        }

    }

}
