package com.dudu.wearlauncher.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dudu.wearlauncher.R;
import com.dudu.wearlauncher.model.WatchFace;
import com.dudu.wearlauncher.utils.WatchFaceHelper;

import java.io.File;

public class WatchFaceFragment extends Fragment{
    WatchFace watchFace;
    FrameLayout watchFaceBox;
    FrameLayout.LayoutParams layoutParams;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_watchface, container, false);
    }

    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        watchFaceBox = view.findViewById(R.id.watchface_box);
        if(!new File(WatchFaceHelper.watchFaceFolder).exists()) {
        	new File(WatchFaceHelper.watchFaceFolder).mkdir();
        }
        watchFace = WatchFaceHelper.getWatchFace("com.dudu.watchface.example","watchface-example");
        if(watchFace != null) {
            layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
            watchFaceBox.addView(watchFace,layoutParams);
            updateTime();
        }else{
            Log.e("","表盘加载失败");
        }
        BroadcastReceiver batteryChangeReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int battery = level * 100 / scale;
                updateBattery(battery);
            }
        };
        requireActivity().registerReceiver(batteryChangeReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        
        BroadcastReceiver timeChangeReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                updateTime();
            }
        };
        IntentFilter timeChangeFilter = new IntentFilter();
        timeChangeFilter.addAction(Intent.ACTION_TIME_TICK);
        timeChangeFilter.addAction(Intent.ACTION_TIME_CHANGED);
        requireActivity().registerReceiver(timeChangeReceiver,timeChangeFilter);
    }
    
    public void updateTime(){
        if(watchFace!=null) {
            watchFace.updateTime();
            //watchFaceView.updateViewLayout(watchFace, watchFaceParams);    //时间更新
        }
    }

    public void updateBattery(int i){
        if(watchFace!=null) {
            watchFace.updateBattery(i);
            //lwatchFaceBox.updateViewLayout(watchFace,layoutParams);    //电池更新
        }
    }
}
