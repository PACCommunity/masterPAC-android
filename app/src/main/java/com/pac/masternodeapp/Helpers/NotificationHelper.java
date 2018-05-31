package com.pac.masternodeapp.Helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.pac.masternodeapp.R;
import com.pac.masternodeapp.View.HomeActivity;
import com.pac.masternodeapp.View.PasscodeActivity;

public class NotificationHelper {

    public static void createLocalNotification(Context context, String title, String content) {
        Intent intent;

        SharedPreferences preferences = context.getSharedPreferences("active_passcode", 0);
        Boolean pinIsActive = preferences.getBoolean("active_passcode", false);
        if (pinIsActive) {
            intent = new Intent(context, PasscodeActivity.class);
        } else {
            intent = new Intent(context, HomeActivity.class);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(context, "com.pac.masternodeapp");

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_pac_icon_notification)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_pac_icon_1))
                .setTicker(context.getString(R.string.app_name))
                .setContentTitle(title)
                .setContentText(content)
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }

}
