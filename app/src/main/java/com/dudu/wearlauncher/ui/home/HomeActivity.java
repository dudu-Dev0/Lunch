package com.dudu.wearlauncher.ui.home;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.dudu.wearlauncher.R;
import com.dudu.wearlauncher.model.WatchFaceInfo;
import com.dudu.wearlauncher.services.NotificationListenerService;
import com.dudu.wearlauncher.ui.BaseActivity;
import com.dudu.wearlauncher.ui.ViewPagerFragmentAdapter;
import com.dudu.wearlauncher.utils.WatchFaceHelper;

import java.util.List;

public class HomeActivity extends BaseActivity {
    ViewPager homeViewPager;
    WatchFaceFragment watchFaceFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        checkIsXTC();

        startService(new Intent(this, NotificationListenerService.class));

        watchFaceFragment = new WatchFaceFragment();

        homeViewPager = findViewById(R.id.home_pager);
        List<Fragment> fragmentList = List.of(watchFaceFragment, new AppListFragment());
        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(),fragmentList);
        homeViewPager.setAdapter(adapter);

        homeViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                if (watchFaceFragment.getWatchFace() == null) return;
                if (i != 0) {
                    watchFaceFragment.getWatchFace().onWatchfaceInvisible();
                } else {
                    watchFaceFragment.getWatchFace().onWatchfaceVisible();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        try {
        	for(WatchFaceInfo data : WatchFaceHelper.getAllWatchFace()) {
        		Log.e("wf",data.name+";"+data.displayName);
        	}
        } catch(Exception err) {
        	err.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (watchFaceFragment.getWatchFace() != null) watchFaceFragment.getWatchFace().onWatchfaceInvisible();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (watchFaceFragment.getWatchFace() != null) watchFaceFragment.getWatchFace().onWatchfaceVisible();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        if(!isDestroyed()) {
        	homeViewPager.setCurrentItem(0);
            if (watchFaceFragment.getSwipeDrawer() != null) watchFaceFragment.getSwipeDrawer().closeDrawer();
        }
    }
    
    private void checkIsXTC() {
        PackageManager pm = getPackageManager();
        ComponentName xtcComponentName = new ComponentName(this, com.xtc.i3launcher.module.home.view.activity.HomeActivity.class);
        ComponentName normalComponentName = new ComponentName(this,HomeActivity.class);
        if (!Build.BRAND.equalsIgnoreCase("XTC")||!Build.MANUFACTURER.equalsIgnoreCase("XTC")) {
            pm.setComponentEnabledSetting(xtcComponentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            pm.setComponentEnabledSetting(normalComponentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        }else{
            pm.setComponentEnabledSetting(xtcComponentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            pm.setComponentEnabledSetting(normalComponentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }
    }
}
