package com.dudu.wearlauncher.ui.home;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.dudu.wearlauncher.R;
import com.dudu.wearlauncher.model.Notification;
import com.dudu.wearlauncher.model.WatchFace;
import com.dudu.wearlauncher.model.WatchFaceInfo;
import com.dudu.wearlauncher.utils.ILog;
import com.dudu.wearlauncher.utils.SharedPreferencesUtil;
import com.dudu.wearlauncher.utils.WatchFaceHelper;
import com.dudu.wearlauncher.widget.MyLinearLayoutManager;
import com.dudu.wearlauncher.widget.MyRecyclerView;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.dudu.wearlauncher.model.WatchFace.watchFaceFolder;

public class WatchFaceFragment extends Fragment{
    WatchFace watchFace;
    FrameLayout watchFaceBox;
    FrameLayout.LayoutParams layoutParams;

    MsgListAdapter msgListAdapter;

    BroadcastReceiver msgReceiver, msgRemovedReceiver, msgListAllReceiver, watchFaceChangeReceiver, batteryChangeReceiver, timeChangeReceiver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_watchface, container, false);
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        watchFaceBox = view.findViewById(R.id.watchface_box);
        MyRecyclerView msgView = view.findViewById(R.id.msg_list);

        msgListAllReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                List<Notification> list = intent.getParcelableArrayListExtra("list");
                msgListAdapter = new MsgListAdapter(requireActivity(), list);
                msgView.setLayoutManager(new MyLinearLayoutManager(requireActivity()));
                msgView.setAdapter(msgListAdapter);
            }
        };
        requireActivity().registerReceiver(msgListAllReceiver, new IntentFilter("com.dudu.wearlauncher.ListAllNotification"));

        postGetAllNotification();

        msgView.setEmptyView(view.findViewById(R.id.empty_list_text));

        watchFaceBox.setOnLongClickListener(v->{
            Intent intent = new Intent(requireActivity(),ChooseWatchFaceActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            return false;
        });

        msgReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Notification notification = intent.getParcelableExtra("notification");
                msgListAdapter.addSbn(notification);
            }
        };
        requireActivity().registerReceiver(msgReceiver, new IntentFilter("com.dudu.wearlauncher.NotificationReceived"));

        msgRemovedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Notification notification = intent.getParcelableExtra("notification");
                msgListAdapter.removeSbn(notification);
            }
        };
        requireActivity().registerReceiver(msgRemovedReceiver, new IntentFilter("com.dudu.wearlauncher.NotificationRemoved"));

        if(!new File(watchFaceFolder).exists()) {
        	new File(watchFaceFolder).mkdir();
        }
        if (Objects.requireNonNull(new File(watchFaceFolder).listFiles(File::isDirectory)).length == 0) {
            try {
                ILog.w("Listing asset files...");
                String[] assetFiles = requireActivity().getAssets().list("builtin-watchfaces");
                for (String assetFile : assetFiles) {
                    new Thread(() -> {
                        try {
                            ILog.w("Copying " + assetFile + " And Unzipping...");
                            File watchfaceZipFile = new File(watchFaceFolder + assetFile);
                            FileIOUtils.writeFileFromIS(watchfaceZipFile, requireActivity().getAssets().open("builtin-watchfaces/" + assetFile));
                            //ILog.e("File has been wrote at " + watchFaceFolder + "/" + watchfaceZipFile.getName().replaceAll(".zip", ""));
                            ZipUtils.unzipFile(watchfaceZipFile, new File(watchFaceFolder + "/" + assetFile.replaceAll(".zip", "")));
                            watchfaceZipFile.delete();
                            refreshWatchFace();
                        } catch (IOException e) {
                            ILog.e(e.getMessage());
                        }
                    }).start();
                }

            } catch (IOException e) {
                ILog.e(e.getMessage());
            }
        } else {
            try {
                if (WatchFaceHelper.getAllWatchFace().isEmpty()) onWatchFaceLoadFailed();
                else refreshWatchFace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        watchFaceChangeReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshWatchFace();
            }
        };
        IntentFilter watchFaceChangeFilter = new IntentFilter("com.dudu.wearlauncher.WatchFaceChange");
        requireActivity().registerReceiver(watchFaceChangeReceiver,watchFaceChangeFilter);
        batteryChangeReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int battery = level * 100 / scale;
                updateBattery(battery);
            }
        };
        requireActivity().registerReceiver(batteryChangeReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        timeChangeReceiver = new BroadcastReceiver(){
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

    private void postGetAllNotification() {
        Intent intent = new Intent("com.dudu.wearlauncher.NOTIFICATION_LISTENER");
        intent.putExtra("command", "listAll");
        requireActivity().sendBroadcast(intent);
    }
    private void refreshWatchFace() {
    	watchFaceBox.removeAllViews();
        try {
            WatchFaceInfo wfInfo = WatchFaceHelper.getWatchFaceInfo((String)SharedPreferencesUtil.getData(SharedPreferencesUtil.NOW_WATCHFACE,"watchface-example"));
            watchFace = WatchFaceHelper.getWatchFace(wfInfo.packageName,wfInfo.name);
            if(watchFace != null) {
                layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
                watchFaceBox.addView(watchFace,layoutParams);
                updateTime();
            }else{
                throw new RuntimeException("Unexpected null watchface");
            }
        } catch (Exception err) {
            onWatchFaceLoadFailed();
            ILog.e("表盘加载失败:" + err.getMessage());
        }
       
    }

    private void onWatchFaceLoadFailed() {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        TextView tv = new TextView(requireActivity());
        tv.setText("表盘加载失败");
        watchFaceBox.addView(tv, lp);
    }
    @Override
    public void onDestroy() {
    	super.onDestroy();
        requireActivity().unregisterReceiver(watchFaceChangeReceiver);
        requireActivity().unregisterReceiver(batteryChangeReceiver);
        requireActivity().unregisterReceiver(timeChangeReceiver);
        requireActivity().unregisterReceiver(msgReceiver);
        requireActivity().unregisterReceiver(msgRemovedReceiver);
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
