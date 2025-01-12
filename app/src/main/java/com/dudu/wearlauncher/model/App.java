package com.dudu.wearlauncher.model;
import android.graphics.drawable.Drawable;

public class App {
    public String packageName;
    public String activityName;
    public transient Drawable icon;
    public String label;
    @Override
    public boolean equals(Object app) {
        if(app instanceof App) {
        	App appInstance = (App)app;
            if(appInstance.activityName.equals(this.activityName)&&appInstance.packageName.equals(this.packageName)) {
            	return true;
            }
        } else {
        	return false;
        }
        return false;
    }
    
}
