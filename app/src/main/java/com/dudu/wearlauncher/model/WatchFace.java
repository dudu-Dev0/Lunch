package com.dudu.wearlauncher.model;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import java.util.HashMap;

public abstract class WatchFace extends FrameLayout {
    public WatchFace(Context context) {
        super(context);
    }

    public WatchFace(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
    }

    public WatchFace(Context context, AttributeSet attributeSet, int i) {
        super(context,attributeSet,i);
    }

    public void dealScreenOff() {
    }

    public void dealScreenOn() {
    }

    public void updateBattery(int i) {
    }

    public void updateStep(int i) {
    }
    
    
    public abstract void updateTime();
}