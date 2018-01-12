package com.example.amalv.oneblood1;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        /*
        mp=MediaPlayer.create(context, R.raw.alrm   );
        mp.start();
        Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();

        */

        final SharedPreferences sharedPreferences=context.getSharedPreferences("mypref",0);
        if(isNetworkAvailable(context)){
            DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
            DatabaseReference  requestdb= mRootRef.child("BloodRequests");
            requestdb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Iterable<DataSnapshot> location = dataSnapshot.getChildren();
                    Iterator itr = location.iterator();
                    while (itr.hasNext()) {
                        DataSnapshot entry = (DataSnapshot) itr.next();
                        HashMap<String, String> str = (HashMap<String, String>) entry.getValue();
                        for (Map.Entry m : str.entrySet()) {
                            if(m.getKey().equals("City")) {
                                if (m.getValue().toString().equalsIgnoreCase(sharedPreferences.getString("Usercity", null))) {
                                    NotificationScheduler.showNotification(context, HomeActivity.class,
                                            "Blood Request Notification", "Someone in your city needs blood");
                                }
                            }
                            }


                    }
                }
                public void onCancelled(DatabaseError dberror) {

                }
            });
        }




    }
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}