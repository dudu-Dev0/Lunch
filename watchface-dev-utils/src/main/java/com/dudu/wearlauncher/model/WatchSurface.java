package com.dudu.wearlauncher.model;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.service.controls.templates.ControlTemplate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
        onCreate();
    }
    
    public abstract void onCreate();
    public void onDestory(){
        
    };
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
    
    public void setContentView(int id) {
        LayoutInflater.from(context).inflate(getResources().getLayout(id),this);
    }
    
    public Resources getResources() {
        return this.resources == null ? super.getResources() : this.resources;
    }
}
