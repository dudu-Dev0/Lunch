package com.dudu.wearlauncher.util;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.dudu.wearlauncher.WearLauncherApp;
import com.dudu.wearlauncher.model.WatchFaceData;
import com.google.gson.JsonObject;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.io.IOException;
import com.dudu.wearlauncher.model.WatchFace;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import kotlin.io.FilesKt;
import org.json.JSONException;
import org.json.JSONObject;

public class WatchFaceHelper {
    public static String watchFaceFolder = WearLauncherApp.getContext().getExternalFilesDir("watchface").getAbsolutePath();
    public static String watchFaceSuffix = ".wf";
    public static String watchFaceClassName = ".WatchFaceImpl";
    
    public static WatchFace getWatchFace(String packageName,String name) {
        String wfPath = watchFaceFolder + "/" + name + "/"+ name + watchFaceSuffix;
        Log.e("wfPath",wfPath);
        if(new File(wfPath).exists()) {
            try {
            	ClassLoader classLoader = WatchFace.class.getClassLoader();
                Class<?> clazz = new DexClassLoader(wfPath,WearLauncherApp.getContext().getCacheDir().getAbsolutePath(),null,classLoader).loadClass(packageName+watchFaceClassName);
                WatchFace watchFace = (WatchFace)clazz.getMethod("getWatchFace",Context.class,String.class).invoke(clazz,new Object[]{WearLauncherApp.getContext(),wfPath});
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
    public static List<WatchFaceData> getAllWatchFace() throws JSONException{
        File[] allFile = new File(watchFaceFolder).listFiles();
        List<WatchFaceData> watchFaceList = new ArrayList<WatchFaceData>();
        for(File file : allFile) {
        	if(file.isDirectory()&&new File(file.getAbsolutePath()+"/"+file.getName()+watchFaceSuffix).exists()) {
        		File watchFaceManifest = new File(file.getAbsolutePath()+"/manifest.json");
                JSONObject manifest = new JSONObject(new String(FilesKt.readBytes(watchFaceManifest),StandardCharsets.UTF_8));
                WatchFaceData data = new WatchFaceData();
                data.name = manifest.getString("name");
                data.displayName = manifest.getString("displayName");
                data.packageName = manifest.getString("packageName");
                data.author = manifest.getString("author");
                watchFaceList.add(data);
        	}
        }
        
    	return watchFaceList;
    }
}
