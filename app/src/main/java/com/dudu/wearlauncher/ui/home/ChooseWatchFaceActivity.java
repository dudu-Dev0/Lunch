package com.dudu.wearlauncher.ui.home;

import android.app.ListActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.dudu.wearlauncher.R;
import com.dudu.wearlauncher.model.WatchFaceInfo;
import com.dudu.wearlauncher.ui.BaseActivity;
import com.dudu.wearlauncher.ui.ViewPagerFragmentAdapter;
import com.dudu.wearlauncher.utils.DensityUtil;
import com.dudu.wearlauncher.utils.WatchFaceHelper;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;

public class ChooseWatchFaceActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_watchface);
        ViewPager pager = findViewById(R.id.wf_choose_vp);
        pager.setPageMargin(DensityUtil.dip2px(this,15));
        try {
        	List<WatchFaceInfo> allWfList = WatchFaceHelper.getAllWatchFace();
            List<Fragment> fragmentList = new ArrayList<>();
            for(WatchFaceInfo info : allWfList) {
            	fragmentList.add(new WatchFacePreviewFragment(info.name));
            }
            ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(),fragmentList);
            pager.setAdapter(adapter);
        } catch(JSONException err) {
        	err.printStackTrace();
        }
        
    }
}
