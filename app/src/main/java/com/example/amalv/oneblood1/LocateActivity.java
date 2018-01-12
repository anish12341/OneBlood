package com.example.amalv.oneblood1;

import android.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class LocateActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference  userdb= mRootRef.child("users");
    DatabaseReference uid;
    DatabaseReference BloodBanksNearBy;
    LatLng location;
    String UserCity=null,userid=null,Longitde=null,Latitude=null,o=null;
    SharedPreferences sharedPreferences;
    public static final String TAG;

    static {
        TAG = "LocateActivity";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        BloodBanksNearBy= mRootRef.child("BloodBankLocation");
        sharedPreferences= getSharedPreferences("mypref", 0);
        userid=sharedPreferences.getString("Userid",null);
        DatabaseReference  uid= mRootRef.child(sharedPreferences.getString("Userid",null));
        DatabaseReference users = mRootRef.child("users");

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> location = dataSnapshot.getChildren();
                Iterator itr = location.iterator();
                while (itr.hasNext()) {
                    DataSnapshot entry = (DataSnapshot) itr.next();
                    String key=(String)entry.getKey();
                    if(key.equals(userid)) {
                        HashMap<String, String> str = (HashMap<String, String>) entry.getValue();
                        for (Map.Entry m : str.entrySet()) {
                            if(m.getKey().equals("City")) {
                                UserCity = (String) m.getValue();
                            }
                        }

                    }
                }
            }
       public void onCancelled(DatabaseError dberror) {

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
        mMap = googleMap;
        BloodBanksNearBy.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> items = dataSnapshot.getChildren();
                Iterator itr = items.iterator();
                while (itr.hasNext()) {
                    DataSnapshot entry = (DataSnapshot) itr.next();
                    HashMap<String, String> str = (HashMap<String, String>) entry.getValue();
                    o=str.get("city");
                    if(o.equals(UserCity)){
                        Longitde=(String) str.get("longitude");
                        Latitude=(String)str.get("latitude");
                        Log.d(TAG,Longitde+"lllllll"+Latitude);
                        location=new LatLng(Double.parseDouble(Latitude),Double.parseDouble(Longitde));
                        mMap.addMarker(new MarkerOptions().position(location).title(str.get("bloodbankname")));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            return;
                        }
                        mMap.setMyLocationEnabled(true);

                    }

                }
                //Log.i("temp",location.toString());
            }

            public void onCancelled(DatabaseError dberror) {

            }

        });

       }


    @Override
    public void onStart() {
        super.onStart();

    }
}
