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
import com.dudu.wearlauncher.ui.home.pagertransformer.CubicOverturnTransformer;
import com.dudu.wearlauncher.ui.home.pagertransformer.XTCTrans;
import com.dudu.wearlauncher.ui.settings.RequestPermissonActivity;
import com.dudu.wearlauncher.utils.SharedPreferencesUtil;
import com.dudu.wearlauncher.utils.WatchFaceHelper;

import java.util.List;

public class HomeActivity extends BaseActivity {
    ViewPager homeViewPager;
    WatchFaceFragment watchFaceFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        checkIsFirstStart();
        checkIsXTC();

        startService(new Intent(this, NotificationListenerService.class));

        watchFaceFragment = new WatchFaceFragment();

        homeViewPager = findViewById(R.id.home_pager);
        List<Fragment> fragmentList = List.of(watchFaceFragment, new AppListFragment());
        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(),fragmentList);
        homeViewPager.setAdapter(adapter);
        homeViewPager.setPageTransformer(false,new CubicOverturnTransformer());

        homeViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                try {
                    if (watchFaceFragment.getWatchFace() == null) return;
                    if (i != 0) {
                        watchFaceFragment.getWatchFace().onWatchfaceInvisible();
                    } else {
                        watchFaceFragment.getWatchFace().onWatchfaceVisible();
                    }
                        
                } catch(Exception err) {
                	
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (watchFaceFragment.getWatchFace() != null) watchFaceFragment.getWatchFace().onWatchfaceInvisible();
        } catch(Exception err) {
        	
        }   
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (watchFaceFragment.getWatchFace() != null) watchFaceFragment.getWatchFace().onWatchfaceVisible();
        } catch(Exception err) {
        	
        }
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
    private void checkIsFirstStart() {
    	if((Boolean)SharedPreferencesUtil.getData(SharedPreferencesUtil.FIRST_START,true)) {
    		SharedPreferencesUtil.putData(SharedPreferencesUtil.SETTING_CENTER,"[{\"button\":\"button_wifi\"},{\"button\":\"button_mobiledata\"},{\"button\":\"button_bluetooth\"}]");
            Intent intent = new Intent(this,RequestPermissonActivity.class);
            startActivity(intent);
    	}
    }
}
