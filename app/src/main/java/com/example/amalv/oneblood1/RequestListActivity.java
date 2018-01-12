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
import java.util.Map;

public class RequestListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        Log.d("eeee","hereee");
        final SharedPreferences sharedPreferences=getSharedPreferences("mypref",0);
        final ArrayList<Req> requests=new ArrayList<Req>();
        DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
        DatabaseReference  requestdb= mRootRef.child("BloodRequests");
        requestdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("fffff","hereee");
                Iterable<DataSnapshot> location = dataSnapshot.getChildren();
                Iterator itr = location.iterator();
                while (itr.hasNext()) {
                    Log.d("ggggg","hereee");
                    DataSnapshot entry = (DataSnapshot) itr.next();
                    HashMap<String, String> str = (HashMap<String, String>) entry.getValue();
                    if(sharedPreferences.getString("Usercity",null).equalsIgnoreCase(str.get("City"))){
                        Log.d("hhhh","hereee"+str.get("City"));
                        requests.add(new Req(str.get("BloodGroup"),str.get("ContactNo")));
                    }
                }
                SetAdapter(requests);
            }
            public void onCancelled(DatabaseError dberror) {

            }
        });

    }
    public void SetAdapter(ArrayList<Req> requests){
        RequestAdapter reqadapter=new RequestAdapter(this,requests);
        final ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(reqadapter);
    }

}
