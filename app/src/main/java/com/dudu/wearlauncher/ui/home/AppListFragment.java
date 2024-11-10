package com.dudu.wearlauncher.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dudu.wearlauncher.R;
import com.dudu.wearlauncher.utils.ILog;
import com.dudu.wearlauncher.utils.IconPackLoader;
import com.dudu.wearlauncher.utils.PmUtils;

import com.dudu.wearlauncher.utils.SharedPreferencesUtil;
import com.dudu.wearlauncher.widget.MyRecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AppListFragment extends Fragment{
    MyRecyclerView recycler;
    AppListAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_list, container, false);
    }

    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recycler = view.findViewById(R.id.recycler);
        refreshAppList();
        BroadcastReceiver receiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent intent) {
                refreshAppList();
            }
            
        };
        requireActivity().registerReceiver(receiver,new IntentFilter("com.dudu.wearlauncher.REFESH_APP_LIST"));
    }
    private void refreshAppList() {
    	List<ResolveInfo> appList = PmUtils.getAllApps();
        Iterator<ResolveInfo> iterator = appList.iterator();
        List<String> hiddenList = new ArrayList();
        hiddenList.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.hidden_activities)));
        ILog.w("hiddenActivities:"+SharedPreferencesUtil.getData(SharedPreferencesUtil.HIDDEN_ACTIVITIES,""));
        hiddenList.addAll(Arrays.asList(((String)SharedPreferencesUtil.getData(SharedPreferencesUtil.HIDDEN_ACTIVITIES,"")).split(":")));
        while (iterator.hasNext()) {
            ResolveInfo info = iterator.next();
            if (hiddenList.contains(info.activityInfo.packageName + "/" + info.activityInfo.name)) {
                iterator.remove();
            }
        }
        Map<String,Drawable> iconMap = new HashMap<>();
        
        if(!SharedPreferencesUtil.getData(SharedPreferencesUtil.ICON_PACK,"default").equals("default")){
            try {
            	iconMap = IconPackLoader.getIconMap((String)SharedPreferencesUtil.getData(SharedPreferencesUtil.ICON_PACK,"default"));
            } catch(Exception err) {
            	ILog.e(err.toString());
            }
            
        }
        adapter = new AppListAdapter(requireActivity(), appList, iconMap, (String)SharedPreferencesUtil.getData(SharedPreferencesUtil.APP_LIST_STYLE,"linear"));
        if(SharedPreferencesUtil.getData(SharedPreferencesUtil.APP_LIST_STYLE,"linear").equals("linear")) {
        	recycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
        }if(SharedPreferencesUtil.getData(SharedPreferencesUtil.APP_LIST_STYLE,"linear").equals("grid")) {
        	recycler.setLayoutManager(new GridLayoutManager(requireActivity(),3));
        }
        
        recycler.setAdapter(adapter);
    }
    
}
