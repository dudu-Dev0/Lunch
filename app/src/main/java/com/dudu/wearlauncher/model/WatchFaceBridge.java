package com.dudu.wearlauncher.model;
import java.lang.reflect.InvocationTargetException;

public class WatchFaceBridge {
    Object watchface;
    public WatchFaceBridge(Object watchface){
    	this.watchface = watchface;
    }
    
    public void onWatchfaceVisibilityChanged(boolean isVisible) throws NoSuchMethodException,IllegalAccessException,InvocationTargetException{
        watchface.getClass().getMethod("onWatchfaceVisibilityChanged",boolean.class).invoke(watchface,isVisible);
    }

    public void onScreenStateChanged(boolean isScreenOn) throws NoSuchMethodException,IllegalAccessException,InvocationTargetException {
        watchface.getClass().getMethod("onScreenStateChanged",boolean.class).invoke(watchface,isScreenOn);
    }
    
    public void updateBattery(int i, int batteryStatus) throws NoSuchMethodException,IllegalAccessException,InvocationTargetException {
        watchface.getClass().getMethod("updateBattery",int.class,int.class).invoke(watchface,i,batteryStatus);
    }

    public void updateTime() throws NoSuchMethodException,IllegalAccessException,InvocationTargetException{
        watchface.getClass().getMethod("updateTime").invoke(watchface);
    };
}
