package com.randi_horace.skillshare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MissionFragment extends Fragment {

    private RecyclerView mMissionlist;

    private DatabaseReference mDatabase;



    public MissionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_mission, container, false);
        //create view for findViewById because Fragment doesn't provide findViewById()

        mMissionlist = (RecyclerView) view.findViewById(R.id.mission_list);
        mMissionlist.setHasFixedSize(true);
        mMissionlist.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Mission");






        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Mission, MissionViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Mission, MissionViewHolder>(
                Mission.class, R.layout.mission_row, MissionViewHolder.class,mDatabase) {
            @Override
            protected void populateViewHolder(MissionViewHolder viewHolder, Mission model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());
                viewHolder.setUid(model.getUid());
                viewHolder.setDistrict(model.getLatitude());
                Log.e("debug lat",""+model.getLatitude());



            }
        };

        mMissionlist.setAdapter(firebaseRecyclerAdapter);

    }



    public static class MissionViewHolder extends RecyclerView.ViewHolder{

        View mView;

        ImageButton chatButton;



        public MissionViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setTitle(String title){

            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);

        }

        public void setDesc(String desc){

            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);

        }

        public void setImage(Context ctx, String image){

            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.get().load(image).into(post_image);

        }

        public void setUid(String uid){

            TextView post_uid = (TextView) mView.findViewById(R.id.post_uid);
            post_uid.setText(uid);

        }

        public void setDistrict (String district){
            TextView post_dist = (TextView) mView.findViewById(R.id.post_dist);

            if (district.equals("22.2724967")){
                district = "Central and Western";
                Log.e("debug dist","central");
            }
            else if (district.equals("22.2756648")){
                district = "Eastern";
            }
            else if (district.equals("22.2395616")){
                district = "Southern";
            }
            else if (district.equals("22.2773499")){
                district = "Wan Chai";
            }
            else if (district.equals("22.3290706")){
                district = "Sham Shui Po";
            }
            else if (district.equals("22.3219813")){
                district = "Kowloon City";
            }
            else if (district.equals("22.3120123")){
                district = "Kwun Tong";
            }
            else if (district.equals("22.3420553")){
                district = "Wong Tai Sin";
            }
            else if (district.equals("22.3099511")){
                district = "Yau Tsim Mong";
            }
            else if (district.equals("22.3548163")){
                district = "Islands";
            }
            else if (district.equals("22.3534523")){
                district = "Kwai Tsing";
            }
            else if (district.equals("22.5124981")){
                district = "North";
            }
            else if (district.equals("22.4081311")){
                district = "Sai Kung";
            }
            else if (district.equals("22.3887767")){
                district = "Sha Tin";
            }
            else if (district.equals("22.4459487")){
                district = "Tai Po";
            }
            else if (district.equals("22.3707447")){
                district = "Tsuen Wan";
            }
            else if (district.equals("22.395427")){
                district = "Tuen Mun";
            }
            else if (district.equals("22.4458533")){
                district = "Yuen Long";
            }

            post_dist.setText(district);
        }


    }




}
