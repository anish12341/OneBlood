package com.example.amalv.oneblood1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class BookedAppoinActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference  requestdb= mRootRef.child("Camps");
    DatabaseReference num,time;
    String userid="",chosen="";
    TextView org,orgcontact,bborg,timeslot,norequst;
    LinearLayout container;
    Button location,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_appoin);
        sharedPreferences = getSharedPreferences("mypref", 0);
        org = (TextView) findViewById(R.id.organizer1);
        orgcontact = (TextView) findViewById(R.id.organizercontact1);
        bborg = (TextView) findViewById(R.id.BloodBankOrg1);
        timeslot = (TextView) findViewById(R.id.BookedTimeSlot);
        location = (Button) findViewById(R.id.btn_location1);
        cancel = (Button) findViewById(R.id.btn_cancel);
        container = (LinearLayout) findViewById(R.id.text_container2);
        norequst = (TextView) findViewById(R.id.norequest);
        if (sharedPreferences.getInt("Key", 0) == 0) {
            container.setVisibility(View.INVISIBLE);
        } else {
            norequst.setVisibility(View.INVISIBLE);
            userid = sharedPreferences.getString("Userid", null);
            Log.d("Booked", Integer.toString(sharedPreferences.getInt("Key", 0)));
            num = requestdb.child(Integer.toString(sharedPreferences.getInt("Key", 0)));
            time = num.child("TimeSlots");
            num.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DataSnapshot entry = (DataSnapshot) dataSnapshot;
                    Iterable<DataSnapshot> oo = entry.getChildren();
                    Iterator itr = oo.iterator();
                    while (itr.hasNext()) {
                        DataSnapshot info = (DataSnapshot) itr.next();
                        if (info.getKey().equals("Organizer")) {
                            org.setText(info.getValue().toString());
                        } else if (info.getKey().equals("OrganizerContact")) {
                            orgcontact.setText(info.getValue().toString());
                        } else if (info.getKey().equals("BloodBankOrg")) {
                            bborg.setText(info.getValue().toString());
                        } else if (info.getKey().equals("TimeSlots")) {
                            Iterable<DataSnapshot> ent = info.getChildren();
                            Iterator iter = ent.iterator();
                            while (iter.hasNext()) {
                                DataSnapshot timings = (DataSnapshot) iter.next();
                                Log.d("inside job", timings.getValue().toString());
                                if (timings.getValue().getClass().getName().equals("java.lang.String")) {
                                    if (timings.getValue().equals(userid)) {
                                        chosen = timings.getKey();
                                        timeslot.setText(timings.getKey());
                                    }
                                }
                            }
                        }
                        Log.d("Information", info.getKey());
                    }


                /*
                Iterable<DataSnapshot> location = dataSnapshot.getChildren();
                Iterator itr = location.iterator();
                while (itr.hasNext()) {
                    Log.d("ggggghhh","hereee");
                    DataSnapshot entry = (DataSnapshot) itr.next();
                    HashMap<String, String> str = (HashMap<String, String>) entry.getValue();
                    if(sharedPreferences.getString("Usercity",null).equalsIgnoreCase(str.get("City"))){
                        Log.d("aaaaa","her"+str.get("Organizer"));
                        Log.d("aaaaa","her"+str.get("OrganizerContact"));
                        appoins.add(new Appointments(str.get("Organizer"),str.get("OrganizerContact"),str.get("BloodBankOrg")
                                ,str.get("Organizer"),str.get("Latitude")));

*/
                }

                public void onCancelled(DatabaseError dberror) {

                }
            });
            location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent iintent = new Intent(BookedAppoinActivity.this, LocateCampActivity.class);
                    startActivity(iintent);
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference ii = time.child(chosen);
                    ii.setValue(0);
                    editor = sharedPreferences.edit();
                    editor.remove("Key");
                    editor.commit();
                }
            });
        }
    }
}
