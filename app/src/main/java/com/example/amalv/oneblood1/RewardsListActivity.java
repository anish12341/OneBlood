package com.example.amalv.oneblood1;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RewardsListActivity extends AppCompatActivity {
    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference  requestdb= mRootRef.child("Rewards");
    DatabaseReference  userdb= mRootRef.child("users");
    DatabaseReference userchil;
    String userid;
    private static final String TAG = "RewardsListActivity";
    TextView point;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_list);
        SharedPreferences sharedPreferences=getSharedPreferences("mypref",0);
        userid=sharedPreferences.getString("Userid",null);

        Log.d(TAG,userid);
        userchil=userdb.child(userid);
        point=(TextView)findViewById(R.id.points);
        final ArrayList<Reward> rewards=new ArrayList<Reward>();
        requestdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> location = dataSnapshot.getChildren();
                Iterator itr = location.iterator();
                while (itr.hasNext()) {
                    Log.d("ggggg","hereee");
                    DataSnapshot entry = (DataSnapshot) itr.next();
                    HashMap<String, String> str = (HashMap<String, String>) entry.getValue();
                     Log.d("hhhh","hereee"+str.get("City"));
                    rewards.add(new Reward(str.get("couponcode"),str.get("company")));

                }
                SetAdapter(rewards);
                get();
            }
            public void onCancelled(DatabaseError dberror) {

            }
        });

    }
    public void SetAdapter(ArrayList<Reward> requests){
        RewardAdapter reqadapter=new RewardAdapter(this,requests);
        final ListView listView = (ListView) findViewById(R.id.list12345);

        listView.setAdapter(reqadapter);
    }
    public void get(){
        userchil.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot entry = (DataSnapshot) dataSnapshot;
                Iterable<DataSnapshot> oo = entry.getChildren();
                Iterator itr = oo.iterator();
                while (itr.hasNext()) {
                    DataSnapshot info = (DataSnapshot) itr.next();
                    if(info.getKey().equals("Points")){
                        point.setText(Long.toString((Long)info.getValue()));
                        break;
                    }
                }
            }

            public void onCancelled(DatabaseError dberror) {

            }
        });

    }
}
