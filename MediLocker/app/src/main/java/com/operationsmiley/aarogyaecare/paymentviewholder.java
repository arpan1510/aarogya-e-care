package com.operationsmiley.aarogyaecare;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class paymentviewholder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView orderid,datep,payable,storagep,paid,failed;
    public ImageView imagefail,imagepass,upkey,downkey;
    public RelativeLayout relativedate,relativepayable,relativestorage,relativestatus;
    public ItemClickListener listner;

    public paymentviewholder(@NonNull View itemView) {
        super(itemView);
        orderid=itemView.findViewById(R.id.orderid);
        datep=itemView.findViewById(R.id.dateofpayment);
        payable=itemView.findViewById(R.id.payableofpayment);
        storagep=itemView.findViewById(R.id.storageofpayment);
        paid=itemView.findViewById(R.id.statusofpaymentpass);
        failed=itemView.findViewById(R.id.statusofpaymentfail);
        imagefail=itemView.findViewById(R.id.pimagered);
        imagepass=itemView.findViewById(R.id.pimagegreen);
       upkey=itemView.findViewById(R.id.pimage1arrowup);
       downkey=itemView.findViewById(R.id.pimage1arrowdown);
        relativedate=itemView.findViewById(R.id.daterelativepayment);
        relativepayable=itemView.findViewById(R.id.payablerelativepayment);
        relativestorage=itemView.findViewById(R.id.storagerelativepayment);
        relativestatus=itemView.findViewById(R.id.statusrelativepayment);


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
