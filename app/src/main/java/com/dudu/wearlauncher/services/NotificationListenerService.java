package com.dudu.wearlauncher.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.notification.StatusBarNotification;
import com.dudu.wearlauncher.utils.ILog;

public class NotificationListenerService extends android.service.notification.NotificationListenerService {

    NotificationServiceReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new NotificationServiceReceiver();
        registerReceiver(receiver, new IntentFilter("com.dudu.wearlauncher.NOTIFICATION_LISTENER"));
        ILog.v("Notification Service Created!!!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Intent intent = new Intent("com.dudu.wearlauncher.NotificationReceived");
        intent.putExtra("sbn", sbn);
        sendBroadcast(intent);
        ILog.v("Notification Received:" + sbn.getNotification().toString());
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Intent intent = new Intent("com.dudu.wearlauncher.NotificationRemoved");
        intent.putExtra("sbn", sbn);
        sendBroadcast(intent);
        ILog.v("Notification Removed:" + sbn.getNotification().toString());
    }

    class NotificationServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("command").equals("clearAll")) {
                NotificationListenerService.this.cancelAllNotifications();
            }
            if (intent.getStringExtra("command").equals("listAll")) {
                Intent intent2 = new Intent("com.dudu.wearlauncher.ListAllNotification");
                intent2.putExtra("sbnList", NotificationListenerService.this.getActiveNotifications());
                sendBroadcast(intent2);
            }
            if (intent.getStringExtra("command").equals("remove")) {
                String key = intent.getStringExtra("key");
                NotificationListenerService.this.cancelNotification(key);
            }
            ;

        }
    }
}
