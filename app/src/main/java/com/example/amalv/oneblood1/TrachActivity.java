package com.example.amalv.oneblood1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class TrachActivity extends AppCompatActivity {
    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference  locations= mRootRef.child("BloodBankLocation");
    public static final String TAG="SignupActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trach);
        String json=null;
        JSONObject jobj;
        String strJson="[{\"id\":\"101\",\"name\":\"Sonoo Jaiswal\",\"salary\":\"50000\"},{\"id\":\"102\",\"name\":\"Vimal Jaiswal\",\"salary\":\"60000\"}]";


        try {
            /*
            InputStream is = getAssets().open("data1.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            Log.d("TrachActivity",json);
            int i=0;
            JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
            reader.beginArray();
            reader.beginObject();
            while(i<10){
                Log.d(TAG,"HEre");
                String name=reader.nextName();
                Log.d("TrachActivity",name+"klaklakldskldkas");
                i++;
            }
            reader.endObject();
            reader.endArray();
*/
        }catch(Exception e){}
        String data = "";
        try {
            // Create the root JSONObject from the JSON string.
            //JSONObject  jsonRootObject = new JSONObject(strJson);
            InputStream is = getAssets().open("data1.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = new JSONArray(json);
            DatabaseReference lid;
            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String id = jsonObject.getString("id");
                String bloodbankname = jsonObject.getString("Blood Bank Name");
                String state = jsonObject.getString("State");
                String dist = jsonObject.getString("District");
                String city = jsonObject.getString("City");
                String addr = jsonObject.getString("Address");
                String lat = jsonObject.getString("Latitude");
                String lag = jsonObject.getString("Longitude");



                lid=locations.child(id);
                Map<String, Object> locationInfo= new HashMap<String, Object>();
                locationInfo.put("bloodbankname",bloodbankname);
                locationInfo.put("state",state);
                locationInfo.put("district",dist);
                locationInfo.put("city",city);
                locationInfo.put("address",addr);
                locationInfo.put("latitude",lat);
                locationInfo.put("longitude",lag);
                lid.updateChildren(locationInfo);
                //String name = jsonObject.optString("name").toString();
                //float salary = Float.parseFloat(jsonObject.optString("salary").toString());

                //data += "Node"+i+" : \n id= "+ id +" \n Name= "+ name +" \n Salary= "+ salary +" \n ";
                Log.d(TAG,"DATA="+id);
            }

        } catch (Exception e) {e.printStackTrace();}
    }
}

