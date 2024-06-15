package com.dudu.wearlauncher.util;
import android.content.Context;
import android.widget.Toast;
import com.dudu.wearlauncher.WearLauncherApp;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.io.IOException;
import com.dudu.wearlauncher.model.WatchFace;
import java.lang.reflect.InvocationTargetException;

public class WatchFaceHelper {
    public static String watchFaceFolder = WearLauncherApp.getContext().getExternalFilesDir("watchface").getAbsolutePath();
    public static String watchFaceSuffix = ".wf";
    public static String watchFaceClassName = ".WatchFaceImpl"
    
    public WatchFace getWatchFace(String packageName,String name) {
        String wfPath = watchFaceFolder + File.pathSeparator + name + watchFaceSuffix;
        if(new File(wfPath).exists()) {
            try {
            	ClassLoader classLoader = WatchFace.class.getClassLoader();
                Class<?> clazz = new DexClassLoader(wfPath,WearLauncherApp.getContext().getCacheDir().getAbsolutePath(),null,classLoader).loadClass(packageName+watchFaceClassName);
                WatchFace watchFace = (WatchFace)clazz.getMethod("getWatchFace",Context.class,String.class).invoke(new Object[]{WearLauncherApp.getContext(),wfPath});
                return watchFace;
            } catch(ClassNotFoundException err){
                err.printStackTrace();
            } catch(NoSuchMethodException err){
                err.printStackTrace();
            } catch(IllegalAccessException err){
                err.printStackTrace();
            } catch(InvocationTargetException err){
                err.printStackTrace();
            }
        }else{
            Toast.makeText(WearLauncherApp.getContext(),"表盘不存在",Toast.LENGTH_SHORT);
        }
        return null;
    }
}
