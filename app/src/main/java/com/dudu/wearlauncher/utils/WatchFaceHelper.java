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
                return (WatchFace) clazz.getConstructor(Context.class, String.class).newInstance(WearLauncherApp.getContext(), wfPath);
            } catch (Exception e) {
                ILog.e("表盘获取错误：" + e.getCause());
                e.printStackTrace();
            }
        }else{
            Toast.makeText(WearLauncherApp.getContext(), "表盘不存在", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    public static List<WatchFaceInfo> getAllWatchFace() throws JSONException{
        File[] allFile = new File(watchFaceFolder).listFiles(File::isDirectory);
        List<WatchFaceInfo> watchFaceList = new ArrayList<WatchFaceInfo>();
        for(File file : allFile) {
        	if(file.isDirectory()&&new File(file.getAbsolutePath()+"/"+file.getName()+watchFaceSuffix).exists()) {
        		File watchFaceManifest = new File(file.getAbsolutePath()+"/manifest.json");
                if (!watchFaceManifest.exists()) continue;
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
