package com.example.amalv.oneblood1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by amalv on 07-10-2017.
 */

public class RewardAdapter extends ArrayAdapter<Reward> {
    public RewardAdapter(Activity context, ArrayList<Reward> rewards) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, rewards);
    }
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View listItemView=convertView;
        if(listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.list_reward,parent,false);
        }
        final Reward currentReq=getItem(position);
        TextView coupon=(TextView)listItemView.findViewById(R.id.ccode);
        TextView company=(TextView)listItemView.findViewById(R.id.comp);
        /*ViewGroup list_item1=(ViewGroup)listItemView.findViewById(R.id.list_item1);
        list_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            int aa=currentWord.getAudioResourceId();
            mediaPlayer=MediaPlayer.create(parent.getContext(),aa);
            mediaPlayer.start();

            }
        });
        */
        coupon.setText(currentReq.getmCode());

        company.setText(currentReq.getmCompany());

        return listItemView;
    }
}
