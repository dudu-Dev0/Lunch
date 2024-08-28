package com.dudu.wearlauncher.utils;

import android.content.Context;
import android.content.Intent;
import com.dudu.wearlauncher.model.WatchSurface;
import com.dudu.wearlauncher.ui.WatchSurfaceBaseActivity;
import dalvik.system.DexClassLoader;

import java.io.File;

import static com.dudu.wearlauncher.model.WatchFace.watchFaceFolder;
import static com.dudu.wearlauncher.model.WatchFace.watchFaceSuffix;

public class WatchSurfaceHelper {
    public static void startWsfActivity(Context context,String wfName,Class<?> surfaceClazz) {
        Intent intent = new Intent(context,WatchSurfaceBaseActivity.class);
        intent.putExtra("wfName",wfName);
        intent.putExtra("wsfClassName",surfaceClazz.getCanonicalName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void startWsfActivity(Context context,String wfName,String watchSurfaceName) {
        Intent intent = new Intent(context,WatchSurfaceBaseActivity.class);
        intent.putExtra("wfName",wfName);
        intent.putExtra("wsfClassName",watchSurfaceName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static WatchSurface getWatchSurface(Context context,String watchFaceName,String wsfClassName) {
        String wfPath = watchFaceFolder + "/" + watchFaceName + "/"+ watchFaceName + watchFaceSuffix;
        ILog.e(wfPath);
        if(new File(wfPath).exists()) {
            try {
            	ClassLoader classLoader = WatchSurface.class.getClassLoader();
                Class<?> clazz = new DexClassLoader(wfPath,context.getCacheDir().getAbsolutePath(),null,classLoader).loadClass(wsfClassName);
                return (WatchSurface) clazz.getConstructor(Context.class, String.class).newInstance(context, wfPath);
            } catch(Exception err){
                ILog.e(String.valueOf(err.getCause()));
                err.printStackTrace();
            }
        }else{
            ILog.e("表盘不存在");
        }
        return null;
    }

    public static void requestRefreshWatchface(Context context) {
        Intent intent = new Intent("com.dudu.wearlauncher.WatchFaceChange");
        context.sendBroadcast(intent);
    }
}
