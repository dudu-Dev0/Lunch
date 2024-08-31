package com.dudu.watchface.example;

import android.content.Context;
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
        setContentView(R.layout.settings_layout, this.getClass().getClassLoader());
        ILog.w("Surface onCreate Running!!!");
        ((TextView)findViewById(R.id.testtv)).setText("Test");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getHostContext(),"onDestory",Toast.LENGTH_SHORT).show();
    }

}
