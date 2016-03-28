package com.memoid.workreminder;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class ScheduleMailService extends IntentService {
    public static final String TAG = "Schedule Mail";
    public static final int NOTIFICATION_ID = 1;

    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    public ScheduleMailService() {
        super("ScheduleMailService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sendNotificartion();
    }

    public void sendNotificartion() {
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, DailyMailActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.mail)
                .setContentTitle("Send daily mail")
                .setContentText("Remember to send your daily mail");

        mBuilder.setContentIntent(contentIntent);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }

}
