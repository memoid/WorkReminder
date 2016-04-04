package com.memoid.workreminder;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.support.v4.app.NotificationCompat;
import android.support.v4.graphics.BitmapCompat;

public class ScheduleMailService extends IntentService {

    public static final int NOTIFICATION_ID = 1;

    private NotificationManager notificationManager;

    public ScheduleMailService() {
        super("ScheduleMailService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sendNotification();
    }

    public void sendNotification() {
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, DailyMailActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.mail)
                .setContentTitle("Send daily mail")
                .setContentText("Remember to send your daily mail")
                .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }

}
