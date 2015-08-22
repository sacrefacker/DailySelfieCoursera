package com.asm.dailyselfieasm;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmHandler extends BroadcastReceiver {
    private static final String TAG = "DailySelfieAsm";

    public static final int NOTE_ID = 12345;
    public static final String NOTE_ID_EXTRA = "selfie notification";
    public static final String NOTIFICATION = "selfie";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "received the intent");
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTE_ID_EXTRA, 0);
        notificationManager.notify(id, notification);

    }

    public static PendingIntent getAlarmIntent(Context context) {
        Intent alarmIntent = new Intent(context, AlarmHandler.class);
        alarmIntent.putExtra(NOTE_ID_EXTRA, NOTE_ID);
        alarmIntent.putExtra(NOTIFICATION, getRestartNotification(context, context
                .getString(R.string.notification_text)));
        return PendingIntent.getBroadcast(context, 0, alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

    }

    private static Notification getRestartNotification(Context context, String content) {

        Intent restartIntent = new Intent(context, MainActivity.class);
        PendingIntent intent = PendingIntent.getActivity(context, 0, restartIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Log.i(TAG, "made the restart intent");
        return new Notification.Builder(context)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(intent)
                .build();
    }
}
