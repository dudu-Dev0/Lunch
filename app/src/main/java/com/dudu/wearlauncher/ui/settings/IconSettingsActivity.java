package com.dudu.wearlauncher.ui.settings;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.blankj.utilcode.util.AppUtils;
import com.dudu.wearlauncher.R;
import com.dudu.wearlauncher.model.IconPack;
import com.dudu.wearlauncher.ui.BaseActivity;
import com.dudu.wearlauncher.utils.IconPackLoader;
import java.util.List;
import me.shihao.library.XRadioGroup;

public class IconSettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_pack_settings);
        
        RecyclerView iconList = findViewById(R.id.icon_pack_recycler);
        XRadioGroup radioGroup = findViewById(R.id.settings_icon_pack_radio_group);
        
        List<IconPack> list = IconPackLoader.queryIconPacks();
        IconPack pack = new IconPack();
        pack.icon = AppUtils.getAppIcon();
        pack.name = "默认";
        pack.packageName = "default";
        list.add(0,pack);
        iconList.setLayoutManager(new LinearLayoutManager(this));
        iconList.setAdapter(new IconPackAdapter(this,list));
        
    }
    
    @Override
    protected void onDestroy() {
        Intent intent = new Intent("com.dudu.wearlauncher.REFESH_APP_LIST");
        sendBroadcast(intent);
        super.onDestroy();
    }
}
