package com.example.amalv.oneblood1;

import android.util.Log;

/**
 * Created by amalv on 07-10-2017.
 */

public class Reward {
    private String mCode;
    private String mCompany;
    public Reward(String code,String company){
        mCode=code;
        mCompany=company;
        //Log.d("ccccc",mTim);
    }

    public String getmCode(){return mCode;}

    public String getmCompany(){return mCompany;
    }
}
