package com.dudu.wearlauncher.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dudu.wearlauncher.R;
import com.dudu.wearlauncher.WearLauncherApp;
import com.dudu.wearlauncher.model.WatchFaceInfo;
import com.dudu.wearlauncher.utils.SharedPreferencesUtil;
import com.dudu.wearlauncher.utils.WatchFaceHelper;
import java.io.IOException;
import org.json.JSONException;

import java.io.File;


public class WatchFacePreviewFragment extends Fragment{
    String watchFaceName;
    WatchFaceInfo info;
    public WatchFacePreviewFragment(String wfName){
        watchFaceName = wfName;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_watchface_preview, container, false);
    }

        
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView img = view.findViewById(R.id.wf_pre_img);
        TextView text = view.findViewById(R.id.wf_name_txt);
        ImageButton settingsBtn = view.findViewById(R.id.wf_settings_button);
        try {
        	info = WatchFaceHelper.getWatchfaceByPackage(watchFaceName);
        } catch(Exception err) {
        	err.printStackTrace();
            text.setText("未知表盘");
            return;
        }
        Glide.with(requireActivity())
            .load(info.preview)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(img);
        text.setText(info.displayName);
        img.setOnClickListener(v->{
            SharedPreferencesUtil.putData(SharedPreferencesUtil.NOW_WATCHFACE,watchFaceName);
            Intent intent = new Intent("com.dudu.wearlauncher.WatchFaceChange");
            requireActivity().sendBroadcast(intent);
            requireActivity().finish();
            requireActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        });
        settingsBtn.setOnClickListener(v->{
            if (info.settingsActivityName!=null) {
                Intent intent = new Intent();
                intent.setClassName(watchFaceName,info.settingsActivityName);
                requireActivity().startActivity(intent);
            } else {
                Toast.makeText(requireActivity(), "该表盘没有设置", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
