package com.dudu.wearlauncher.ui.home;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dudu.wearlauncher.R;
import com.dudu.wearlauncher.model.WatchFaceInfo;
import com.dudu.wearlauncher.utils.SharedPreferencesUtil;
import com.dudu.wearlauncher.utils.WatchFaceHelper;
import java.io.File;
import org.json.JSONException;

public class WatchFacePreviewFragment extends Fragment{
    String watchFaceName;
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
        WatchFaceInfo info = new WatchFaceInfo();
        
        try {
        	info = WatchFaceHelper.getWatchFaceInfo(watchFaceName);
        } catch(JSONException err) {
        	err.printStackTrace();
        }
        Glide.with(requireActivity())
            .load(new File(WatchFaceHelper.watchFaceFolder+"/"+watchFaceName+"/preview.png"))
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
    }
}
