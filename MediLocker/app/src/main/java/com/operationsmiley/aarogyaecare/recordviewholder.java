package com.operationsmiley.aarogyaecare;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class recordviewholder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView vhfilename,vhfilesize,vhdate,vhrecordtype;
    public ImageView vhpdforimg,vhdropdown;
    public ItemClickListener listner;

    public recordviewholder(@NonNull View itemView) {
        super(itemView);
        vhfilename=itemView.findViewById(R.id.cardfilename);
        vhfilesize=itemView.findViewById(R.id.cardsize);
        vhdate=itemView.findViewById(R.id.carddate);
        vhrecordtype=itemView.findViewById(R.id.cardrecordtype);
        vhpdforimg=itemView.findViewById(R.id.cardfiletypeimg);
        vhdropdown=itemView.findViewById(R.id.carddropdown);
    }

    public void setitemclicklistener(ItemClickListener listner)
    {
        this.listner=listner;
    }
    @Override
    public void onClick(View v) {
        listner.onClick(v, getAdapterPosition(), false);
    }


}
