package com.dudu.wearlauncher.ui.home;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import com.dudu.wearlauncher.R;
import com.dudu.wearlauncher.model.WatchFaceData;
import com.dudu.wearlauncher.ui.BaseActivity;
import com.dudu.wearlauncher.ui.ViewPagerFragmentAdapter;
import com.dudu.wearlauncher.utils.WatchFaceHelper;
import java.util.List;

public class HomeActivity extends BaseActivity {
    ViewPager homeViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        homeViewPager = findViewById(R.id.home_pager);
        List<Fragment> fragmentList = List.of(new WatchFaceFragment(),new AppListFragment());
        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(),fragmentList);
        homeViewPager.setAdapter(adapter);
        
        try {
        	for(WatchFaceData data : WatchFaceHelper.getAllWatchFace()) {
        		Log.e("wf",data.name+";"+data.displayName);
        	}
        } catch(Exception err) {
        	err.printStackTrace();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
