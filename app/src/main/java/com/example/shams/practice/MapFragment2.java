package com.example.shams.practice;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment2 extends Fragment implements OnMapReadyCallback {


    private GoogleMap mMap;

    LocationManager locationManager;

    LocationListener locationListener;

    GoogleApiClient mGoogleApiClient;

    PlaceAutocompleteFragment autocompleteFragment;

    ArrayList<LocationObject> locationLists = new ArrayList<LocationObject>();


    public MapFragment2() {
        // Required empty public constructor
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        /*if(rootView==null){
            rootView = inflater.inflate(R.layout.MapFragment, container, false);
        }
        return rootView;*/

        View v = inflater.inflate(R.layout.fragment_map_fragment2, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationLists.add(new LocationObject(23.7829396,90.4065545));
        locationLists.add(new LocationObject(23.7728661,90.4042706));
        locationLists.add(new LocationObject(23.7782392,90.3955507));

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map2);

        if(mapFragment != null){
            mapFragment.getMapAsync(this);
        }else{
            Toast.makeText(getContext(), "MapFragmentNull", Toast.LENGTH_LONG).show();
        }

        autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);



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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("INFO_MAP_READY","map is ready..");

        mMap = googleMap;

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

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
            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
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

    @Override
    public void onStop() {
        super.onStop();

        Toast.makeText(getContext(), "Stoping", Toast.LENGTH_LONG).show();

        /*Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.map2);
        if(fragment != null)
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();

        Fragment fragment1 = getActivity().getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);
        if(fragment1 != null)
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment1).commit();*/

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Toast.makeText(getContext(), "Destroying..", Toast.LENGTH_LONG).show();

        PlaceAutocompleteFragment fragment1 = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);

        if(fragment1 == null){
            Toast.makeText(getContext(), "fragment 1 null..", Toast.LENGTH_LONG).show();
        }

        if(fragment1 != null){
            getActivity().getFragmentManager().beginTransaction().remove(fragment1).commit();
        }

        SupportMapFragment fragment2 = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map2);
        if(fragment2 != null){
            getFragmentManager().beginTransaction().remove(fragment2).commit();
        }

    }
}
