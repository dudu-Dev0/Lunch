package com.dudu.wearlauncher.listener;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;
import com.blankj.utilcode.util.ToastUtils;
import com.dudu.wearlauncher.WearLauncherApp;
import com.dudu.wearlauncher.utils.ILog;

public class ZenModeObserver {
    Context context;
    ZenModeListener listener;
    ZenModeReceiver receiver;
    public ZenModeObserver(Context context) {
        this.context = context;
        receiver = new ZenModeReceiver();
    }

    public void register(ZenModeListener listener) {
    	this.listener = listener;
        if(receiver != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(NotificationManager.ACTION_NOTIFICATION_POLICY_CHANGED);
            context.registerReceiver(receiver, filter);
        }
    }
    public void unregister() {
        if (receiver != null) {
            context.unregisterReceiver(receiver);
        }
    }
    public interface ZenModeListener {
        void onEnabled();

        void onDisabled();
    }
    public static void setZenModeStatu(boolean enabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                NotificationManager notificationManager = (NotificationManager) WearLauncherApp.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                if(enabled) {
                    notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY);
                }else{
                    notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
                }
            } catch(SecurityException err) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    WearLauncherApp.getContext().startActivity(intent);
                    Toast.makeText(WearLauncherApp.getContext(),"请给予相应权限",Toast.LENGTH_SHORT).show();
                }                  
            }
            
        }
    }
    public class ZenModeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (NotificationManager.ACTION_NOTIFICATION_POLICY_CHANGED.equals(
                        intent.getAction())) {
                    NotificationManager notificationManager =
                            (NotificationManager)
                                    context.getSystemService(Context.NOTIFICATION_SERVICE);
                    int interruptionFilter = notificationManager.getCurrentInterruptionFilter();
                    if(listener!=null) {
                    	if(interruptionFilter == NotificationManager.INTERRUPTION_FILTER_NONE||interruptionFilter==NotificationManager.INTERRUPTION_FILTER_PRIORITY) {
                    		listener.onEnabled();
                    	}else{
                            listener.onDisabled();
                        }
                    }
                    
                }
            }
        }
    }
    public static boolean isZenModeEnabled() {
        NotificationManager notificationManager = 
                (NotificationManager) WearLauncherApp.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            int interruptionFilter = notificationManager.getCurrentInterruptionFilter();
            return (interruptionFilter == NotificationManager.INTERRUPTION_FILTER_NONE ||
                    interruptionFilter == NotificationManager.INTERRUPTION_FILTER_PRIORITY);
        }
        return false; // 默认没有开启免打扰模式
    }
}
