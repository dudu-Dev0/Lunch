package com.dudu.wearlauncher.ui.home;

import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dudu.wearlauncher.R;
import com.dudu.wearlauncher.utils.PmUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class AppListFragment extends Fragment{
    RecyclerView recycler;
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
        List<ResolveInfo> appList = PmUtils.getAllApps();
        Iterator<ResolveInfo> iterator = appList.iterator();
        List<String> hiddenList = Arrays.asList(getActivity().getResources().getStringArray(R.array.hidden_activities));
        while (iterator.hasNext()) {
            ResolveInfo info = iterator.next();
            if (hiddenList.contains(info.activityInfo.packageName + "/" + info.activityInfo.name)) {
                iterator.remove();
            }
        }
        adapter = new AppListAdapter(requireActivity(), appList);
        recycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recycler.setAdapter(adapter);
    }
    
}
