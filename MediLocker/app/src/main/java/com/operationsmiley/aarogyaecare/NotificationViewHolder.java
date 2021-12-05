package com.operationsmiley.aarogyaecare;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    TextView notification_name,notification_desc,notification_time;
    ImageView notification_image,notification_icon;
    private itemonclicklistener listener;

    NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
        notification_name = itemView.findViewById(R.id.notification_header);
        notification_desc = itemView.findViewById(R.id.notification_text);
        notification_time = itemView.findViewById(R.id.time_notification);
        notification_image = itemView.findViewById(R.id.notification_image);
        notification_icon = itemView.findViewById(R.id.notification_icon);
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
