package com.dudu.wearlauncher.ui;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;
import com.dudu.wearlauncher.model.WatchSurface;
import com.dudu.wearlauncher.utils.ILog;
import com.dudu.wearlauncher.utils.WatchSurfaceHelper;

public class WatchSurfaceBaseActivity extends Activity{
    WatchSurface wsf;
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Intent intent = getIntent();
        String wfName = intent.getStringExtra("wfName");
        String wsfClassName = intent.getStringExtra("wsfClassName");
        wsf = WatchSurfaceHelper.getWatchSurface(this,wfName,wsfClassName);
        setContentView(wsf);
    }
    @Override
    protected void onDestroy() {
        wsf.onDestroy();
        super.onDestroy();
    }
    @Override
    protected void attachBaseContext(Context arg0) {
        super.attachBaseContext(getFitDisplayContext(arg0));
    }
    public static Context getFitDisplayContext(Context old){
        Context newContext = old;
        
        float density = old.getResources().getDisplayMetrics().widthPixels/360; //获取放大dpi倍数
        
        try{
            //DisplayMetrics displayMetrics = old.getResources().getDisplayMetrics();
            Configuration configuration = old.getResources().getConfiguration();
            configuration.densityDpi = (int)(/*displayMetrics.densityDpi*/320 * density);    //锁死dpi为320，防止设备默认dpi不同导致界面出问题
            newContext = old.createConfigurationContext(configuration);
        }catch (Exception e){
            Toast.makeText(newContext, "调整缩放失败, 请联系开发者", Toast.LENGTH_SHORT).show();
            ILog.e("调整dpi:不支持application");
        }
        
        return newContext;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        wsf.onActivityResult(requestCode, resultCode, data);
    }
}
