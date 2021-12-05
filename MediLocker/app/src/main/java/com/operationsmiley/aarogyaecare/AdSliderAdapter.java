package com.operationsmiley.aarogyaecare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class AdSliderAdapter extends SliderViewAdapter<SliderViewHolder> {

    Context context;
    int setTotalCount;
    String ImageLink;

    public AdSliderAdapter(Context context,  int setTotalCount) {
        this.context = context;
        this.setTotalCount = setTotalCount;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_slider_item, parent,false);
        return new SliderViewHolder(view);

    }

    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {

        FirebaseDatabase.getInstance().getReference("AdBannerImages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                switch (position){
                    case 0:
                        if(dataSnapshot.child("1").exists()){
                        ImageLink = dataSnapshot.child("1").getValue().toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.sliderImageView);
                        }
                        break;
                    case 1:
                        if(dataSnapshot.child("2").exists()) {
                            ImageLink = dataSnapshot.child("2").getValue().toString();
                            Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.sliderImageView);
                        }
                        break;
                    case 2:
                        if(dataSnapshot.child("3").exists()) {
                            ImageLink = dataSnapshot.child("3").getValue().toString();
                            Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.sliderImageView);
                        }
                        break;
                    case 3:
                        if(dataSnapshot.child("4").exists()) {
                            ImageLink = dataSnapshot.child("4").getValue().toString();
                            Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.sliderImageView);
                        }
                        break;
                    case 4:
                        if(dataSnapshot.child("5").exists()) {
                            ImageLink = dataSnapshot.child("5").getValue().toString();
                            Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.sliderImageView);
                        }
                        break;
                    case 5:
                        if(dataSnapshot.child("6").exists()) {
                            ImageLink = dataSnapshot.child("6").getValue().toString();
                            Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.sliderImageView);
                        }
                        break;
                    case 6:
                        if(dataSnapshot.child("7").exists()) {
                            ImageLink = dataSnapshot.child("7").getValue().toString();
                            Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.sliderImageView);
                        }
                        break;
                    case 7:
                        if(dataSnapshot.child("8").exists()) {
                            ImageLink = dataSnapshot.child("8").getValue().toString();
                            Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.sliderImageView);
                        }
                        break;
                    case 8:
                        if(dataSnapshot.child("9").exists()) {
                            ImageLink = dataSnapshot.child("9").getValue().toString();
                            Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.sliderImageView);
                        }
                        break;
                    case 9:
                        if(dataSnapshot.child("10").exists()) {
                            ImageLink = dataSnapshot.child("10").getValue().toString();
                            Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.sliderImageView);
                        }
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getCount() {
        return setTotalCount;
    }
}


class SliderViewHolder extends SliderViewAdapter.ViewHolder{

    ImageView sliderImageView;
    View itemView;
    public SliderViewHolder(View itemView) {
        super(itemView);
        sliderImageView = itemView.findViewById(R.id.ad_imageview);
        this.itemView = itemView;
    }
}

