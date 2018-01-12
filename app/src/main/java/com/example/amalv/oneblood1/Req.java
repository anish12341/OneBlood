package com.example.amalv.oneblood1;

/**
 * Created by amalv on 04-10-2017.
 */

public class Req {
    private String mBloodgroup;
    private String mContactno;
    public Req (String Bloodgroup,String Contactno){
           mBloodgroup=Bloodgroup;
           mContactno=Contactno;
    }

    public String getmBloodgroup(){return mBloodgroup;}

    public String getmContactno(){return mContactno;}
}
