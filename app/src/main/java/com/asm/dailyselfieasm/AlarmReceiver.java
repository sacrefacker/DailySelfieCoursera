package com.asm.dailyselfieasm;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "DailySelfieAsm";

    public static final String NOTE_ID = "selfie notification";
    public static final String NOTE = "selfie";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "received the intent");
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTE);
        int id = intent.getIntExtra(NOTE_ID, 0);
        notificationManager.notify(id, notification);

    }
}
