package com.dudu.wearlauncher.ui;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.dudu.wearlauncher.model.WatchSurface;
import com.dudu.wearlauncher.utils.WatchSurfaceHelper;

public class WatchSurfaceBaseActivity extends Activity{
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Intent intent = getIntent();
        String wfName = intent.getStringExtra("wfName");
        String wsfClassName = intent.getStringExtra("wsfClassName");
        WatchSurface wsf = WatchSurfaceHelper.getWatchSurface(this,wfName,wsfClassName);
        setContentView(wsf);
    }
    
}
