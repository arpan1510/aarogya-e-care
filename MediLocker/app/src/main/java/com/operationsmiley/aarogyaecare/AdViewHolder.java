package com.operationsmiley.aarogyaecare;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView ad_header,ad_hashtag;
    public ImageView ad_image;
    public itemonclicklistener listener;

    public AdViewHolder(@NonNull View itemView) {
        super(itemView);
        ad_header = itemView.findViewById(R.id.ad_header);
        ad_image = itemView.findViewById(R.id.ad_image);
        ad_hashtag = itemView.findViewById(R.id.ad_hashtag);
    }

    public void setitemclicklistener(itemonclicklistener listener)
    {
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(),false);
    }

}
