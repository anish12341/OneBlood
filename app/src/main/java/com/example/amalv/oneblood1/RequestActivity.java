package com.example.amalv.oneblood1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RequestActivity extends AppCompatActivity {
    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference  requestdb= mRootRef.child("BloodRequests");
    DatabaseReference reqchil;
    private static final String TAG = "RequestActivity";
    EditText _contact,_city;
    Spinner _bloodgroup;
    Button _generaterequest;
    String userid=null,bg="";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int last=0,flag=0,flag1=0,flag2=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        sharedPreferences= getSharedPreferences("mypref", 0);
        if(sharedPreferences.contains("Userid")) {
            userid = sharedPreferences.getString("Userid", null);
        }
        _bloodgroup=(Spinner)findViewById(R.id.blood_group);
        _contact=(EditText)findViewById(R.id.contact_no);
        _city=(EditText)findViewById(R.id.city);
        _generaterequest=(Button)findViewById(R.id.btn_request);

        _generaterequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (_contact.getText().toString().length() == 0) {
                    _contact.setError("Please Enter ContactNo.");
                }
                else {
                    flag1=1;
                }
                if (_city.getText().toString().length() == 6) {
                    _city.setError("Please Enter City");
                }
                else{
                    flag2=1;
                }
                if(flag1==1&&flag2==1) {
                    generate();
                }
            }
        });
        requestdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> items = dataSnapshot.getChildren();

                Iterator itr = items.iterator();
                while (itr.hasNext()) {
                    flag=1;
                    //Log.d(TAG,itr.next().toString());
                    DataSnapshot entry = (DataSnapshot) itr.next();
                    String j=entry.getKey();
                    last=Integer.parseInt(j)+1;

                }
                //Log.i("temp",location.toString());
            }

            public void onCancelled(DatabaseError dberror) {

            }

        });
       _bloodgroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               bg=adapterView.getItemAtPosition(i).toString();
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {
              bg="O+";
           }
       });

        //requestInfo.put("");

    }
    public void generate(){
        Map<String, Object> requestInfo= new HashMap<String, Object>();
          if(flag==0){
              last=1;
             reqchil=requestdb.child(Integer.toString(last));
          }
          else{
              reqchil=requestdb.child(Integer.toString(last));
          }
          requestInfo.put("Userid",userid);
          requestInfo.put("BloodGroup",bg);
          requestInfo.put("ContactNo",_contact.getText().toString());
          requestInfo.put("City",_city.getText().toString());
          reqchil.updateChildren(requestInfo);
          Toast.makeText(getApplicationContext(),"Blood Request generated",Toast.LENGTH_LONG).show();
        editor=sharedPreferences.edit();
        editor.putInt("Keyforreq",last);
        editor.commit();
        Log.d("keyforreq",Integer.toString(sharedPreferences.getInt("Keyforreq",0)));
        Intent HomeIntent = new Intent(RequestActivity.this,HomeActivity.class);
        //processingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(HomeIntent);
        finish();

    }
}
