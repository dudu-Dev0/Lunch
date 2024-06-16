package com.dudu.wearlauncher.ui.home;
import android.content.Context;
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
import com.dudu.wearlauncher.util.WatchFaceHelper;
import java.io.File;

public class WatchFaceFragment extends Fragment{
    WatchFace watchFace;
    FrameLayout watchFaceBox;
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
        watchFace = WatchFaceHelper.getWatchFace("com.xtc.pisces","pisces");
        if(watchFace != null) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
            watchFaceBox.addView(watchFace,layoutParams);
            updateBattery();
            updateTime();
        }else{
            Log.e("","表盘加载失败");
        }
    }
    
    public void updateTime(){
        if(watchFace!=null) {
            watchFace.updateTime();
            //watchFaceView.updateViewLayout(watchFace, watchFaceParams);    //时间更新
        }
    }

    public void updateBattery(){
        if(watchFace!=null) {
            BatteryManager batteryManager = (BatteryManager) requireContext().getSystemService(Context.BATTERY_SERVICE);
            watchFace.updateBattery(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY));
            //watchFaceView.updateViewLayout(watchFace, watchFaceParams);    //电池更新
        }
    }
}
