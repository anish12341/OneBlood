package com.example.amalv.oneblood1;

import android.util.Log;

/**
 * Created by amalv on 04-10-2017.
 */

public class Appointments {
    private String mOrganizer;
    private String mOrganizerContact;
    private String mBloodBankOrg;
    private String mLatitude;
    private String mLongitude;
    private String mKey;
    public Appointments (String Bloodgroup,String Contactno,String BloodBankOrg,String Latitude,String Longitude,String kk){
        mOrganizer=Bloodgroup;
        mOrganizerContact=Contactno;
        mBloodBankOrg=BloodBankOrg;
        mLatitude=Latitude;
        mLongitude=Longitude;
        mKey=kk;
        Log.d("aaa","init"+mOrganizer);
        Log.d("aaa","init"+mOrganizerContact);
    }

    public String getmOrganizer(){return mOrganizer;}

    public String getmOrganizerContact(){return mOrganizerContact;}

    public String getmBloodBankOrg(){return mBloodBankOrg;}

    public String getmLatitude(){return mLatitude;}

    public String getmLongitude(){return mLongitude;}

    public String getmKey(){return mKey;}
}
