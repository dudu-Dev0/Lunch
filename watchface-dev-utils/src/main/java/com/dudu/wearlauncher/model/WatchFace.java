package com.dudu.wearlauncher.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public abstract class WatchFace extends FrameLayout {
    
    public static String watchFaceFolder = "/sdcard/Android/data/com.dudu.wearlauncher/files/watchface";
    public static String watchFaceSuffix = ".wf";
    public static String watchFaceClassName = ".WatchFaceImpl";

    private String path; //表盘路径(.wf)
    private Context context;
    private Resources resources;

    public WatchFace(Context context,String path) {
        this(context,null,path);
    }

    public WatchFace(Context context, AttributeSet attributeSet,String path) {
        this(context,attributeSet,0,path);
    }

    public WatchFace(Context context, AttributeSet attributeSet, int i,String path) {
        super(context,attributeSet,i);
        this.context = context;
        this.path = path;
        this.resources = initResources(context);
        initView();
    }

    public Context getHostContext() {
        return context;
    }
    public Resources initResources(Context context) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            assetManager.getClass().getMethod("addAssetPath", String.class).invoke(assetManager, path);
            Resources resources = context.getResources();
            return new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Resources getResources() {
        return this.resources == null ? super.getResources() : this.resources;
    }

    public abstract void initView();

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