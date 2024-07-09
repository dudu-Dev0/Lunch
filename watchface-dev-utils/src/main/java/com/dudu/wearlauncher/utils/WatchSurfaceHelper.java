package com.dudu.wearlauncher.utils;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.dudu.wearlauncher.model.WatchSurface;
import com.dudu.wearlauncher.ui.WatchSurfaceBaseActivity;
import dalvik.system.DexClassLoader;
import java.io.File;

import static com.dudu.wearlauncher.model.WatchFace.watchFaceFolder;
import static com.dudu.wearlauncher.model.WatchFace.watchFaceSuffix;
import static com.dudu.wearlauncher.model.WatchFace.watchFaceClassName;

public class WatchSurfaceHelper {
    public static void startWsfActivity(Context context,String watchFaceName,String wsfClassName) {
        Intent intent = new Intent(context,WatchSurfaceBaseActivity.class);
        intent.putExtra("wfName",watchFaceName);
        intent.putExtra("wsfClassName",wsfClassName);
        context.startActivity(intent);
    }
    public static WatchSurface getWatchSurface(Context context,String watchFaceName,String wsfClassName) {
    	
        String wfPath = watchFaceFolder + "/" + watchFaceName + "/"+ watchFaceName + watchFaceSuffix;
        ILog.e(wfPath);
        if(new File(wfPath).exists()) {
            try {
            	ClassLoader classLoader = WatchSurface.class.getClassLoader();
                Class<?> clazz = new DexClassLoader(wfPath,context.getCacheDir().getAbsolutePath(),null,classLoader).loadClass(wsfClassName);
                WatchSurface watchSurface = (WatchSurface)clazz.getMethod("getWatchSurface",Context.class,String.class).invoke(clazz,new Object[]{context,wfPath});
                return watchSurface;
            } catch(Exception err){
                ILog.e(err.toString());
            }
        }else{
            ILog.e("表盘不存在");
        }
        return null;
    }
}
