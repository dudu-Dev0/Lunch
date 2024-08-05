package com.dudu.wearlauncher.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.blankj.utilcode.util.FileIOUtils;
import com.dudu.wearlauncher.WearLauncherApp;
import com.dudu.wearlauncher.model.WatchFace;
import com.dudu.wearlauncher.model.WatchFaceInfo;
import dalvik.system.DexClassLoader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static com.dudu.wearlauncher.model.WatchFace.*;

public class WatchFaceHelper {
    
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
    public static List<WatchFaceInfo> getAllWatchFace() throws JSONException{
        File[] allFile = new File(watchFaceFolder).listFiles(File::isDirectory);
        List<WatchFaceInfo> watchFaceList = new ArrayList<WatchFaceInfo>();
        for(File file : allFile) {
        	if(file.isDirectory()&&new File(file.getAbsolutePath()+"/"+file.getName()+watchFaceSuffix).exists()) {
        		File watchFaceManifest = new File(file.getAbsolutePath()+"/manifest.json");
                JSONObject manifest = new JSONObject(FileIOUtils.readFile2String(watchFaceManifest));
                WatchFaceInfo data = new WatchFaceInfo();
                data.name = manifest.getString("name");
                data.displayName = manifest.getString("displayName");
                data.packageName = manifest.getString("packageName");
                data.author = manifest.getString("author");
                data.versionCode = manifest.getInt("versionCode");
                watchFaceList.add(data);
        	}
        }
        
    	return watchFaceList;
    }
    public static WatchFaceInfo getWatchFaceInfo(String name) throws JSONException{
    	File watchFaceManifest = new File(watchFaceFolder+"/"+name+"/manifest.json");
        JSONObject manifest = new JSONObject(FileIOUtils.readFile2String(watchFaceManifest));
        WatchFaceInfo data = new WatchFaceInfo();
        data.name = manifest.getString("name");
        data.displayName = manifest.getString("displayName");
        data.packageName = manifest.getString("packageName");
        data.author = manifest.getString("author");
        data.versionCode = manifest.getInt("versionCode");
        return data;
    }
}
