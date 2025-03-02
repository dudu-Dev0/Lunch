package com.dudu.wearlauncher.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.dudu.wearlauncher.ui.CatchActivity;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ErrorCatch implements Thread.UncaughtExceptionHandler {
    @SuppressLint("StaticFieldLeak")
    public static ErrorCatch instance;
    private Context context;

    public static ErrorCatch getInstance() {
        if (instance == null) instance = new ErrorCatch();
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        
        registerPermissionExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    Map<String, String> permissionNames = new HashMap<String, String>() {{
        // 存储相关
        put("android.permission.READ_EXTERNAL_STORAGE",   "读取文件");
        put("android.permission.WRITE_EXTERNAL_STORAGE",  "修改文件");
    
        // 系统设置相关
        put("android.permission.WRITE_SECURE_SETTINGS",   "修改安全设置（需系统权限）");
        put("android.permission.WRITE_SETTINGS",          "修改系统设置");
    
        // 网络相关
        put("android.permission.CHANGE_NETWORK_STATE",    "切换移动网络");
        put("android.permission.CHANGE_WIFI_STATE",       "控制Wi-Fi连接");
        put("android.permission.ACCESS_NETWORK_STATE",    "检测网络状态");
        put("android.permission.ACCESS_WIFI_STATE",       "检测Wi-Fi状态");
        put("android.permission.INTERNET",                "访问互联网");
    
        // 设备功能
        put("android.permission.FOREGROUND_SERVICE",      "后台运行");
        put("android.permission.WAKE_LOCK",               "保持唤醒");
        put("android.permission.BLUETOOTH",               "蓝牙");
        put("android.permission.BLUETOOTH_ADMIN",         "管理蓝牙");
    }};
    private void registerPermissionExceptionHandler() {
    	new Handler(Looper.getMainLooper()).post(()->{
            while(true) {
            	try {
            		Looper.loop();
            	} catch(SecurityException err) {
                    handlePermissionException(err);
            	}
            }
        });
        Thread.setDefaultUncaughtExceptionHandler((thread,err)->{
            if(err instanceof SecurityException) {
                new Handler(Looper.getMainLooper()).post(()->{
                    handlePermissionException((SecurityException)err);
                });
            }
        });
    }/*
    private void registerPermissionExceptionHandlerV2() {
        try {
            // 获取主线程 Handler
            Handler mainHandler = new Handler(Looper.getMainLooper());
            Class<?> handlerClass = Handler.class;

            // 获取 mCallback 字段
            Field mCallbackField = handlerClass.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);

            // 保存原始 Callback（如有需要恢复时使用）
            Handler.Callback originalCallback = (Handler.Callback) mCallbackField.get(mainHandler);

            // 设置自定义 Callback 拦截消息
            Handler.Callback customCallback = msg -> {
                try {
                    // 调用原始 Callback 处理消息
                    if (originalCallback != null && originalCallback.handleMessage(msg)) {
                        return true;
                    }
                    // 默认处理逻辑
                    mainHandler.handleMessage(msg);
                } catch (SecurityException e) {
                    handlePermissionException(e);
                    return true; // 阻止异常继续抛出
                } catch (Exception e) {
                    //e.printStackTrace();
                }
                return true;
            };

            // 替换 mCallback 字段
            mCallbackField.set(mainHandler, customCallback);

        } catch (Exception e) {
            ILog.e("ReflectionError"+ "Hook failed: " + e.getMessage());
        }
    }*/
    private long lastHandlePermissonTime = 0;
    public void handlePermissionException(SecurityException throwable) {
    	if(System.currentTimeMillis()-lastHandlePermissonTime>=1500&&throwable.getMessage().toString().contains("not granted")&&throwable.getMessage().toString().contains("android.permission")){
            lastHandlePermissonTime = System.currentTimeMillis();
            String errorMessage = throwable.getMessage();
            Pattern pattern = Pattern.compile("android\\.permission\\.\\w+");
            Matcher matcher = pattern.matcher(errorMessage);
            if (matcher.find()) {
                String permissionName = matcher.group();
                Toast.makeText(context, "需要权限: " + (permissionNames.containsKey(permissionName) ? permissionNames.get(permissionName): permissionName), Toast.LENGTH_LONG).show();
            }
        }if(System.currentTimeMillis()-lastHandlePermissonTime<1500){
            
        }else{
            throw throwable;
        }
    }
    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        
        try {
            Intent intent = new Intent(context, CatchActivity.class);
            intent.putExtra("stack", writer.toString());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //这句是安卓4必须有的
            context.startActivity(intent);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        throwable.printStackTrace();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
