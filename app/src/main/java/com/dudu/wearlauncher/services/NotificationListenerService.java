package com.dudu.wearlauncher.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.service.notification.StatusBarNotification;
import com.blankj.utilcode.util.AppUtils;
import com.dudu.wearlauncher.model.Notification;
import com.dudu.wearlauncher.utils.ILog;

import java.util.ArrayList;
import java.util.List;

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
        intent.putExtra("notification", sbn2Notification(sbn));
        sendBroadcast(intent);
        ILog.v("Notification Received:" + sbn.getNotification().toString());
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Intent intent = new Intent("com.dudu.wearlauncher.NotificationRemoved");
        intent.putExtra("notification", sbn2Notification(sbn));
        sendBroadcast(intent);
        ILog.v("Notification Removed:" + sbn.getNotification().toString());
    }

    public Notification sbn2Notification(StatusBarNotification sbn) {
        return new Notification(
                sbn.getNotification().getSmallIcon(),
                AppUtils.getAppName(sbn.getPackageName()),
                sbn.getNotification().extras.getCharSequence(android.app.Notification.EXTRA_TITLE, "Title").toString(),
                sbn.getNotification().extras.getCharSequence(android.app.Notification.EXTRA_TEXT, "Content").toString(),
                sbn.getPostTime());
    }
    class NotificationServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("command").equals("clearAll")) {
                NotificationListenerService.this.cancelAllNotifications();
            }
            if (intent.getStringExtra("command").equals("listAll")) {
                List<Notification> list = new ArrayList<>();
                for (StatusBarNotification sbn : NotificationListenerService.this.getActiveNotifications()) {
                    list.add(sbn2Notification(sbn));
                }
                Intent intent2 = new Intent("com.dudu.wearlauncher.ListAllNotification");
                intent2.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) list);
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
