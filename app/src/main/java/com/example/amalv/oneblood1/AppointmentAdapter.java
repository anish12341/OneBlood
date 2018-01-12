package com.example.amalv.oneblood1;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by amalv on 05-10-2017.
 */

public class AppointmentAdapter extends ArrayAdapter<Appointments> {
    Context conForActivity;
    public AppointmentAdapter(Context context, ArrayList<Appointments> app,Context con){
        super(context,0,app);
        conForActivity=con;
    }
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        Log.d("llll","Here");
        View listitem=convertView;
        if(listitem==null){
            listitem= LayoutInflater.from(getContext()).inflate(R.layout.list_appoint,parent,false);
        }
        final Appointments currentApp=getItem(position);
        TextView OR=(TextView)listitem.findViewById(R.id.organizer);
        TextView ORCON=(TextView)listitem.findViewById(R.id.organizercontact);
        TextView BloodBankOrg=(TextView)listitem.findViewById(R.id.BloodBankOrg);
        Button location=(Button)listitem.findViewById(R.id.btn_location);
        Button book=(Button)listitem.findViewById(R.id.btn_book);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent iintent = new Intent(conForActivity, LocateCampActivity.class);
                iintent.putExtra("latt",currentApp.getmLatitude());
                iintent.putExtra("longi",currentApp.getmLongitude());
                conForActivity.startActivity(iintent);



            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent iintent = new Intent(conForActivity, TimeSlotsActivity.class);
                Log.d("keyssssssssss",currentApp.getmKey());
                iintent.putExtra(Intent.EXTRA_TEXT,currentApp.getmKey());
                conForActivity.startActivity(iintent);



            }
        });
        OR.setText(currentApp.getmOrganizer());

        ORCON.setText(currentApp.getmOrganizerContact());

        BloodBankOrg.setText(currentApp.getmBloodBankOrg());
        return listitem;
    }
}
