package com.example.amalv.oneblood1;

import android.util.Log;

/**
 * Created by amalv on 06-10-2017.
 */

public class Timings {
    private String mTim;
    public Timings(String Tim){
        mTim=Tim;
        Log.d("ccccc",mTim);
    }

    public String getmTim(){return mTim;}
    @Override
    public String toString(){
        return mTim;
    }
}
