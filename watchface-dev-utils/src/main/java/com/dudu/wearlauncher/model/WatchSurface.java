package com.dudu.wearlauncher.model;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.dudu.wearlauncher.WatchfaceLayoutInflater;
import com.dudu.wearlauncher.utils.ILog;
import dalvik.system.DexClassLoader;

import java.lang.reflect.Field;

public abstract class WatchSurface extends FrameLayout{
    /**
    *在表盘中实现类似activity的效果
    */
    private String path; //表盘路径(.wf)
    private Context context;
    private Resources resources;
    
    public WatchSurface(Context context,String path) {
        this(context,null,path);
    }

    public WatchSurface(Context context, AttributeSet attributeSet,String path) {
        this(context,attributeSet,0,path);
    }

    public WatchSurface(Context context, AttributeSet attributeSet, int i,String path) {
        super(context,attributeSet,i);
        this.context = context;
        this.path = path;
        this.resources = initResources(context);
        replaceContextResources(this.context);
        onCreate();
    }
    
    public abstract void onCreate();

    public void onDestroy() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    /**
     * @param context
     */
    public void replaceContextResources(Context context) {
        try {
            Field field = context.getClass().getDeclaredField("mResources");
            field.setAccessible(true);
            field.set(context, this.resources);
            Field mClassLoader = context.getClass().getDeclaredField("mClassLoader");
            mClassLoader.setAccessible(true);
            mClassLoader.set(context, new DexClassLoader(path, context.getCacheDir().getAbsolutePath(), null, this.getClass().getClassLoader()));
            this.context = context;
            ILog.e("replace resources succeed");
        } catch (Exception e) {
            ILog.e("replace resources failed");
            e.printStackTrace();
        }
    }

    public void setContentView(int id, ClassLoader classLoader) {
        WatchfaceLayoutInflater.from(context, classLoader).inflate(getResources().getLayout(id), this);
    }
    
    public Resources getResources() {
        return this.resources == null ? super.getResources() : this.resources;
    }
}
