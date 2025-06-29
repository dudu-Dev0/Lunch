package com.dudu.wearlauncher.ui.home.fastsettings;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;
import com.dudu.wearlauncher.R;
import android.graphics.drawable.Drawable;
import com.dudu.wearlauncher.WearLauncherApp;
import com.dudu.wearlauncher.listener.ZenModeObserver;
import com.dudu.wearlauncher.model.FastSettingsItem;

public class ZenModeItem extends FastSettingsItem {
    @Override
    public Drawable getDrawable() {
        return WearLauncherApp.getContext()
                .getResources()
                .getDrawable(R.drawable.zen_mode_selector);
    }

    @Override
    public String getSettingsName() {
        return "勿扰";
    }
    
    @Override
    public ButtonItemListener getTouchListener() {
        return new ButtonItemListener() {
            @Override
            public void onClick(boolean checked) {
                ZenModeObserver.setZenModeStatu(checked);
                if(checked) {
                	Toast.makeText(WearLauncherApp.getContext(),"勿扰：开",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(WearLauncherApp.getContext(),"勿扰：关",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLongClick(boolean checked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    WearLauncherApp.getContext().startActivity(intent);
                }                  
            }
        };
    }
    

    public ZenModeObserver observer;

    @Override
    public void registerStateObserver(Context context) {
        super.registerStateObserver(context);
        observer = new ZenModeObserver(context);
        if(ZenModeObserver.isZenModeEnabled()) {
        	button.setActivated(true);
        }else{
            button.setActivated(false);
        }
        observer.register(
                new ZenModeObserver.ZenModeListener() {
                    @Override
                    public void onEnabled() {
                        button.setActivated(true);
                    }

                    @Override
                    public void onDisabled() {
                        button.setActivated(false);
                    }

                });
    }

    @Override
    public void unregisterStateObserver() {
        super.unregisterStateObserver();
        observer.unregister();
    }
}
