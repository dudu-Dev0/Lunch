package com.dudu.wearlauncher.model;
import android.content.Context;
import android.graphics.drawable.Drawable;
import com.dudu.wearlauncher.widget.SwitchIconButton;

public class FastSettingsItem {
    public FastSettingsItem() {
    	
    }
    public interface ButtonItemListener{
        void onClick(boolean checked);
        void onLongClick(boolean checked);
    }
    public enum ItemAction{
        ACTION_METHOD_CLICK,
        ACTION_OPEN_ACTIVITY
    }
    public SwitchIconButton button;
    public ItemAction action;
    public Drawable drawable;
    public ButtonItemListener touchListener;
    public String settingsName;
    public String targetActivity;
    public String targetPackage;
    public void registerStateObserver(Context context){};
    public void unregisterStateObserver(){};
}
