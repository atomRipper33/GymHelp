package com.example.android.gymhelp;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MyReceiver extends BroadcastReceiver {

    private NotificationManagerCompat notificationManager;
    DatabaseHelper myDataHelper;
    @Override
    public void onReceive(final Context context, Intent intent) {
        Intent activityIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, activityIntent, 0);
        Intent clickBroad = new Intent(context,NotificationReceiver.class);
        PendingIntent clickIntent = PendingIntent.getBroadcast(context,0,clickBroad,0);
        myDataHelper = new DatabaseHelper(context);
        Integer exMemToday = myDataHelper.getTheNumberOfExMemships();
        notificationManager = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context,App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_people_green)
                .setContentTitle("Expiring memberships")
                .setContentText("There is "+exMemToday+" expiring memberships today")
                .setColor(Color.GREEN)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .addAction(R.drawable.baseline_add_white_18dp,"SEND",clickIntent)
                .build();
        notificationManager.notify(1,notification);

    }


}
