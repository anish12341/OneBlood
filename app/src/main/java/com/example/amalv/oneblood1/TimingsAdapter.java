package com.example.amalv.oneblood1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by amalv on 06-10-2017.
 */

public class TimingsAdapter extends ArrayAdapter<Timings> {
    int count=0;
    Context conForTimeSlots;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public TimingsAdapter(Activity context, ArrayList<Timings> t,Context con){
        super(context,0,t);
        conForTimeSlots=con;
        sharedPreferences=conForTimeSlots.getSharedPreferences("mypref",0);
         editor=sharedPreferences.edit();
        editor.putInt("count",0);
        editor.commit();

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Log.d("ddd","herrr");
        View listitemw = convertView;
        if(listitemw==null){
            listitemw= LayoutInflater.from(getContext()).inflate(R.layout.list_timings,parent,false);
        }
        Timings currentTim=getItem(position);
        final TextView timeSlots=(TextView)listitemw.findViewById(R.id.timeslot1);

        timeSlots.setText(currentTim.getmTim());

/*
        timeSlots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int o=sharedPreferences.getInt("count",0);
                Log.d("pppqqq",sharedPreferences.getInt("count",0)+"yayy");
                editor=sharedPreferences.edit();
                editor.putInt("count",o+1);
                editor.commit();
                count+=1;
               timeSlots.setBackgroundColor(Color.parseColor("#FFF70F2A"));
                }
        });
        */
        return listitemw;
    }
}
