package com.dudu.wearlauncher.services;

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;
import com.dudu.wearlauncher.utils.ILog;

public class NotificationListenerService extends android.service.notification.NotificationListenerService {
    @Override
    public void onCreate() {
        super.onCreate();
        ILog.v("Notification Service Created!!!");
    }

    @Override
    public IBinder onBind(Intent intent) {
        ILog.d("Service Bind:" + intent.getAction());
        return super.onBind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        ILog.d("Service Unbind:" + intent.getAction());
        return super.onUnbind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Intent intent = new Intent("com.dudu.wearlauncher.NotificationReceived");
        intent.putExtra("sbn", sbn);
        ILog.v("Notification Received:" + sbn.getNotification().toString());
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Intent intent = new Intent("com.dudu.wearlauncher.NotificationRemoved");
        intent.putExtra("sbn", sbn);
        ILog.v("Notification Removed:" + sbn.getNotification().toString());
    }
}
