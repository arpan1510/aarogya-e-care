package com.operationsmiley.aarogyaecare;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class sharedwithmeviewholder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView email;
    public ItemClickListener listner;

    public sharedwithmeviewholder(@NonNull View itemView) {
        super(itemView);
        email=itemView.findViewById(R.id.cardsharedwithmeemail);
    }

    public void setitemclicklistener(ItemClickListener listner)
    {
        this.listner=listner;
    }
    @Override
    public void onClick(View v)
    {
        listner.onClick(v, getAdapterPosition(), false);
    }


}
