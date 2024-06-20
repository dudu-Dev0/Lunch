package com.dudu.wearlauncher.ui.home;
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
        adapter = new AppListAdapter(requireActivity(),PmUtils.getAllApps());
        recycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recycler.setAdapter(adapter);
    }
    
}
