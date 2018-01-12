package com.example.amalv.oneblood1;

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

public class GeneratedReqActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference  requestdb= mRootRef.child("BloodRequests");
    DatabaseReference req;
    TextView bg,contact,norequst;
    Button cancel;
    LinearLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_req);
        bg=(TextView)findViewById(R.id.bloodgrp);
        contact=(TextView)findViewById(R.id.cnno);
        norequst=(TextView)findViewById(R.id.norequest2);
        container=(LinearLayout)findViewById(R.id.text_container3);
        cancel=(Button)findViewById(R.id.btn_cancel2);
        sharedPreferences = getSharedPreferences("mypref", 0);

        if (sharedPreferences.getInt("Keyforreq", 0) == 0) {
            container.setVisibility(View.INVISIBLE);
        } else {
            norequst.setVisibility(View.INVISIBLE);
            req=requestdb.child(Integer.toString(sharedPreferences.getInt("Keyforreq",0)));
            req.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DataSnapshot entry = (DataSnapshot) dataSnapshot;
                    Iterable<DataSnapshot> oo = entry.getChildren();
                    Iterator itr = oo.iterator();
                    while (itr.hasNext()) {
                        DataSnapshot info = (DataSnapshot) itr.next();
                        if (info.getKey().equals("BloodGroup")) {
                            bg.setText(info.getValue().toString());
                        } else if (info.getKey().equals("ContactNo")) {
                            contact.setText(info.getValue().toString());
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
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    req.removeValue();
                    editor = sharedPreferences.edit();
                    editor.remove("Keyforreq");
                    editor.commit();
                }
            });
        }
    }


}
