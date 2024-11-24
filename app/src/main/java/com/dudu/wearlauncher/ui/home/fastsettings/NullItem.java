package com.dudu.wearlauncher.ui.home.fastsettings;
import android.graphics.drawable.Drawable;
import com.dudu.wearlauncher.R;
import com.dudu.wearlauncher.WearLauncherApp;
import com.dudu.wearlauncher.model.FastSettingsItem;

public class NullItem extends FastSettingsItem{
    @Override
    public Drawable getDrawable() {
        return WearLauncherApp.getContext().getResources().getDrawable(R.drawable.icon_about);
        
    }
    @Override
    public ButtonItemListener getTouchListener() {
        return new ButtonItemListener(){
            @Override
            public void onClick(boolean checked) {
                // TODO: Implement this method
            }
            @Override
            public void onLongClick(boolean checked) {
                // TODO: Implement this method
            }
            
        };
    }
    @Override
    public String getSettingsName() {
        return "null";
    }
    
    
    
}
