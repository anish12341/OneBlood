package com.example.amalv.oneblood1;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AppointmentActivity extends AppCompatActivity {
    String key=null;
    //for each key of each blood donation camp
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        final SharedPreferences sharedPreferences=getSharedPreferences("mypref",0);
        final ArrayList<Appointments> appoins=new ArrayList<Appointments>();
        DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
        DatabaseReference  requestdb= mRootRef.child("Camps");
        requestdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("fffffggg","hereee");
                Iterable<DataSnapshot> location = dataSnapshot.getChildren();
                Iterator itr = location.iterator();
                while (itr.hasNext()) {
                    Log.d("ggggghhh","hereee");
                    DataSnapshot entry = (DataSnapshot) itr.next();
                    String key=entry.getKey();
                    //Log.d("keyssss",key);
                    HashMap<String, String> str = (HashMap<String, String>) entry.getValue();
                    Log.d("nnnnnn",sharedPreferences.getString("Usercity",null)+"KKKK");
                    if(sharedPreferences.getString("Usercity",null).equalsIgnoreCase(str.get("City"))){
                        Log.d("aaaaa","her"+str.get("Organizer"));
                        Log.d("aaaaa","her"+str.get("OrganizerContact"));
                        appoins.add(new Appointments(str.get("Organizer"),str.get("OrganizerContact"),str.get("BloodBankOrg")
                        ,str.get("Latitude"),str.get("Longitude"),key));

                        Log.d("hihihi",appoins.toString());
                    }

                }
                SetAdapter(appoins);
            }
            public void onCancelled(DatabaseError dberror) {

            }
        });


    }
    public void SetAdapter(ArrayList<Appointments> appoins){
        AppointmentAdapter appadapter=new AppointmentAdapter (this,appoins,AppointmentActivity.this);

        ListView li=(ListView)findViewById(R.id.list123);

        li.setAdapter(appadapter);
    }
}
