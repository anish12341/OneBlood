package com.example.amalv.oneblood1;

import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TimeSlotsActivity extends AppCompatActivity {
    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference  requestdb= mRootRef.child("Camps");
    DatabaseReference  userDb=mRootRef.child("users");
    /*
    DatabaseReference num=requestdb.child("1");
    DatabaseReference time=num.child("TimeSlots");
    */
    DatabaseReference num,time,userChil;
    Button confirm;
    Bundle bundle1;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    View savedView=null;
    Long savedId=null;
    String chosen="",userid="";
    String text;
    int l=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slots);
        //final ArrayList<String> listofstring=new ArrayList<>();
            final ArrayList<Timings> tim=new ArrayList<Timings>();
        sharedPreferences=getSharedPreferences("mypref",0);
        userid = sharedPreferences.getString("Userid", null);
        Intent intent = getIntent();
       text = intent.getStringExtra(Intent.EXTRA_TEXT);
        num=requestdb.child(text);
        time=num.child("TimeSlots");
        userChil=userDb.child(userid);
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                time.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot entry = (DataSnapshot) dataSnapshot;
                        Iterable<DataSnapshot> oo = entry.getChildren();
                        Iterator itr = oo.iterator();
                        while (itr.hasNext()) {
                            DataSnapshot timings = (DataSnapshot) itr.next();
                            Log.d("class",timings.getValue().getClass().getName());
                            if(timings.getValue().getClass().getName().equals("java.lang.Long")) {
                                if ((Long)timings.getValue()==0) {
                                    tim.add(new Timings(timings.getKey()));
                                    Log.d("hhh", tim.toString());
                                }
                            }
                        }
                        SetAdapter(tim);

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
            }
        });
        t.start();
        try {
            t.join();
        }catch (InterruptedException e){
          Log.d("hhh","Exception");
        }

       if(l==1) {

       }

    }

    public void SetAdapter(final ArrayList<Timings> tim){
        Log.d("hhh","hererere");
        Log.d("ghghg",tim.toString());
        TimingsAdapter timingsAdapter=new TimingsAdapter(this,tim,TimeSlotsActivity.this);

        final ListView li=(ListView)findViewById(R.id.newlist);
        confirm=(Button)findViewById(R.id.btn_confirm);

        li.setAdapter(timingsAdapter);
        li.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d("view",Long.toString(savedId));

                if(savedId==null){
                    Log.d("view","Here");
                    TextView timeSlots=(TextView)view.findViewById(R.id.timeslot1);
                    timeSlots.setBackgroundColor(Color.parseColor("#FFF1E6E9"));
                    timeSlots.setTextColor(Color.parseColor("#FFEC173A"));
                    chosen=timeSlots.getText().toString();
                    savedId=id;
                    savedView=view;
                }
                else{
                    Log.d("view","Here1");
                    TextView timeSlots=(TextView)savedView.findViewById(R.id.timeslot1);
                    Log.d("view1",timeSlots.getText().toString());
                    timeSlots.setBackgroundColor(Color.parseColor("#FFEC173A"));
                    timeSlots.setTextColor(Color.parseColor("#FFF1E6E9"));
                    TextView timeSlots1=(TextView)view.findViewById(R.id.timeslot1);
                    timeSlots1.setBackgroundColor(Color.parseColor("#FFF1E6E9"));
                    timeSlots1.setTextColor(Color.parseColor("#FFEC173A"));
                    chosen=timeSlots1.getText().toString();
                    savedId=id;
                    savedView=view;
                }
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chosen.equals("")){
                    Toast.makeText(getApplicationContext(),"Setect TimeSlot First",Toast.LENGTH_LONG).show();
                }
                else {
                    Log.d("tktktk",chosen);
                    DatabaseReference ii = time.child(chosen);
                    ii.setValue(userid);
                    DatabaseReference oo=userChil.child("donationdone");
                    oo.setValue(0);
                    editor = sharedPreferences.edit();
                    editor.putInt("Key", Integer.parseInt(text));
                    editor.commit();
                    Log.d("booked",Integer.toString(sharedPreferences.getInt("Key",0)));
                    Toast.makeText(getApplicationContext(), "Appointment Taken...check it in my Appointments", Toast.LENGTH_LONG).show();
                    Intent HomeIntent = new Intent(TimeSlotsActivity.this, HomeActivity.class);
                    HomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(HomeIntent);
                    finish();
                }

            }
        });

    }
}
