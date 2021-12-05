package com.operationsmiley.aarogyaecare;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

public class sharedbymeviewholder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView email;
    public SwitchCompat switchaccess;
    public ItemClickListener listner;

    public sharedbymeviewholder(@NonNull View itemView) {
        super(itemView);
        email=itemView.findViewById(R.id.cardsharedbymeemail);
        switchaccess=itemView.findViewById(R.id.availableswitch);
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
