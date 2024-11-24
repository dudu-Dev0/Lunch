package com.dudu.wearlauncher.ui.home.fastsettings;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import com.dudu.wearlauncher.R;
import com.dudu.wearlauncher.WearLauncherApp;
import com.dudu.wearlauncher.model.FastSettingsItem;
import com.dudu.wearlauncher.widget.SwitchIconButton;

public class OpenActivityItem extends FastSettingsItem {
    String packageName;
    String activityName;
    public OpenActivityItem(String packageName,String activityName){
        this.packageName=packageName;
        this.activityName=activityName;
    }
    public OpenActivityItem() {
    	this.packageName="";
        this.activityName="";
    }
    @Override
    public Drawable getDrawable() {
        return WearLauncherApp.getContext().getResources().getDrawable(R.drawable.icon_open);
    }
    @Override
    public String getSettingsName() {
        return "打开活动";
    }
    @Override
    public ButtonItemListener getTouchListener() {
        return new ButtonItemListener(){
            @Override
            public void onClick(boolean checked) {
                Intent intent = new Intent();
                intent.setClassName(packageName,activityName);
                WearLauncherApp.getContext().startActivity(intent);
            }
            @Override
            public void onLongClick(boolean checked) {
                
            }
            
        };
    }
    
}
