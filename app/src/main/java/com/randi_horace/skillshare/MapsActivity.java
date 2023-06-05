package com.randi_horace.skillshare;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<String> longitudeArray;
    ArrayList<String> latitudeArray;
    ArrayList<String> titleArray;
    LatLng newPlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Mission");     //get the data before initialize the map
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of mission in datasnapshot
                        collectLatitude((Map<String,Object>) dataSnapshot.getValue());
                        collectLongitude((Map<String,Object>) dataSnapshot.getValue());
                        collectTitle((Map<String,Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void collectLatitude(Map<String,Object> users) {        //get latitude

        ArrayList<String> latitude = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            latitude.add(singleUser.get("latitude").toString());
        }

        latitudeArray = (ArrayList<String>) latitude.clone();
        for (String i : latitudeArray){    //for debug
            Log.i("latitudeArray: ", i);
        }
    }

    private void collectLongitude(Map<String,Object> users) {       //get longitude

        ArrayList<String> longitude = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            longitude.add(singleUser.get("longitude").toString());
        }

        longitudeArray = (ArrayList<String>) longitude.clone();
        for (String i : longitudeArray) {  //for debug
            Log.i("longitudeArray: ", i);
        }
    }

    private void collectTitle(Map<String,Object> users) {       //get title

        ArrayList<String> title = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            title.add(singleUser.get("title").toString());
        }

        titleArray = (ArrayList<String>) title.clone(); //clone class array to public array
        for (String i : titleArray) {   //for debug
            Log.i("longitudeArray: ", i);
        }
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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng hk = new LatLng(22.28056, 114.17222);
        moveMap(hk);
        for(int x = 0; x < latitudeArray.size(); x++) {
            newPlace = new LatLng(Double.parseDouble(latitudeArray.get(x)), Double.parseDouble(longitudeArray.get(x)));
            addMarker(newPlace, titleArray.get(x), "...");
        }

    }

    private void addMarker(LatLng place, String title, String snippet) {
        BitmapDescriptor icon =
                BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(place)
                .title(title)
                .snippet(snippet)
                .icon(icon);

        mMap.addMarker(markerOptions);
    }

    private void moveMap(LatLng place) {

        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(10)
                        .build();

        //animation
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
