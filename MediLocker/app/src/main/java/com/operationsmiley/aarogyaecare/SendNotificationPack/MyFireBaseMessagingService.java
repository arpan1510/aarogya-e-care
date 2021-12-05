package com.operationsmiley.aarogyaecare.SendNotificationPack;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.operationsmiley.aarogyaecare.NotificationsActivity;
import com.operationsmiley.aarogyaecare.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MyFireBaseMessagingService extends FirebaseMessagingService {
    String title,message;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
            super.onMessageReceived(remoteMessage);
            title=remoteMessage.getData().get("Title");
            message=remoteMessage.getData().get("Message");

        String CHANNEL_ID = "MESSAGE";
        String CHANNEL_NAME = "MESSAGE";

        Intent notificationIntent = new Intent(getApplicationContext(), NotificationsActivity.class);
// set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
            Notification notification=new NotificationCompat.Builder(MyFireBaseMessagingService.this,CHANNEL_ID)
                    .setSmallIcon(R.drawable.newlogotrans)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(intent)
                    .build();
            manager.notify(getRandomNumber(),notification);

    }

    private static int getRandomNumber()
    {
        Date dd=new Date();
        SimpleDateFormat ft=new SimpleDateFormat("mmssSS");
        String s =ft.format(dd);
        return Integer.parseInt(s);
    }

}
