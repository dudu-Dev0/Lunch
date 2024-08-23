package com.dudu.watchface.example;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dudu.wearlauncher.model.WatchSurface;
import com.dudu.wearlauncher.utils.ILog;

public class SettingsSurface extends WatchSurface{

    
    public SettingsSurface(Context context,String path) {
        super(context,path);
    }

    @Override
    public void onCreate() {
        setContentView(R.layout.settings_layout);
        ILog.w("Surface onCreate Running!!!");
        ((TextView)findViewById(R.id.testtv)).setText("Test");
    }
    @Override
    public void onDestory() {
        super.onDestory();
        Toast.makeText(getHostContext(),"onDestory",Toast.LENGTH_SHORT).show();
    }
    
    public static FrameLayout getWatchSurface(Context context,String watchFacePath) {
        return new SettingsSurface(context,watchFacePath);
    }
}
